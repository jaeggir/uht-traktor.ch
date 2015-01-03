package ch.uhttraktor.website.rest.service;

import ch.uhttraktor.website.domain.Event;
import ch.uhttraktor.website.persistence.EventRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class EventService extends BaseService<Event> {

    @Inject
    private EventRepository eventRepository;

    protected EventRepository getRepository() {
        return eventRepository;
    }
}
