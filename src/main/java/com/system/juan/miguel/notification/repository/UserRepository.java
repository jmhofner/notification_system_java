package com.system.juan.miguel.notification.repository;

import com.system.juan.miguel.notification.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findBySubscribedContaining(String category);
}