package ru.legionevil.asserts;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Класс сравнения скорости выполнения ассертов в разных фреймворках, последовательно и параллельно, hard&soft,
 * с наличием ошибок и без них
 * <br>
 * <br>
 * Запуск по кнопке <icon src="AllIcons.Actions.Execute"/> с интерфейса.
 * <p>Для запуска всех тестов из корня проекта выполните:</p>
 * <pre>{@code mvn test}</pre>
 *
 <h2>Тестируемые фреймворки</h2>
 * <ul>
 *   <li><b>JUnit 5 (Jupiter):</b> Нативный фреймворк тестов.</li>
 *   <li><b>AssertJ:</b> Fluent-ассерты, проверка коллекций и кастомных объектов.</li>
 *   <li><b>Hamcrest:</b> Матчеры, композиция условий.</li>
 *   <li><b>TestNG:</b> Ассерты схожие с JUnit, но с отдельным SoftAssert и без визуального мусора в виде лямбд.</li>
 * </ul>
 *
 * <h2>Краткие результаты (JDK 22, java 17)</h2>
 * <br>
 * При запуске параллельно (время в мс):
 * <table border="1">
 *   <tr><th>Фреймворк</th><th>Hard</th><th>Soft</th><th>Hard&Error</th><th>Soft&Error</th></tr>
 *   <tr><td>JUnit 5</td><td>44</td><td>44</td><td>45</td><td>1</td></tr>
 *   <tr><td>AssertJ</td><td>82</td><td>984</td><td>95</td><td>989</td></tr>
 *   <tr><td>Hamcrest</td><td>1</td><td>6</td><td>45</td><td>45</td></tr>
 *   <tr><td>TestNG</td><td>44</td><td>2</td><td>45</td><td>13</td></tr>
 * </table>
 * <br>
 * При запуске последовательно (время в мс):
 * <table border="1">
 *   <tr><th>Фреймворк</th><th>Hard</th><th>Soft</th><th>Hard&Error</th><th>Soft&Error</th></tr>
 *   <tr><td>JUnit 5</td><td>1</td><td>3</td><td>2</td><td>26</td></tr>
 *   <tr><td>AssertJ</td><td>66</td><td>818</td><td>1</td><td>13</td></tr>
 *   <tr><td>Hamcrest</td><td>1</td><td>1</td><td>2</td><td>8</td></tr>
 *   <tr><td>TestNG</td><td>1</td><td>1</td><td>18</td><td>4</td></tr>
 * </table>
 *
 * <p><i>Образец результатов доступен в <code>/docs/results/</code>.</i></p>
 *
 * @author legionevil
 * @see org.testng.Assert
 * @see org.testng.asserts.SoftAssert
 * @see org.junit.jupiter.api.Assertions
 * @see org.assertj.core.api.Assertions
 * @see org.assertj.core.api.SoftAssertions
 * @see org.hamcrest.MatcherAssert
 */
@Tag("Unit")
@DisplayName("Тесты ассертов")
public class AssertTest {
    public static final String EMPTY = "";
    public static final String SPACE = " ";
    public static final String HAMCREST = "Hamcrest";
    public static final String ERROR = "error";
    public static final String SOFT = "soft";
    public static final String SUCCESS = "success";
    public static final String ASSERT_J = "AssertJ";
    public static final String JUNIT = "JUnit";
    public static final String TEST_NG = "TestNG";
    static long start;

    @BeforeAll
    static void setTime() {
        start = System.nanoTime();
    }

    @AfterAll
    static void stopTime() {
        System.out.println("Время выполнения: " + (System.nanoTime() - start) / 1_000_000 + "мс");
    }

    @Test
    @DisplayName("Тест error AssertJ")
    @Tags({@Tag(ASSERT_J), @Tag(ERROR)})
    void checkAssertJError() {
        org.assertj.core.api.Assertions.assertThat(true).isFalse();
    }

    @Test
    @DisplayName("Тест error junit")
    @Tags({@Tag(JUNIT), @Tag(ERROR)})
    void checkAssertError() {
        assertFalse(true);
    }

    @Test
    @DisplayName("Тест error Hamcrest")
    @Tags({@Tag(HAMCREST), @Tag(ERROR)})
    void checkHamcrestError() {
        assertThat("Expecting value to be false", false);
    }

    @Test
    @DisplayName("Тест error TestNG")
    @Tags({@Tag(TEST_NG), @Tag(ERROR)})
    public void checkAssertTestNGError() {
        Assert.assertFalse(true);
    }

    @Test
    @DisplayName("Тест Soft error AssertJ")
    @Tags({@Tag(ASSERT_J), @Tag(ERROR), @Tag(SOFT)})
    void checkSoftAssertJError() {
        SoftAssertions softly;
        softly = new SoftAssertions();
        softly.assertThat(true).isFalse();
        softly.assertThat(1).isEqualTo(0);
        softly.assertThat(EMPTY).isEqualTo(SPACE);
        softly.assertAll();
    }

    @Test
    @DisplayName("Тест Soft error junit")
    @Tags({@Tag(JUNIT), @Tag(ERROR), @Tag(SOFT)})
    void checkGroupedAssertError() {
        assertAll("boolean",
                () -> assertFalse(true),
                () -> assertEquals(EMPTY, SPACE),
                () -> assertEquals(0, 1)
        );
    }

    @Test
    @DisplayName("Тест Soft error TestNG")
    @Tags({@Tag(TEST_NG), @Tag(ERROR), @Tag(SOFT)})
    public void checkSoftAssertTestNGError() {
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertFalse(true);
        softAssert.assertEquals(1, 0);
        softAssert.assertEquals(EMPTY, SPACE);
        softAssert.assertAll();
    }

    @Test
    @DisplayName("Тест Soft error Hamcrest")
    @Tags({@Tag(HAMCREST), @Tag(ERROR), @Tag(SOFT)})
    void checkSoftHamcrestError() {
        assertThat(true, allOf(is(false), is(false)));
    }

    // success
    @Test
    @DisplayName("Тест success AssertJ")
    @Tags({@Tag(ASSERT_J), @Tag(SUCCESS)})
    void checkAssertJSuccess() {
        Assertions.assertThat(true).isTrue();
    }

    @Test
    @DisplayName("Тест success Hamcrest")
    @Tags({@Tag(HAMCREST), @Tag(SUCCESS)})
    void checkHamcrestSuccess() {
        assertThat("Причина", true);
    }

    @Test
    @DisplayName("Тест success junit")
    @Tags({@Tag(JUNIT), @Tag(SUCCESS)})
    void checkAssertSuccess() {
        assertTrue(true);
    }

    @Test
    @DisplayName("Тест success TestNG")
    @Tags({@Tag(TEST_NG), @Tag(SUCCESS)})
    public void checkAssertTestNGSuccess() {
        Assert.assertTrue(true);
    }

    // success soft
    @Test
    @DisplayName("Тест Soft success AssertJ")
    @Tags({@Tag(ASSERT_J), @Tag(SUCCESS), @Tag(SOFT)})
    void checkSoftAssertJSuccess() {
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(true).isTrue();
        softly.assertThat(1).isEqualTo(1);
        softly.assertThat(EMPTY).isEqualTo(EMPTY);
        softly.assertAll();
    }

    @Test
    @DisplayName("Тест Soft success Hamcrest")
    @Tags({@Tag(HAMCREST), @Tag(SUCCESS), @Tag(SOFT)})
    void checkSoftHamcrestSuccess() {
        assertThat(true, allOf(is(true), is(true)));
    }

    @Test
    @DisplayName("Тест Soft success junit")
    @Tags({@Tag(JUNIT), @Tag(SUCCESS), @Tag(SOFT)})
    void checkGroupedAssertSuccess() {
        assertAll("Проверка разных типов ассертов",
                () -> assertTrue(true),
                () -> assertEquals(EMPTY, EMPTY),
                () -> assertEquals(1, 1)
        );
    }

    @Test
    @DisplayName("Тест Soft success TestNG")
    @Tags({@Tag(TEST_NG), @Tag(SUCCESS), @Tag(SOFT)})
    public void checkSoftAssertTestNGSuccess() {
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(true);
        softAssert.assertEquals(1, 1);
        softAssert.assertEquals(EMPTY, EMPTY);
        softAssert.assertAll();
    }
}
