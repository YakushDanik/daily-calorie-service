package com.yakush.dailycalorieservice.service.meal;

import com.yakush.dailycalorieservice.model.Meal;
import com.yakush.dailycalorieservice.service.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface MealService extends Service<Meal> {
    Map<LocalDateTime, Double> getCaloriesHistory(Long userId);

    Double calculateCaloriesInDay(Long userId, LocalDateTime date);

    List<Meal> getMealsByUserAndDate(Long userId, LocalDate date);
    Map<LocalDate, Boolean> getCaloriesComplianceForUser(Long userId);
}
