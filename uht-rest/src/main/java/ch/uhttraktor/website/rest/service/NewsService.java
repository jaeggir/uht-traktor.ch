package ch.uhttraktor.website.rest.service;

import ch.uhttraktor.website.domain.News;
import ch.uhttraktor.website.persistence.NewsRepository;
import ch.uhttraktor.website.persistence.util.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
public class NewsService {

    @Inject
    private NewsRepository newsRepository;

    @Transactional(readOnly = true)
    public Page<News> find(Integer pageSize, Integer page) {
        return newsRepository.find(pageSize, page);
    }
}
