package com.hedgerock.api.repository.redis;

import com.hedgerock.api.entity.redis.EventRedis;
import org.springframework.data.repository.CrudRepository;

public interface EventRedisRepository extends CrudRepository<EventRedis, String> {
}
