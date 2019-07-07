package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;

import static ru.javawebinar.topjava.MealTestData.MEAL1;
import static ru.javawebinar.topjava.MealTestData.MEAL1_ID;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(profiles = {Profiles.DATAJPA})
public class MealServiceTestDataJpa extends MealServiceTest {

    @Test
    public void getUserWithMeal() throws Exception {
        Meal testMeal = new Meal(MEAL1);
        testMeal.setUser(USER);
        Meal meal = service.getWithUser(MEAL1_ID, USER_ID);
        MealTestData.assertMatch(meal, testMeal);
    }
}
