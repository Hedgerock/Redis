package com.hedgerock.api.mapper;

import com.hedgerock.api.entity.EntityType;

public interface KingMapper<DTO, ENTITY> {
    ENTITY toEntity(DTO dto);

    EntityType getEntityType();
}
