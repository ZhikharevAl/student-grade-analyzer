package org.example.model;

import java.util.Map;
import java.util.Objects;

/**
 * Модель данных студента.
 */
public class Student {
  private final String fullName;
  private final Map<String, Integer> grades;

  public Student(String fullName, Map<String, Integer> grades) {
    this.fullName = fullName;
    this.grades = grades;
  }

  public String getFullName() {
    return fullName;
  }

  public Map<String, Integer> getGrades() {
    return grades;
  }

  /**
   * Расчет среднего балла
   */
  public double getAverageGrade() {
    if (grades.isEmpty()) {
      return 0.0;
    }
    double sum = 0;
    for (Integer grade : grades.values()) {
      sum += grade;
    }
    return sum / grades.size();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Student student = (Student) o;
    return Objects.equals(fullName, student.fullName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fullName);
  }
}
