/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.webmarket.controller;

import it.univaq.webmarket.application.ApplicationBaseController;
import it.univaq.webmarket.data.dao.impl.WebMarketDataLayer;
import it.univaq.webmarket.data.model.PresaInCarico;
import it.univaq.webmarket.data.model.ProdottoCandidato;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author abrah
 */

public class GestioneProdottiController extends ApplicationBaseController {
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, TemplateManagerException {
        
        HttpSession session = SecurityHelpers.checkSession(request);
        if(session == null){
            response.sendRedirect("login?error=3");
            return;
        }
        
        String userType = (String) session.getAttribute("userType");
        if(!"tecnico".equals(userType)) {
            response.sendRedirect("login?error=4");
            return;
        }
        
        String azione = request.getParameter("action");
        
        if ("proponiProdotto".equals(azione)) {
            action_proponiProdotto(request, response);
        } else {
            action_default(request, response);
        }
    }
    
private void action_default(HttpServletRequest request, HttpServletResponse response) 
        throws TemplateManagerException, IOException, ServletException {

    try {
        TemplateResult result = new TemplateResult(getServletContext());
        Map<String, Object> datamodel = new HashMap<>();
        HttpSession session = SecurityHelpers.checkSession(request);
        
        
        String username = (String) session.getAttribute("username");
        Integer tecnicoId = (Integer) session.getAttribute("userid");
        if (tecnicoId == null) {
            tecnicoId = (Integer) session.getAttribute("userId");
        }
        if (tecnicoId == null) {
            Object idObj = session.getAttribute("id");
            if (idObj != null) {
                try {
                    tecnicoId = Integer.parseInt(idObj.toString());
                } catch (NumberFormatException e) {
                    System.err.println("Cannot parse id: " + idObj);
                }
            }
        }
        
        datamodel.put("userType", "tecnico");
        datamodel.put("username", username);
        
        
        List<PresaInCarico> richiesteInCarico = new ArrayList<>();
        
        if (tecnicoId != null) {
            WebMarketDataLayer dl = (WebMarketDataLayer) request.getAttribute("datalayer");
            if(dl != null) {
                
                try {
                    richiesteInCarico = dl.getPresaInCaricoDAO().getRichiesteInCaricoByTecnico(tecnicoId);
                    
                    
                    // PER OGNI RICHIESTA, RECUPERA LE SPECIFICHE
                    for (PresaInCarico richiesta : richiesteInCarico) {
                        try {
                            // Recupera le specifiche della richiesta usando il nuovo metodo
                            List<Map<String, String>> specifiche = 
                                dl.getRichiestaAcquistoDAO().getSpecificheByRichiestaId(richiesta.getRichiestaKey());
                      
                            datamodel.put("specificheRichiesta_" + richiesta.getKey(), specifiche);
                            
                        } catch (Exception e) {
                            System.err.println("Errore nel recupero specifiche: " + e.getMessage());
                            datamodel.put("specificheRichiesta_" + richiesta.getKey(), new ArrayList<Map<String, String>>());
                        }
                        
                        // Codice esistente per i prodotti
                        try {
                            List<ProdottoCandidato> prodottiProposti = 
                                dl.getProdottoCandidatoDAO().getProdottiCandidatiByRichiestaID(richiesta.getKey());
                            
                            datamodel.put("prodottiRichiesta_" + richiesta.getKey(), prodottiProposti);
                        } catch (Exception e) {
                            System.err.println("Errore nel recupero prodotti: " + e.getMessage());
                            datamodel.put("prodottiRichiesta_" + richiesta.getKey(), new ArrayList<ProdottoCandidato>());
                        }
                    }
                    
                } catch (Exception e) {
                    System.err.println("ERRORE in getRichiesteInCaricoByTecnico: " + e.getMessage());
                    e.printStackTrace();
                    datamodel.put("error", "Errore nel caricamento delle richieste: " + e.getMessage());
                }
            } else {
                System.err.println("=== DEBUG: DataLayer è null! ===");
                datamodel.put("error", "Errore di connessione al database");
            }
        } else {
            System.err.println("=== DEBUG: tecnicoId è null ===");
            datamodel.put("error", "Utente non autenticato - ID tecnico non trovato nella sessione");
        }
        
        datamodel.put("richiesteInCarico", richiesteInCarico);
        
        result.activate("gestioneProdotti.ftl.html", datamodel, request, response);
        
    } catch (Exception e) {
        System.err.println("=== ERRORE GRAVE nel controller: " + e.getMessage() + " ===");
        e.printStackTrace();
        throw new ServletException("Errore nel caricamento delle richieste in carico", e);
    }
}
    
private void action_proponiProdotto(HttpServletRequest request, HttpServletResponse response) 
        throws IOException, ServletException {
    
    try {
        System.out.println("=== INIZIO action_proponiProdotto ===");
        
        HttpSession session = SecurityHelpers.checkSession(request);
        Integer tecnicoId = (Integer) session.getAttribute("userid");
        if (tecnicoId == null) {
            tecnicoId = (Integer) session.getAttribute("userId");
        }
        
        if (tecnicoId == null) {
            response.sendRedirect("login?error=3");
            return;
        }
        
        WebMarketDataLayer dl = (WebMarketDataLayer) request.getAttribute("datalayer");
        if(dl == null) {
            System.err.println("=== DATALAYER NULL ===");
            response.sendRedirect("gestioneProdotti?error=2");
            return;
        }
        
        // Recupera i parametri dal form
        String nomeProdotto = request.getParameter("nomeProdotto");
        String descrizioneProdotto = request.getParameter("descrizioneProdotto");
        String prezzoProdottoStr = request.getParameter("prezzoProdotto");
        String idRichiestaInCaricoStr = request.getParameter("idRichiestaInCarico");
        
        
        // Validazione dei campi
        if (nomeProdotto == null || nomeProdotto.trim().isEmpty() ||
            descrizioneProdotto == null || descrizioneProdotto.trim().isEmpty() ||
            prezzoProdottoStr == null || prezzoProdottoStr.trim().isEmpty() ||
            idRichiestaInCaricoStr == null) {
            
            System.err.println("=== DATI OBBLIGATORI MANCANTI ===");
            response.sendRedirect("gestioneProdotti?error=1");
            return;
        }
        
        double prezzoProdotto;
        int idRichiestaInCarico;
        
        try {
            prezzoProdotto = Double.parseDouble(prezzoProdottoStr);
            idRichiestaInCarico = Integer.parseInt(idRichiestaInCaricoStr);
        } catch (NumberFormatException e) {
            System.err.println("=== ERRORE NUMBER FORMAT: " + e.getMessage() + " ===");
            response.sendRedirect("gestioneProdotti?error=1");
            return;
        }
        
        // Verifica che il prezzo sia positivo
        if (prezzoProdotto <= 0) {
            System.err.println("=== PREZZO NON VALIDO ===");
            response.sendRedirect("gestioneProdotti?error=1");
            return;
        }
        
        // Verifica che la richiesta sia ancora in carico al tecnico
        PresaInCarico presaInCarico = dl.getPresaInCaricoDAO().getPresaInCaricoByID(idRichiestaInCarico);
        if (presaInCarico == null || presaInCarico.getTecnicoKey() != tecnicoId) {
            System.err.println("=== PRESA IN CARICO NON TROVATA O NON AUTORIZZATA ===");
            response.sendRedirect("gestioneProdotti?error=2");
            return;
        }
        
        // Crea e salva il prodotto candidato
        ProdottoCandidato prodotto = dl.getProdottoCandidatoDAO().createProdottoCandidato();
        prodotto.setNome(nomeProdotto.trim());
        prodotto.setDescrizione(descrizioneProdotto.trim());
        prodotto.setPrezzo(prezzoProdotto);
        prodotto.setRichiestaKey(idRichiestaInCarico);
        prodotto.setDataProposta(new Timestamp(System.currentTimeMillis()));
        
        
        dl.getProdottoCandidatoDAO().insertProdottoCandidato(prodotto);
       
        
        
        dl.getPresaInCaricoDAO().segnaComeCompletato(idRichiestaInCarico);
        
        response.sendRedirect("gestioneProdotti?success=1");
        
    } catch (NumberFormatException e) {
        System.err.println("=== ERRORE NUMBER FORMAT: " + e.getMessage() + " ===");
        e.printStackTrace();
        response.sendRedirect("gestioneProdotti?error=1");
    } catch (Exception e) {
        System.err.println("=== ERRORE GENERICO: " + e.getMessage() + " ===");
        e.printStackTrace();
        response.sendRedirect("gestioneProdotti?error=2");
    }
}
}