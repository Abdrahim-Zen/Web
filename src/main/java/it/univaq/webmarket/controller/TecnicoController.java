/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package it.univaq.webmarket.controller;

import it.univaq.webmarket.application.ApplicationBaseController;
import it.univaq.webmarket.framework.security.SecurityHelpers;
import it.univaq.webmarket.framework.view.TemplateManagerException;
import it.univaq.webmarket.framework.view.TemplateResult;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author abdrahimzeno
 */
public class TecnicoController extends ApplicationBaseController {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        HttpSession session = SecurityHelpers.checkSession(request);
        if (session == null) {
            response.sendRedirect("login?error=3");
            return;
        }
        String azione = request.getParameter("azione");
        if ("a".equals(azione)) {
          response.sendRedirect("richiesteTecnico");
        }  else if ("b".equals(azione)) {
            response.sendRedirect("gestioneProdotti");

        } else {
            action_default(request, response);
        }
    }

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException {
        TemplateResult result = new TemplateResult(getServletContext());
        Map<String, Object> datamodel = new HashMap<>();
        HttpSession session = SecurityHelpers.checkSession(request);
        String username = (String) session.getAttribute("username");
        datamodel.put("username", username);
        String type = (String) session.getAttribute("userType");
        datamodel.put("userType", type);
        datamodel.put("userType", type);
        result.activate("tecnico.ftl.html", datamodel, request, response);
    }

}
