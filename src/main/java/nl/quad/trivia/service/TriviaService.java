package nl.quad.trivia.service;

import nl.quad.trivia.client.OpenTriviaClient;
import nl.quad.trivia.dto.CheckAnswersRequest;
import nl.quad.trivia.dto.CheckAnswersResponse;
import nl.quad.trivia.dto.ClientQuestion;
import nl.quad.trivia.mapper.TriviaMapper;
import nl.quad.trivia.model.TriviaResponse;
import nl.quad.trivia.repository.TriviaRepository;
import nl.quad.trivia.dto.QuestionResult;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TriviaService {

    private final OpenTriviaClient openTriviaClient;
    private final TriviaMapper triviaMapper;
    private final TriviaRepository triviaRepository;

    public TriviaService(OpenTriviaClient openTriviaClient, TriviaMapper triviaMapper,
            TriviaRepository triviaRepository) {
        this.openTriviaClient = openTriviaClient;
        this.triviaMapper = triviaMapper;
        this.triviaRepository = triviaRepository;
    }

    public List<ClientQuestion> getQuestions(int amount) {
        TriviaResponse response = openTriviaClient.fetchQuestions(amount);

        if (response == null || response.results() == null) {
            return Collections.emptyList();
        }

        return response.results().stream()
                .map(triviaMapper::mapToClientQuestion)
                .collect(Collectors.toList());
    }

    public CheckAnswersResponse checkAnswers(CheckAnswersRequest request) {
        if (request == null || request.answers() == null) {
            return new CheckAnswersResponse(0, 0, Collections.emptyList());
        }

        List<QuestionResult> results = new ArrayList<>();
        int score = 0;
        int total = request.answers().size();

        for (Map.Entry<String, String> entry : request.answers().entrySet()) {
            QuestionResult result = evaluateAnswer(entry.getKey(), entry.getValue());
            results.add(result);
            if (result.isCorrect()) {
                score++;
            }
        }

        return new CheckAnswersResponse(score, total, results);
    }

    private QuestionResult evaluateAnswer(String questionId, String userAnswer) {
        String correctAnswer = triviaRepository.getCorrectAnswer(questionId);
        boolean isCorrect = correctAnswer != null && correctAnswer.equals(userAnswer);

        return new QuestionResult(
                questionId,
                userAnswer,
                correctAnswer != null ? correctAnswer : "Unknown",
                isCorrect);
    }

    public void clearAnswers() {
        triviaRepository.clearAll();
    }
}
