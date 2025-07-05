package org.example.service;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Link;
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

@DisplayName("Тестирование калькулятора оценок")
@Epic("Анализ успеваемости")
@Feature("Калькулятор оценок")
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
  @Description("Проверяем корректность расчета среднего балла когда у студентов разные предметы")
  @Severity(SeverityLevel.CRITICAL)
  @Story("Расчет средних оценок")
  @Link(name = "Требования", url = "https://example.com/requirements")
  @Issue("TASK-301")
  void testCalculateAverageGradesBySubject() {
    stepPrepareTestStudents();
    Map<String, Double> avgGrades = stepCalculateAverageGrades();
    stepVerifyAverageGrades(avgGrades);
  }

  @Test
  @DisplayName("Расчет среднего балла для пустого списка студентов")
  @Description("Проверяем, что сервис корректно обрабатывает пустой список")
  @Severity(SeverityLevel.NORMAL)
  @Story("Граничные случаи")
  @Link(name = "Требования", url = "https://example.com/requirements")
  @Issue("TASK-302")
  void testCalculateAverageGradesWithEmptyList() {
    stepPrepareEmptyList();
    Map<String, Double> avgGrades = stepCalculateAverageGradesForEmptyList();
    stepVerifyEmptyResult(avgGrades);
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

  @Step("Расчет средних оценок по предметам")
  private Map<String, Double> stepCalculateAverageGrades() {
    Map<String, Double> avgGrades = gradeCalculator.calculateAverageGradesBySubject(students);

    StringBuilder gradesInfo = new StringBuilder();
    for (Map.Entry<String, Double> entry : avgGrades.entrySet()) {
      gradesInfo.append(String.format("%s: %.2f%n", entry.getKey(), entry.getValue()));
    }

    Allure.attachment("Результаты расчета средних оценок", gradesInfo.toString());
    Allure.parameter("Количество предметов", avgGrades.size());

    return avgGrades;
  }

  @Step("Проверка корректности расчета средних оценок")
  private void stepVerifyAverageGrades(Map<String, Double> avgGrades) {
    // Математика: (5 + 3) / 2 = 4.0
    Allure.parameter("Ожидаемая средняя по математике", 4.0);
    Allure.parameter("Фактическая средняя по математике", avgGrades.get("Математика"));
    assertEquals(4.0, avgGrades.get("Математика"), 0.01,
        "Средняя оценка по математике должна быть 4.0");

    // Физика: (4 + 5) / 2 = 4.5
    Allure.parameter("Ожидаемая средняя по физике", 4.5);
    Allure.parameter("Фактическая средняя по физике", avgGrades.get("Физика"));
    assertEquals(4.5, avgGrades.get("Физика"), 0.01,
        "Средняя оценка по физике должна быть 4.5");

    // Химия: только у одного студента = 3.0
    Allure.parameter("Ожидаемая средняя по химии", 3.0);
    Allure.parameter("Фактическая средняя по химии", avgGrades.get("Химия"));
    assertEquals(3.0, avgGrades.get("Химия"), 0.01,
        "Средняя оценка по химии должна быть 3.0");

    // Биология: только у одного студента = 4.0
    Allure.parameter("Ожидаемая средняя по биологии", 4.0);
    Allure.parameter("Фактическая средняя по биологии", avgGrades.get("Биология"));
    assertEquals(4.0, avgGrades.get("Биология"), 0.01,
        "Средняя оценка по биологии должна быть 4.0");

    Allure.attachment("Результат проверки",
        "Все средние оценки рассчитаны корректно");
  }

  @Step("Подготовка пустого списка студентов")
  private void stepPrepareEmptyList() {
    Allure.parameter("Количество студентов", 0);
    Allure.attachment("Тестовые данные", "Пустой список студентов");
  }

  @Step("Расчет средних оценок для пустого списка")
  private Map<String, Double> stepCalculateAverageGradesForEmptyList() {
    Map<String, Double> avgGrades = gradeCalculator.calculateAverageGradesBySubject(List.of());

    Allure.parameter("Количество предметов в результате", avgGrades.size());
    Allure.attachment("Результат расчета", "Пустая карта средних оценок");

    return avgGrades;
  }

  @Step("Проверка результата для пустого списка")
  private void stepVerifyEmptyResult(Map<String, Double> avgGrades) {
    Allure.parameter("Ожидаемый размер результата", 0);
    Allure.parameter("Фактический размер результата", avgGrades.size());

    assertTrue(avgGrades.isEmpty(),
        "Результат должен быть пустой картой для пустого списка студентов");

    Allure.attachment("Результат проверки",
        "Пустой список студентов корректно обработан");
  }
}
