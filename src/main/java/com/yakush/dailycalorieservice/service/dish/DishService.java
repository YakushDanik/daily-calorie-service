package com.yakush.dailycalorieservice.service.dish;

import com.yakush.dailycalorieservice.model.Dish;
import com.yakush.dailycalorieservice.service.Service;

public interface DishService extends Service<Dish> {
    Dish updateName(Dish dish);

}
