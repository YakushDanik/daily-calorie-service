package com.yakush.dailycalorieservice.service.dish;

import com.yakush.dailycalorieservice.exception.ResourceNotFoundException;
import com.yakush.dailycalorieservice.model.Dish;
import com.yakush.dailycalorieservice.repository.DishRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishServiceImplTest {

    @Mock
    private DishRepository dishRepository;

    @InjectMocks
    private DishServiceImpl dishService;

    private Dish dish;

    @BeforeEach
    void setUp() {
        dish = new Dish();
        dish.setId(1L);
        dish.setName("Test Dish");
    }

    @Test
    void save_ShouldReturnSavedDish() {
        when(dishRepository.save(any(Dish.class))).thenReturn(dish);

        Dish savedDish = dishService.save(dish);

        assertNotNull(savedDish);
        assertEquals(dish.getId(), savedDish.getId());
        assertEquals(dish.getName(), savedDish.getName());
        verify(dishRepository, times(1)).save(any(Dish.class));
    }

    @Test
    void update_ShouldReturnUpdatedDish() {
        when(dishRepository.save(any(Dish.class))).thenReturn(dish);

        Dish updatedDish = dishService.update(dish);

        assertNotNull(updatedDish);
        assertEquals(dish.getId(), updatedDish.getId());
        assertEquals(dish.getName(), updatedDish.getName());
        verify(dishRepository, times(1)).save(any(Dish.class));
    }

    @Test
    void delete_ShouldDeleteDish() {
        doNothing().when(dishRepository).delete(any(Dish.class));

        dishService.delete(dish);

        verify(dishRepository, times(1)).delete(any(Dish.class));
    }

    @Test
    void getAll_ShouldReturnAllDishes() {
        List<Dish> dishes = new ArrayList<>();
        dishes.add(dish);
        Dish dish2 = new Dish();
        dish2.setId(2L);
        dish2.setName("Test Dish 2");
        dishes.add(dish2);
        when(dishRepository.findAll()).thenReturn(dishes);

        List<Dish> allDishes = dishService.getAll();

        assertNotNull(allDishes);
        assertEquals(2, allDishes.size());
        verify(dishRepository, times(1)).findAll();
    }

    @Test
    void getById_ShouldReturnDish_WhenDishExists() {
        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));

        Dish foundDish = dishService.getById(1L);

        assertNotNull(foundDish);
        assertEquals(dish.getId(), foundDish.getId());
        assertEquals(dish.getName(), foundDish.getName());
        verify(dishRepository, times(1)).findById(1L);
    }

    @Test
    void getById_ShouldThrowException_WhenDishDoesNotExist() {
        // Arrange
        when(dishRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> dishService.getById(1L)
        );

        // Проверяем сообщение исключения
        assertEquals(null, exception.getMessage());

        // Проверяем, что метод findById был вызван 1 раз
        verify(dishRepository, times(1)).findById(1L);
    }

    @Test
    void updateName_ShouldUpdateDishNameAndReturnUpdatedDish() {
        String newName = "Updated Dish Name";
        dish.setName(newName);
        when(dishRepository.save(any(Dish.class))).thenReturn(dish);

        Dish updatedDish = dishService.updateName(dish);

        assertNotNull(updatedDish);
        assertEquals(newName, updatedDish.getName());
        verify(dishRepository, times(1)).save(any(Dish.class));
    }
}