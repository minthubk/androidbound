package solutions.alterego.androidbound.converters.interfaces;

import java.util.Locale;

public interface IValueConverter {

    Object convert(Object value, Class<?> targetType, Object param, Locale locale);

    Object convertBack(Object value, Class<?> targetType, Object param, Locale locale);
}
