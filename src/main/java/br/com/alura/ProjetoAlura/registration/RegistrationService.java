package br.com.alura.ProjetoAlura.registration;

import br.com.alura.ProjetoAlura.course.Course;
import br.com.alura.ProjetoAlura.course.CourseRepository;
import br.com.alura.ProjetoAlura.user.Role;
import br.com.alura.ProjetoAlura.user.UserRepository;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;


    public RegistrationService(RegistrationRepository registrationRepository, CourseRepository courseRepository, UserRepository userRepository) {
        this.registrationRepository = registrationRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public void registerStudent(NewRegistrationDTO newRegistration) {
        Course course = courseRepository.findByCode(newRegistration.getCourseCode())
                .orElseThrow(() -> new IllegalArgumentException("Curso não encontrado."));

        if (!course.isActive()) {
            throw new IllegalArgumentException("Curso não está ativo.");
        }

        boolean isInstructor = userRepository.findByEmail(newRegistration.getStudentEmail())
                .map(user -> user.getRole() == Role.INSTRUCTOR)
                .orElse(false);

        if (isInstructor) {
            throw new IllegalArgumentException("E-mail pertence a um instrutor. Não é permitido registrar instrutores.");
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

    public List<RegistrationReportItem> generateReport() {
        List<Tuple> results = registrationRepository.findMostPopularCourses();
        List<RegistrationReportItem> reportItems = new ArrayList<>();

        for (Tuple tuple : results) {
            String courseName = tuple.get(0, String.class);
            String courseCode = tuple.get(1, String.class);
            String instructorName = tuple.get(2, String.class);
            String instructorEmail = tuple.get(3, String.class);
            Long totalRegistrations = tuple.get(4, Long.class);

            RegistrationReportItem item = new RegistrationReportItem(courseName, courseCode, instructorName, instructorEmail, totalRegistrations);
            reportItems.add(item);
        }

        return reportItems;
    }
}
