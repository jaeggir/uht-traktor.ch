package ch.uhttraktor.website.rest.api;

import ch.uhttraktor.website.domain.News;
import ch.uhttraktor.website.rest.service.NewsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

@RestController
public class NewsResource {

    @Inject
    private NewsService newsService;

    /**
     * GET  /news -> get news entries based on filter parameters (or all if not given).
     */
    @RequestMapping(value = "/news", method = RequestMethod.GET, produces = "application/json")
    public List<News> getNewsEntries() {
        return newsService.findAll();
    }

}
