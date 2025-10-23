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
            System.out.println("=== INIZIO processRequest GestioneOrdini ===");
            
            HttpSession session = SecurityHelpers.checkSession(request);
            System.out.println("=== SESSION: " + (session != null ? "OK" : "NULL") + " ===");
            
            if(session == null){
                System.out.println("=== SESSION NULL - Redirect login ===");
                response.sendRedirect("login?error=3");
                return;
            }
            
            String userType = (String) session.getAttribute("userType");
            System.out.println("=== USER TYPE: " + userType + " ===");
            
            if(!"amministratore".equals(userType)) {
                System.out.println("=== USER TYPE NON AMMINISTRATORE - Redirect login ===");
                response.sendRedirect("login?error=4");
                return;
            }
            
            String azione = request.getParameter("azione");
            System.out.println("=== AZIONE: " + azione + " ===");
            
            if("aggiornaStato".equals(azione)) {
                System.out.println("=== CHIAMATA aggiornaStatoOrdine ===");
                aggiornaStatoOrdine(request, response);
            } else {
                System.out.println("=== CHIAMATA visualizzaOrdini ===");
                visualizzaOrdini(request, response);
            }
            
            System.out.println("=== FINE processRequest SUCCESS ===");
            
        } catch (Exception e) {
            System.err.println("=== ERRORE GENERALE processRequest ===");
            System.err.println("=== MESSAGGIO ERRORE: " + e.getMessage() + " ===");
            e.printStackTrace();
            throw new ServletException("Errore gestione ordini: " + e.getMessage(), e);
        }
    }
    
    private void visualizzaOrdini(HttpServletRequest request, HttpServletResponse response) 
            throws TemplateManagerException, IOException, DataException {
        
        System.out.println("=== INIZIO visualizzaOrdini ===");
        
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            Map<String, Object> datamodel = new HashMap<>();
            HttpSession session = SecurityHelpers.checkSession(request);
            
            // Info utente
            datamodel.put("userType", session.getAttribute("userType"));
            datamodel.put("username", session.getAttribute("username"));
            System.out.println("=== INFO UTENTE AGGIUNTE AL DATAMODEL ===");
            
            // Carica ordini
            WebMarketDataLayer dl = (WebMarketDataLayer) request.getAttribute("datalayer");
            System.out.println("=== DATALAYER: " + (dl != null ? "OK" : "NULL") + " ===");
            
            if (dl == null) {
                throw new DataException("DataLayer è null");
            }
            
            System.out.println("=== PRIMA DI getAllOrdiniWithDetails ===");
            List<Ordine> ordini = dl.getOrdineDAO().getAllOrdiniWithDetails();
            System.out.println("=== DOPO getAllOrdiniWithDetails - Numero ordini: " + (ordini != null ? ordini.size() : "NULL") + " ===");
            
            datamodel.put("ordini", ordini);
            
            System.out.println("=== PRIMA DI activate template ===");
            result.activate("gestioneOrdini.ftl.html", datamodel, request, response);
            System.out.println("=== DOPO activate template ===");
            
        } catch (Exception e) {
            System.err.println("=== ERRORE IN visualizzaOrdini ===");
            System.err.println("=== MESSAGGIO ERRORE: " + e.getMessage() + " ===");
            e.printStackTrace();
            throw e;
        }
    }
    
    private void aggiornaStatoOrdine(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        System.out.println("=== INIZIO aggiornaStatoOrdine ===");
        
        try {
            String idOrdineStr = request.getParameter("idOrdine");
            String nuovoStato = request.getParameter("stato");
            String motivazione = request.getParameter("motivazione");
            
            System.out.println("=== PARAMETRI: idOrdine=" + idOrdineStr + ", stato=" + nuovoStato + ", motivazione=" + motivazione + " ===");
            
            if(idOrdineStr != null && nuovoStato != null) {
                int idOrdine = Integer.parseInt(idOrdineStr);
                WebMarketDataLayer dl = (WebMarketDataLayer) request.getAttribute("datalayer");
                System.out.println("=== DATALAYER: " + (dl != null ? "OK" : "NULL") + " ===");
                
                if(motivazione != null && !motivazione.isEmpty()) {
                    System.out.println("=== UPDATE CON MOTIVAZIONE ===");
                    dl.getOrdineDAO().updateStatoOrdine(idOrdine, nuovoStato, motivazione);
                } else {
                    System.out.println("=== UPDATE SENZA MOTIVAZIONE ===");
                    dl.getOrdineDAO().updateStatoOrdine(idOrdine, nuovoStato);
                }
                
                System.out.println("=== UPDATE COMPLETATO - Redirect ===");
                response.sendRedirect("gestioneOrdini?success=Stato+aggiornato");
            } else {
                System.out.println("=== PARAMETRI MANCANTI - Redirect errore ===");
                response.sendRedirect("gestioneOrdini?error=Parametri+mancanti");
            }
        } catch (Exception e) {
            System.err.println("=== ERRORE IN aggiornaStatoOrdine ===");
            System.err.println("=== MESSAGGIO ERRORE: " + e.getMessage() + " ===");
            e.printStackTrace();
            response.sendRedirect("gestioneOrdini?error=Errore+aggiornamento");
        }
    }
}