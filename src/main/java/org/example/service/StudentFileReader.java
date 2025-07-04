package org.example.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exception.InvalidStudentFileException;
import org.example.model.Student;

/**
 * Сервис для чтения и парсинга файлов студентов.
 */
public class StudentFileReader {
  private static final Logger logger = LogManager.getLogger(StudentFileReader.class);
  private final FileValidator fileValidator = new FileValidator();

  /**
   * Читает и парсит файлы студентов из указанной директории.
   */
  public List<Student> readStudentsFromDirectory(String directoryPath) throws IOException {
    List<Student> students = new ArrayList<>();
    File folder = new File(directoryPath);

    if (!folder.isDirectory()) {
      String errorMessage = "Указанный путь не является директорией: " + directoryPath;
      logger.error(errorMessage);
      throw new IOException(errorMessage);
    }

    File[] files = folder.listFiles();
    if (files == null) {
      return students;
    }

    for (File file : files) {
      if (!shouldProcessFile(file)) {
        continue;
      }

      String fileName = file.getName().replace(".txt", "");
      if (!fileValidator.isValidFullName(fileName)) {
        logger.warn("Некорректное имя файла (ФИО): {}. Файл пропущен.", file.getName());
        continue;
      }

      try {
        List<String> lines = Files.readAllLines(file.toPath());
        students.add(createStudentFromFile(fileName, lines));
        logger.info("Успешно обработан файл: {}", file.getName());
      } catch (InvalidStudentFileException | IOException e) {
        logger.error("Ошибка обработки файла {}: {}", file.getName(), e.getMessage());
      }
    }
    return students;
  }

  private boolean shouldProcessFile(File file) {
    return file.isFile()
        && file.getName().endsWith(".txt")
        && !file.getName().equalsIgnoreCase("отчет.тхт");
  }

  private Student createStudentFromFile(String fullName, List<String> lines)
      throws InvalidStudentFileException {
    if (lines.size() < 5) {
      throw new InvalidStudentFileException("Файл должен содержать не менее 5 предметов.");
    }

    Map<String, Integer> grades = new HashMap<>();

    try {
      for (String line : lines) {
        String trimmedLine = line.trim();
        if (trimmedLine.contains(" - ")) {
          String subject = trimmedLine.substring(0, trimmedLine.lastIndexOf(" - ")).trim();
          int grade = Integer.parseInt(
              trimmedLine.substring(trimmedLine.lastIndexOf(" - ") + 3).trim());

          if (!fileValidator.isValidGrade(grade)) {
            throw new InvalidStudentFileException("Оценка должна быть в диапазоне от 1 до 5.");
          }
          grades.put(subject, grade);
        }
      }
    } catch (NumberFormatException e) {
      throw new InvalidStudentFileException("Неверный формат оценки в файле.", e);
    }

    return new Student(fullName, grades);
  }

}
