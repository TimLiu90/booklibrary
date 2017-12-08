package edu.towson.booklibrary.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by lastcow_chen on 7/7/17.
 */
@Component
public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {

    // Logger
    private static Logger logger = LoggerFactory.getLogger(AccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null){
            logger.info("User " + auth.getName() + " attempted to access the protected URL: "
                    + httpServletRequest.getRequestURL());
        }

        httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/403");
    }
}
