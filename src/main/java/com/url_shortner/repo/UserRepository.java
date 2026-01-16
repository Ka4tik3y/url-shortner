package com.url_shortner.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.url_shortner.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
Optional<User> findByUsername (String username);

}
