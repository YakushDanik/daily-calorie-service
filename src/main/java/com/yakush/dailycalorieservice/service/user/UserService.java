package com.yakush.dailycalorieservice.service.user;

import com.yakush.dailycalorieservice.model.Goal;
import com.yakush.dailycalorieservice.model.User;
import com.yakush.dailycalorieservice.service.Service;

public interface UserService extends Service<User> {
    Double calculateDailyCalories(Long userId);
    User updateUserGoal(Long userId, Goal goal);
}
