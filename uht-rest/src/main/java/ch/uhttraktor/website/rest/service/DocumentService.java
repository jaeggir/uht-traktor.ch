package ch.uhttraktor.website.rest.service;

import ch.uhttraktor.website.domain.Document;
import ch.uhttraktor.website.persistence.DocumentRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class DocumentService extends BaseService<Document> {

    @Inject
    private DocumentRepository documentRepository;

    protected DocumentRepository getRepository() {
        return documentRepository;
    }
}
