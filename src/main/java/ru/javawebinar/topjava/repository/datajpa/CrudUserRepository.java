package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.User;

@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public interface CrudUserRepository extends JpaRepository<User, Integer> {
    @Transactional
    @Modifying
//    @Query(name = User.DELETE)
    @Query("DELETE FROM User u WHERE u.id=:id")
    int delete(@Param("id") int id);

    User getByEmail(String email);

/*    @Query(name = User.GET_WITH_MEAL, nativeQuery = true)
    User getWithMeal(@Param("id") int id);*/

    @EntityGraph(value = User.GRAPH_WITH_MEALS)
    @Query("SELECT u FROM User u WHERE u.id=:id ORDER BY u.name, u.email")
    User getWithMeal(@Param("id") int id);
}
