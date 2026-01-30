package it.univaq.webmarket.controller;

import it.univaq.webmarket.application.ApplicationBaseController;
import it.univaq.webmarket.data.dao.impl.WebMarketDataLayer;
import it.univaq.webmarket.data.model.Ordine;
import it.univaq.webmarket.framework.data.DataException;
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

public class GestioneOrdiniController extends ApplicationBaseController {
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            
            
            HttpSession session = SecurityHelpers.checkSession(request);
          
            
            if(session == null){
                response.sendRedirect("login?error=3");
                return;
            }
            
            String userType = (String) session.getAttribute("userType");
            if (!"tecnico".equals(userType)) {
            response.sendRedirect("login?error=4");
            return;
        }
            
            
            String azione = request.getParameter("azione");
      
            
            if("aggiornaStato".equals(azione)) {
           
                aggiornaStatoOrdine(request, response);
            } else {
      
                visualizzaOrdini(request, response);
            }
            
    
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Errore gestione ordini: " + e.getMessage(), e);
        }
    }
    
    private void visualizzaOrdini(HttpServletRequest request, HttpServletResponse response) 
            throws TemplateManagerException, IOException, DataException {
        

        
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            Map<String, Object> datamodel = new HashMap<>();
            HttpSession session = SecurityHelpers.checkSession(request);
            
            // Info utente
            datamodel.put("userType", session.getAttribute("userType"));
            datamodel.put("username", session.getAttribute("username"));
         
            
            // Carica ordini
            WebMarketDataLayer dl = (WebMarketDataLayer) request.getAttribute("datalayer");
        
            
            if (dl == null) {
                throw new DataException("DataLayer è null");
            }
            Integer tecnicoId = (Integer) session.getAttribute("userid");
      
            List<Ordine> ordini = dl.getOrdineDAO().getAllOrdiniWithDetails(tecnicoId);
           
            
            datamodel.put("ordini", ordini);
            
       
            result.activate("gestioneOrdini.ftl.html", datamodel, request, response);

            
        } catch (Exception e) {
       
            e.printStackTrace();
            throw e;
        }
    }
    
    private void aggiornaStatoOrdine(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
      
        
        try {
            String idOrdineStr = request.getParameter("idOrdine");
            String nuovoStato = request.getParameter("stato");
            String motivazione = request.getParameter("motivazione");
            
           
            
            if(idOrdineStr != null && nuovoStato != null) {
                int idOrdine = Integer.parseInt(idOrdineStr);
                WebMarketDataLayer dl = (WebMarketDataLayer) request.getAttribute("datalayer");
          
                
                if(motivazione != null && !motivazione.isEmpty()) {
                
                    dl.getOrdineDAO().updateStatoOrdine(idOrdine, nuovoStato, motivazione);
                } else {
            
                    dl.getOrdineDAO().updateStatoOrdine(idOrdine, nuovoStato);
                }
     
                response.sendRedirect("gestioneOrdini");
            } else {
             
                response.sendRedirect("gestioneOrdini?error=1");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("gestioneOrdini?error=1");
        }
    }
}