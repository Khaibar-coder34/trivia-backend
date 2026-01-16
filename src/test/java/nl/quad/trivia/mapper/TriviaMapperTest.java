package nl.quad.trivia.mapper;

import nl.quad.trivia.dto.ClientQuestion;
import nl.quad.trivia.model.TriviaQuestion;
import nl.quad.trivia.repository.TriviaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TriviaMapperTest {

    @Mock
    private TriviaRepository triviaRepository;

    private TriviaMapper triviaMapper;

    @BeforeEach
    void setUp() {
        triviaMapper = new TriviaMapper(triviaRepository);
    }

    @Test
    void testMapToClientQuestion() {
        // Arrange
        TriviaQuestion source = new TriviaQuestion(
                "multiple",
                "easy",
                "Science",
                "What is 1+1?",
                "2",
                List.of("3", "4", "5"));

        // Act
        ClientQuestion result = triviaMapper.mapToClientQuestion(source);

        // Assert
        assertNotNull(result);
        assertNotNull(result.id());
        assertEquals("Science", result.category());
        assertEquals("What is 1+1?", result.question());
        assertEquals(4, result.answers().size());
        assertTrue(result.answers().contains("2")); // Correct answer included

        // Verify Side Effect (Storing answer)
        verify(triviaRepository).saveAnswer(result.id(), "2");
    }

    @Test
    void testHtmlUnescaping() {
        // Arrange
        TriviaQuestion source = new TriviaQuestion(
                "multiple", "easy", "Cat", "Q&amp;A", "A&lt;B", List.of("C&gt;D"));

        // Act
        ClientQuestion result = triviaMapper.mapToClientQuestion(source);

        // Assert
        assertEquals("Q&A", result.question());
        assertTrue(result.answers().contains("A<B"));
        assertTrue(result.answers().contains("C>D"));
        verify(triviaRepository).saveAnswer(result.id(), "A<B");
    }
}
