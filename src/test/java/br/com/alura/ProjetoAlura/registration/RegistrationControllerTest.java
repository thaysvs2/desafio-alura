package br.com.alura.ProjetoAlura.registration;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(RegistrationController.class)
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistrationService registrationService;

    private List<RegistrationReportItem> reportItems;

    @BeforeEach
    public void setup() {
        reportItems = Arrays.asList(
                new RegistrationReportItem("Curso 1", "C1", "Instrutor 1", "instrutor1@example.com", 100L),
                new RegistrationReportItem("Curso 2", "C2", "Instrutor 2", "instrutor2@example.com", 50L)
        );
    }

    @Test
    void testGetRegistrationReport() throws Exception {
        when(registrationService.generateReport()).thenReturn(reportItems);

        mockMvc.perform(get("/registration/report"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseName").value("Curso 1"))
                .andExpect(jsonPath("$[0].courseCode").value("C1"))
                .andExpect(jsonPath("$[0].instructorName").value("Instrutor 1"))
                .andExpect(jsonPath("$[0].instructorEmail").value("instrutor1@example.com"))
                .andExpect(jsonPath("$[0].totalRegistrations").value(100))

                .andExpect(jsonPath("$[1].courseName").value("Curso 2"))
                .andExpect(jsonPath("$[1].courseCode").value("C2"))
                .andExpect(jsonPath("$[1].instructorName").value("Instrutor 2"))
                .andExpect(jsonPath("$[1].instructorEmail").value("instrutor2@example.com"))
                .andExpect(jsonPath("$[1].totalRegistrations").value(50));
    }

    @Test
    void testGetRegistrationReport_WhenEmpty() throws Exception {
        when(registrationService.generateReport()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/registration/report"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
