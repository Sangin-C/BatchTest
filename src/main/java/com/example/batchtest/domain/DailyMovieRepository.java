package com.example.batchtest.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyMovieRepository extends JpaRepository<DailyMovie, Long> {
}
