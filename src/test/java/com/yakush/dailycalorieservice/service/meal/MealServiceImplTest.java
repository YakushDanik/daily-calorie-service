package com.yakush.dailycalorieservice.service.meal;

import com.yakush.dailycalorieservice.exception.ResourceNotFoundException;
import com.yakush.dailycalorieservice.model.Dish;
import com.yakush.dailycalorieservice.model.Meal;
import com.yakush.dailycalorieservice.model.User;
import com.yakush.dailycalorieservice.repository.DishRepository;
import com.yakush.dailycalorieservice.repository.MealRepository;
import com.yakush.dailycalorieservice.repository.UserRepository;
import com.yakush.dailycalorieservice.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MealServiceImplTest {

    @Mock
    private MealRepository mealRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DishRepository dishRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private MealServiceImpl mealService;

    private User user;
    private Meal meal1;
    private Meal meal2;
    private Dish dish1;
    private Dish dish2;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        dish1 = new Dish();
        dish1.setId(1L);
        dish1.setCalories(100.0);

        dish2 = new Dish();
        dish2.setId(2L);
        dish2.setCalories(200.0);

        meal1 = new Meal();
        meal1.setId(1L);
        meal1.setUser(user);
        meal1.setMealDateTime(LocalDateTime.of(2023, 10, 27, 10, 0));
        meal1.setDishes(List.of(dish1));

        meal2 = new Meal();
        meal2.setId(2L);
        meal2.setUser(user);
        meal2.setMealDateTime(LocalDateTime.of(2023, 10, 27, 14, 0));
        meal2.setDishes(List.of(dish2));
    }

    @Test
    void calculateCaloriesInDay_ValidUserIdAndDate_ReturnsCorrectCalories() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(mealRepository.findAllByDate(LocalDate.of(2023, 10, 27))).thenReturn(List.of(meal1, meal2));

        Double calories = mealService.calculateCaloriesInDay(1L, LocalDateTime.of(2023, 10, 27, 12, 0));

        assertEquals(300.0, calories);
    }

    @Test
    void calculateCaloriesInDay_UserNotFound_ThrowsResourceNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> mealService.calculateCaloriesInDay(1L, LocalDateTime.now()));
    }


    @Test
    void getCaloriesHistory_UserNotFound_ThrowsResourceNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> mealService.getCaloriesHistory(1L));
    }

    @Test
    void getMealsByUserAndDate_ValidUserIdAndDate_ReturnsCorrectMeals() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(mealRepository.findAllByUserIdAndDate(1L, LocalDate.of(2023, 10, 27))).thenReturn(List.of(meal1, meal2));

        List<Meal> meals = mealService.getMealsByUserAndDate(1L, LocalDate.of(2023, 10, 27));

        assertEquals(2, meals.size());
        assertTrue(meals.contains(meal1));
        assertTrue(meals.contains(meal2));
    }

    @Test
    void getMealsByUserAndDate_UserNotFound_ThrowsResourceNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> mealService.getMealsByUserAndDate(1L, LocalDate.now()));
    }

    @Test
    void getCaloriesComplianceForUser_ValidUserId_ReturnsCorrectComplianceMap() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(mealRepository.findAllByUserId(1L)).thenReturn(List.of(meal1, meal2));
        when(userService.calculateDailyCalories(1L)).thenReturn(500.0);
        when(mealRepository.findAllByDate(meal1.getMealDateTime().toLocalDate())).thenReturn(List.of(meal1));
        when(mealRepository.findAllByDate(meal2.getMealDateTime().toLocalDate())).thenReturn(List.of(meal2));

        Map<LocalDate, Boolean> complianceMap = mealService.getCaloriesComplianceForUser(1L);

        assertEquals(1, complianceMap.size());
        assertTrue(complianceMap.get(LocalDate.of(2023, 10, 27)));
    }

    @Test
    void getCaloriesComplianceForUser_UserNotFound_ThrowsResourceNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> mealService.getCaloriesComplianceForUser(1L));
    }

    @Test
    void save_ValidMeal_ReturnsSavedMeal() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(dishRepository.findAllById(List.of(1L))).thenReturn(List.of(dish1));
        when(mealRepository.save(any(Meal.class))).thenReturn(meal1);

        Meal savedMeal = mealService.save(meal1);

        assertEquals(meal1, savedMeal);
        verify(mealRepository, times(1)).save(meal1);
    }

    @Test
    void save_UserNotFound_ThrowsResourceNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        meal1.setUser(user);

        assertThrows(ResourceNotFoundException.class, () -> mealService.save(meal1));
    }

    @Test
    void save_DishNotFound_ThrowsResourceNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(dishRepository.findAllById(List.of(1L))).thenReturn(Collections.emptyList());
        meal1.setUser(user);

        assertThrows(ResourceNotFoundException.class, () -> mealService.save(meal1));
    }

    @Test
    void update_ValidMeal_ReturnsUpdatedMeal() {
        when(mealRepository.save(meal1)).thenReturn(meal1);

        Meal updatedMeal = mealService.update(meal1);

        assertEquals(meal1, updatedMeal);
        verify(mealRepository, times(1)).save(meal1);
    }

    @Test
    void delete_ValidMeal_DeletesMeal() {
        mealService.delete(meal1);

        verify(mealRepository, times(1)).delete(meal1);
    }

    @Test
    void getAll_ReturnsAllMeals() {
        when(mealRepository.findAll()).thenReturn(List.of(meal1, meal2));

        List<Meal> allMeals = mealService.getAll();

        assertEquals(2, allMeals.size());
        assertTrue(allMeals.contains(meal1));
        assertTrue(allMeals.contains(meal2));
    }

    @Test
    void getById_ValidId_ReturnsMeal() {
        when(mealRepository.findById(1L)).thenReturn(Optional.of(meal1));

        Meal foundMeal = mealService.getById(1L);

        assertEquals(meal1, foundMeal);
    }

    @Test
    void getById_MealNotFound_ThrowsResourceNotFoundException() {
        when(mealRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> mealService.getById(1L));
    }
}