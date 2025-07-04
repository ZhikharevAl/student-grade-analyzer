package org.example.service;

import org.example.model.Student;
import io.qameta.allure.*;
import org.example.service.GradeAnalyzerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Тестирование сервиса анализа успеваемости")
public class GradeAnalyzerServiceTest {

  private GradeAnalyzerService analyzerService;
  private List<Student> students;

  @BeforeEach
  void setUp() {
    analyzerService = new GradeAnalyzerService();

    Map<String, Integer> grades1 = new HashMap<>();
    grades1.put("Математика", 5);
    grades1.put("Физика", 4);
    Student student1 = new Student("Иванов Иван Иванович", grades1);

    Map<String, Integer> grades2 = new HashMap<>();
    grades2.put("Математика", 4);
    grades2.put("Физика", 5);
    Student student2 = new Student("Петров Петр Петрович", grades2);

    Map<String, Integer> grades3 = new HashMap<>();
    grades3.put("Математика", 3);
    grades3.put("Физика", 3);
    Student student3 = new Student("Сидоров Сидор Сидорович", grades3);

    students = Arrays.asList(student1, student2, student3);
  }

  @Test
  @DisplayName("Расчет среднего балла по предметам")
  @Description("Проверяем корректность расчета среднего балла для каждого предмета.")
  @Severity(SeverityLevel.CRITICAL)
  @Story("Анализ успеваемости")
  @Feature("Средние оценки")
  void testCalculateAverageGradesBySubject() {
    Map<String, Double> avgGrades = analyzerService.calculateAverageGradesBySubject(students);
    assertEquals(4.0, avgGrades.get("Математика"), 0.01);
    assertEquals(4.0, avgGrades.get("Физика"), 0.01);
  }

  @Test
  @DisplayName("Поиск лучшего ученика")
  @Description("Проверяем, что сервис правильно находит ученика с самым высоким средним баллом.")
  @Severity(SeverityLevel.NORMAL)
  @Story("Анализ успеваемости")
  @Feature("Рейтинг учеников")
  void testFindBestStudent() {
    List<Student> bestStudents = analyzerService.findBestStudents(students);
    assertEquals(2, bestStudents.size());
    assertTrue(bestStudents.stream().anyMatch(s -> s.getFullName().equals("Иванов Иван Иванович")));
    assertTrue(bestStudents.stream().anyMatch(s -> s.getFullName().equals("Петров Петр Петрович")));
  }

  @Test
  @DisplayName("Поиск худшего ученика")
  @Description("Проверяем, что сервис правильно находит ученика с самым низким средним баллом.")
  @Severity(SeverityLevel.NORMAL)
  @Story("Анализ успеваемости")
  @Feature("Рейтинг учеников")
  void testFindWorstStudent() {
    List<Student> worstStudents = analyzerService.findWorstStudents(students);
    assertEquals(1, worstStudents.size());
    assertEquals("Сидоров Сидор Сидорович", worstStudents.getFirst().getFullName());
  }
}
