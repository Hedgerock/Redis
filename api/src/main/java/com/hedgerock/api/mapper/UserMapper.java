package com.hedgerock.api.mapper;

import com.hedgerock.api.dto.UserDTO;
import com.hedgerock.api.entity.EntityType;
import com.hedgerock.api.entity.jpa.UserEntity;
import com.hedgerock.api.entity.redis.UserRedis;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends KingMapper<UserDTO, UserRedis, UserEntity> {

    @Override
    default EntityType getEntityType() {
        return EntityType.EVENT;
    }
}
