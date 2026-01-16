package nl.quad.trivia.mapper;

import nl.quad.trivia.dto.ClientQuestion;
import nl.quad.trivia.model.TriviaQuestion;
import nl.quad.trivia.repository.TriviaRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
public class TriviaMapper {

    private final TriviaRepository triviaRepository;

    public TriviaMapper(TriviaRepository triviaRepository) {
        this.triviaRepository = triviaRepository;
    }

    public ClientQuestion mapToClientQuestion(TriviaQuestion q) {
        String id = UUID.randomUUID().toString();

        // Logic: HTML Unescape strings to ensure clean text for frontend
        String correctAnswer = HtmlUtils.htmlUnescape(q.correctAnswer());

        // Side Effect: Store the correct answer immediately
        // This keeps the mapping and storage logic coupled for this specific use case,
        // ensuring we never generate a client question without storing its answer.
        triviaRepository.saveAnswer(id, correctAnswer);

        List<String> answers = new ArrayList<>();
        answers.add(correctAnswer);
        q.incorrectAnswers().forEach(a -> answers.add(HtmlUtils.htmlUnescape(a)));

        // Logic: Shuffle answers so the correct one isn't always first
        Collections.shuffle(answers);

        return new ClientQuestion(
                id,
                HtmlUtils.htmlUnescape(q.category()),
                HtmlUtils.htmlUnescape(q.question()),
                answers);
    }
}
