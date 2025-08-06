package com.hedgerock.api.service.user;

import com.hedgerock.api.dto.UserDTO;
import com.hedgerock.api.mapper.UserMapper;
import com.hedgerock.api.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyUserService implements UserService {

    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    @Override
    @CachePut(cacheNames = "users", key = "#result.id()")
    public UserDTO create(UserDTO userDTO) {
        log.info("Saving User to Postgres and Redis");
        final UserDTO dtoWithId =
                new UserDTO(UUID.randomUUID().toString(),
                        userDTO.name(),
                        userDTO.age(),
                        userDTO.events()
                );

        return userMapper.toDto(userJpaRepository.save(userMapper.toEntity(dtoWithId)));
    }

    @Override
    @Cacheable(cacheNames = "users", key = "#id", unless = "#result.name() == null")
    public UserDTO get(String id) {
        return userJpaRepository.findById(id).map(userMapper::toDto).orElseThrow();
    }

    @Override
    @CachePut(cacheNames = "users", key = "#id")
    public UserDTO update(String id, UserDTO dto) {
        log.info("Updating user in database and redis: {}", id);

        final var updatedDTO = new UserDTO(id, dto.name(), dto.age(), dto.events());

        return userMapper.toDto(userJpaRepository.save(userMapper.toEntity(updatedDTO)));
    }

    @Override
    @CacheEvict(cacheNames = "events", key = "#id")
    public void delete(String id) {
        userJpaRepository.deleteById(id);
    }
}
