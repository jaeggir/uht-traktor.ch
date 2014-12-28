package ch.uhttraktor.website.rest.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * This filter is used in production, to serve static resources generated by "grunt build".
 * <p/>
 * <p>
 * It is configured to serve resources from the "dist" directory, which is the Grunt
 * destination directory.
 * </p>
 */
public class StaticResourcesProductionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Nothing to initialize
    }

    @Override
    public void destroy() {
        // Nothing to destroy
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String contextPath = ((HttpServletRequest) request).getContextPath();
        String requestURI = httpRequest.getRequestURI();
        requestURI = substringAfter(requestURI, contextPath);
        if ("/".equals(requestURI)) {
            requestURI = requestURI + "index.html";
        }
        String newURI = "/dist" + requestURI;
        request.getRequestDispatcher(newURI).forward(request, response);
    }

    private String substringAfter(String str, String separator) {
        if (isEmpty(str)) {
            return str;
        }
        if (separator == null) {
            return "";
        }
        int pos = str.indexOf(separator);
        if (pos == -1) {
            return "";
        }
        return str.substring(pos + separator.length());
    }

    private boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

}