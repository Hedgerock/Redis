package com.hedgerock.api.mapper;

import com.hedgerock.api.dto.UserDTO;
import com.hedgerock.api.entity.EntityType;
import com.hedgerock.api.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends KingMapper<UserDTO, UserEntity> {
    @Override
    default EntityType getEntityType() {
        return EntityType.USER;
    }
}
