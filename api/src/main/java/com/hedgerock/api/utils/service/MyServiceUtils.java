package com.hedgerock.api.utils.service;

import com.hedgerock.api.mapper.KingMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;

@UtilityClass
@Slf4j
public class MyServiceUtils {

    public static<D, E, R extends Serializable, M extends KingMapper<D, R, E>> D getFromCache(
            String id, R event, M mapper
    ) {
        log.info("Cache hit for {} id: {} ", getEntityTitle(mapper), id);
        return mapper.toDTO(event);
    }

    private static String getEntityTitle(KingMapper<?, ?, ?> mapper) {
        return mapper.getEntityType().toString().toLowerCase();
    }

    public static<D, R, E> D getFromDataBaseAndSaveToRedis(
            String id,
            JpaRepository<E, String> jpaRepository,
            CrudRepository<R, String> crudRepository,
            KingMapper<D, R, E> mapper
    ) {
        final String title = getEntityTitle(mapper);
        log.info("Cache miss for {} id: {}, loading from database", title, id);
        return jpaRepository.findById(id)
                .map(entity -> {
                    final D dto = mapper.toDto(entity);
                    crudRepository.save(mapper.toRedis(dto));
                    log.info("{} id after cache loading from database is {}", title, id);
                    return dto;
                })
                .orElseThrow();
    }

    public static<T, R, E> T update(
            T updatedDto,
            CrudRepository<R, String> redisRepository,
            JpaRepository<E, String> jpaRepository,
            KingMapper<T, R, E> mapper
    ) {
        final var saved = jpaRepository.save(mapper.toEntity(updatedDto));
        redisRepository.save(mapper.toRedis(updatedDto));
        return mapper.toDto(saved);
    }

    public static<T, R, E> T create(
            T dto,
            CrudRepository<R, String> redisRepository,
            JpaRepository<E, String> entityRepository,
            KingMapper<T, R, E> mapper
    ) {
        final var saved = entityRepository.save(mapper.toEntity(dto));
        redisRepository.save(mapper.toRedis(dto));

        return mapper.toDto(saved);
    }
}
