package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private final MealRepository repository;

    @Autowired
    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal save(Meal meal) {
        return repository.save(meal);
    }

    public void delete(int id, int userID) throws NotFoundException {
        checkNotFoundWithId(repository.delete(id, userID), id);
    }

    public void deleteAll(int userID) {
        repository.deleteAll(userID);
    }

    public Meal get(int id, int userID) throws NotFoundException {
        return checkNotFoundWithId(repository.get(id, userID), id);
    }

    public void update(Meal meal, int userID) {
        checkNotFoundWithId(repository.save(meal), meal.getId());
    }

    public Collection<Meal> getAll() {
        return repository.getAll();
    }

    public Collection<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    public Collection<Meal> getBetween(int userId, LocalDateTime startDate, LocalDateTime endDate) {
        return repository.getBetween(userId, startDate, endDate);
    }
}