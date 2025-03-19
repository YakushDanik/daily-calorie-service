package com.yakush.dailycalorieservice.service.dish;

import com.yakush.dailycalorieservice.exception.ResourceNotFoundException;
import com.yakush.dailycalorieservice.model.Dish;
import com.yakush.dailycalorieservice.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DishServiceImpl implements DishService{

    private final DishRepository dishRepository;

    @Override
    public Dish save(Dish dish) {
        return dishRepository.save(dish);
    }

    @Override
    public Dish update(Dish dish) {
        return dishRepository.save(dish);
    }

    @Override
    public void delete(Dish dish) {
        dishRepository.delete(dish);
    }

    @Override
    public List<Dish> getAll() {
        return dishRepository.findAll();
    }

    @Override
    public Dish getById(Long id) {
        return dishRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Пища не найдена с id: " + id));
    }

    @Override
    public Dish updateName(Dish dish) {
        dish.setName(dish.getName());
        return dishRepository.save(dish);
    }
}
