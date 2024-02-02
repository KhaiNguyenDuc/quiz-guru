package com.khai.quizguru.repository;

import com.khai.quizguru.model.Library;
import com.khai.quizguru.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LibraryRepository extends JpaRepository<Library, String> {
    Optional<Library> findByUser(User user);
}
