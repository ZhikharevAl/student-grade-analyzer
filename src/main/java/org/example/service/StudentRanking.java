package org.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import org.example.model.Student;

/**
 * Сервис для ранжирования студентов по успеваемости.
 */
public class StudentRanking {

  /**
   * Находит студентов с наивысшим средним баллом.
   */
  public List<Student> findBestStudents(List<Student> students) {
    if (students.isEmpty()) {
      return new ArrayList<>();
    }

    double maxAverage = findMaxAverage(students);
    return filterStudentsByAverage(students, maxAverage, true);
  }

  /**
   * Находит студентов с наименьшим средним баллом.
   */
  public List<Student> findWorstStudents(List<Student> students) {
    if (students.isEmpty()) {
      return new ArrayList<>();
    }

    double minAverage = findMinAverage(students);
    return filterStudentsByAverage(students, minAverage, false);
  }

  private double findMaxAverage(List<Student> students) {
    OptionalDouble maxAverage = students.stream()
        .mapToDouble(Student::getAverageGrade)
        .max();
    return maxAverage.orElse(-1.0);
  }

  private double findMinAverage(List<Student> students) {
    OptionalDouble minAverage = students.stream()
        .mapToDouble(Student::getAverageGrade)
        .min();
    return minAverage.orElse(6.0);
  }

  private List<Student> filterStudentsByAverage(List<Student> students,
                                                double targetAverage,
                                                boolean isMaximum) {
    List<Student> result = new ArrayList<>();

    for (Student student : students) {
      double studentAverage = student.getAverageGrade();
      boolean matches = isMaximum ?
          studentAverage >= targetAverage :
          studentAverage <= targetAverage;

      if (matches) {
        result.add(student);
      }
    }

    return result;
  }
}
