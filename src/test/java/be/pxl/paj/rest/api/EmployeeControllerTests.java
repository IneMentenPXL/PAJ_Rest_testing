package be.pxl.paj.rest.api;


import be.pxl.paj.rest.domain.Employee;
import be.pxl.paj.rest.domain.EmployeeBuilder;
import be.pxl.paj.rest.exception.EmployeeNotFoundException;
import be.pxl.paj.rest.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeController employeeController;

    @Test
    public void testFindByIdMockito() {
        long employeeId = 1;
        String employeeName = "Bilbo Baggins";
        String employeeRole = "burglar";

        Employee employee = EmployeeBuilder.anEmployee().withId(employeeId).withName(employeeName).withRole(employeeRole).build();
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        Employee response = employeeController.find(employeeId);

        assertEquals(employeeId, response.getId());
        assertEquals(employeeName, response.getName());
        assertEquals(employeeRole, response.getRole());
    }

    @Test
    public void testFindByIdWithEmployeeNotFoundExceptionMockito() {
        long employeeId = 1;

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());
        assertThrows(EmployeeNotFoundException.class, () -> employeeController.find(employeeId));
    }

    @Test
    public void testCreateEmployeeMockito() {
        long employeeId = 1;
        String employeeName = "Bilbo Baggins";
        String employeeRole = "burglar";

        when(employeeRepository.save(any(Employee.class))).thenAnswer(returnsFirstArg());
        Employee employee = EmployeeBuilder.anEmployee().build();
        ResponseEntity<Employee> response = employeeController.createEmployee(employee);
        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertTrue(response.getHeaders().getLocation().getPath().endsWith("/" + employeeId));
        Employee responseBody = response.getBody();
        assertEquals(employeeId, responseBody.getId());
        assertEquals(employeeName, responseBody.getName());
        assertEquals(employeeRole, responseBody.getRole());
    }

    @Test
    public void testFindByIdWebMVC() throws Exception {
        long employeeId = 1;
        String employeeName = "Bilbo Baggins";
        String employeeRole = "burglar";

        Employee employee = EmployeeBuilder.anEmployee()
                .withId(employeeId).withName(employeeName).withRole(employeeRole).build();
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        mockMvc.perform(MockMvcRequestBuilders.get("/employees/{id}", employeeId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(employeeId))
                .andExpect(jsonPath("$.name").value(employeeName))
                .andExpect(jsonPath("$.role").value(employeeRole));
    }

    @Test
    public void testFindByIdWithEmployeeNotFoundExceptionWebMVC() throws Exception {
        long employeeId = 3;

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/employees/{id}", employeeId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateEmployeeWebMVC() throws Exception {
        long employeeId = 3;

        when(employeeRepository.save(any(Employee.class))).thenAnswer(iom -> {
            Employee createdEmployee = iom.getArgument(0);
            createdEmployee.setId(employeeId);
            return createdEmployee;
        });
        mockMvc.perform(MockMvcRequestBuilders.post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Demo\", \"role\": \"developer\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/employees/" + employeeId))
                .andExpect(jsonPath("$.id").value(employeeId))
                .andExpect(jsonPath("$.name").value("Demo"))
                .andExpect(jsonPath("$.role").value("developer"));
    }
}
