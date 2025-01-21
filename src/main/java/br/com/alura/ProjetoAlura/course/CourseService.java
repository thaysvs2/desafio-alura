package br.com.alura.ProjetoAlura.course;

import br.com.alura.ProjetoAlura.user.Role;
import br.com.alura.ProjetoAlura.user.User;
import br.com.alura.ProjetoAlura.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseService(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public boolean existsByCode(String code) {
        return courseRepository.existsByCode(code);
    }

    public Optional<Course> findByCode(String code) {
        return courseRepository.findByCode(code);
    }

    // Verifica se o e-mail pertence a um instrutor no banco de dados
    public boolean isInstructor(String email) {
        // Tenta encontrar o usuário no banco de dados pelo e-mail
        Optional<User> user = userRepository.findByEmail(email);

        // Verifica se o usuário existe e se o role é INSTRUCTOR
        return user.map(u -> u.getRole() == Role.INSTRUCTOR).orElse(false);
    }

    public void save(Course course) {
        courseRepository.save(course);
    }

    public User getInstructorByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found"));
    }
}
