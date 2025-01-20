package br.com.alura.ProjetoAlura.course;

import br.com.alura.ProjetoAlura.user.User;
import br.com.alura.ProjetoAlura.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
public class CourseController {

    private final CourseService courseService;

    // Injeta o serviço de cursos
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/course/new")
    public ResponseEntity<String> createCourse(@Validated @RequestBody NewCourseDTO newCourseDTO) {
        try {
            if (courseService.existsByCode(newCourseDTO.getCode())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Course code already exists. Please choose a unique code.");
            }

            if (!courseService.isInstructor(newCourseDTO.getInstructorEmail())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only instructors can create courses.");
            }

            Course course = new Course();
            course.setName(newCourseDTO.getName());
            course.setCode(newCourseDTO.getCode());
            course.setDescription(newCourseDTO.getDescription());
            course.setInstructor(courseService.getInstructorByEmail(newCourseDTO.getInstructorEmail()));
            course.setStatus(Course.Status.ACTIVE); // Define o status como ACTIVE
            courseService.save(course);

            return ResponseEntity.status(HttpStatus.CREATED).body("Course created successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while creating the course.");
        }
    }

    @PostMapping("/course/{code}/inactive")
    public ResponseEntity<String> deactivateCourse(@PathVariable("code") String courseCode) {
        try {
            Optional<Course> courseOptional = courseService.findByCode(courseCode);
            if (courseOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Course not found with the provided code.");
            }

            Course course = courseOptional.get();

            course.setStatus(Course.Status.INACTIVE);

            course.setInactiveDate(LocalDateTime.now());

            courseService.save(course);

            return ResponseEntity.status(HttpStatus.OK)
                    .body("Course has been successfully deactivated.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deactivating the course.");
        }
    }

}