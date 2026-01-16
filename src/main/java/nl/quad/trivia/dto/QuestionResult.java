package nl.quad.trivia.dto;

public record QuestionResult(
        String questionId,
        String userAnswer,
        String correctAnswer,
        boolean isCorrect) {
}
