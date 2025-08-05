package com.hedgerock.api.mapper;

import com.hedgerock.api.entity.EntityType;

public interface KingMapper<DTO, REDIS, ENTITY> {
    DTO toDto(ENTITY entity);
    ENTITY toEntity(DTO dto);

    REDIS toRedis(DTO entity);
    DTO toDTO(REDIS redis);

    EntityType getEntityType();
}
