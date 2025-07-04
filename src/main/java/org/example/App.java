package org.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.example.model.Student;
import org.example.service.GradeAnalyzerService;

public class App {
  private static final Logger logger = LogManager.getLogger(App.class);
  private static final String REPORT_FILE_NAME = "отчет.тхт";
  private final GradeAnalyzerService analyzerService = new GradeAnalyzerService();

  public static void main(String[] args) {
    new App().run();
  }

  public void run() {
    logger.info("Запуск приложения анализатора оценок.");
    Scanner scanner = new Scanner(System.in);
    System.out.print("Введите путь к папке с файлами оценок: ");
    String path = scanner.nextLine();

    try {
      List<Student> students = analyzerService.readStudentsFromDirectory(path);
      if (students.isEmpty()) {
        System.out.println("В указанной директории не найдено корректных файлов с оценками.");
        logger.warn("Студенты не найдены в директории: {}", path);
        return;
      }

      Map<String, Double> avgGrades = analyzerService.calculateAverageGradesBySubject(students);
      List<Student> bestStudents = analyzerService.findBestStudents(students);
      List<Student> worstStudents = analyzerService.findWorstStudents(students);

      printReportToConsole(avgGrades, bestStudents, worstStudents, students.size());
      writeReportToFile(avgGrades, bestStudents, worstStudents, students.size(), path);

    } catch (IOException e) {
      System.err.println("Произошла ошибка ввода-вывода: " + e.getMessage());
      logger.fatal("Критическая ошибка I/O", e);
    }
    logger.info("Приложение завершило работу.");
  }

  private void printReportToConsole(Map<String, Double> avgGrades, List<Student> best,
                                    List<Student> worst, int total) {
    System.out.println("\n--- Отчет об успеваемости ---");
    System.out.println("\nСредний балл по предметам:");
    avgGrades.forEach((subject, avg) -> System.out.printf("%s - %.2f%n", subject, avg));

    System.out.println("\nЛучшие ученики:");
    best.forEach(
        s -> System.out.printf("%s (средний балл - %.2f)%n", s.getFullName(), s.getAverageGrade()));

    System.out.println("\nХудшие ученики:");
    worst.forEach(
        s -> System.out.printf("%s (средний балл - %.2f)%n", s.getFullName(), s.getAverageGrade()));

    System.out.printf("%nКоличество учеников: %d%n", total);
    System.out.println("--- Конец отчета ---\n");
  }

  private void writeReportToFile(Map<String, Double> avgGrades, List<Student> best,
                                 List<Student> worst, int total, String dirPath) throws
      IOException {
    Path reportPath = Paths.get(dirPath, REPORT_FILE_NAME);

    Files.deleteIfExists(reportPath);

    try (PrintWriter writer = new PrintWriter(
        Files.newBufferedWriter(reportPath, StandardCharsets.UTF_8))) {
      avgGrades.forEach((subject, avg) -> writer.printf("%s - %.2f%n", subject, avg));
      writer.println();

      writer.println("Лучшие ученики:");
      best.forEach(
          s -> writer.printf("%s (средний балл - %.2f)%n", s.getFullName(), s.getAverageGrade()));
      writer.println();

      writer.println("Худшие ученики:");
      worst.forEach(
          s -> writer.printf("%s (средний балл - %.2f)%n", s.getFullName(), s.getAverageGrade()));
      writer.println();

      writer.printf("Количество учеников: %d%n", total);
    }
    System.out.println("Отчет успешно сохранен в файл: " + reportPath);
  }
}
