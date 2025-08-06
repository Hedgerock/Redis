package com.hedgerock.api.utils.service;

import com.hedgerock.api.mapper.KingMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Duration;
import java.util.NoSuchElementException;

@UtilityClass
@Slf4j
public class MyServiceUtils {

    public static<I, T, E> void saveToDBWithDelay(
            Duration duration,
            I id,
            T dto,
            JpaRepository<E, I> jpaRepository,
            KingMapper<T, E> mapper
    ) {
        final String type = getEntityType(mapper);

        try {
            Thread.sleep(duration);
            log.info("Async DB save for {} id: {}", type, id);
            jpaRepository.save(mapper.toEntity(dto));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Async write task was interrupted", e);
        } catch (Exception e) {
            log.warn("Failed to save {} to DB", type, e);
        }
    }

    public static String getEntityType(KingMapper<?, ?> mapper) {
        return mapper.getEntityType().toString().toLowerCase();
    }

    public static Cache getCache(String name, CacheManager cacheManager) {
        Cache cache = cacheManager.getCache(name);
        if (cache == null) throw new NoSuchElementException("Cache not found " + name);
        return cache;
    }

}

