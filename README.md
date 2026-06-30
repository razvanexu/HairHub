# HairHub / HairKrishna / Hairly / HairBnB / HairBook

Platformă de gestionare a programărilor pentru un salon (frizerie / coafor), construită ca MVP pentru demonstrație către un potențial client.

## Stack

- **Frontend:** Flutter (Web + Mobile)
- **Backend:** Spring Boot 3 (Java)
- **Bază de date:** PostgreSQL

## Structură proiect

```
salon-app/
├── backend/        # Spring Boot — REST API
├── frontend/        # Flutter — UI Web/Mobile
└── docs/             # diagrame arhitectură, ERD, flow-uri
```

## Funcționalități MVP

- Gestionare angajați
- Gestionare clienți (cu cont și fără cont)
- Programări cu calendar/datetime picker liber
- Validare suprapuneri programări
- Confirmare programare prin cod (pentru clienți fără cont)
- Analiză aglomerare (zi / săptămână / lună)

## Roluri

| Rol | Acces |
|---|---|
| **Frizer** (admin) | Acces complet: angajați, clienți, programări, servicii, analiză |
| **Client cu cont** | Propriile programări, profil propriu |
| **Client fără cont** | Creează programare, verifică/anulează cu cod de confirmare |

## Cum rulezi local

### Backend

```bash
cd backend
./mvnw spring-boot:run
```

Necesită PostgreSQL local configurat în `application.properties`.

### Frontend

```bash
cd frontend
flutter pub get
flutter run
```

## Roadmap

- [ ] V1 — Programări, clienți, angajați, analiză (MVP curent)
- [ ] V2 — Galerie poze tunsori, acord GDPR
- [ ] V3 — Plăți online, AR preview, notificări SMS, rol Angajat limitat

## Echipă

Proiect dezvoltat de [Razvan Dumitrescu] și [Radu Costache].
