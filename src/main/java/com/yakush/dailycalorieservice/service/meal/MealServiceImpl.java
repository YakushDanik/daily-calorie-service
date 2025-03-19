package com.yakush.dailycalorieservice.service.meal;

import com.yakush.dailycalorieservice.exception.ResourceNotFoundException;
import com.yakush.dailycalorieservice.model.Dish;
import com.yakush.dailycalorieservice.model.Meal;
import com.yakush.dailycalorieservice.model.User;
import com.yakush.dailycalorieservice.repository.DishRepository;
import com.yakush.dailycalorieservice.repository.MealRepository;
import com.yakush.dailycalorieservice.repository.UserRepository;
import com.yakush.dailycalorieservice.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;
    private final UserRepository userRepository;
    private final DishRepository dishRepository;
    private final UserService userService;


    @Override
    public Double calculateCaloriesInDay(Long userId, LocalDateTime date) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден с id: " + userId));
        List<Meal> meals = mealRepository.findAllByDate(date.toLocalDate());
        Double calories = 0.0;
        for (Meal meal : meals) {
            for (Dish dish : meal.getDishes()) {
                calories += dish.getCalories();
            }
        }
        return calories;
    }

    @Override
    public Map<LocalDateTime, Double> getCaloriesHistory(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден с id: " + userId));
        List<Meal> meals = mealRepository.findAllByUserId(user.getId());
        Map<LocalDateTime, Double> caloriesHistory = new HashMap<>();
        for (Meal meal : meals) {
            caloriesHistory.put(meal.getMealDateTime(), calculateCaloriesInDay(user.getId(), meal.getMealDateTime()));
        }
        return caloriesHistory;
    }

    @Override
    public List<Meal> getMealsByUserAndDate(Long userId, LocalDate date) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден с id: " + userId));
        return mealRepository.findAllByUserIdAndDate(user.getId(), date);
    }


    @Override
    public Map<LocalDate, Boolean> getCaloriesComplianceForUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден с id: " + userId));
        Map<LocalDate, Boolean> complianceMap = new HashMap<>();
        List<Meal> meals = mealRepository.findAllByUserId(user.getId());
        for (Meal meal : meals) {
            LocalDate mealDate = meal.getMealDateTime().toLocalDate();
            Double dailyCalories = userService.calculateDailyCalories(userId);
            Double mealCalories = calculateCaloriesInDay(user.getId(), meal.getMealDateTime());
            complianceMap.put(mealDate, mealCalories <= dailyCalories);
        }
        return complianceMap;

    }

    @Override
    public Meal save(Meal meal) {
        User user = userRepository.findById(meal.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден с id: " + meal.getUser().getId()));

        List<Dish> dishes = dishRepository.findAllById(meal.getDishes().stream()
                .map(Dish::getId)
                .collect(Collectors.toList()));

        if (dishes.size() != meal.getDishes().size()) {
            throw new ResourceNotFoundException("Одно или несколько блюд не найдены");
        }

        meal.setUser(user);
        meal.setDishes(dishes);
        return mealRepository.save(meal);
    }

    @Override
    public Meal update(Meal meal) {
        return mealRepository.save(meal);
    }

    @Override
    public void delete(Meal meal) {
        mealRepository.delete(meal);
    }

    @Override
    public List<Meal> getAll() {
        return mealRepository.findAll();
    }

    @Override
    public Meal getById(Long id) {
        return mealRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Прием пищи не найден с id: " + id));
    }
}