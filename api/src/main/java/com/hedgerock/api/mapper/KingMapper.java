package com.hedgerock.api.mapper;

public interface KingMapper<DTO, ENTITY> {
    DTO toDto(ENTITY entity);
    ENTITY toEntity(DTO dto);
}
