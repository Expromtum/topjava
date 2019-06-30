package ru.javawebinar.topjava.service;

import javafx.util.Pair;
import org.junit.*;
import org.junit.rules.TestName;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.validation.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static java.time.LocalDateTime.of;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.model.Role.ROLE_USER;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService service;

    private long currentMilliseconds;

    @Rule
    public final TestName name = new TestName();
    private static final Logger log = LoggerFactory.getLogger("MealServiceTest");

    private static List<Pair<String, Long>> testTime = new ArrayList<>();
    private static Validator validator;

    @Rule
    public final TestRule watchman = new TestWatcher() {

        @Override
        protected void starting(Description description) {
            currentMilliseconds = System.currentTimeMillis();
            super.starting(description);
        }

        @Override
        protected void finished(Description description) {
            super.finished(description);

            long millisec = System.currentTimeMillis() - currentMilliseconds;
            testTime.add(new Pair<>(name.getMethodName(), millisec));
            log.info(name.getMethodName() + ": " + millisec + " millisec");
        }
    };

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        testTime.forEach(time ->
                log.info(time.getKey() + ": " + time.getValue() + " millisec"));
    }

    @Test
    public void delete() throws Exception {
        service.delete(MEAL1_ID, USER_ID);
        assertMatch(service.getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() throws Exception {
        service.delete(1, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotOwn() throws Exception {
        service.delete(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void create() throws Exception {
        Meal newMeal = getCreated();
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatch(newMeal, created);
        assertMatch(service.getAll(USER_ID), newMeal, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void get() throws Exception {
        Meal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        actual.setUser(null);
        assertMatch(actual, ADMIN_MEAL1);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception {
        service.get(1, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getNotOwn() throws Exception {
        service.get(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void update() throws Exception {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL1_ID, USER_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() throws Exception {
        service.update(MEAL1, ADMIN_ID);
    }

    @Test
    public void getAll() throws Exception {
        assertMatch(service.getAll(USER_ID), MEALS);
    }

    @Test
    public void getBetween() throws Exception {
        assertMatch(service.getBetweenDates(
                LocalDate.of(2015, Month.MAY, 30),
                LocalDate.of(2015, Month.MAY, 30), USER_ID), MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void mealIsValid() {
        Meal newMeal = getCreated();
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());

        Set<ConstraintViolation<Meal>> validationErrors = validator.validate(newMeal);
        assertThat(validationErrors.size()).isEqualTo(0);
    }

    @Test
    public void mealIsNotValid() {
        Meal invalidMeal = new Meal(MEAL1_ID + 5, of(2015, Month.MAY, 31, 20, 0), "Ужин", 7000);
        invalidMeal.setUser(new User(1, "user", "email", "password", ROLE_USER));
        Set<ConstraintViolation<Meal>> validationErrors  = validator.validate(invalidMeal);

        assertThat(validationErrors.size()).isEqualTo(1);
        Assert.assertEquals("{javax.validation.constraints.Max.message}", validationErrors.iterator().next().getMessageTemplate());
    }
}