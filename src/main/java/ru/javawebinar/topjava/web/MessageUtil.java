package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Component
public class MessageUtil {

    public static final Locale RU_LOCALE = new Locale("ru");

    public static final String EXCEPTION_DUPLICATE_EMAIL = "exception.user.duplicateEmail";
    public static final String EXCEPTION_DUPLICATE_DATETIME = "exception.meal.duplicateDateTime";

    @Autowired
    private MessageSource messageSource;

    private static final Map<String, String> constraintMap = new HashMap<>() {
        {
            put("users_unique_email_idx", EXCEPTION_DUPLICATE_EMAIL);
            put("meals_unique_user_datetime_idx", EXCEPTION_DUPLICATE_DATETIME);
        }
    };

    public String getMessage(String message, Locale locale) {
        return messageSource.getMessage(message, null, locale);
    }

    public String getMessage(String message) {
        return messageSource.getMessage(message, null, LocaleContextHolder.getLocale());
    }

    public String translateMessage(String msg) {
        Optional<Map.Entry<String, String>> entry =  constraintMap.entrySet().stream()
                .filter(e -> msg.contains(e.getKey()))
                .findAny();

        if (entry.isPresent()){
            return getMessage(entry.get().getValue());
        } else {
            return msg;
        }
    }    
}
