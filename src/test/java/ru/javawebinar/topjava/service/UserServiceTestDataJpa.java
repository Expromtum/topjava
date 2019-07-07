package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;

import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(profiles = {Profiles.DATAJPA})
public class UserServiceTestDataJpa extends UserServiceTest {

    @Test
    public void getUserWithMeal() throws Exception {
        User testUser = new User(USER);
        testUser.setMeals(List.of(MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1));

        User user = service.getWithMeal(USER_ID);
        assertMatchWithMeal(user, testUser);
    }
}
