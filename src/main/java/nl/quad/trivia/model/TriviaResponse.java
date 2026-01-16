package nl.quad.trivia.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record TriviaResponse(
        @JsonProperty("response_code") int responseCode,
        List<TriviaQuestion> results) {
}
