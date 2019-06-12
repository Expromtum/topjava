package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import ru.javawebinar.topjava.util.MealsUtil;

public class MealRepositoryInMemory implements MealRepository {

    private final ConcurrentHashMap<Integer, Meal> repository = new ConcurrentHashMap<>(); //ConcurrentHashMap
    private static final AtomicInteger sequence = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(sequence.incrementAndGet());
            return repository.put(meal.getId(), meal);
        }

        // treat case: update, but absent in storage
        // все вычисления будут выполнены только в случае, если элемент с ключом key уже существует.
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public Meal get(int id) {
        return repository.get(id);
    }

    @Override
    public void delete(int id) {
        repository.remove(id);
    }

    @Override
    public Collection<Meal> getAll() {
        return repository.values();
    }
}
