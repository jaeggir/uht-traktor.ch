package ch.uhttraktor.website.rest.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;


public class UhtService {

    protected final Logger log = LogManager.getLogger(UhtService.class);

    protected UUID uuidFromString(String uuid) {
        try {
            return UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            log.debug("Illegal uuid: {}", uuid, e);
            return null;
        }
    }

}
