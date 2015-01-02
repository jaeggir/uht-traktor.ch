package ch.uhttraktor.website.rest.api;

import ch.uhttraktor.website.domain.News;
import ch.uhttraktor.website.persistence.util.Page;
import ch.uhttraktor.website.rest.service.NewsService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import static ch.uhttraktor.website.domain.SecurityRole.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Secured(value = {USER, NEWS})
public class NewsResource {

    @Inject
    private NewsService newsService;

    /**
     * GET  /news/{uuid} -> get news entry specified by uuid.
     */
    @Secured(value = ANONYMOUS)
    @RequestMapping(value = "/news/{uuid}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public News getEntry(HttpServletResponse response, @PathVariable(value = "uuid") String uuid) {
        return newsService.findOne(response, uuid);
    }

    /**
     * GET  /news -> get news entries based on filter parameters (or all if not given).
     */
    @Secured(value = ANONYMOUS)
    @RequestMapping(value = "/news", method = RequestMethod.GET, produces = "application/json")
    public Page<News> getEntries(@RequestParam(value = "pageSize", required = false) Integer pageSize,
                                     @RequestParam(value = "page", required = false) Integer page) {

        return newsService.find(pageSize, page);
    }

    /**
     * DELETE  /news/{uuid} -> delete news entry specified by uuid.
     */
    @RequestMapping(value = "/news/{uuid}", method = RequestMethod.DELETE)
    public void deleteEntry(HttpServletResponse response, @PathVariable(value = "uuid") String uuid) {
        newsService.delete(response, uuid);
    }

    /**
     * POST  /news -> create or update news entry
     */
    @RequestMapping(value = "/news", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public News createOrUpdateEntry(HttpServletResponse response, @RequestBody News news) {
        return newsService.createOrUpdate(response, news);
    }

}
