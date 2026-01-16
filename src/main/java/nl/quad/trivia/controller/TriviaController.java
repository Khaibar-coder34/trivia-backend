package nl.quad.trivia.controller;

import nl.quad.trivia.dto.CheckAnswersRequest;
import nl.quad.trivia.dto.CheckAnswersResponse;
import nl.quad.trivia.dto.ClientQuestion;
import nl.quad.trivia.service.TriviaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TriviaController {

    private final TriviaService triviaService;

    public TriviaController(TriviaService triviaService) {
        this.triviaService = triviaService;
    }

    @GetMapping("/questions")
    public List<ClientQuestion> getQuestions(@RequestParam(defaultValue = "5") int amount) {
        return triviaService.getQuestions(amount);
    }

    @PostMapping("/checkanswers")
    public CheckAnswersResponse checkAnswers(@RequestBody CheckAnswersRequest request) {
        return triviaService.checkAnswers(request);
    }

    @DeleteMapping("/answers")
    public void reset() {
        triviaService.clearAnswers();
    }
}
