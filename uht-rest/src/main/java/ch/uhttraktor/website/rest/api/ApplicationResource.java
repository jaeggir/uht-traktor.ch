package ch.uhttraktor.website.rest.api;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static ch.uhttraktor.website.AppConstants.STAGE_DEV;
import static ch.uhttraktor.website.AppConstants.STAGE_PROD;
import static ch.uhttraktor.website.domain.SecurityRole.ANONYMOUS;

/**
 * REST controller for application details (e.g. version).
 */
@RestController
@Secured(value = {ANONYMOUS})
public class ApplicationResource implements EnvironmentAware {

    private Environment env;

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }

    /**
     * GET  /application -> return static application details
     */
    @RequestMapping(value = "/application", method = RequestMethod.GET, produces = "application/json")
    public Map<String, String> getApplicationDetails() {

        Map<String, String> result = new HashMap<>();

        // environment (dev or prod)
        result.put("releaseStage", env.acceptsProfiles(STAGE_PROD) ? STAGE_PROD : STAGE_DEV);

        // app version
        String appVersion = getClass().getPackage().getImplementationVersion();
        if (appVersion == null) {
            Properties prop = new Properties();
            try {
                prop.load(getClass().getResourceAsStream("/META-INF/MANIFEST.MF"));
                appVersion = prop.getProperty("Implementation-Version");
            } catch (IOException e) {
                appVersion = "error";
            }
        }
        result.put("version", appVersion);

        return result;
    }

}
