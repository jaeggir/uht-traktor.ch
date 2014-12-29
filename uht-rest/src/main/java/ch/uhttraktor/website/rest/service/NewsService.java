package ch.uhttraktor.website.rest.service;

import ch.uhttraktor.website.domain.News;
import ch.uhttraktor.website.persistence.NewsRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class NewsService extends BaseService<News> {

    @Inject
    private NewsRepository newsRepository;

    protected NewsRepository getRepository() {
        return newsRepository;
    }
}
