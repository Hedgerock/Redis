package com.hedgerock.api.repository;

import com.hedgerock.api.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventJpaRepository extends JpaRepository<EventEntity, String> {}
