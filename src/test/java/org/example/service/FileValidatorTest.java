package org.example.service;

import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Тестирование валидатора файлов")
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
  @Description("Проверяем, что валидатор правильно определяет корректные ФИО.")
  @Severity(SeverityLevel.CRITICAL)
  @Story("Валидация данных")
  @Feature("ФИО")
  void testValidFullNames(String fullName) {
    assertTrue(fileValidator.isValidFullName(fullName));
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "Иванов Иван",
      "Петров",
      "Сидорова Анна Викторовна Петровна",
      "",
      "   ",
    //"Иванов  Иван  Иванович"
  })
  @DisplayName("Валидация некорректных ФИО")
  @Description("Проверяем, что валидатор правильно определяет некорректные ФИО.")
  @Severity(SeverityLevel.CRITICAL)
  @Story("Валидация данных")
  @Feature("ФИО")
  void testInvalidFullNames(String fullName) {
    assertFalse(fileValidator.isValidFullName(fullName));
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 4, 5})
  @DisplayName("Валидация корректных оценок")
  @Description("Проверяем, что валидатор правильно определяет корректные оценки.")
  @Severity(SeverityLevel.CRITICAL)
  @Story("Валидация данных")
  @Feature("Оценки")
  void testValidGrades(int grade) {
    assertTrue(fileValidator.isValidGrade(grade));
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 6, -1, 10})
  @DisplayName("Валидация некорректных оценок")
  @Description("Проверяем, что валидатор правильно определяет некорректные оценки.")
  @Severity(SeverityLevel.CRITICAL)
  @Story("Валидация данных")
  @Feature("Оценки")
  void testInvalidGrades(int grade) {
    assertFalse(fileValidator.isValidGrade(grade));
  }
}
