package it.univaq.webmarket.controller;

import it.univaq.webmarket.application.ApplicationBaseController;
import it.univaq.webmarket.data.dao.impl.WebMarketDataLayer;
import it.univaq.webmarket.data.model.PresaInCarico;
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
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RichiesteTecnicoController extends ApplicationBaseController {

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
        if (!"tecnico".equals(userType)) {
            response.sendRedirect("login?error=4");
            return;
        }
        
        String azione = request.getParameter("action");
        
        if ("accettaRichiesta".equals(azione)) {
            action_accettaRichiesta(request, response);
        } else {
            action_default(request, response);
        }
    }

    private void action_default(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, TemplateManagerException {
        
        try {
            WebMarketDataLayer dl = (WebMarketDataLayer) request.getAttribute("datalayer");
            List<RichiestaAcquisto> lista = dl.getRichiestaAcquistoDAO().getRichiesteNonAssociate();
            
            Map<String, Object> datamodel = new HashMap<>();
            datamodel.put("lista", lista);
            
            HttpSession session = SecurityHelpers.checkSession(request);
            datamodel.put("userType", session.getAttribute("userType"));
            datamodel.put("username", session.getAttribute("username"));
            
            TemplateResult result = new TemplateResult(getServletContext());
            result.activate("richiesteTecnico.ftl.html", datamodel, request, response);
            
        } catch (Exception ex) {
            throw new ServletException("Errore nel caricamento delle richieste", ex);
        }
    }

    private void action_accettaRichiesta(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            HttpSession session = SecurityHelpers.checkSession(request);
            Integer tecnicoId = (Integer) session.getAttribute("userid");
            if (tecnicoId == null) {
                tecnicoId = (Integer) session.getAttribute("userId");
            }
            
            if (tecnicoId == null) {
                response.sendRedirect("login?error=3");
                return;
            }
            
            String idRichiestaStr = request.getParameter("idRichiesta");
            if (idRichiestaStr == null) {
                response.sendRedirect("richiesteTecnico?error=1");
                return;
            }
            
            int idRichiesta;
            try {
                idRichiesta = Integer.parseInt(idRichiestaStr);
            } catch (NumberFormatException e) {
                response.sendRedirect("richiesteTecnico?error=1");
                return;
            }
            
            WebMarketDataLayer dl = (WebMarketDataLayer) request.getAttribute("datalayer");
            if (dl == null) {
                response.sendRedirect("richiesteTecnico?error=1");
                return;
            }
            
            // CORREZIONE: usa il nuovo metodo getRichiestaAcquistoByID
            RichiestaAcquisto richiesta = dl.getRichiestaAcquistoDAO().getRichiestaAcquistoByID(idRichiesta);
            if (richiesta == null) {
                response.sendRedirect("richiesteTecnico?error=1");
                return;
            }
            
            // Verifica che la richiesta non sia già in carico a qualcuno
            PresaInCarico presaEsistente = dl.getPresaInCaricoDAO().getPresaInCaricoByRichiestaID(idRichiesta);
            if (presaEsistente != null) {
                response.sendRedirect("richiesteTecnico?error=2");
                return;
            }
            
            // Crea la presa in carico
            PresaInCarico presaInCarico = dl.getPresaInCaricoDAO().createPresaInCarico();
            presaInCarico.setTecnicoKey(tecnicoId);
            presaInCarico.setRichiestaKey(idRichiesta);
            presaInCarico.setDataIncarico(new Timestamp(System.currentTimeMillis()));
            
            // Inserisce la presa in carico nel database
            dl.getPresaInCaricoDAO().insertPresaInCarico(presaInCarico);
            
            // Aggiorna lo stato della richiesta a "in_valutazione"
            richiesta.setStato("in_valutazione");
            dl.getRichiestaAcquistoDAO().updateRichiestaAcquisto(richiesta);
            response.sendRedirect("richiesteTecnico?success=1");
          
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("richiesteTecnico?error=1");
        }
    }
}