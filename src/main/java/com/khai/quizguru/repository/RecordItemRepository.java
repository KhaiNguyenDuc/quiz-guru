package com.khai.quizguru.repository;

import com.khai.quizguru.model.RecordItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordItemRepository extends JpaRepository<RecordItem, String> {
}
