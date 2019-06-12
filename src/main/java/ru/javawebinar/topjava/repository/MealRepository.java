package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;
import java.util.List;

public interface MealRepository {

    Meal save(Meal meal);

    Meal get(int id);

    void delete(int id);

    Collection<Meal> getAll();
}
