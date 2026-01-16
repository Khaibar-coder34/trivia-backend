package nl.quad.trivia.repository;

public interface TriviaRepository {
    void saveAnswer(String questionId, String correctAnswer);

    String getCorrectAnswer(String questionId);

    void clearAll();
}
