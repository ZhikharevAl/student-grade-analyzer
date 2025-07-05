package org.example.service;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
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
@Epic("Анализ успеваемости")
@Feature("Ранжирование студентов")
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
  @Description("Проверяем, что сервис правильно находит ученика с самым высоким средним баллом")
  @Severity(SeverityLevel.CRITICAL)
  @Story("Поиск лучших студентов")
  @Issue("TASK-401")
  void testFindBestStudent() {
    stepPrepareTestStudents();
    List<Student> bestStudents = stepFindBestStudents();
    stepVerifyBestStudent(bestStudents);
  }

  @Test
  @DisplayName("Поиск худшего ученика")
  @Description("Проверяем, что сервис правильно находит ученика с самым низким средним баллом")
  @Severity(SeverityLevel.CRITICAL)
  @Story("Поиск худших студентов")
  @Issue("TASK-402")
  void testFindWorstStudent() {
    stepPrepareTestStudents();
    List<Student> worstStudents = stepFindWorstStudents();
    stepVerifyWorstStudent(worstStudents);
  }

  @Test
  @DisplayName("Поиск лучших учеников с одинаковыми баллами")
  @Description("Проверяем, что сервис правильно находит всех учеников с одинаковым максимальным баллом")
  @Severity(SeverityLevel.NORMAL)
  @Story("Одинаковые баллы")
  @Issue("TASK-403")
  void testFindBestStudentsWithSameGrades() {
    List<Student> studentsWithSameGrades = stepPrepareStudentsWithSameGrades();
    List<Student> bestStudents = stepFindBestStudentsWithSameGrades(studentsWithSameGrades);
    stepVerifyBestStudentsWithSameGrades(bestStudents);
  }

  @Test
  @DisplayName("Поиск студентов в пустом списке")
  @Description("Проверяем, что сервис корректно обрабатывает пустой список студентов")
  @Severity(SeverityLevel.NORMAL)
  @Story("Граничные случаи")
  @Issue("TASK-404")
  void testFindStudentsInEmptyList() {
    stepPrepareEmptyList();
    List<Student> bestStudents = stepFindBestStudentsInEmptyList();
    List<Student> worstStudents = stepFindWorstStudentsInEmptyList();
    stepVerifyEmptyResults(bestStudents, worstStudents);
  }

  @Step("Подготовка тестовых данных студентов")
  private void stepPrepareTestStudents() {
    Allure.parameter("Количество студентов", students.size());

    StringBuilder studentsInfo = new StringBuilder();
    for (Student student : students) {
      studentsInfo.append(String.format("Студент: %s%n", student.getFullName()));
      studentsInfo.append(String.format("Средний балл: %.2f%n", student.getAverageGrade()));
      studentsInfo.append("Оценки: ");
      for (Map.Entry<String, Integer> entry : student.getGrades().entrySet()) {
        studentsInfo.append(String.format("%s=%d ", entry.getKey(), entry.getValue()));
      }
      studentsInfo.append("%n%n");
    }

    Allure.attachment("Тестовые данные студентов", studentsInfo.toString());
  }

  @Step("Поиск лучших студентов")
  private List<Student> stepFindBestStudents() {
    List<Student> bestStudents = studentRanking.findBestStudents(students);

    StringBuilder bestInfo = new StringBuilder();
    for (Student student : bestStudents) {
      bestInfo.append(String.format("Студент: %s, Средний балл: %.2f%n",
          student.getFullName(), student.getAverageGrade()));
    }

    Allure.attachment("Лучшие студенты", bestInfo.toString());
    Allure.parameter("Количество найденных лучших студентов", bestStudents.size());

    return bestStudents;
  }

  @Step("Проверка результата поиска лучшего студента")
  private void stepVerifyBestStudent(List<Student> bestStudents) {
    Allure.parameter("Ожидаемое количество лучших студентов", 1);
    Allure.parameter("Фактическое количество лучших студентов", bestStudents.size());

    assertEquals(1, bestStudents.size(),
        "Должен быть найден 1 лучший студент");

    Allure.parameter("Ожидаемое имя лучшего студента", "Иванов Иван Иванович");
    Allure.parameter("Фактическое имя лучшего студента", bestStudents.get(0).getFullName());

    assertEquals("Иванов Иван Иванович", bestStudents.get(0).getFullName(),
        "Лучшим студентом должен быть Иванов Иван Иванович");

    Allure.attachment("Результат проверки",
        String.format("Лучший студент: %s с средним баллом %.2f",
            bestStudents.get(0).getFullName(), bestStudents.get(0).getAverageGrade()));
  }

  @Step("Поиск худших студентов")
  private List<Student> stepFindWorstStudents() {
    List<Student> worstStudents = studentRanking.findWorstStudents(students);

    StringBuilder worstInfo = new StringBuilder();
    for (Student student : worstStudents) {
      worstInfo.append(String.format("Студент: %s, Средний балл: %.2f%n",
          student.getFullName(), student.getAverageGrade()));
    }

    Allure.attachment("Худшие студенты", worstInfo.toString());
    Allure.parameter("Количество найденных худших студентов", worstStudents.size());

    return worstStudents;
  }

  @Step("Проверка результата поиска худшего студента")
  private void stepVerifyWorstStudent(List<Student> worstStudents) {
    Allure.parameter("Ожидаемое количество худших студентов", 1);
    Allure.parameter("Фактическое количество худших студентов", worstStudents.size());

    assertEquals(1, worstStudents.size(),
        "Должен быть найден 1 худший студент");

    Allure.parameter("Ожидаемое имя худшего студента", "Сидоров Сидор Сидорович");
    Allure.parameter("Фактическое имя худшего студента", worstStudents.get(0).getFullName());

    assertEquals("Сидоров Сидор Сидорович", worstStudents.get(0).getFullName(),
        "Худшим студентом должен быть Сидоров Сидор Сидорович");

    Allure.attachment("Результат проверки",
        String.format("Худший студент: %s с средним баллом %.2f",
            worstStudents.get(0).getFullName(), worstStudents.get(0).getAverageGrade()));
  }

  @Step("Подготовка студентов с одинаковыми баллами")
  private List<Student> stepPrepareStudentsWithSameGrades() {
    Map<String, Integer> grades1 = new HashMap<>();
    grades1.put("Математика", 5);
    grades1.put("Физика", 4);
    Student student1 = new Student("Иванов Иван Иванович", grades1);

    Map<String, Integer> grades2 = new HashMap<>();
    grades2.put("Математика", 4);
    grades2.put("Физика", 5);
    Student student2 = new Student("Петров Петр Петрович", grades2);

    List<Student> studentsWithSameGrades = Arrays.asList(student1, student2);

    Allure.parameter("Количество студентов с одинаковыми баллами", studentsWithSameGrades.size());

    StringBuilder studentsInfo = new StringBuilder();
    for (Student student : studentsWithSameGrades) {
      studentsInfo.append(String.format("Студент: %s, Средний балл: %.2f%n",
          student.getFullName(), student.getAverageGrade()));
    }

    Allure.attachment("Студенты с одинаковыми баллами", studentsInfo.toString());

    return studentsWithSameGrades;
  }

  @Step("Поиск лучших студентов с одинаковыми баллами")
  private List<Student> stepFindBestStudentsWithSameGrades(List<Student> studentsWithSameGrades) {
    List<Student> bestStudents = studentRanking.findBestStudents(studentsWithSameGrades);

    StringBuilder bestInfo = new StringBuilder();
    for (Student student : bestStudents) {
      bestInfo.append(String.format("Студент: %s, Средний балл: %.2f%n",
          student.getFullName(), student.getAverageGrade()));
    }

    Allure.attachment("Найденные лучшие студенты", bestInfo.toString());
    Allure.parameter("Количество найденных лучших студентов", bestStudents.size());

    return bestStudents;
  }

  @Step("Проверка результата поиска студентов с одинаковыми баллами")
  private void stepVerifyBestStudentsWithSameGrades(List<Student> bestStudents) {
    Allure.parameter("Ожидаемое количество лучших студентов", 2);
    Allure.parameter("Фактическое количество лучших студентов", bestStudents.size());

    assertEquals(2, bestStudents.size(),
        "Должно быть найдено 2 лучших студента с одинаковым средним баллом");

    boolean hasIvanov = bestStudents.stream()
        .anyMatch(s -> s.getFullName().equals("Иванов Иван Иванович"));
    boolean hasPetrov = bestStudents.stream()
        .anyMatch(s -> s.getFullName().equals("Петров Петр Петрович"));

    Allure.parameter("Найден Иванов", hasIvanov);
    Allure.parameter("Найден Петров", hasPetrov);

    assertTrue(hasIvanov,
        "Среди лучших студентов должен быть Иванов Иван Иванович");
    assertTrue(hasPetrov,
        "Среди лучших студентов должен быть Петров Петр Петрович");

    Allure.attachment("Результат проверки",
        "Оба студента с одинаковыми максимальными баллами найдены корректно");
  }

  @Step("Подготовка пустого списка студентов")
  private void stepPrepareEmptyList() {
    Allure.parameter("Количество студентов", 0);
    Allure.attachment("Тестовые данные", "Пустой список студентов");
  }

  @Step("Поиск лучших студентов в пустом списке")
  private List<Student> stepFindBestStudentsInEmptyList() {
    List<Student> emptyList = List.of();
    List<Student> bestStudents = studentRanking.findBestStudents(emptyList);

    Allure.parameter("Количество найденных лучших студентов", bestStudents.size());
    Allure.attachment("Результат поиска лучших", "Пустой список");

    return bestStudents;
  }

  @Step("Поиск худших студентов в пустом списке")
  private List<Student> stepFindWorstStudentsInEmptyList() {
    List<Student> emptyList = List.of();
    List<Student> worstStudents = studentRanking.findWorstStudents(emptyList);

    Allure.parameter("Количество найденных худших студентов", worstStudents.size());
    Allure.attachment("Результат поиска худших", "Пустой список");

    return worstStudents;
  }

  @Step("Проверка результатов для пустого списка")
  private void stepVerifyEmptyResults(List<Student> bestStudents, List<Student> worstStudents) {
    Allure.parameter("Ожидаемое количество лучших студентов", 0);
    Allure.parameter("Фактическое количество лучших студентов", bestStudents.size());

    assertTrue(bestStudents.isEmpty(),
        "Список лучших студентов должен быть пустым");

    Allure.parameter("Ожидаемое количество худших студентов", 0);
    Allure.parameter("Фактическое количество худших студентов", worstStudents.size());

    assertTrue(worstStudents.isEmpty(),
        "Список худших студентов должен быть пустым");

    Allure.attachment("Результат проверки",
        "Пустой список студентов корректно обработан для обоих случаев");
  }
}
