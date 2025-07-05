package org.example.service;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.model.Student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Тестирование ранжирования студентов")
public class StudentRankingTest {

  private StudentRanking studentRanking;
  private List<Student> students;

  @BeforeEach
  void setUp() {
    studentRanking = new StudentRanking();

    Map<String, Integer> grades1 = new HashMap<>();
    grades1.put("Математика", 5);
    grades1.put("Физика", 4);
    grades1.put("Химия", 5);
    Student student1 = new Student("Иванов Иван Иванович", grades1);

    Map<String, Integer> grades2 = new HashMap<>();
    grades2.put("Математика", 4);
    grades2.put("Физика", 5);
    grades2.put("Химия", 4);
    Student student2 = new Student("Петров Петр Петрович", grades2);

    Map<String, Integer> grades3 = new HashMap<>();
    grades3.put("Математика", 3);
    grades3.put("Физика", 3);
    grades3.put("Химия", 3);
    Student student3 = new Student("Сидоров Сидор Сидорович", grades3);

    students = Arrays.asList(student1, student2, student3);
  }

  @Test
  @DisplayName("Поиск лучшего ученика")
  @Description("Проверяем, что сервис правильно находит ученика с самым высоким средним баллом.")
  @Severity(SeverityLevel.CRITICAL)
  @Story("Ранжирование студентов")
  @Feature("Лучшие студенты")
  void testFindBestStudent() {
    List<Student> bestStudents = studentRanking.findBestStudents(students);
    assertEquals(1, bestStudents.size());
    assertEquals("Иванов Иван Иванович", bestStudents.getFirst().getFullName());
  }

  @Test
  @DisplayName("Поиск худшего ученика")
  @Description("Проверяем, что сервис правильно находит ученика с самым низким средним баллом.")
  @Severity(SeverityLevel.CRITICAL)
  @Story("Ранжирование студентов")
  @Feature("Худшие студенты")
  void testFindWorstStudent() {
    List<Student> worstStudents = studentRanking.findWorstStudents(students);
    assertEquals(1, worstStudents.size());
    assertEquals("Сидоров Сидор Сидорович", worstStudents.getFirst().getFullName());
  }

  @Test
  @DisplayName("Поиск лучших учеников с одинаковыми баллами")
  @Description("Проверяем, что сервис правильно находит всех учеников с одинаковым максимальным баллом.")
  @Severity(SeverityLevel.NORMAL)
  @Story("Ранжирование студентов")
  @Feature("Одинаковые баллы")
  void testFindBestStudentsWithSameGrades() {
    // Создаем студентов с одинаковыми средними баллами
    Map<String, Integer> grades1 = new HashMap<>();
    grades1.put("Математика", 5);
    grades1.put("Физика", 4);
    Student student1 = new Student("Иванов Иван Иванович", grades1);

    Map<String, Integer> grades2 = new HashMap<>();
    grades2.put("Математика", 4);
    grades2.put("Физика", 5);
    Student student2 = new Student("Петров Петр Петрович", grades2);

    List<Student> studentsWithSameGrades = Arrays.asList(student1, student2);

    List<Student> bestStudents = studentRanking.findBestStudents(studentsWithSameGrades);
    assertEquals(2, bestStudents.size());
    assertTrue(bestStudents.stream().anyMatch(s -> s.getFullName().equals("Иванов Иван Иванович")));
    assertTrue(bestStudents.stream().anyMatch(s -> s.getFullName().equals("Петров Петр Петрович")));
  }

  @Test
  @DisplayName("Поиск студентов в пустом списке")
  @Description("Проверяем, что сервис корректно обрабатывает пустой список студентов.")
  @Severity(SeverityLevel.NORMAL)
  @Story("Ранжирование студентов")
  @Feature("Граничные случаи")
  void testFindStudentsInEmptyList() {
    List<Student> emptyList = List.of();

    List<Student> bestStudents = studentRanking.findBestStudents(emptyList);
    assertTrue(bestStudents.isEmpty());

    List<Student> worstStudents = studentRanking.findWorstStudents(emptyList);
    assertTrue(worstStudents.isEmpty());
  }
}
