package be.pxl.paj.rest.domain;

public class EmployeeBuilder {
    private static Employee employee;

    public EmployeeBuilder() {
        employee = new Employee();
        employee.setId(1L);
        employee.setRole("burglar");
        employee.setName("Bilbo Baggins");
    }

    public static EmployeeBuilder anEmployee() {
        return new EmployeeBuilder();
    }

    public EmployeeBuilder withId(long id) {
        employee.setId(id);
        return this;
    }

    public EmployeeBuilder withName(String name) {
        employee.setName(name);
        return this;
    }

    public EmployeeBuilder withRole(String role) {
        employee.setRole(role);
        return this;
    }

    public Employee build() {
        return employee;
    }
}
