package com.benjamin.authservice.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class FieldsMatchValidator implements ConstraintValidator<FieldsMatch, Object> {
	private String fieldName;
	private String fieldMatchName;
	private String message;

	@Override
	public void initialize(FieldsMatch constraintAnnotation) {
		this.fieldName = constraintAnnotation.field();
		this.fieldMatchName = constraintAnnotation.fieldMatch();
		this.message = constraintAnnotation.message();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		Object fieldValue = getFieldValue(value, fieldName);
		Object fieldMatchValue = getFieldValue(value, fieldMatchName);

		boolean isValid = Objects.equals(fieldValue, fieldMatchValue);

		if (!isValid) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(message)
				  .addPropertyNode(fieldName)
				  .addConstraintViolation();
		}

		return isValid;
	}

	private Object getFieldValue(Object object, String fieldName) {
		try {
			Method method = object.getClass().getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
			return method.invoke(object);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Could not access field: " + fieldName, e);
		}
	}
}
