package br.com.alura.ProjetoAlura.registration;

import br.com.alura.ProjetoAlura.course.Course;
import br.com.alura.ProjetoAlura.course.CourseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final CourseRepository courseRepository;

    public RegistrationService(RegistrationRepository registrationRepository, CourseRepository courseRepository) {
        this.registrationRepository = registrationRepository;
        this.courseRepository = courseRepository;
    }

    public void registerStudent(NewRegistrationDTO newRegistration) {
        Course course = courseRepository.findByCode(newRegistration.getCourseCode())
                .orElseThrow(() -> new IllegalArgumentException("Curso não encontrado."));

        if (!course.isActive()) {
            throw new IllegalArgumentException("Curso não está ativo.");
        }

        boolean alreadyRegistered = registrationRepository.existsByStudentEmailAndCourseCode(
                newRegistration.getStudentEmail(),
                newRegistration.getCourseCode()
        );

        if (alreadyRegistered) {
            throw new IllegalArgumentException("Aluno já está matriculado neste curso.");
        }

        Registration registration = new Registration();
        registration.setCourse(course);
        registration.setStudentEmail(newRegistration.getStudentEmail());
        registration.setRegistrationDate(LocalDate.now());

        registrationRepository.save(registration);
    }
}
