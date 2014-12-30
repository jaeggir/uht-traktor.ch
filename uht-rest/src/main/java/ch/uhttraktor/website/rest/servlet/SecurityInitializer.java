package ch.uhttraktor.website.rest.servlet;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * Registers the {@link org.springframework.web.filter.DelegatingFilterProxy} to use the springSecurityFilterChain.
 */
public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {
}