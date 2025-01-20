package br.com.alura.ProjetoAlura.registration;

import br.com.alura.ProjetoAlura.course.Course;
import br.com.alura.ProjetoAlura.course.CourseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    public List<RegistrationReportItem> generateReport() {
        List<Object[]> results = registrationRepository.findMostPopularCourses();

        List<RegistrationReportItem> reportItems = new ArrayList<>();

        for (Object[] row : results) {
            String courseName = (String) row[0];
            String courseCode = (String) row[1];
            String instructorName = (String) row[2];
            String instructorEmail = (String) row[3];
            Long totalRegistrations = ((Number) row[4]).longValue();

            RegistrationReportItem item = new RegistrationReportItem(courseName, courseCode, instructorName, instructorEmail, totalRegistrations);
            reportItems.add(item);
        }

        return reportItems;
    }
}
