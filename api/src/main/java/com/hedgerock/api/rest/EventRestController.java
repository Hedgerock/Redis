package com.hedgerock.api.rest;

import com.hedgerock.api.dto.EventDTO;
import com.hedgerock.api.service.event.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/events")
@RequiredArgsConstructor
public class EventRestController {

    private final EventService eventService;

    @PostMapping
    public EventDTO createEvent(@RequestBody EventDTO eventDTO) {
        return eventService.create(eventDTO);
    }

    @GetMapping("{id}")
    public EventDTO getEventById(@PathVariable String id) {
        return eventService.get(id);
    }

    @PatchMapping("{id}")
    public EventDTO update(@PathVariable String id, @RequestBody EventDTO eventDTO) {
        return eventService.update(id, eventDTO);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        eventService.delete(id);
    }
}
