package org.example.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.example.model.Student;

/**
 * Главный сервис для анализа оценок, координирующий работу других сервисов.
 */
public class GradeAnalyzerService {
  private final StudentFileReader fileReader;
  private final GradeCalculator gradeCalculator;
  private final StudentRanking studentRanking;

  public GradeAnalyzerService() {
    this.fileReader = new StudentFileReader();
    this.gradeCalculator = new GradeCalculator();
    this.studentRanking = new StudentRanking();
  }

  // Конструктор для внедрения зависимостей
  public GradeAnalyzerService(StudentFileReader fileReader,
                              GradeCalculator gradeCalculator,
                              StudentRanking studentRanking) {
    this.fileReader = fileReader;
    this.gradeCalculator = gradeCalculator;
    this.studentRanking = studentRanking;
  }

  /**
   * Читает студентов из директории.
   */
  public List<Student> readStudentsFromDirectory(String directoryPath) throws IOException {
    return fileReader.readStudentsFromDirectory(directoryPath);
  }

  /**
   * Рассчитывает средний балл по предметам.
   */
  public Map<String, Double> calculateAverageGradesBySubject(List<Student> students) {
    return gradeCalculator.calculateAverageGradesBySubject(students);
  }

  /**
   * Находит лучших студентов.
   */
  public List<Student> findBestStudents(List<Student> students) {
    return studentRanking.findBestStudents(students);
  }

  /**
   * Находит худших студентов.
   */
  public List<Student> findWorstStudents(List<Student> students) {
    return studentRanking.findWorstStudents(students);
  }
}
