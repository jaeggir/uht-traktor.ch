package ch.uhttraktor.website.rest.servlet;

import ch.uhttraktor.website.AppConstants;
import ch.uhttraktor.website.rest.filter.CachingHttpHeadersFilter;
import ch.uhttraktor.website.rest.filter.CorsFilter;
import ch.uhttraktor.website.rest.filter.StaticResourcesProductionFilter;
import ch.uhttraktor.website.rest.servlet.config.ApplicationConfiguration;
import ch.uhttraktor.website.rest.servlet.config.DatabaseConfiguration;
import ch.uhttraktor.website.rest.servlet.config.MvcConfiguration;
import ch.uhttraktor.website.rest.servlet.config.SecurityConfiguration;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.TimeZone;

public class MvcInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    private static final String SERVLET_NAME = "uht-web";

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] {
                ApplicationConfiguration.class,
                DatabaseConfiguration.class,
                SecurityConfiguration.class
        };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{
                MvcConfiguration.class
        };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/api/*" };
    }

    @Override
    protected String getServletName() {
        return SERVLET_NAME;
    }

    @Override
    protected Filter[] getServletFilters() {
         return new Filter[] {
                 new CorsFilter(),
                 new HiddenHttpMethodFilter()
        };
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        // set default timezone to UTC - REQUIRED for saving Instant's to DB
        // see InstantPersistenceConverter. For the record: java.sql.Timestamp is a bitch.
        TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));

        // if no active profile is set via -Dspring.profiles.active or some other mechanism then this will be used.
        servletContext.setInitParameter("spring.profiles.default", AppConstants.STAGE_DEV);

        if (isProduction()) {
            addCachingFilter(servletContext);
            addStaticResourcesFilter(servletContext);
        }

        super.onStartup(servletContext);
    }

    private void addCachingFilter(ServletContext container) {

        FilterRegistration.Dynamic filter;
        filter = container.addFilter("cachingHeadersFilter", new CachingHttpHeadersFilter());

        filter.addMappingForUrlPatterns(null, true, "/dist/images/*");
        filter.addMappingForUrlPatterns(null, true, "/dist/fonts/*");
        filter.addMappingForUrlPatterns(null, true, "/dist/styles/*");
        filter.addMappingForUrlPatterns(null, true, "/dist/app/*");

        filter.setAsyncSupported(true);
    }

    private void addStaticResourcesFilter(ServletContext container) {

        FilterRegistration.Dynamic filter;
        filter = container.addFilter("staticResourcesFilter", StaticResourcesProductionFilter.class);

        filter.addMappingForUrlPatterns(null, true, "/");
        filter.addMappingForUrlPatterns(null, true, "/*");
        filter.addMappingForUrlPatterns(null, true, "/index.html");

        filter.setAsyncSupported(true);
    }

    private boolean isProduction() {
        String profile = System.getProperty("spring.profiles.active");
        return profile != null && AppConstants.STAGE_PROD.equals(profile);
    }
}
