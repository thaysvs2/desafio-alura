package br.com.alura.ProjetoAlura.course;

import br.com.alura.ProjetoAlura.user.User;
import br.com.alura.ProjetoAlura.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CourseControllerTest {

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseController courseController;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCourseSuccess() {
        // Mocking the behavior
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setCode("JAVABA");
        newCourseDTO.setName("Java Basics");
        newCourseDTO.setDescription("A course for beginners in Java.");
        newCourseDTO.setInstructorEmail("instructor@example.com");

        User instructor = new User();
        instructor.setEmail("instructor@example.com");

        // Mocking courseService methods
        when(courseService.existsByCode("JAVABA")).thenReturn(false);
        when(courseService.isInstructor("instructor@example.com")).thenReturn(true);
        when(courseService.getInstructorByEmail("instructor@example.com")).thenReturn(instructor);

        // Calling the controller method
        ResponseEntity<String> response = courseController.createCourse(newCourseDTO);

        // Verifying the response status and message
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Course created successfully!", response.getBody());

        // Verifying that the save method was called on courseService
        verify(courseService, times(1)).save(any(Course.class));
    }

    @Test
    public void testCreateCourseCodeAlreadyExists() {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setCode("JAVABA");
        newCourseDTO.setName("Java Basics");
        newCourseDTO.setDescription("A course for beginners in Java.");
        newCourseDTO.setInstructorEmail("instructor@example.com");

        // Mocking courseService methods
        when(courseService.existsByCode("JAVABA")).thenReturn(true);

        // Calling the controller method
        ResponseEntity<String> response = courseController.createCourse(newCourseDTO);

        // Verifying the response status and message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Course code already exists. Please choose a unique code.", response.getBody());
    }

    @Test
    public void testCreateCourseNotInstructor() {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setCode("JAVABA");
        newCourseDTO.setName("Java Basics");
        newCourseDTO.setDescription("A course for beginners in Java.");
        newCourseDTO.setInstructorEmail("student@example.com");

        // Mocking courseService methods
        when(courseService.existsByCode("JAVABA")).thenReturn(false);
        when(courseService.isInstructor("student@example.com")).thenReturn(false);

        // Calling the controller method
        ResponseEntity<String> response = courseController.createCourse(newCourseDTO);

        // Verifying the response status and message
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Only instructors can create courses.", response.getBody());
    }

    @Test
    public void testDeactivateCourseSuccess() {
        // Mocking the course service
        Course course = new Course();
        course.setCode("JAVABA");
        course.setStatus(Course.Status.ACTIVE);

        when(courseService.findByCode("JAVABA")).thenReturn(java.util.Optional.of(course));

        // Calling the controller method
        ResponseEntity<String> response = courseController.deactivateCourse("JAVABA");

        // Verifying the response status and message
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Course has been successfully deactivated.", response.getBody());

        // Verifying the course service method is called
        verify(courseService, times(1)).save(course);
    }

    @Test
    public void testDeactivateCourseNotFound() {
        // Mocking the course service to return an empty Optional
        when(courseService.findByCode("JAVABA")).thenReturn(java.util.Optional.empty());

        // Calling the controller method
        ResponseEntity<String> response = courseController.deactivateCourse("JAVABA");

        // Verifying the response status and message
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Course not found with the provided code.", response.getBody());
    }
}
