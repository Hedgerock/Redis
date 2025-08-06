package com.hedgerock.api.service.event;

import com.hedgerock.api.dto.EventDTO;
import com.hedgerock.api.mapper.EventMapper;
import com.hedgerock.api.repository.EventJpaRepository;
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
public class EventWriteBehindQueueService {
    private final EventJpaRepository jpaRepository;
    private final EventMapper eventMapper;

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    public void scheduleWrite(EventDTO eventDTO) {
        executorService.submit(() ->
                MyServiceUtils.saveToDBWithDelay(
                        Duration.ofSeconds(15),
                        eventDTO.id(),
                        eventDTO,
                        jpaRepository,
                        eventMapper
                )
        );
    }

}
