package org.example.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.example.model.Student;

/**
 * Сервис для расчета различных метрик по оценкам.
 */
public class GradeCalculator {

  /**
   * Рассчитывает средний балл по каждому предмету.
   */
  public Map<String, Double> calculateAverageGradesBySubject(List<Student> students) {
    Map<String, List<Integer>> gradesBySubject = groupGradesBySubject(students);
    Map<String, Double> avgGrades = new HashMap<>();

    for (Map.Entry<String, List<Integer>> entry : gradesBySubject.entrySet()) {
      String subject = entry.getKey();
      List<Integer> grades = entry.getValue();
      double average = calculateAverage(grades);
      avgGrades.put(subject, average);
    }

    return avgGrades;
  }

  private Map<String, List<Integer>> groupGradesBySubject(List<Student> students) {
    Map<String, List<Integer>> gradesBySubject = new HashMap<>();

    for (Student student : students) {
      for (Map.Entry<String, Integer> entry : student.getGrades().entrySet()) {
        String subject = entry.getKey();
        Integer grade = entry.getValue();

        gradesBySubject.computeIfAbsent(subject, k -> new ArrayList<>()).add(grade);
      }
    }

    return gradesBySubject;
  }

  private double calculateAverage(List<Integer> grades) {
    if (grades.isEmpty()) {
      return 0.0;
    }

    double sum = grades.stream().mapToInt(Integer::intValue).sum();
    return sum / grades.size();
  }
}
