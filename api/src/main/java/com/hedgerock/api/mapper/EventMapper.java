package com.hedgerock.api.mapper;

import com.hedgerock.api.dto.EventDTO;
import com.hedgerock.api.entity.EntityType;
import com.hedgerock.api.entity.jpa.EventEntity;
import com.hedgerock.api.entity.redis.EventRedis;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventMapper extends KingMapper<EventDTO, EventRedis, EventEntity> {
    @Override
    default EntityType getEntityType() {
        return EntityType.EVENT;
    }
}
