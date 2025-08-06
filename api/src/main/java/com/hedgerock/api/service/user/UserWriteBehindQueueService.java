package com.hedgerock.api.service.user;

import com.hedgerock.api.dto.UserDTO;
import com.hedgerock.api.mapper.UserMapper;
import com.hedgerock.api.repository.UserJpaRepository;
import com.hedgerock.api.utils.service.MyServiceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserWriteBehindQueueService {
    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    public void scheduleWrite(UserDTO userDTO) {
        executorService.submit(() ->
                MyServiceUtils.saveToDBWithDelay(
                        Duration.ofSeconds(0),
                        userDTO.id(),
                        userDTO,
                        userJpaRepository,
                        userMapper
                )
        );
    }
}
