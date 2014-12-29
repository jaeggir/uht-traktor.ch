package ch.uhttraktor.website.rest.api;

import ch.uhttraktor.website.domain.News;
import ch.uhttraktor.website.persistence.util.Page;
import ch.uhttraktor.website.rest.service.NewsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
public class NewsResource {

    @Inject
    private NewsService newsService;

    /**
     * GET  /news -> get news entries based on filter parameters (or all if not given).
     */
    @RequestMapping(value = "/news", method = RequestMethod.GET, produces = "application/json")
    public Page<News> getNewsEntries(@RequestParam(value = "pageSize", required = false) Integer pageSize,
                                     @RequestParam(value = "page", required = false) Integer page) {

        return newsService.find(pageSize, page);
    }

}
