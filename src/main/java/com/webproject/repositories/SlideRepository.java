package com.webproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.webproject.models.Slide;

@Repository
public interface SlideRepository extends JpaRepository<Slide, Long> {

}
