package com.khai.quizguru.repository;

import com.khai.quizguru.model.Library;
import com.khai.quizguru.model.Word;
import com.khai.quizguru.model.WordSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<Word, String> {

    Page<Word> findAllByWordSet(WordSet wordSet, Pageable pageable);
}
