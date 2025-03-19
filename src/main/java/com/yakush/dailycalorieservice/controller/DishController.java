package com.yakush.dailycalorieservice.controller;

import com.yakush.dailycalorieservice.model.Dish;
import com.yakush.dailycalorieservice.service.dish.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dishes")
public class DishController {
    private final DishService dishService;

    @GetMapping("/")
    public ResponseEntity<List<Dish>> getAllDishes() {
        List<Dish> dishes = dishService.getAll();
        return ResponseEntity.ok(dishes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dish> getDishById(@PathVariable Long id) {
        Dish dish = dishService.getById(id);
        return ResponseEntity.ok(dish);
    }

    @PostMapping("/")
    public ResponseEntity<Dish> createDish(@RequestBody Dish dish) {
        Dish createdDish = dishService.save(dish);
        return ResponseEntity.status(201).body(createdDish);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dish> updateDish( @RequestBody Dish dish) {
        Dish updatedDish = dishService.update(dish);
        return ResponseEntity.ok(updatedDish);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable Long id) {
        Dish dish = dishService.getById(id);
        dishService.delete(dish);
        return ResponseEntity.noContent().build();
    }

}
