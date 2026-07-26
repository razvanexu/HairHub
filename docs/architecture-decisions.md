# Decizii de arhitectură — HairHub

## Devansare automată a programărilor (Programare)

**Data:** iulie 2026
**Status:** decis, de implementat când ajungem la entitatea `Programare`

### Context

Serviciile au o durată standard, multiplu de 30 minute (validat prin `DurationValidation` pe `ServiceType`). În practică, frizerul poate termina un serviciu mai devreme sau mai târziu decât durata standard (ex: o programare de 2 ore durează efectiv 1h25 sau 1h45).

### Decizie

Quando o programare se termină mai devreme/târziu decât durata standard, **programările următoare din aceeași zi pot fi devansate/întârziate automat**, pentru a reflecta timpul real disponibil.

**Implicație directă asupra validării:**

- `ServiceType.duration` — rămâne validat strict, trebuie să fie multiplu de 30 minute (durata standard definită în catalogul de servicii)
- `Programare.data_ora` (ora de start a unei programări individuale) — **NU trebuie validată ca multiplu de 30**. Ora de start poate fi orice valoare (ex: `14:37`) ca rezultat al devansării automate

### De implementat (când ajungem la `Programare`)

- Logică de recalculare a orelor următoare din ziua respectivă, atunci când o programare se finalizează cu o durată diferită de cea standard
- Notificare automată către clienții afectați de devansare/întârziere (schimbare de oră)
- Posibil: prag de toleranță (ex: devansare sub 5 minute nu declanșează notificare, ca să nu spamăm clienții pentru diferențe minore) — de discutat separat

### Ce NU se schimbă

`DurationValidation` (validator pentru `ServiceType.duration`) rămâne exact cum e implementat acum — se aplică doar la definirea unui tip de serviciu în catalog, nu la programări individuale.

## Client cu cont vs. client unic (Client, Programare)

**Data:** iulie 2026
**Status:** decis, de implementat când ajungem la entitatea `Programare` și la separarea credențialelor

### Context

Nu se fac plăți prin site, deci autentificarea nu e necesară pentru procesarea unei programări în sine (spre deosebire de un site de bilete de avion, unde fiecare achiziție e o tranzacție independentă fără cont). S-a discutat dacă `Client` ar trebui să existe mereu ca entitate persistentă, cu identitate stabilă (telefon/email unic, istoric), sau doar ca date efemere legate de o singură programare.

### Decizie

La rezervare, UI-ul prezintă clientului două opțiuni, în două carduri alăturate:

1. **Creare cont** — cu explicație a beneficiilor (istoric, preferințe, recunoaștere la vizite viitoare). Presupune o identitate `Client` persistentă, cu credențiale separate (vezi review PR: entitatea JPA de client nu trebuie să gestioneze și credențialele — e nevoie de o entitate separată, ex. `ClientCredentials`, legată 1-la-1).
2. **Client unic (fără cont)** — cu explicație explicită că datele **vor fi șterse după expirarea programării**. Nu se face deduplicare pe telefon/email, nu se păstrează istoric, nu contează preferințele.

Varianta aleasă e cea simplă: **nu se face legare retroactivă**. Dacă un client care a rezervat de mai multe ori ca "unic" decide ulterior să-și facă cont, programările anterioare nu se leagă automat la noul cont — istoricul "oficial" începe de la crearea contului.

### Implicații tehnice

- **Client cu cont**: identitate persistentă, cu `id` stabil, căutare prin `findByPhone`/`findByEmail` (deduplicare la programări viitoare — `findOrCreate`, nu `create` orb). Credențialele (parolă/hash) trăiesc într-o entitate separată, nu pe `Client`.
- **Client unic**: date legate direct de `Programare` (fie `Client` marcat ca temporar cu relație care declanșează `CascadeType.REMOVE` la ștergerea programării, fie date embedded pe `Programare`, fără entitate `Client` reutilizabilă). Necesită un mecanism de ștergere efectivă la expirarea/finalizarea programării — de preferat declanșat la tranziția de stare a programării, nu doar printr-un job cron separat.
- Ștergerea automată a datelor clienților unici e și un beneficiu de minimizare a datelor (GDPR-friendly), nu doar simplificare tehnică.

### De implementat (când ajungem la `Programare` și `ClientCredentials`)

- Entitate `ClientCredentials` (sau `Account`), separată de `Client`, cu relație 1-la-1 (`@MapsId`)
- Eliminarea `hasAccount`/`passwordHash` de pe `Client` — `hasAccount` devine derivat din existența `ClientCredentials`, nu flag stocat
- Logică `findOrCreate` pentru clienți cu cont la rezervare
- Model de date + cascade de ștergere pentru clienți unici legați de `Programare`
- UI: cele două carduri de opțiuni la începutul fluxului de rezervare
