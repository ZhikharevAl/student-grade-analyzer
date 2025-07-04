package org.example.service;

import org.example.model.Student;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Тестирование калькулятора оценок")
public class GradeCalculatorTest {

  private GradeCalculator gradeCalculator;
  private List<Student> students;

  @BeforeEach
  void setUp() {
    gradeCalculator = new GradeCalculator();

    Map<String, Integer> grades1 = new HashMap<>();
    grades1.put("Математика", 5);
    grades1.put("Физика", 4);
    grades1.put("Химия", 3);
    Student student1 = new Student("Иванов Иван Иванович", grades1);

    Map<String, Integer> grades2 = new HashMap<>();
    grades2.put("Математика", 3);
    grades2.put("Физика", 5);
    grades2.put("Биология", 4);
    Student student2 = new Student("Петров Петр Петрович", grades2);

    students = Arrays.asList(student1, student2);
  }

  @Test
  @DisplayName("Расчет среднего балла по предметам с разными наборами предметов")
  @Description("Проверяем корректность расчета среднего балла когда у студентов разные предметы.")
  @Severity(SeverityLevel.CRITICAL)
  @Story("Калькулятор оценок")
  @Feature("Средние оценки")
  void testCalculateAverageGradesBySubject() {
    Map<String, Double> avgGrades = gradeCalculator.calculateAverageGradesBySubject(students);

    assertEquals(4.0, avgGrades.get("Математика"), 0.01);
    assertEquals(4.5, avgGrades.get("Физика"), 0.01);
    assertEquals(3.0, avgGrades.get("Химия"), 0.01);
    assertEquals(4.0, avgGrades.get("Биология"), 0.01);
  }

  @Test
  @DisplayName("Расчет среднего балла для пустого списка студентов")
  @Description("Проверяем, что сервис корректно обрабатывает пустой список.")
  @Severity(SeverityLevel.NORMAL)
  @Story("Калькулятор оценок")
  @Feature("Граничные случаи")
  void testCalculateAverageGradesWithEmptyList() {
    Map<String, Double> avgGrades = gradeCalculator.calculateAverageGradesBySubject(List.of());
    assertTrue(avgGrades.isEmpty());
  }
}
