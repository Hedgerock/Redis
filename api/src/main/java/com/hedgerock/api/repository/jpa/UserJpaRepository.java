package com.hedgerock.api.repository.jpa;

import com.hedgerock.api.entity.jpa.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, String> {
}
