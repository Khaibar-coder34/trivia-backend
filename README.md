# Trivia Backend

Dit is de Spring Boot backend voor de Trivia Applicatie. Het communiceert met de OpenTrivia Database API en serveert vragen aan de frontend, terwijl het de antwoorden opslaat om valsspelen te voorkomen.

## 🚀 De Core Oplossing
Het belangrijkste probleem dat we hier hebben opgelost is **veiligheid en ontkoppeling**:
*   De frontend krijgt **nooit** de goede antwoorden te zien in de netwerk-requests. De backend slaat deze tijdelijk op in een `InMemoryTriviaRepository`.
*   Pas wanneer de gebruiker antwoorden instuurt, checkt de backend deze en stuurt alleen `correct/incorrect` terug.

## 🛠️ Lokaal Opstarten

### Vereisten
*   Java 21 (Microsoft OpenJDK of Temurin)
*   Maven

### Stappen
1.  Open de terminal in deze map (`trivia-backend`).
2.  Bouw en start de app:
    ```powershell
    mvn spring-boot:run
    ```
3.  De backend draait nu op `http://localhost:8080`.
    *   API: `http://localhost:8080/api/questions`

## ☁️ Deployment (Render)
Dit project is gedeployt op **Render** (gratis tier).
*   **Live URL**: `https://trivia-backend-luuy.onrender.com`
*   **Let op**: Omdat het gratis is, kan de eerste request na inactiviteit **10-15 minuten** duren.