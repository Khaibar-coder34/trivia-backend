package nl.quad.trivia.client;

import nl.quad.trivia.model.TriviaResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class OpenTriviaClient {

    private final WebClient webClient;

    public OpenTriviaClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://opentdb.com").build();
    }

    public TriviaResponse fetchQuestions(int amount) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api.php")
                        .queryParam("amount", amount)
                        .queryParam("type", "multiple")
                        .build())
                .retrieve()
                .bodyToMono(TriviaResponse.class)
                .block();
    }
}
