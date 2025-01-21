package br.com.alura.ProjetoAlura.registration;

import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    boolean existsByStudentEmailAndCourseCode(String studentEmail, String courseCode);

    @Query(value = "SELECT c.name, c.code, u.name, u.email, COUNT(r.id) " +
            "FROM registration r " +
            "JOIN course c ON r.course_id = c.id " +
            "JOIN user u ON c.instructor_id = u.id " +
            "GROUP BY c.id, u.id " +
            "ORDER BY COUNT(r.id) DESC", nativeQuery = true)
    List<Tuple> findMostPopularCourses();

}

