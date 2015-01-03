package ch.uhttraktor.website.rest.api;

import ch.uhttraktor.website.domain.Document;
import ch.uhttraktor.website.rest.service.DocumentService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static ch.uhttraktor.website.domain.SecurityRole.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Secured(value = {USER, DOCUMENTS})
public class DocumentResource {

    @Inject
    private DocumentService documentService;

    /**
     * GET  /documents/{uuid} -> get document specified by uuid.
     */
    @Secured(value = ANONYMOUS)
    @RequestMapping(value = "/documents/{uuid}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public Document getDocument(HttpServletResponse response, @PathVariable(value = "uuid") String uuid) {
        return documentService.findOne(response, uuid);
    }

    /**
     * GET  /documents -> get all documents
     */
    @Secured(value = ANONYMOUS)
    @RequestMapping(value = "/documents", method = RequestMethod.GET, produces = "application/json")
    public List<Document> getDocuments() {
        return documentService.findAll();
    }

    /**
     * DELETE  /documents/{uuid} -> delete document specified by uuid.
     */
    @RequestMapping(value = "/documents/{uuid}", method = RequestMethod.DELETE)
    public void deleteDocument(HttpServletResponse response, @PathVariable(value = "uuid") String uuid) {
        documentService.delete(response, uuid);
    }

    /**
     * POST  /documents -> create or update document
     */
    @RequestMapping(value = "/documents", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public Document createOrUpdateDocument(HttpServletResponse response, @RequestBody Document document) {
        return documentService.createOrUpdate(response, document);
    }

}
