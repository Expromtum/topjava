package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.util.DateTimeUtil.DATE_TIME_FORMATTER;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService service;

    @Test
    public void get() {
        assertThat(service.get(MEAL_ID, USER_ID)).usingDefaultComparator().isEqualTo(UPDATE_MEAL);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        service.get(OTHER_MEAL_ID, USER_ID);
    }

    @Test
    public void delete() {
        service.delete(DEL_MEAL.getId(), USER_ID);
        assertThat(service.getAll(USER_ID)).usingDefaultComparator().doesNotContain(DEL_MEAL);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() {
        service.delete(OTHER_MEAL_ID, USER_ID);
    }

    @Test
    public void update() {
        Meal updatedMeal = new Meal(UPDATE_MEAL);
        updatedMeal.setDateTime(LocalDateTime.of(2019, 7, 15, 10, 20));
        updatedMeal.setCalories(100);
        service.update(updatedMeal, USER_ID);
        assertThat(service.get(updatedMeal.getId(), USER_ID)).usingDefaultComparator().isEqualTo(updatedMeal);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        Meal otherMeal = new Meal(OTHER_MEAL_ID, LocalDateTime.parse("2019-01-01 09:00",
                DATE_TIME_FORMATTER), "ADMIN завтрак", 500);
        service.update(otherMeal, USER_ID);
    }

    @Test
    public void create() {
        Meal created = service.create(NEW_MEAL, USER_ID);
        NEW_MEAL.setId(created.getId());
        assertThat(service.get(NEW_MEAL.getId(), USER_ID)).usingDefaultComparator().isEqualTo(NEW_MEAL);
        assertThat(service.getAll(USER_ID)).usingDefaultComparator().contains(NEW_MEAL);
    }

    @Test
    public void getBetweenDates() {
        LocalDate startDate = LocalDate.of(2019, 1, 1);
        LocalDate endDate = LocalDate.of(2019, 1, 2);

        assertThat(service.getBetweenDates(startDate, endDate, USER_ID)).usingDefaultComparator()
                .isEqualTo(getSortedList(filterMeals));
    }

    @Test
    public void getBetweenDateTimes() {
        LocalDateTime startDate = LocalDateTime.of(2019, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2019, 1, 2, 23, 59);

        assertThat(service.getBetweenDateTimes(startDate, endDate, USER_ID))
                .usingDefaultComparator().isEqualTo(getSortedList(filterMeals));
    }

    @Test
    public void getAll() {
        assertThat(service.getAll(USER_ID)).usingDefaultComparator().isEqualTo(getSortedList(userMeals));
    }
}