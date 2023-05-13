package pro.sky.java.course2.examinerservice.service;

import org.junit.jupiter.api.Test;
import pro.sky.java.course2.examinerservice.domain.Question;
import pro.sky.java.course2.examinerservice.exception.NoSuchQuestionException;
import pro.sky.java.course2.examinerservice.repository.QuestionRepositoryImpl;

import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;


class MathQuestionServiceImplTest {


    private final QuestionService mathQuestionService = new MathQuestionServiceImpl(new QuestionRepositoryImpl());


    @Test
    void add_and_get() {
        //GIVEN
        final int count = mathQuestionService.count();
        //WHEN
        Question actual = mathQuestionService.add("This is a question", "This is an answer:)");
        final Question expected = new Question("This is a question", "This is an answer:)");
        //THEN
        assertThat(mathQuestionService.count()).isEqualTo(count + 1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void addQuestion_and_get() {
        //GIVEN
        final int count = mathQuestionService.count();
        //WHEN
        Question actual = mathQuestionService.add(new Question("This is a question", "This is an answer:)"));
        final Question expected = new Question("This is a question", "This is an answer:)");
        //THEN
        assertThat(actual).isEqualTo(expected);
        assertThat(mathQuestionService.count()).isEqualTo(count + 1);
        assertThat(mathQuestionService.get(actual)).isEqualTo(expected);
    }

    @Test
    void tryToAddQuestionWithSameQuestionTwice() {
        //GIVEN
        final int count1 = mathQuestionService.count();
        //WHEN
        Question actual1 = mathQuestionService.add(new Question("This is a question", "This is an answer:)"));
        final Question expected1 = new Question("This is a question", "This is an answer:)");
        //THEN
        assertThat(actual1).isEqualTo(expected1);
        assertThat(mathQuestionService.count()).isEqualTo(count1 + 1);
        assertThat(mathQuestionService.get(actual1)).isEqualTo(expected1);

        //The Second Try:
        final int count2 = mathQuestionService.count();
        Question  actual2 = mathQuestionService.add(new Question("This is a question", "This is ANOTHER answer:)"));
        Question expected2 = new Question("This is a question", "This is ANOTHER answer:)");
        assertThat(mathQuestionService.count()).isEqualTo(count2);
        assertThat(mathQuestionService.getAll().stream().distinct().count()).isEqualTo(count2);
        assertThat(mathQuestionService.get(actual2)).isNotEqualTo(expected1);
        assertThat(mathQuestionService.get(actual2)).isEqualTo(expected2);
    }



    @Test
    void add_and_remove() {
        //GIVEN
        final int count = mathQuestionService.count();
        //WHEN
        Question actual = mathQuestionService.add(new Question("This is a question", "This is an answer:)"));
        Question expected = new Question("This is a question", "This is an answer:)");
        //THEN
        assertThat(mathQuestionService.get(actual)).isEqualTo(expected);
        assertThat(mathQuestionService.count()).isEqualTo(count + 1);
        assertThat(actual).isEqualTo(expected);
        assertThat(mathQuestionService.remove(actual)).isEqualTo(expected);
        assertThat(mathQuestionService.count()).isEqualTo(count);
        assertThatExceptionOfType(NoSuchQuestionException.class).isThrownBy(() -> mathQuestionService.get(actual));

    }

    @Test
    void getAll_and_remove_and_add() {
        final int count = mathQuestionService.count();
        Collection<Question> questionCollection = mathQuestionService.getAll();
        assertThat(questionCollection.size()).isEqualTo(count);
        for (Question question: questionCollection) {
            mathQuestionService.remove(question);
        }
        assertThat(mathQuestionService.count()).isEqualTo(0);
        assertThat(mathQuestionService.getAll().size()).isEqualTo(0);

        for (int i = 0; i < 10; i++) {
            mathQuestionService.add("Question " + i, "Answer " + i);
        }

        assertThat(mathQuestionService.count()).isEqualTo(10);
        questionCollection = mathQuestionService.getAll();
        assertThat(questionCollection.size()).isEqualTo(10);

        for (int i = 0; i < 10; i++) {
            assertThat(questionCollection.contains(new Question("Question " + i, "Answer " + i)))
                    .isTrue();
        }
    }
}
