package ch.uhttraktor.website.rest.config;


import ch.uhttraktor.website.AppConstants;
import ch.uhttraktor.website.rest.filter.CachingHttpHeadersFilter;
import ch.uhttraktor.website.rest.filter.CorsFilter;
import ch.uhttraktor.website.rest.filter.StaticResourcesProductionFilter;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;
import java.util.EnumSet;
import java.util.TimeZone;

import static javax.servlet.DispatcherType.*;

public class WebAppInitializer implements WebApplicationInitializer {

    private static final String SERVLET_NAME = "uht-web";

    @Override
    public void onStartup(ServletContext container) throws ServletException {

        // set default timezone to UTC - REQUIRED for saving Instant's to DB
        // see InstantPersistenceConverter. For the record: java.sql.Timestamp is a bitch.
        TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));

        EnumSet<DispatcherType> disps = EnumSet.of(REQUEST, FORWARD, ASYNC);

        // if no active profile is set via -Dspring.profiles.active or some other mechanism then this will be used.
        container.setInitParameter("spring.profiles.default", AppConstants.STAGE_DEV);

        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.setDisplayName("UHT Traktor");
        appContext.register(
                ApplicationConfiguration.class,
                DatabaseConfiguration.class,
                SecurityConfiguration.class
        );
        container.addListener(new ContextLoaderListener(appContext));

        // FILTERS
        container.addFilter("corsFilter", CorsFilter.class).addMappingForUrlPatterns(disps, true, "/api/*");

        // Spring security
        container.addFilter("springSecurityFilterChain", DelegatingFilterProxy.class)
                .addMappingForUrlPatterns(disps, true, "/*");

        // To support PUT, DELETE, ..
        container.addFilter("hiddenHttpMethodFilter", HiddenHttpMethodFilter.class)
                .addMappingForServletNames(disps, true, SERVLET_NAME);

        if (isProduction()) {
            addCachingFilter(container, disps);
            addStaticResourcesFilter(container, disps);
        }

        // DispatchServlet
        AnnotationConfigWebApplicationContext servletContext = new AnnotationConfigWebApplicationContext();
        servletContext.register(MvcConfiguration.class);

        ServletRegistration.Dynamic dispatcher;
        dispatcher = container.addServlet(SERVLET_NAME, new DispatcherServlet(servletContext));
        dispatcher.setAsyncSupported(true);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/api/*");
    }

    private void addCachingFilter(ServletContext container, EnumSet<DispatcherType> disps) {

        FilterRegistration.Dynamic cachingHttpHeadersFilter =
                container.addFilter("cachingHttpHeadersFilter", new CachingHttpHeadersFilter());

        cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/dist/images/*");
        cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/dist/fonts/*");
        cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/dist/styles/*");
        cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/dist/app/*");

        cachingHttpHeadersFilter.setAsyncSupported(true);
    }

    private void addStaticResourcesFilter(ServletContext container, EnumSet<DispatcherType> disps) {

        FilterRegistration.Dynamic staticResourcesProductionFilter =
                container.addFilter("staticResourcesFilter", StaticResourcesProductionFilter.class);

        staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/");
        staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/*");
        staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/index.html");

        staticResourcesProductionFilter.setAsyncSupported(true);
    }

    private boolean isProduction() {
        String profile = System.getProperty("spring.profiles.active");
        return profile != null && AppConstants.STAGE_PROD.equals(profile);
    }
}
