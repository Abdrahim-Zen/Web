
package it.univaq.webmarket.controller;

import it.univaq.webmarket.application.ApplicationBaseController;
import it.univaq.webmarket.data.dao.impl.WebMarketDataLayer;
import it.univaq.webmarket.data.model.RichiestaAcquisto;
import it.univaq.webmarket.framework.security.SecurityHelpers;
import it.univaq.webmarket.framework.view.TemplateManagerException;
import it.univaq.webmarket.framework.view.TemplateResult;
import jakarta.servlet.ServletConfig;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author abdrahimzeno
 */
public class StatoRichiesteController extends ApplicationBaseController {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, TemplateManagerException {

        HttpSession session = SecurityHelpers.checkSession(request);
        if (session == null) {
            response.sendRedirect("login?error=3");
            return;
        }
        String userType = (String) session.getAttribute("userType");
        if (!"utenteRegistrato".equals(userType)) {
            response.sendRedirect("login?error=4");
            return;
        }
        WebMarketDataLayer dl = (WebMarketDataLayer) request.getAttribute("datalayer");
        Integer idUtente = (Integer) session.getAttribute("userid");
        String username = (String) session.getAttribute("username");
        Map<String, Object> datamodel = new HashMap<>();

        List<RichiestaAcquisto> richieste = dl.getRichiestaAcquistoDAO().getRichiesteByUtenteId(idUtente);

        for (RichiestaAcquisto richiesta : richieste) {
            try {
                List<Map<String, String>> specifiche
                        = dl.getRichiestaAcquistoDAO().getSpecificheByRichiestaId(richiesta
                                .getKey());

                datamodel.put("specificheRichiesta_" + richiesta.getKey(), specifiche);

            } catch (Exception e) {
                System.err.println("Errore nel recupero specifiche: " + e.getMessage());

            }

        }
        
        datamodel.put("userType", userType);
        datamodel.put("richieste", richieste);
        datamodel.put("username", username);
        TemplateResult result = new TemplateResult(getServletContext());
        result.activate("richiesteDiAcquisto.ftl.html", datamodel, request, response);

    }

}
