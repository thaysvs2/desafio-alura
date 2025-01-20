package br.com.alura.ProjetoAlura.registration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    boolean existsByStudentEmailAndCourseCode(String studentEmail, String courseCode);
}

