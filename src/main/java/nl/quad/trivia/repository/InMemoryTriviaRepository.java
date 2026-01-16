package nl.quad.trivia.repository;

import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryTriviaRepository implements TriviaRepository {

    private final Map<String, String> storage = new ConcurrentHashMap<>();

    @Override
    public void saveAnswer(String questionId, String correctAnswer) {
        storage.put(questionId, correctAnswer);
    }

    @Override
    public String getCorrectAnswer(String questionId) {
        return storage.get(questionId);
    }

    @Override
    public void clearAll() {
        storage.clear();
    }
}
