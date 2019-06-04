package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.util.List;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.summingInt;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2019, Month.JUNE, 1, 9, 0), "Завтрак", 1500),
                new UserMeal(LocalDateTime.of(2019, Month.MAY, 31, 11, 0), "oo", 2500),

                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),

                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        List<UserMealWithExceed> filteredList = getFilteredWithExceeded3(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);

        filteredList.forEach(System.out::println);
    }

    private static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = mealList.stream()
                .collect(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate(), summingInt(UserMeal::getCalories)));

        return mealList.stream()
                .filter(meal -> (TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime)))
                .map(meal -> new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        (caloriesSumByDate.get(meal.getDateTime().toLocalDate())) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static List<UserMealWithExceed> getFilteredWithExceeded2(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        return mealList.stream()
                .collect(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate()))
                .values()
                .stream()
                .flatMap(um -> um.stream()
                        .filter(meal -> (TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime)))
                        .map(meal -> new UserMealWithExceed(meal.getDateTime(),
                                meal.getDescription(),
                                meal.getCalories(),
                                (um.stream().mapToInt(UserMeal::getCalories).sum() > caloriesPerDay))))//Желательно расчет перенести на уровень выше
                //.sorted(Comparator.comparingInt(UserMealWithExceed::getCalories))
                .collect(Collectors.toList());
    }


    private static List<UserMealWithExceed> getFilteredWithExceeded2_2(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        return mealList.stream()
                .collect(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate()))
                .values()
                .stream()
                .flatMap(dayMeals -> {
                    boolean exceed = dayMeals.stream().mapToInt(UserMeal::getCalories).sum() > caloriesPerDay;
                    return dayMeals.stream().filter(meal ->
                            TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime))
                            .map(meal -> new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), exceed));
                })
                //.sorted(Comparator.comparingInt(UserMealWithExceed::getCalories))
                .collect(Collectors.toList());

    }

    private static List<UserMealWithExceed> getFilteredWithExceeded3(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        //Коллектор
        class Aggregate {
            private int dailySumOfCalories = 0;
            private List<UserMeal> dailyMeals = new ArrayList<>();

            private void accumulate(UserMeal meal) {
                dailySumOfCalories += meal.getCalories();
                if (TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime))
                    dailyMeals.add(meal);
            }

            // Вызывается если Stream().parallel()
            private Aggregate combine(Aggregate other) {
                this.dailySumOfCalories += other.dailySumOfCalories;
                this.dailyMeals.addAll(other.dailyMeals);
                return this;
            }

            private Stream<UserMealWithExceed> finisher() {
                final boolean exceed = dailySumOfCalories > caloriesPerDay;
                return dailyMeals.stream()
                        .map(meal -> new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), exceed));
            }
        }

        return mealList.stream()
                .collect(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate(),
                        Collector.of(Aggregate::new, Aggregate::accumulate, Aggregate::combine, Aggregate::finisher)))
                .values()
                .stream()
                //.flatMap(x -> x)
                .flatMap(Function.identity())
                .sorted(Comparator.comparingInt(UserMealWithExceed::getCalories))
                .collect(Collectors.toList());
    }


}
