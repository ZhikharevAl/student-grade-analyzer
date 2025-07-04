package org.example.service;

import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.example.exception.InvalidStudentFileException;
import org.example.model.Student;


/**
 * Сервисный класс, который содержит всю основную бизнес-логику.
 */
public class GradeAnalyzerService {
  private static final Logger logger = LogManager.getLogger(GradeAnalyzerService.class);

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
      if (!file.isFile() || !file.getName().endsWith(".txt")
          || file.getName().equalsIgnoreCase("отчет.тхт")) {
        continue;
      }

      String fileName = file.getName().replace(".txt", "");
      if (!isValidFullName(fileName)) {
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
          int grade =
              Integer.parseInt(trimmedLine.substring(trimmedLine.lastIndexOf(" - ") + 3).trim());

          if (grade < 1 || grade > 5) {
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

  private boolean isValidFullName(String fullName) {
    return fullName.trim().split("\\s+").length == 3;
  }


  public Map<String, Double> calculateAverageGradesBySubject(List<Student> students) {
    Map<String, List<Integer>> gradesBySubject = new HashMap<>();

    for (Student student : students) {
      for (Map.Entry<String, Integer> entry : student.getGrades().entrySet()) {
        String subject = entry.getKey();
        Integer grade = entry.getValue();

        if (!gradesBySubject.containsKey(subject)) {
          gradesBySubject.put(subject, new ArrayList<>());
        }
        gradesBySubject.get(subject).add(grade);
      }
    }

    Map<String, Double> avgGrades = new HashMap<>();
    for (Map.Entry<String, List<Integer>> entry : gradesBySubject.entrySet()) {
      String subject = entry.getKey();
      List<Integer> grades = entry.getValue();
      double sum = 0;
      for (Integer grade : grades) {
        sum += grade;
      }
      avgGrades.put(subject, sum / grades.size());
    }

    return avgGrades;
  }

  public List<Student> findBestStudents(List<Student> students) {
    if (students.isEmpty()) {
      return new ArrayList<>();
    }

    double maxAvg = -1.0;

    for (Student student : students) {
      if (student.getAverageGrade() > maxAvg) {
        maxAvg = student.getAverageGrade();
      }
    }


    List<Student> bestStudents = new ArrayList<>();
    for (Student student : students) {
      if (student.getAverageGrade() >= maxAvg) {
        bestStudents.add(student);
      }
    }
    return bestStudents;
  }

  public List<Student> findWorstStudents(List<Student> students) {
    if (students.isEmpty()) {
      return new ArrayList<>();
    }

    double minAvg = 6.0;

    for (Student student : students) {
      if (student.getAverageGrade() < minAvg) {
        minAvg = student.getAverageGrade();
      }
    }


    List<Student> worstStudents = new ArrayList<>();
    for (Student student : students) {
      if (student.getAverageGrade() <= minAvg) {
        worstStudents.add(student);
      }
    }
    return worstStudents;
  }
}
