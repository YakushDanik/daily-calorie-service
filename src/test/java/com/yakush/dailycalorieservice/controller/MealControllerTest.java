package com.yakush.dailycalorieservice.controller;

import com.yakush.dailycalorieservice.model.Dish;
import com.yakush.dailycalorieservice.model.Meal;
import com.yakush.dailycalorieservice.model.User;
import com.yakush.dailycalorieservice.service.meal.MealService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MealControllerTest {

    @Mock
    private MealService mealService;

    @InjectMocks
    private MealController mealController;

    private Meal meal1;
    private Meal meal2;
    private User user;
    private List<Meal> mealList;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        meal1 = new Meal();
        meal1.setId(1L);
        meal1.setUser(user);
        meal1.setMealDateTime(LocalDateTime.of(2023, 10, 27, 10, 0));

        meal2 = new Meal();
        meal2.setId(2L);
        meal2.setUser(user);
        meal2.setMealDateTime(LocalDateTime.of(2023, 10, 27, 14, 0));

        mealList = new ArrayList<>();
        mealList.add(meal1);
        mealList.add(meal2);
    }

    @Test
    void getAllMeals_ReturnsListOfMeals() {
        when(mealService.getAll()).thenReturn(mealList);

        ResponseEntity<List<Meal>> response = mealController.getAllMeals();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mealList, response.getBody());
        verify(mealService, times(1)).getAll();
    }

    @Test
    void getMealById_ReturnsMeal() {
        when(mealService.getById(1L)).thenReturn(meal1);

        ResponseEntity<Meal> response = mealController.getMealById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(meal1, response.getBody());
        verify(mealService, times(1)).getById(1L);
    }

    @Test
    void createMeal_ReturnsCreatedMeal() {
        when(mealService.save(meal1)).thenReturn(meal1);

        ResponseEntity<Meal> response = mealController.createMeal(meal1);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(meal1, response.getBody());
        verify(mealService, times(1)).save(meal1);
    }

    @Test
    void updateMeal_ReturnsUpdatedMeal() {
        when(mealService.update(meal1)).thenReturn(meal1);

        ResponseEntity<Meal> response = mealController.updateMeal(meal1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(meal1, response.getBody());
        verify(mealService, times(1)).update(meal1);
    }

    @Test
    void deleteMeal_ReturnsNoContent() {
        when(mealService.getById(1L)).thenReturn(meal1);
        doNothing().when(mealService).delete(meal1);

        ResponseEntity<Void> response = mealController.deleteMeal(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(mealService, times(1)).getById(1L);
        verify(mealService, times(1)).delete(meal1);
    }

    @Test
    void getCaloriesHistory_ReturnsCaloriesHistory() {
        Map<LocalDateTime, Double> caloriesHistory = new HashMap<>();
        caloriesHistory.put(LocalDateTime.now(), 300.0);
        caloriesHistory.put(LocalDateTime.now().plusDays(1), 500.0);

        when(mealService.getCaloriesHistory(1L)).thenReturn(caloriesHistory);

        ResponseEntity<Map<LocalDateTime, Double>> response = mealController.getCaloriesHistory(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(caloriesHistory, response.getBody());
        verify(mealService, times(1)).getCaloriesHistory(1L);
    }

    @Test
    void getMealsByUserAndDate_ReturnsMeals() {
        LocalDate date = LocalDate.of(2023, 10, 27);
        List<Meal> meals = new ArrayList<>();
        meals.add(meal1);

        when(mealService.getMealsByUserAndDate(1L, date)).thenReturn(meals);

        ResponseEntity<List<Meal>> response = mealController.getMealsByUserAndDate(1L, date);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(meals, response.getBody());
        verify(mealService, times(1)).getMealsByUserAndDate(1L, date);
    }

    @Test
    void getCaloriesComplianceForUser_ReturnsComplianceMap() {
        Map<LocalDate, Boolean> complianceMap = new HashMap<>();
        complianceMap.put(LocalDate.now(), true);
        complianceMap.put(LocalDate.now().plusDays(1), false);

        when(mealService.getCaloriesComplianceForUser(1L)).thenReturn(complianceMap);

        ResponseEntity<Map<LocalDate, Boolean>> response = mealController.getCaloriesComplianceForUser(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(complianceMap, response.getBody());
        verify(mealService, times(1)).getCaloriesComplianceForUser(1L);
    }
}