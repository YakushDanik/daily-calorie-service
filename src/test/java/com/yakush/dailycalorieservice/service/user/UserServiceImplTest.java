package com.yakush.dailycalorieservice.service.user;

import com.yakush.dailycalorieservice.exception.InvalidInputException;
import com.yakush.dailycalorieservice.exception.ResourceNotFoundException;
import com.yakush.dailycalorieservice.model.Activity;
import com.yakush.dailycalorieservice.model.Goal;
import com.yakush.dailycalorieservice.model.Sex;
import com.yakush.dailycalorieservice.model.User;
import com.yakush.dailycalorieservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User userMale;
    private User userFemale;

    @BeforeEach
    void setUp() {
        userMale = new User();
        userMale.setId(1L);
        userMale.setSex(Sex.MALE); // Assuming 1 represents MALE
        userMale.setWeight(80.0);
        userMale.setHeight(180.0);
        userMale.setAge(30);
        userMale.setActivity(Activity.MODERATELY_ACTIVE);
        userMale.setGoal(Goal.MAINTENANCE);

        userFemale = new User();
        userFemale.setId(2L);
        userFemale.setSex(Sex.FEMALE); // Assuming 2 represents FEMALE
        userFemale.setWeight(60.0);
        userFemale.setHeight(165.0);
        userFemale.setAge(25);
        userFemale.setActivity(Activity.LIGHTLY_ACTIVE);
        userFemale.setGoal(Goal.WEIGHT_LOSS);
    }

    @Test
    void calculateDailyCalories_Male_Maintain() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userMale));
        double expectedCalories = (88.362 + (13.397 * 80.0) + (4.799 * 180.0) - (5.677 * 30)) *
                userMale.getActivity().getValue();
        expectedCalories = expectedCalories;
        double actualCalories = userService.calculateDailyCalories(1L);
        assertEquals(expectedCalories, actualCalories, 0.01);
    }

    @Test
    void calculateDailyCalories_Female_Lose() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(userFemale));
        double expectedCalories = (447.593 + (9.247 * 60.0) + (3.098 * 165.0) - (4.330 * 25)) *
                userFemale.getActivity().getValue();
        expectedCalories = expectedCalories * 0.9;
        double actualCalories = userService.calculateDailyCalories(2L);
        assertEquals(expectedCalories, actualCalories, 0.01);
    }
    @Test
    void calculateDailyCalories_Male_Gain() {
        userMale.setGoal(Goal.MASS_GAIN);
        when(userRepository.findById(1L)).thenReturn(Optional.of(userMale));
        double expectedCalories = (88.362 + (13.397 * 80.0) + (4.799 * 180.0) - (5.677 * 30)) *
                userMale.getActivity().getValue();
        expectedCalories = expectedCalories * 1.1;
        double actualCalories = userService.calculateDailyCalories(1L);
        assertEquals(expectedCalories, actualCalories, 0.01);
    }

    @Test
    void calculateDailyCalories_UserNotFound() {
        when(userRepository.findById(3L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.calculateDailyCalories(3L)
        );

        assertEquals(null, exception.getMessage());
        verify(userRepository).findById(3L);
    }

    @Test
    void updateUserGoal() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userMale));
        Goal newGoal = Goal.MASS_GAIN;
        User updatedUser = userService.updateUserGoal(1L, newGoal);
        assertEquals(newGoal, updatedUser.getGoal());
    }


    @Test
    void delete() {
        userService.delete(userMale);
        verify(userRepository, times(1)).delete(userMale);
    }

    @Test
    void getAll() {
        List<User> users = Arrays.asList(userMale, userFemale);
        when(userRepository.findAll()).thenReturn(users);
        List<User> retrievedUsers = userService.getAll();
        assertEquals(2, retrievedUsers.size());
        assertEquals(users, retrievedUsers);
    }

    @Test
    void getById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userMale));
        User retrievedUser = userService.getById(1L);
        assertEquals(userMale, retrievedUser);
    }

    @Test
    void getById_UserNotFound() {
        when(userRepository.findById(3L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getById(3L)
        );

        assertEquals(null, exception.getMessage());
        verify(userRepository).findById(3L);
    }
}