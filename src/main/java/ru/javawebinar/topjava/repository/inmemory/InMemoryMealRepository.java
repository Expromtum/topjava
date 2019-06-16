package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>(); // TODO Эффективнее была бы мапа по ключу Пользователь, чтобы не перебирать всю еду всех пользователей
    private AtomicInteger counter = new AtomicInteger(0);

    //
    //private static final Comparator<Meal> MEAL_COMPARATOR = Comparator.comparing(Meal::getDateTime, Collections.reverseOrder());
//    private static final Comparator<Meal> MEAL_COMPARATOR = (meal1, meal2) -> meal2.getDateTime().compareTo(meal1.getDateTime());
    private static final Comparator<Meal> MEAL_COMPARATOR = Comparator.comparing(Meal::getDateTime).reversed();

    {
        MealsUtil.MEALS.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {//TODO Сделать одно обращение к репозиторию
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // treat case: update, but absent in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) { //TODO Сделать одно обращение к репозиторию
        Meal meal = repository.get(id);
        if ((meal != null) && (Objects.equals(meal.getUserId(), userId)))
            return repository.remove(id) != null;
        else
            return false;
    }

    @Override
    public boolean deleteAll(int userId) {
        Collection<Meal> meals = getAll(userId);
        meals.forEach(meal -> repository.remove(meal.getId()));
        return true;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = repository.get(id);
        return ((meal != null) && (Objects.equals(meal.getUserId(), userId))) ? meal : null;
    }

    @Override
    public Collection<Meal> getAll() {
        return repository.values().stream()
                .sorted(MEAL_COMPARATOR)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return repository.values().stream()
                .filter(meal -> Objects.equals(meal.getUserId(), userId))
                .sorted(MEAL_COMPARATOR)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Meal> getBetween(int userId, LocalDateTime startDate, LocalDateTime endDate) {
        Objects.requireNonNull(startDate);
        Objects.requireNonNull(endDate);

        return repository.values().stream()
                .filter(meal -> {
                    if (!(Objects.equals(meal.getUserId(), userId)))
                        return false;

                    return (DateTimeUtil.isBetweenDateTime(meal.getDateTime(), startDate, endDate));

                }).sorted(MEAL_COMPARATOR)
                .collect(Collectors.toList());
    }

 /*   public static void main(String[] args) {
        InMemoryMealRepositoryImpl repository = new InMemoryMealRepositoryImpl();
        repository.deleteAll(2);

        repository.getAll().forEach(System.out::println);
    }*/
}

