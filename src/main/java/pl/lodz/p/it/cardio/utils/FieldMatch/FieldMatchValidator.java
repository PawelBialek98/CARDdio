package pl.lodz.p.it.cardio.utils.FieldMatch;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

    private String firstField;
    private String secondField;

    @Override
    public void initialize(FieldMatch fieldMatch) {
        firstField = fieldMatch.first();
        secondField = fieldMatch.second();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        try
        {
            final Object firstFieldValue = getFieldValue(value, firstField);
            final Object secondFieldValue = getFieldValue(value, secondField);

            return firstFieldValue != null && firstFieldValue.equals(secondFieldValue);
        }
        catch (final Exception ignore)
        {
            Logger.getGlobal().log(Level.INFO, "Passwords do not match");
            return false;
        }
    }

    private Object getFieldValue(Object object, String fieldName) throws Exception {
        Class<?> clazz = object.getClass();
        Field passwordField = clazz.getDeclaredField(fieldName);
        passwordField.setAccessible(true);
        return passwordField.get(object);
    }
}