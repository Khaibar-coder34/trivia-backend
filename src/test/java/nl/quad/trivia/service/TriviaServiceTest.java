package nl.quad.trivia.service;

import nl.quad.trivia.client.OpenTriviaClient;
import nl.quad.trivia.dto.CheckAnswersRequest;
import nl.quad.trivia.dto.CheckAnswersResponse;
import nl.quad.trivia.dto.ClientQuestion;
import nl.quad.trivia.mapper.TriviaMapper;
import nl.quad.trivia.model.TriviaQuestion;
import nl.quad.trivia.model.TriviaResponse;
import nl.quad.trivia.repository.TriviaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TriviaServiceTest {

    @Mock
    private OpenTriviaClient openTriviaClient;
    @Mock
    private TriviaRepository triviaRepository;
    @Mock
    private TriviaMapper triviaMapper;

    private TriviaService triviaService;

    @BeforeEach
    void setUp() {
        triviaService = new TriviaService(openTriviaClient, triviaMapper, triviaRepository);
    }

    @Test
    void testGetQuestions() {
        // Arrange
        TriviaQuestion q = new TriviaQuestion("multiple", "medium", "General", "Q1", "Answer", List.of("Wrong1", "Wrong2"));
        TriviaResponse response = new TriviaResponse(0, List.of(q));
        ClientQuestion clientQ = new ClientQuestion("id1", "General", "Q1", List.of("Answer", "Wrong1", "Wrong2"));

        when(openTriviaClient.fetchQuestions(anyInt())).thenReturn(response);
        when(triviaMapper.mapToClientQuestion(q)).thenReturn(clientQ);

        // Act
        List<ClientQuestion> result = triviaService.getQuestions(5);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Q1", result.get(0).question());
    }

    @Test
    void testCheckAnswers() {
        // Arrange
        Map<String, String> userAnswers = new HashMap<>();
        userAnswers.put("q1", "Paris");
        CheckAnswersRequest request = new CheckAnswersRequest(userAnswers);

        when(triviaRepository.getCorrectAnswer("q1")).thenReturn("Paris");

        // Act
        CheckAnswersResponse response = triviaService.checkAnswers(request);

        // Assert
        assertEquals(1, response.score());
        assertTrue(response.results().get(0).isCorrect());
    }
    
    @Test
    void testClearAnswers() {
        triviaService.clearAnswers();
        verify(triviaRepository).clearAll();
    }
}
