package nl.quad.trivia.dto;

public record CheckAnswersResponse(
                int score,
                int total,
                java.util.List<QuestionResult> results) {
}
