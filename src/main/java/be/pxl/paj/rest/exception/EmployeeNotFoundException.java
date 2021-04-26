package be.pxl.paj.rest.exception;

public class EmployeeNotFoundException extends RuntimeException {

	public EmployeeNotFoundException(long id) {
		super("No employee found with id [" + id + "]");
	}
}
