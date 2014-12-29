package ch.uhttraktor.website.rest.service;

import ch.uhttraktor.website.domain.News;
import ch.uhttraktor.website.persistence.NewsRepository;
import ch.uhttraktor.website.persistence.util.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
public class NewsService extends UhtService {

    @Inject
    private NewsRepository newsRepository;

    @Transactional(readOnly = true)
    public News findOne(HttpServletResponse response, String uuidString) {
        UUID uuid = uuidFromString(uuidString);
        if (uuid == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        News news = newsRepository.findOne(uuid);
        if (news == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return news;
    }
    @Transactional(readOnly = true)
    public Page<News> find(Integer pageSize, Integer page) {
        return newsRepository.find(pageSize, page);
    }

    @Transactional
    public void delete(HttpServletResponse response, String uuidString) {
        UUID uuid = uuidFromString(uuidString);
        if (uuid == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        News news = newsRepository.findOne(uuid);
        if (news == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            newsRepository.remove(news);
        }
    }
}
