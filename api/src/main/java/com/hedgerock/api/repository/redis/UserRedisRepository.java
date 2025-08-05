package com.hedgerock.api.repository.redis;

import com.hedgerock.api.entity.redis.UserRedis;
import org.springframework.data.repository.CrudRepository;

public interface UserRedisRepository extends CrudRepository<UserRedis, String> {
}
