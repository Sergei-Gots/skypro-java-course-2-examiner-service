package pro.sky.java.course2.examinerservice.service;

import org.junit.jupiter.api.Test;
import pro.sky.java.course2.examinerservice.domain.Question;
import pro.sky.java.course2.examinerservice.exception.NoSuchQuestionException;
import pro.sky.java.course2.examinerservice.repository.QuestionRepositoryImpl;

import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;


class JavaQuestionServiceImplTest {


    private final QuestionService javaQuestionService = new JavaQuestionServiceImpl(new QuestionRepositoryImpl());


    @Test
    void add_and_get() {
        //GIVEN
        final int count = javaQuestionService.count();
        //WHEN
        Question actual = javaQuestionService.add("This is a question", "This is an answer:)");
        final Question expected = new Question("This is a question", "This is an answer:)");
        //THEN
        assertThat(javaQuestionService.count()).isEqualTo(count + 1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void addQuestion_and_get() {
        //GIVEN
        final int count = javaQuestionService.count();
        //WHEN
        Question actual = javaQuestionService.add(new Question("This is a question", "This is an answer:)"));
        final Question expected = new Question("This is a question", "This is an answer:)");
        //THEN
        assertThat(actual).isEqualTo(expected);
        assertThat(javaQuestionService.count()).isEqualTo(count + 1);
        assertThat(javaQuestionService.get(actual)).isEqualTo(expected);
    }

    @Test
    void tryToAddQuestionWithSameQuestionTwice() {
        //GIVEN
        final int count1 = javaQuestionService.count();
        //WHEN
        Question actual1 = javaQuestionService.add(new Question("This is a question", "This is an answer:)"));
        final Question expected1 = new Question("This is a question", "This is an answer:)");
        //THEN
        assertThat(actual1).isEqualTo(expected1);
        assertThat(javaQuestionService.count()).isEqualTo(count1 + 1);
        assertThat(javaQuestionService.get(actual1)).isEqualTo(expected1);

        //The Second Try:
        final int count2 = javaQuestionService.count();
        Question  actual2 = javaQuestionService.add(new Question("This is a question", "This is ANOTHER answer:)"));
        Question expected2 = new Question("This is a question", "This is ANOTHER answer:)");
        assertThat(javaQuestionService.count()).isEqualTo(count2);
        assertThat(javaQuestionService.getAll().stream().distinct().count()).isEqualTo(count2);
        assertThat(javaQuestionService.get(actual2)).isNotEqualTo(expected1);
        assertThat(javaQuestionService.get(actual2)).isEqualTo(expected2);
    }



    @Test
    void add_and_remove() {
        //GIVEN
        final int count = javaQuestionService.count();
        //WHEN
        Question actual = javaQuestionService.add(new Question("This is a question", "This is an answer:)"));
        Question expected = new Question("This is a question", "This is an answer:)");
        //THEN
        assertThat(javaQuestionService.get(actual)).isEqualTo(expected);
        assertThat(javaQuestionService.count()).isEqualTo(count + 1);
        assertThat(actual).isEqualTo(expected);
        assertThat(javaQuestionService.remove(actual)).isEqualTo(expected);
        assertThat(javaQuestionService.count()).isEqualTo(count);
        assertThatExceptionOfType(NoSuchQuestionException.class).isThrownBy(() -> javaQuestionService.get(actual));

    }

    @Test
    void getAll_and_remove_and_add() {
        final int count = javaQuestionService.count();
        Collection<Question> questionCollection = javaQuestionService.getAll();
        assertThat(questionCollection.size()).isEqualTo(count);
        for (Question question: questionCollection) {
            javaQuestionService.remove(question);
        }
        assertThat(javaQuestionService.count()).isEqualTo(0);
        assertThat(javaQuestionService.getAll().size()).isEqualTo(0);

        for (int i = 0; i < 10; i++) {
            javaQuestionService.add("Question " + i, "Answer " + i);
        }

        assertThat(javaQuestionService.count()).isEqualTo(10);
        questionCollection = javaQuestionService.getAll();
        assertThat(questionCollection.size()).isEqualTo(10);

        for (int i = 0; i < 10; i++) {
            assertThat(questionCollection.contains(new Question("Question " + i, "Answer " + i)))
                    .isTrue();
        }
    }
}
