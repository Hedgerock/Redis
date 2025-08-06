package com.hedgerock.api.service.event;

import com.hedgerock.api.dto.EventDTO;
import com.hedgerock.api.mapper.EventMapper;
import com.hedgerock.api.repository.EventJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyEventService implements EventService {
    private final EventJpaRepository eventJpaRepository;
    private final EventMapper eventMapper;

    @Override
    @CachePut(cacheNames = "events", key = "#result.id()")
    public EventDTO create(EventDTO eventDTO) {
        log.info("Saving Event to Postgres and Redis");
        final EventDTO withIdDTO =
                new EventDTO(UUID.randomUUID().toString(), eventDTO.title(), eventDTO.description());

        return eventMapper.toDto(eventJpaRepository.save(eventMapper.toEntity(withIdDTO)));
    }

    @Override
    @Cacheable(cacheNames = "events", key = "#id", unless = "#result.id() == null")
    public EventDTO get(String id) {
        log.info("Trying to get info from Redis with id: {}", id);
        return eventJpaRepository.findById(id).map(eventMapper::toDto).orElseThrow();
    }

    @Override
    @Transactional
    @CachePut(cacheNames = "events", key = "#id")
    public EventDTO update(String id, EventDTO eventDTO) {
        log.info("Updating event id: {} in Postgres and Redis", id);
        final var updatedDTO = new EventDTO(id, eventDTO.title(), eventDTO.description());
        final var updated = eventJpaRepository.save(eventMapper.toEntity(updatedDTO));

        return eventMapper.toDto(updated);
    }

    @Override
    @CacheEvict(cacheNames = "events", key = "#id")
    public void delete(String id) {
        eventJpaRepository.deleteById(id);
    }
}
