package com.hedgerock.api.mapper;

import com.hedgerock.api.dto.EventDTO;
import com.hedgerock.api.entity.EventEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventMapper extends KingMapper<EventDTO, EventEntity> {
}
