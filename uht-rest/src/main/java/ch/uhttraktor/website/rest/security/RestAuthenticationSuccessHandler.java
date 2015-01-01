package ch.uhttraktor.website.rest.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

 
    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth)
            throws ServletException, IOException {

        clearAuthenticationAttributes(req);
        res.setStatus(HttpServletResponse.SC_OK);
    }

}