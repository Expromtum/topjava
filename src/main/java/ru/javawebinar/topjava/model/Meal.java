package ru.javawebinar.topjava.model;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NamedQueries({
        @NamedQuery(name = Meal.DELETE, query = "DELETE FROM Meal t WHERE t.id=:id AND t.user.id=:userId"),
        @NamedQuery(name = Meal.GET, query = "SELECT t FROM Meal t WHERE t.id=:id AND t.user.id=:userId"),
        @NamedQuery(name = Meal.UPDATE, query = "UPDATE Meal t SET t.dateTime=:dateTime, t.calories=:calories, t.description=:description WHERE t.id=:id AND t.user.id=:userId"),
        @NamedQuery(name = Meal.ALL_SORTED, query = "SELECT t FROM Meal t WHERE t.user.id=:userId ORDER BY t.dateTime DESC"),
        @NamedQuery(name = Meal.ALL_BETWEEN,
                query = "SELECT t FROM Meal t WHERE t.user.id=:userId AND t.dateTime >= :startDate AND t.dateTime <= :endDate ORDER BY t.dateTime DESC")
})
@Entity
@Table(name = "meals")
public class Meal extends AbstractBaseEntity {

    public static final String DELETE = "Meal.delete";
    public static final String GET = "Meal.get";
    public static final String ALL_SORTED = "Meal.getAllSorted";
    public static final String ALL_BETWEEN = "Meal.getAllBetweenSorted";
    public static final String UPDATE = "Meal.update";

    @Column(name = "date_time", nullable = false)
    @NotNull
    private LocalDateTime dateTime;

    @Column(name = "description", nullable = false)
    @NotBlank
    @Size(max = 200)
    private String description;

    @Column(name = "calories", nullable = false)
    @NotNull
    @Min(0)
    @Max(5000)
    private int calories = 0;

    @JoinColumn(name="user_id")
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Meal() {
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this(null, dateTime, description, calories);
    }

    public Meal(Integer id, LocalDateTime dateTime, String description, int calories) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
