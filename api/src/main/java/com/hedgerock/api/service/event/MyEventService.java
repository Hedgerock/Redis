package com.hedgerock.api.service.event;

import com.hedgerock.api.dto.EventDTO;
import com.hedgerock.api.mapper.EventMapper;
import com.hedgerock.api.repository.jpa.EventJpaRepository;
import com.hedgerock.api.repository.redis.EventRedisRepository;
import com.hedgerock.api.utils.service.MyServiceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.hedgerock.api.utils.service.MyServiceUtils.getFromCache;
import static com.hedgerock.api.utils.service.MyServiceUtils.getFromDataBaseAndSaveToRedis;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyEventService implements EventService {
    private final EventRedisRepository eventRedisRepository;
    private final EventJpaRepository eventJpaRepository;
    private final EventMapper eventMapper;

    @Override
    public EventDTO create(EventDTO eventDTO) {
        log.info("Saving Event to Postgres and Redis");
        final EventDTO withIdDTO =
                new EventDTO(UUID.randomUUID().toString(), eventDTO.title(), eventDTO.description());

        return MyServiceUtils
                .create(withIdDTO, eventRedisRepository, eventJpaRepository, eventMapper);
    }

    @Override
    public EventDTO get(String id) {
        log.info("Trying to get info from Redis with id: {}", id);
        return eventRedisRepository.findById(id)
                .map(event -> getFromCache(id, event, eventMapper))
                .orElseGet(() ->
                        getFromDataBaseAndSaveToRedis(
                                id, eventJpaRepository, eventRedisRepository, eventMapper));
    }

    @Override
    @Transactional
    public EventDTO update(String id, EventDTO eventDTO) {
        log.info("Updating event id: {} in Postgres and Redis", id);
        return MyServiceUtils
                .update(eventDTO, eventRedisRepository, eventJpaRepository, eventMapper);
    }

    @Override
    public void delete(String id) {
        eventJpaRepository.deleteById(id);
        eventRedisRepository.deleteById(id);
    }
}
