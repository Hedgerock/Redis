package com.hedgerock.api.service.user;

import com.hedgerock.api.dto.UserDTO;
import com.hedgerock.api.mapper.UserMapper;
import com.hedgerock.api.repository.jpa.UserJpaRepository;
import com.hedgerock.api.repository.redis.UserRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.hedgerock.api.utils.service.MyServiceUtils.getFromCache;
import static com.hedgerock.api.utils.service.MyServiceUtils.getFromDataBaseAndSaveToRedis;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyUserService implements UserService {

    private final UserRedisRepository userRedisRepository;
    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    @Override
    public UserDTO create(UserDTO userDTO) {
        log.info("Saving User to Postgres and Redis");
        final UserDTO dtoWithId =
                new UserDTO(UUID.randomUUID().toString(),
                        userDTO.name(),
                        userDTO.age(),
                        userDTO.events()
                );

        final var saved = userJpaRepository.save(userMapper.toEntity(dtoWithId));
        userRedisRepository.save(userMapper.toRedis(dtoWithId));

        return userMapper.toDto(saved);
    }

    @Override
    public UserDTO get(String id) {
        return userRedisRepository.findById(id)
                .map(entity -> getFromCache(id, entity, userMapper))
                .orElseGet(() ->
                        getFromDataBaseAndSaveToRedis(
                                id, userJpaRepository, userRedisRepository, userMapper));
    }

    @Override
    public UserDTO update(String id, UserDTO dto) {
        log.info("Updating user in database and redis: {}", id);

        final var updatedUser = new UserDTO(id, dto.name(), dto.age(), dto.events());
        final var updated = userJpaRepository.save(userMapper.toEntity(updatedUser));
        userRedisRepository.save(userMapper.toRedis(updatedUser));

        return userMapper.toDto(updated);
    }

    @Override
    public void delete(String id) {
        userJpaRepository.deleteById(id);
        userRedisRepository.deleteById(id);
    }
}
