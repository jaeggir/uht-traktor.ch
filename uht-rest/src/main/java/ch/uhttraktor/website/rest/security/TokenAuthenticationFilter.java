package ch.uhttraktor.website.rest.security;

import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Takes care of HTTP request/response pre-processing for login/logout and token check.
 * Login can be performed on specified {@link #loginUrl}, logout on specified {@link #logoutUrl}.
 * All the interaction with Spring Security should be performed via {@link TokenAuthenticationService}.
 */
public class TokenAuthenticationFilter extends GenericFilterBean {

    private static final String HEADER_TOKEN = "X-Auth-Token";
    private static final String HEADER_USERNAME = "X-Auth-Username";
    private static final String HEADER_PASSWORD = "X-Auth-Password";

    // Request attribute that indicates that this filter will not continue with the chain, e.g. after login, logout
    private static final String REQUEST_ATTR_DO_NOT_CONTINUE = "TokenAuthenticationFilter-doNotContinue";

    // Url for login (POST)
    private final String loginUrl;

    // Url for logout (POST)
    private final String logoutUrl;


    private final TokenAuthenticationService authenticationService;

    public TokenAuthenticationFilter(TokenAuthenticationService authenticationService, String loginUrl, String logoutUrl) {
        this.authenticationService = authenticationService;
        this.loginUrl = loginUrl;
        this.logoutUrl = logoutUrl;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        boolean authenticated = checkToken(httpRequest, httpResponse);

        if (canRequestProcessingContinue(httpRequest) && httpRequest.getMethod().equals("POST")) {
            if (authenticated) {
                checkLogout(httpRequest);
            } else {
                checkLogin(httpRequest, httpResponse);
            }
        }

        if (canRequestProcessingContinue(httpRequest)) {
            chain.doFilter(request, response);
        }
    }

    private void checkLogin(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
        if (currentLink(httpRequest).equals(loginUrl)) {

            String username = httpRequest.getHeader(HEADER_USERNAME);
            String password = httpRequest.getHeader(HEADER_PASSWORD);

            if (username != null && password != null) {
                checkUsernameAndPassword(username, password, httpResponse);
                doNotContinueWithRequestProcessing(httpRequest);
            }
        }
    }

    private void checkUsernameAndPassword(String username, String password, HttpServletResponse response)
            throws IOException {

        TokenInfo tokenInfo = authenticationService.authenticate(username, password);
        if (tokenInfo != null) {
            response.setHeader(HEADER_TOKEN, tokenInfo.getToken());
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    /** Returns true, if request contains valid authentication token. */
    private boolean checkToken(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
        String token = httpRequest.getHeader(HEADER_TOKEN);
        if (token == null) {
            return false;
        }

        if (authenticationService.checkToken(token)) {
            return true;
        } else {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            doNotContinueWithRequestProcessing(httpRequest);
        }
        return false;
    }

    private void checkLogout(HttpServletRequest httpRequest) {
        if (currentLink(httpRequest).equals(logoutUrl)) {
            String token = httpRequest.getHeader(HEADER_TOKEN);
            // we go here only authenticated, token must not be null
            authenticationService.logout(token);
            doNotContinueWithRequestProcessing(httpRequest);
        }
    }

    private String currentLink(HttpServletRequest httpRequest) {
        if (httpRequest.getPathInfo() == null) {
            return httpRequest.getServletPath();
        }
        return httpRequest.getServletPath() + httpRequest.getPathInfo();
    }

    /**
     * This is set in cases when we don't want to continue down the filter chain. This occurs
     * for any {@link HttpServletResponse#SC_UNAUTHORIZED} and also for login or logout.
     */
    private void doNotContinueWithRequestProcessing(HttpServletRequest httpRequest) {
        httpRequest.setAttribute(REQUEST_ATTR_DO_NOT_CONTINUE, "");
    }

    private boolean canRequestProcessingContinue(HttpServletRequest httpRequest) {
        return httpRequest.getAttribute(REQUEST_ATTR_DO_NOT_CONTINUE) == null;
    }
}
