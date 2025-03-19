package com.yakush.dailycalorieservice.controller;

import com.yakush.dailycalorieservice.model.Meal;
import com.yakush.dailycalorieservice.model.User;
import com.yakush.dailycalorieservice.service.meal.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meals")
public class MealController {
    private final MealService mealService;

    @GetMapping
    public ResponseEntity<List<Meal>> getAllMeals() {
        List<Meal> meals = mealService.getAll();
        return new ResponseEntity<>(meals, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Meal> getMealById(@PathVariable Long id) {
        Meal meal = mealService.getById(id);
        return new ResponseEntity<>(meal, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Meal> createMeal(@RequestBody Meal meal) {
        Meal createdMeal = mealService.save(meal);
        return new ResponseEntity<>(createdMeal, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Meal> updateMeal(@RequestBody Meal meal) {
        Meal updatedMeal = mealService.update(meal);
        return new ResponseEntity<>(updatedMeal, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeal(@PathVariable Long id) {
        Meal meal = mealService.getById(id);
        mealService.delete(meal);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/user/{userID}caloriesHistory")
    public ResponseEntity<Map<LocalDateTime, Double>> getCaloriesHistory(@PathVariable Long userID){
        Map<LocalDateTime, Double> caloriesHistory = mealService.getCaloriesHistory(userID);
        return new ResponseEntity<>(caloriesHistory, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/date/{date}")
    public ResponseEntity<List<Meal>> getMealsByUserAndDate(
            @PathVariable Long userId,
            @PathVariable LocalDate date) {
        List<Meal> meals = mealService.getMealsByUserAndDate(userId, date);
        return ResponseEntity.ok(meals);
    }

    @GetMapping("/user/{userId}/calories-compliance")
    public ResponseEntity<Map<LocalDate, Boolean>> getCaloriesComplianceForUser(@PathVariable Long userId) {

        Map<LocalDate, Boolean> complianceMap = mealService.getCaloriesComplianceForUser(userId);
        return new ResponseEntity<>(complianceMap, HttpStatus.OK);
    }
}