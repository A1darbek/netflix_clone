package com.ayderbek.netflxSpring.netflix_clone.repos;

import com.ayderbek.netflxSpring.netflix_clone.domain.Director;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectorRepository extends JpaRepository<Director,Long> {
}
