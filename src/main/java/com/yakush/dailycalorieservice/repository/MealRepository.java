package com.yakush.dailycalorieservice.repository;

import com.yakush.dailycalorieservice.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
    @Query("SELECT m FROM Meal m WHERE DATE(m.mealDateTime) = :date")
    List<Meal> findAllByDate(@Param("date") LocalDate date);

    List<Meal> findAllByUserId(Long userId);

    @Query("SELECT m FROM Meal m WHERE m.user.id = :userId AND DATE(m.mealDateTime) = :date")
    List<Meal> findAllByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);
}
