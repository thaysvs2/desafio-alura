package br.com.alura.ProjetoAlura.course;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public class NewCourseDTO {

    @NotNull(message = "Name cannot be null.")
    @NotBlank(message = "Name cannot be blank.")
    private String name;

    @NotNull(message = "Code cannot be null.")
    @NotBlank(message = "Code cannot be blank.")
    @Length(min = 4, max = 10, message = "Code must be between 4 and 10 characters.")
    @Pattern(regexp = "^[a-zA-Z-]+$", message = "The code must contain only letters and hyphens.")
    private String code;

    private String description;

    @NotNull(message = "Instructor email cannot be null.")
    @NotBlank(message = "Instructor email cannot be blank.")
    @Email(message = "Invalid email format.")
    private String instructorEmail;

    public NewCourseDTO() {}

    public NewCourseDTO(String name, String code, String description, String instructorEmail) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.instructorEmail = instructorEmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstructorEmail() {
        return instructorEmail;
    }

    public void setInstructorEmail(String instructorEmail) {
        this.instructorEmail = instructorEmail;
    }

    @Override
    public String toString() {
        return "NewCourseDTO{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", instructorEmail='" + instructorEmail + '\'' +
                '}';
    }
}
