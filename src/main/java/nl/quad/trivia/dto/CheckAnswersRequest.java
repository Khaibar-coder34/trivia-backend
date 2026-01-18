package nl.quad.trivia.dto;

import java.util.Map;

public record CheckAnswersRequest(
        Map<String, String> answers) {
}
