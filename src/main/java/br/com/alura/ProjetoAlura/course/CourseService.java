package br.com.alura.ProjetoAlura.course;

import br.com.alura.ProjetoAlura.user.User;
import br.com.alura.ProjetoAlura.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final Set<String> instructors;

    public CourseService(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.instructors = new HashSet<>();
        instructors.add("instrutor1@alura.com");
        instructors.add("instrutor2@alura.com");
    }

    public boolean existsByCode(String code) {
        return courseRepository.existsByCode(code);
    }

    public Optional<Course> findByCode(String code) {
        return courseRepository.findByCode(code);
    }

    public boolean isInstructor(String email) {
        return instructors.contains(email);
    }

    public void save(Course course) {
        courseRepository.save(course);
    }

    public User getInstructorByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found"));
    }
}
