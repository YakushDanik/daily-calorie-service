package com.yakush.dailycalorieservice.service.user;

import com.yakush.dailycalorieservice.exception.InvalidInputException;
import com.yakush.dailycalorieservice.exception.ResourceNotFoundException;
import com.yakush.dailycalorieservice.model.Goal;
import com.yakush.dailycalorieservice.model.User;
import com.yakush.dailycalorieservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public Double calculateDailyCalories(Long userId) {
        User user = getById(userId);
        double bmr;
        if(user.getSex().getValue() == 1){
            bmr = (88.362 + (13.397 * user.getWeight()) + (4.799 * user.getHeight()) - (5.677 * user.getAge())) *
                    user.getActivity().getValue();
        }
        else {
            bmr = (447.593 + (9.247 * user.getWeight()) + (3.098 * user.getHeight()) - (4.330 * user.getAge())) *
                    user.getActivity().getValue();
        }
        return switch (user.getGoal().getValue()) {
            case 1 -> bmr * 0.9;
            case 2 -> bmr;
            case 3 -> bmr * 1.1;
            default -> null;
        };
    }

    @Override
    public User updateUserGoal(Long userId, Goal goal) {
        User user = getById(userId);
        user.setGoal(goal);
        return user;
    }

    @Override
    public User save(User user) {
        validateEmail(user.getEmail());
        validateName(user.getName());
        if (user.getWeight() <= 0) {
            throw new InvalidInputException("Вес не может быть отрицательным");
        }
        if (user.getHeight() <= 0) {
            throw new InvalidInputException("Рост не может быть отрицательным");
        }
        if (user.getAge() <= 0) {
            throw new InvalidInputException("Возраст не может быть отрицательным");
        }

        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        validateEmail(user.getEmail());
        validateName(user.getName());
        if (user.getWeight() <= 0) {
            throw new InvalidInputException("Вес не может быть отрицательным");
        }
        if (user.getHeight() <= 0) {
            throw new InvalidInputException("Рост не может быть отрицательным");
        }
        if (user.getAge() <= 0) {
            throw new InvalidInputException("Возраст не может быть отрицательным");
        }

        return userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден с id: " + id));
    }

    private void validateEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!email.matches(emailRegex)) {
            throw new IllegalArgumentException("Invalid email format for " + email);
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("invalid user name");
    }
}
