package com.khai.quizguru.repository;

import com.khai.quizguru.model.Library;
import com.khai.quizguru.model.WordSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WordSetRepository extends JpaRepository<WordSet, String> {

    @Query("SELECT ws FROM WordSet ws WHERE ws.isDeleted = false AND ws.library = :library")
    Page<WordSet> findByLibrary(@Param("library") Library library, Pageable pageable);
}
