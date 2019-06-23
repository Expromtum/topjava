package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;
import static ru.javawebinar.topjava.util.DateTimeUtil.DATE_TIME_FORMATTER;

public class MealTestData {
    public static final int USER_ID = START_SEQ;
    public static final int MEAL_ID = START_SEQ + 2;
    public static final int OTHER_MEAL_ID = MEAL_ID + 9;

    public static List<Meal> userMeals = new ArrayList<>();
    public static List<Meal> filterMeals = new ArrayList<>();

    public static Meal NEW_MEAL = new Meal(null, LocalDateTime.parse("2019-07-01 09:00", DATE_TIME_FORMATTER), "USER завтрак NEW", 500);
    public static Meal UPDATE_MEAL = new Meal(MEAL_ID, LocalDateTime.parse("2019-01-01 09:00", DATE_TIME_FORMATTER), "USER завтрак1", 500);
    public static Meal DEL_MEAL = new Meal(MEAL_ID + 8,  LocalDateTime.parse("2025-07-03 18:11", DATE_TIME_FORMATTER), "USER ужин FUTURE", 777);

    static {
        userMeals.add(new Meal(MEAL_ID, LocalDateTime.parse("2019-01-01 00:01", DATE_TIME_FORMATTER), "USER завтрак1", 500));
        userMeals.add(new Meal(MEAL_ID + 1, LocalDateTime.parse("2019-01-01 12:00", DATE_TIME_FORMATTER), "USER обед1", 700));
        userMeals.add(new Meal(MEAL_ID + 2, LocalDateTime.parse("2019-01-01 19:00", DATE_TIME_FORMATTER), "USER ужин1", 500));
        userMeals.add(new Meal(MEAL_ID + 3, LocalDateTime.parse("2019-01-02 09:00", DATE_TIME_FORMATTER), "USER завтрак2", 300));
        userMeals.add(new Meal(MEAL_ID + 4, LocalDateTime.parse("2019-01-02 12:30", DATE_TIME_FORMATTER), "USER обед2", 300));
        userMeals.add(new Meal(MEAL_ID + 5, LocalDateTime.parse("2019-01-02 23:59", DATE_TIME_FORMATTER), "USER ужин2", 300));

        filterMeals.addAll(userMeals);

        userMeals.add(new Meal(MEAL_ID + 6, LocalDateTime.parse("2025-07-03 09:00", DATE_TIME_FORMATTER), "USER завтрак FUTURE", 555));
        userMeals.add(new Meal(MEAL_ID + 7, LocalDateTime.parse("2025-07-03 12:11", DATE_TIME_FORMATTER), "USER обед FUTURE", 777));
        userMeals.add(new Meal(MEAL_ID + 8, LocalDateTime.parse("2025-07-03 18:11", DATE_TIME_FORMATTER), "USER ужин FUTURE", 777));


        /* OtherMeals
        INSERT INTO meals (user_id, dateTime, description, calories) VALUES
        (100001, to_timestamp('2019-01-01 09:00', 'YYYY-MM-DD HH24:MI'), 'ADMIN завтрак', 500),
        (100001, to_timestamp('2019-01-01 12:00', 'YYYY-MM-DD HH24:MI'), 'ADMIN обед', 700),
        (100001, to_timestamp('2019-01-01 19:00', 'YYYY-MM-DD HH24:MI'), 'ADMIN ужин', 700);
         */
    }

    public static List<Meal> getSortedList(List<Meal> meals) {
        return meals.stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

