package edu.towson.booklibrary.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SuccessLoginHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

        authentication.getAuthorities().forEach(authority -> {
            if(authority.getAuthority().equals("ROLE_PARENT")){
                System.out.println("Parent");
                try{
                    redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/dashboard/parent");
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }else if(authority.getAuthority().equals("ROLE_ADMIN")){
                try{
                    System.out.println("Admin");
                    redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/dashboard/admin");
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }
}
