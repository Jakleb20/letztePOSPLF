# letztePOSPLF
# Social Media Kommentarservice – Spring Security Anwendung

Social Media wird immer wichtiger. Verschiedene Personen (`Student`) erstellen zu Beiträgen (`Post`) Kommentare (`Comment`). Die Kommentare beinhalten auch ein Rating.

## Ziel

Entwickle eine Spring-Security-Applikation, die mit einem **4-Schicht-Modell** arbeitet.

---

## Datenbankinitialisierung – `DBInit` Klasse

Diese Klasse liest nach dem Start der Applikation folgende Dateien ein:

- **`PostComment.json`**  
  Wird eingelesen und in einer **PostgreSQL-Datenbank** gespeichert.  
  **Beziehungen:**
  - `Post` ↔ `Comment`: **bidirektionale OneToMany-Beziehung**
  - `Comment` → `Student`: **ManyToOne-Beziehung**

- **`Student.json`**  
  Enthält alle User, die das REST-Service benutzen dürfen.  
  - Das **Passwort** ist das **Geburtsjahr** (als ganze Zahl).  
  - In der Datenbank werden **Name** und das **gehashte Passwort** gespeichert.

---

## Security

- Alle User haben die Rolle `USER`.
- Die Rolle wird **nicht** in der Datenbank gespeichert.
- Alle Security-Fehler werden über einen eigenen **`JwtUnauthorizedEndpoint`** an den Client gemeldet.

---

## REST-Endpoints

### `POST /public/signin`

- Jeder darf diesen Endpoint aufrufen.
- Erwartet im `RequestBody`:  
  ```json
  {
    "name": "Max Mustermann",
    "password": 2001
  }
