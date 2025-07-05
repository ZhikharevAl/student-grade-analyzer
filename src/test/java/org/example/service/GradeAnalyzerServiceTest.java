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

@DisplayName("Тестирование сервиса анализа успеваемости")
@Epic("Анализ успеваемости")
@Feature("Сервис анализа оценок")
public class GradeAnalyzerServiceTest {

  private GradeAnalyzerService analyzerService;
  private List<Student> students;

  @BeforeEach
  void setUp() {
    analyzerService = new GradeAnalyzerService();
    students = createTestStudents();
  }

  @Test
  @DisplayName("Расчет среднего балла по предметам")
  @Description("Проверяем корректность расчета среднего балла для каждого предмета на основе оценок всех студентов")
  @Severity(SeverityLevel.CRITICAL)
  @Story("Расчет средних оценок")
  @Issue("TASK-201")
  void testCalculateAverageGradesBySubject() {
    stepPrepareTestData();
    Map<String, Double> avgGrades = stepCalculateAverageGrades();
    stepVerifyAverageGrades(avgGrades);
  }

  @Test
  @DisplayName("Поиск лучшего ученика")
  @Description("Проверяем, что сервис правильно находит ученика(ов) с самым высоким средним баллом")
  @Severity(SeverityLevel.NORMAL)
  @Story("Рейтинг учеников")
  @Issue("TASK-202")
  void testFindBestStudent() {
    stepPrepareTestData();
    List<Student> bestStudents = stepFindBestStudents();
    stepVerifyBestStudents(bestStudents);
  }

  @Test
  @DisplayName("Поиск худшего ученика")
  @Description("Проверяем, что сервис правильно находит ученика(ов) с самым низким средним баллом")
  @Severity(SeverityLevel.NORMAL)
  @Story("Рейтинг учеников")
  @Issue("TASK-203")
  void testFindWorstStudent() {
    stepPrepareTestData();
    List<Student> worstStudents = stepFindWorstStudents();
    stepVerifyWorstStudents(worstStudents);
  }

  @Step("Подготовка тестовых данных")
  private void stepPrepareTestData() {
    Allure.parameter("Количество студентов", students.size());

    StringBuilder studentsInfo = new StringBuilder();
    for (Student student : students) {
      studentsInfo.append(String.format("Студент: %s, Средний балл: %.2f%n",
          student.getFullName(), student.getAverageGrade()));
    }

    Allure.attachment("Тестовые данные", studentsInfo.toString());
  }

  @Step("Расчет средних оценок по предметам")
  private Map<String, Double> stepCalculateAverageGrades() {
    Map<String, Double> avgGrades = analyzerService.calculateAverageGradesBySubject(students);

    StringBuilder gradesInfo = new StringBuilder();
    for (Map.Entry<String, Double> entry : avgGrades.entrySet()) {
      gradesInfo.append(String.format("%s: %.2f%n", entry.getKey(), entry.getValue()));
    }

    Allure.attachment("Результаты расчета", gradesInfo.toString());
    return avgGrades;
  }

  @Step("Проверка корректности расчета средних оценок")
  private void stepVerifyAverageGrades(Map<String, Double> avgGrades) {
    Allure.parameter("Ожидаемая средняя по математике", 4.0);
    Allure.parameter("Фактическая средняя по математике", avgGrades.get("Математика"));

    assertEquals(4.0, avgGrades.get("Математика"), 0.01,
        "Средняя оценка по математике должна быть 4.0");

    Allure.parameter("Ожидаемая средняя по физике", 4.0);
    Allure.parameter("Фактическая средняя по физике", avgGrades.get("Физика"));

    assertEquals(4.0, avgGrades.get("Физика"), 0.01,
        "Средняя оценка по физике должна быть 4.0");
  }

  @Step("Поиск лучших студентов")
  private List<Student> stepFindBestStudents() {
    List<Student> bestStudents = analyzerService.findBestStudents(students);

    StringBuilder bestInfo = new StringBuilder();
    for (Student student : bestStudents) {
      bestInfo.append(String.format("Студент: %s, Средний балл: %.2f%n",
          student.getFullName(), student.getAverageGrade()));
    }

    Allure.attachment("Лучшие студенты", bestInfo.toString());
    return bestStudents;
  }

  @Step("Проверка результатов поиска лучших студентов")
  private void stepVerifyBestStudents(List<Student> bestStudents) {
    Allure.parameter("Ожидаемое количество лучших студентов", 2);
    Allure.parameter("Фактическое количество лучших студентов", bestStudents.size());

    assertEquals(2, bestStudents.size(),
        "Должно быть найдено 2 лучших студента с одинаковым средним баллом");

    assertTrue(bestStudents.stream().anyMatch(s -> s.getFullName().equals("Иванов Иван Иванович")),
        "Среди лучших студентов должен быть Иванов Иван Иванович");

    assertTrue(bestStudents.stream().anyMatch(s -> s.getFullName().equals("Петров Петр Петрович")),
        "Среди лучших студентов должен быть Петров Петр Петрович");
  }

  @Step("Поиск худших студентов")
  private List<Student> stepFindWorstStudents() {
    List<Student> worstStudents = analyzerService.findWorstStudents(students);

    StringBuilder worstInfo = new StringBuilder();
    for (Student student : worstStudents) {
      worstInfo.append(String.format("Студент: %s, Средний балл: %.2f%n",
          student.getFullName(), student.getAverageGrade()));
    }

    Allure.attachment("Худшие студенты", worstInfo.toString());
    return worstStudents;
  }

  @Step("Проверка результатов поиска худших студентов")
  private void stepVerifyWorstStudents(List<Student> worstStudents) {
    Allure.parameter("Ожидаемое количество худших студентов", 1);
    Allure.parameter("Фактическое количество худших студентов", worstStudents.size());

    assertEquals(1, worstStudents.size(),
        "Должен быть найден 1 худший студент");

    assertEquals("Сидоров Сидор Сидорович", worstStudents.getFirst().getFullName(),
        "Худшим студентом должен быть Сидоров Сидор Сидорович");
  }

  private List<Student> createTestStudents() {
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

    return Arrays.asList(student1, student2, student3);
  }
}
