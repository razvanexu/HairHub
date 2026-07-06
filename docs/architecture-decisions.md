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
