package com.khai.quizguru.repository;

import com.khai.quizguru.model.Record;
import com.khai.quizguru.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, String> {
    List<Record> findAllByUser(User user);
}
