package epaw.lab2.model;

public class ValidationError {

	private final String propertyPath;
	private final String message;

	public ValidationError(String propertyPath, String message) {
		this.propertyPath = propertyPath;
		this.message = message;
	}

	public String getPropertyPath() {
		return propertyPath;
	}

	public String getMessage() {
		return message;
	}
}
