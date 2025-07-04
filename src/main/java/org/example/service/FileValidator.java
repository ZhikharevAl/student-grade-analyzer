package org.example.service;

import java.util.Objects;

/**
 * Валидатор для проверки корректности данных из файлов.
 */
public class FileValidator {

  /**
   * Проверяет, что ФИО содержит ровно 3 слова.
   */
  public boolean isValidFullName(String fullName) {
    if (Objects.equals(fullName, "") || fullName == null) {
      return false;
    }
    if (fullName.trim().isEmpty()) {
      return false;
    }
    return fullName.trim().split("\\s+").length == 3;
  }

  /**
   * Проверяет, что оценка находится в допустимом диапазоне.
   */
  public boolean isValidGrade(int grade) {
    return grade >= 1 && grade <= 5;
  }
}
