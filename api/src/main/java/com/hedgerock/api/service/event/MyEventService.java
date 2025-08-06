package com.hedgerock.api.service.event;

import com.hedgerock.api.dto.EventDTO;
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
public class MyEventService implements EventService {
    private static final String CACHE_NAME = "events";

    private final CacheManager cacheManager;
    private final EventWriteBehindQueueService writeBehindQueueService;

    @Override
    public EventDTO create(EventDTO eventDTO) {
        log.info("Write-Behind: saving event in cache and scheduling to db for save");
        final EventDTO withIdDTO =
                new EventDTO(UUID.randomUUID().toString(), eventDTO.title(), eventDTO.description());

        putToCache(withIdDTO);
        writeBehindQueueService.scheduleWrite(withIdDTO);

        return withIdDTO;
    }

    @Override
    public EventDTO get(String id) {
        return getCache(CACHE_NAME, cacheManager).get(id, EventDTO.class);
    }

    @Override
    @Transactional
    public EventDTO update(String id, EventDTO eventDTO) {
        log.info("Write-Behind: updating event in cache and scheduling to db for update");
        final var updatedDTO = new EventDTO(id, eventDTO.title(), eventDTO.description());
        putToCache(updatedDTO);
        writeBehindQueueService.scheduleWrite(updatedDTO);

        return updatedDTO;
    }

    @Override
    public void delete(String id) {
        log.info("Evicting event from cache and db");
        final Cache cache = getCache(CACHE_NAME, cacheManager);
        cache.evict(id);
    }

    private void putToCache(EventDTO eventDTO) {
        final Cache cache = getCache(CACHE_NAME, cacheManager);
        cache.put(eventDTO.id(), eventDTO);
    }

}
