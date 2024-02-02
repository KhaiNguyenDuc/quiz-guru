package com.khai.quizguru.repository;

import com.khai.quizguru.model.Word;
import com.khai.quizguru.model.WordSet;
import com.khai.quizguru.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<Word, String> {

    Page<Word> findAllByWordSet(WordSet wordSet, Pageable pageable);


    @Query("SELECT w FROM Word w JOIN w.wordSet ws JOIN ws.library l WHERE ws.id = :wordSetId AND LOWER(w.name) = LOWER(:name) AND l.user = :user")
    Optional<Word> findByNameAndUser(@Param("name") String name, @Param("wordSetId") String wordSetId ,@Param("user") User user);

    @Query("SELECT w FROM Word w JOIN w.wordSet ws JOIN ws.library l WHERE LOWER(w.id) = LOWER(:id) AND l.user = :user")

    Optional<Word> findByIdAndUser(@Param("id") String id, @Param("user") User user);

    boolean existsByNameAndWordSet(String name, WordSet wordSet);
}
