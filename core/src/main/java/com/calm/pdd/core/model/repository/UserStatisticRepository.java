package com.calm.pdd.core.model.repository;

import com.calm.pdd.core.model.entity.UserStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserStatisticRepository extends JpaRepository<UserStatistic, Integer> {
	
	Optional<UserStatistic> findByUserId(int userId);
}
