
package it.univaq.webmarket.controller;

import it.univaq.webmarket.application.ApplicationBaseController;
import it.univaq.webmarket.framework.security.SecurityHelpers;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author abrah
 */

public class LogoutController extends ApplicationBaseController {


    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException
             {
                 action_logout(request, response);
    }
    private void action_logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SecurityHelpers.disposeSession(request);
            
            response.sendRedirect("login");
        
    }


}
