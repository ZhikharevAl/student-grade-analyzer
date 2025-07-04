package org.example.exception;


/**
 * Пользовательское исключение для ошибок в файлах.
 */
public class InvalidStudentFileException extends Exception {
  public InvalidStudentFileException(String message) {
    super(message);
  }

  public InvalidStudentFileException(String message, Throwable cause) {
    super(message, cause);
  }
}
