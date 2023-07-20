package com.pi.stepup.domain.dance.dao;

import com.pi.stepup.domain.dance.domain.Dance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface DanceRepository extends JpaRepository<Dance, Long> {

}
