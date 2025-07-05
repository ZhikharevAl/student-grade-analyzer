package org.example.service;


import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Тестирование валидатора файлов")
@Epic("Валидация данных")
@Feature("Валидация файлов")
public class FileValidatorTest {

  private FileValidator fileValidator;

  @BeforeEach
  void setUp() {
    fileValidator = new FileValidator();
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "Иванов Иван Иванович",
      "Петров Петр Петрович",
      "Сидорова Анна Викторовна"
  })
  @DisplayName("Валидация корректных ФИО")
  @Description("Тестируем валидацию корректных ФИО, которые содержат ровно 3 слова (фамилия, имя, отчество)")
  @Severity(SeverityLevel.CRITICAL)
  @Story("Валидация ФИО")
  @Issue("TASK-123")
  void testValidFullNames(String fullName) {
    stepValidateFullName(fullName, true);
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "Иванов Иван",
      "Петров",
      "Сидорова Анна Викторовна Петровна",
      "",
      "   ",
  })
  @DisplayName("Валидация некорректных ФИО")
  @Description("Тестируем валидацию некорректных ФИО: неполные имена, слишком длинные, пустые строки")
  @Severity(SeverityLevel.CRITICAL)
  @Story("Валидация ФИО")
  @Issue("TASK-124")
  void testInvalidFullNames(String fullName) {
    stepValidateFullName(fullName, false);
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 4, 5})
  @DisplayName("Валидация корректных оценок")
  @Description("Тестируем валидацию корректных оценок в диапазоне от 1 до 5 включительно")
  @Severity(SeverityLevel.CRITICAL)
  @Story("Валидация оценок")
  @Issue("TASK-125")
  void testValidGrades(int grade) {
    stepValidateGrade(grade, true);
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 6, -1, 10})
  @DisplayName("Валидация некорректных оценок")
  @Description("Тестируем валидацию некорректных оценок: отрицательные, нулевые, больше 5")
  @Severity(SeverityLevel.CRITICAL)
  @Story("Валидация оценок")
  @Issue("TASK-126")
  void testInvalidGrades(int grade) {
    stepValidateGrade(grade, false);
  }

  @Step("Валидация ФИО: '{fullName}', ожидается: {expected}")
  private void stepValidateFullName(String fullName, boolean expected) {
    Allure.parameter("Входное значение", fullName);
    Allure.parameter("Ожидаемый результат", expected ? "Валидно" : "Невалидно");

    boolean actualResult = fileValidator.isValidFullName(fullName);

    Allure.parameter("Фактический результат", actualResult ? "Валидно" : "Невалидно");

    if (expected) {
      assertTrue(actualResult,
          String.format("ФИО '%s' должно быть валидным, но получили: %s", fullName, actualResult));
    } else {
      assertFalse(actualResult,
          String.format("ФИО '%s' должно быть невалидным, но получили: %s", fullName,
              actualResult));
    }

    Allure.attachment("Результат валидации",
        String.format("Входное значение: %s\nОжидалось: %s\nПолучено: %s",
            fullName, expected ? "валидно" : "невалидно", actualResult ? "валидно" : "невалидно"));
  }

  @Step("Валидация оценки: {grade}, ожидается: {expected}")
  private void stepValidateGrade(int grade, boolean expected) {
    Allure.parameter("Входное значение", grade);
    Allure.parameter("Ожидаемый результат", expected ? "Валидно" : "Невалидно");

    boolean actualResult = fileValidator.isValidGrade(grade);

    Allure.parameter("Фактический результат", actualResult ? "Валидно" : "Невалидно");

    if (expected) {
      assertTrue(actualResult,
          String.format("Оценка %d должна быть валидной, но получили: %s", grade, actualResult));
    } else {
      assertFalse(actualResult,
          String.format("Оценка %d должна быть невалидной, но получили: %s", grade, actualResult));
    }

    Allure.attachment("Результат валидации",
        String.format("Входное значение: %d\nОжидалось: %s\nПолучено: %s",
            grade, expected ? "валидно" : "невалидно", actualResult ? "валидно" : "невалидно"));
  }
}
