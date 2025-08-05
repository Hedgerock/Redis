package com.hedgerock.api.repository.jpa;

import com.hedgerock.api.entity.jpa.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventJpaRepository extends JpaRepository<EventEntity, String> {}
