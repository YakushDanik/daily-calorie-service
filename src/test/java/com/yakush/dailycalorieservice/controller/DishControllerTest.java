package com.yakush.dailycalorieservice.controller;

import com.yakush.dailycalorieservice.model.Dish;
import com.yakush.dailycalorieservice.service.dish.DishService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishControllerTest {

    @Mock
    private DishService dishService;

    @InjectMocks
    private DishController dishController;

    private Dish dish1;
    private Dish dish2;

    @BeforeEach
    void setUp() {
        dish1 = new Dish();
        dish1.setId(1L);
        dish1.setName("Dish 1");
        dish2 = new Dish();
        dish2.setId(2L);
        dish2.setName("Dish 2");
    }

    @Test
    void getAllDishes_ShouldReturnListOfDishes() {
        // Arrange
        List<Dish> dishes = Arrays.asList(dish1, dish2);
        when(dishService.getAll()).thenReturn(dishes);

        // Act
        ResponseEntity<List<Dish>> response = dishController.getAllDishes();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dishes, response.getBody());
        verify(dishService, times(1)).getAll();
    }

    @Test
    void getDishById_ShouldReturnDish() {
        // Arrange
        when(dishService.getById(1L)).thenReturn(dish1);

        // Act
        ResponseEntity<Dish> response = dishController.getDishById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dish1, response.getBody());
        verify(dishService, times(1)).getById(1L);
    }

    @Test
    void createDish_ShouldReturnCreatedDish() {
        // Arrange
        when(dishService.save(dish1)).thenReturn(dish1);

        // Act
        ResponseEntity<Dish> response = dishController.createDish(dish1);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(dish1, response.getBody());
        verify(dishService, times(1)).save(dish1);
    }

    @Test
    void updateDish_ShouldReturnUpdatedDish() {
        // Arrange
        when(dishService.update(dish1)).thenReturn(dish1);

        // Act
        ResponseEntity<Dish> response = dishController.updateDish(dish1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dish1, response.getBody());
        verify(dishService, times(1)).update(dish1);
    }

    @Test
    void deleteDish_ShouldReturnNoContent() {
        // Arrange
        when(dishService.getById(1L)).thenReturn(dish1);
        doNothing().when(dishService).delete(dish1);

        // Act
        ResponseEntity<Void> response = dishController.deleteDish(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(dishService, times(1)).getById(1L);
        verify(dishService, times(1)).delete(dish1);
    }

    @Test
    void createDish_shouldReturnDishWithId(){
        Dish dish = new Dish();
        dish.setName("Test Dish");
        Dish savedDish = new Dish();
        savedDish.setId(1L);
        savedDish.setName("Test Dish");
        when(dishService.save(dish)).thenReturn(savedDish);

        ResponseEntity<Dish> response = dishController.createDish(dish);

        assertNotNull(response.getBody().getId());
    }
}