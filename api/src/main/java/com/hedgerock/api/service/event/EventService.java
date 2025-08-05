package com.hedgerock.api.service.event;

import com.hedgerock.api.dto.EventDTO;

public interface EventService {
    EventDTO create(EventDTO eventDTO);
    EventDTO get(String id);
    EventDTO update(String id, EventDTO dto);
    void delete(String id);
}
