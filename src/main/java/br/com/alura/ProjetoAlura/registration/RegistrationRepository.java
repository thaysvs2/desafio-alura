package br.com.alura.ProjetoAlura.registration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    boolean existsByStudentEmailAndCourseCode(String studentEmail, String courseCode);

    @Query(value = "SELECT " +
            "c.name AS courseName, " +
            "c.code AS courseCode, " +
            "u.name AS instructorName, " +
            "u.email AS instructorEmail, " +
            "COUNT(r.id) AS totalRegistrations " +
            "FROM registration r " +
            "JOIN course c ON r.course_id = c.id " +
            "JOIN user u ON c.instructor_id = u.id " +
            "GROUP BY c.id, u.id " +
            "ORDER BY totalRegistrations DESC",
            nativeQuery = true)
    List<Object[]> findMostPopularCourses();
}

