package com.khai.quizguru.repository;

import com.khai.quizguru.model.Record;
import com.khai.quizguru.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, String> {
    Page<Record> findAllByUser(User user, Pageable pageable);
}
