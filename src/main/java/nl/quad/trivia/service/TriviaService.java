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
        if (isRequestInvalid(request)) {
            return new CheckAnswersResponse(0, 0, Collections.emptyList());
        }

        List<QuestionResult> results = request.answers().entrySet().stream()
                .map(entry -> evaluateAnswer(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        int score = (int) results.stream().filter(QuestionResult::isCorrect).count();
        int total = request.answers().size();

        return new CheckAnswersResponse(score, total, results);
    }

    private boolean isRequestInvalid(CheckAnswersRequest request) {
        return request == null || request.answers() == null || request.answers().isEmpty();
    }

    private QuestionResult evaluateAnswer(String questionId, String userAnswer) {
        // Step 1: Retrieve the correct answer from the repository
        String correctAnswer = triviaRepository.getCorrectAnswer(questionId);

        // Step 2: Determine if the user's answer matches (handling nulls)
        boolean isCorrect = isValidMatch(correctAnswer, userAnswer);

        // Step 3: Return the result object
        return new QuestionResult(
                questionId,
                userAnswer,
                correctAnswer != null ? correctAnswer : "Unknown Question",
                isCorrect);
    }

    private boolean isValidMatch(String correctAnswer, String userAnswer) {
        return correctAnswer != null && correctAnswer.equals(userAnswer);
    }

    public void clearAnswers() {
        triviaRepository.clearAll();
    }
}
