package com.yakush.dailycalorieservice.controller;

import com.yakush.dailycalorieservice.model.User;
import com.yakush.dailycalorieservice.service.user.UserService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1L);
        user1.setName("Test User 1");

        user2 = new User();
        user2.setId(2L);
        user2.setName("Test User 2");
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        List<User> users = Arrays.asList(user1, user2);
        when(userService.getAll()).thenReturn(users);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
        verify(userService, times(1)).getAll();
    }

    @Test
    void getUserById_ShouldReturnUser() {
        when(userService.getById(1L)).thenReturn(user1);

        ResponseEntity<User> response = userController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user1, response.getBody());
        verify(userService, times(1)).getById(1L);
    }

    @Test
    void createUser_ShouldReturnCreatedUser() {
        when(userService.save(user1)).thenReturn(user1);

        ResponseEntity<User> response = userController.createUser(user1);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user1, response.getBody());
        verify(userService, times(1)).save(user1);
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() {
        when(userService.update(user1)).thenReturn(user1);

        ResponseEntity<User> response = userController.updateUser(user1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user1, response.getBody());
        verify(userService, times(1)).update(user1);
    }

    @Test
    void deleteUser_ShouldReturnNoContent() {
        when(userService.getById(1L)).thenReturn(user1);
        doNothing().when(userService).delete(user1);

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).getById(1L);
        verify(userService, times(1)).delete(user1);
    }

    @Test
    void calculateDailyCalories_ShouldReturnCalories() {
        double calories = 2000.0;
        when(userService.calculateDailyCalories(1L)).thenReturn(calories);

        ResponseEntity<Double> response = userController.calculateDailyCalories(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(calories, response.getBody());
        verify(userService, times(1)).calculateDailyCalories(1L);
    }
}