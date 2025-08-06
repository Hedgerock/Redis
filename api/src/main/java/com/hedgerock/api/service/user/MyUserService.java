package com.hedgerock.api.service.user;

import com.hedgerock.api.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.hedgerock.api.utils.service.MyServiceUtils.getCache;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyUserService implements UserService {
    private static final String CACHE_NAME = "users";

    private final CacheManager cacheManager;
    private final UserWriteBehindQueueService writeBehindQueueService;

    @Override
    public UserDTO create(UserDTO userDTO) {
        log.info("Write-Behind: saving User to cache and scheduling save for db");
        final UserDTO dtoWithId =
                new UserDTO(UUID.randomUUID().toString(),
                        userDTO.name(),
                        userDTO.age(),
                        userDTO.events()
                );

        putToCache(dtoWithId);
        writeBehindQueueService.scheduleWrite(dtoWithId);
        return dtoWithId;
    }

    @Override
    public UserDTO get(String id) {
        return getCache(CACHE_NAME, cacheManager).get(id, UserDTO.class);
    }

    @Override
    @Transactional
    public UserDTO update(String id, UserDTO dto) {
        log.info("Write-Behind: updating user in cache and scheduling update for db");

        final var updatedDTO = new UserDTO(id, dto.name(), dto.age(), dto.events());

        putToCache(updatedDTO);
        writeBehindQueueService.scheduleWrite(updatedDTO);

        return updatedDTO;
    }

    @Override
    public void delete(String id) {
        log.info("Write-Behind: evicting user from cache and db");
        final Cache cache = getCache(CACHE_NAME, cacheManager);
        cache.evict(id);
    }

    private void putToCache(UserDTO userDTO) {
        final Cache cache = getCache(CACHE_NAME, cacheManager);
        cache.put(userDTO.id(), userDTO);
    }
}
