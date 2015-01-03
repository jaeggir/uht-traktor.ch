package ch.uhttraktor.website.rest.api;

import ch.uhttraktor.website.domain.Event;
import ch.uhttraktor.website.rest.service.EventService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static ch.uhttraktor.website.domain.SecurityRole.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Secured(value = {USER, EVENTS})
public class EventResource {

    @Inject
    private EventService eventService;

    /**
     * GET  /events/{uuid} -> get event specified by uuid.
     */
    @Secured(value = ANONYMOUS)
    @RequestMapping(value = "/events/{uuid}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public Event getEvent(HttpServletResponse response, @PathVariable(value = "uuid") String uuid) {
        return eventService.findOne(response, uuid);
    }

    /**
     * GET  /events -> get all events
     */
    @Secured(value = ANONYMOUS)
    @RequestMapping(value = "/events", method = RequestMethod.GET, produces = "application/json")
    public List<Event> getEvents() {
        return eventService.findAll();
    }

    /**
     * DELETE  /events/{uuid} -> delete event specified by uuid.
     */
    @RequestMapping(value = "/events/{uuid}", method = RequestMethod.DELETE)
    public void deleteEvent(HttpServletResponse response, @PathVariable(value = "uuid") String uuid) {
        eventService.delete(response, uuid);
    }

    /**
     * POST  /events -> create or update event
     */
    @RequestMapping(value = "/events", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public Event createOrUpdateEvent(HttpServletResponse response, @RequestBody Event event) {
        return eventService.createOrUpdate(response, event);
    }

}
