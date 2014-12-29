package ch.uhttraktor.website.rest.service;

import ch.uhttraktor.website.domain.News;
import ch.uhttraktor.website.persistence.NewsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

@Service
public class NewsService {

    @Inject
    private NewsRepository newsRepository;

    @Transactional(readOnly = true)
    public List<News> findAll() {
        return newsRepository.findAll();
    }
}
