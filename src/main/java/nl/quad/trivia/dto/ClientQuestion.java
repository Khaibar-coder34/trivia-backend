package nl.quad.trivia.dto;

import java.util.List;

public record ClientQuestion(
        String id,
        String category,
        String question,
        List<String> answers) {
}
