package com.yakush.dailycalorieservice.repository;


import com.yakush.dailycalorieservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Object findByEmail(String email);
}
