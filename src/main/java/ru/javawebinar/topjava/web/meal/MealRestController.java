package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

import static ru.javawebinar.topjava.util.DateTimeUtil.*;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public Collection<MealTo> getAll() {
        int userId = SecurityUtil.authUserId();
        log.info("getAll Meal for User {}", userId);

        return MealsUtil.getWithExcess(service.getAll(userId),
                MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }


    public Collection<MealTo> getBetween(@Nullable LocalDate startDate, @Nullable LocalDate endDate,
                                         @Nullable LocalTime startTime, @Nullable LocalTime endTime) {
        int userId = SecurityUtil.authUserId();
        log.info("getBetween Meal {} and {} for User {}", startDate, endDate, userId);

        return MealsUtil.getFilteredWithExcess(
                service.getBetween(userId, adjustStartDateTime(startDate), adjustEndDateTime(endDate)),
                MealsUtil.DEFAULT_CALORIES_PER_DAY,
                startTime,
                endTime);
    }

    public Meal get(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("get Meal {} for User {}", id, userId);
        return service.get(id, userId);
    }

    public Meal create(Meal meal) {
        int userId = SecurityUtil.authUserId();
        log.info("create Meal {} for User {}", meal, userId);
        checkNew(meal);
        return service.save(meal);
    }

    public void delete(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("delete Meal {} for User {}", id, userId);
        service.delete(id, userId);
    }

    public void deleteAll(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("deleteAll Meal for User {}", userId);
        service.deleteAll(userId);
    }

    public void update(Meal meal, int id) {
        int userId = SecurityUtil.authUserId();
        log.info("update Meal {} with id={} for User {}", meal, id, userId);
        assureIdConsistent(meal, id);
        service.update(meal, userId);
    }
}