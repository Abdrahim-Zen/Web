package it.univaq.webmarket.controller;

import it.univaq.webmarket.application.ApplicationBaseController;
import it.univaq.webmarket.data.dao.impl.WebMarketDataLayer;
import it.univaq.webmarket.data.model.Categoria;
import it.univaq.webmarket.data.model.SpecificaCategoria;
import it.univaq.webmarket.framework.security.SecurityHelpers;
import it.univaq.webmarket.framework.view.TemplateManagerException;
import it.univaq.webmarket.framework.view.TemplateResult;
import jakarta.servlet.ServletConfig;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author abrah
 */

public class GestioneCategorieController extends ApplicationBaseController {
    
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
    if(!"amministratore".equals(userType)) {
        response.sendRedirect("login?error=4");
        return;
    }
    
    String azione = request.getParameter("azione");
    
    
    if ("aggiungiCategoria".equals(azione)) {
        action_aggiungiCategoria(request, response);
    } else if ("modificaCategoria".equals(azione)) {
        action_modificaCategoria(request, response);
    } else if ("elimina".equals(azione)) {
        action_eliminaCategoria(request, response);
    } else if ("modifica".equals(azione)) {  
        action_preparaModifica(request, response);
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
            
            // Aggiungi info utente al datamodel
            String username = (String) session.getAttribute("username");
            String type = (String) session.getAttribute("userType");
            datamodel.put("userType", type);
            datamodel.put("username", username);
            
            // Recupera le categorie dal database
            WebMarketDataLayer dl = (WebMarketDataLayer) request.getAttribute("datalayer");
            if(dl != null) {
                // Assumendo che esista un metodo per ottenere tutte le categorie con le loro specifiche
                // Sostituisci con i tuoi metodi DAO reali
                 List<Categoria> categorie = dl.getCategoriaDAO().getAllCategorieWithSpecifiche();
                 datamodel.put("categorie", categorie);
                
                
            }
            
            // Gestione messaggi di successo/errore
            String success = request.getParameter("success");
            String error = request.getParameter("error");
            if(success != null) {
                datamodel.put("success", success);
            }
            if(error != null) {
                datamodel.put("error", error);
            }
            
            result.activate("gestioneCategorie.ftl.html", datamodel, request, response);
            
        } catch (Exception e) {
            throw new ServletException("Errore nel caricamento delle categorie", e);
        }
    }
    
    private void action_aggiungiCategoria(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException {
        
        try {
            WebMarketDataLayer dl = (WebMarketDataLayer) request.getAttribute("datalayer");
            if(dl == null) {
                response.sendRedirect("gestioneCategorie?error=Errore+database");
                return;
            }
            
            String nomeCategoria = request.getParameter("nomeCategoria");
            String[] specifiche = request.getParameterValues("specifiche");
            
            if(nomeCategoria == null || nomeCategoria.trim().isEmpty()) {
                response.sendRedirect("gestioneCategorie?error=Nome+categoria+obbligatorio");
                return;
            }
            
            // Inserisci la categoria nel database
            // Assumendo che esista un metodo per inserire una categoria
            int idCategoria = dl.getCategoriaDAO().insertCategoria(nomeCategoria.trim());
            
            
            if(specifiche != null) {
                for(String nomeSpecifica : specifiche) {
                    if(nomeSpecifica != null && !nomeSpecifica.trim().isEmpty()) {
                         dl.getSpecificaCategoriaDAO().insertSpecifica(idCategoria, nomeSpecifica.trim());
                    }
                }
            }
            
            
            response.sendRedirect("gestioneCategorie?success=Categoria+aggiunta+con+successo");
            
        } catch (Exception e) {
            throw new ServletException("Errore nell'aggiunta della categoria", e);
        }
    }
    
  private void action_modificaCategoria(HttpServletRequest request, HttpServletResponse response) 
        throws IOException, ServletException {
    
    try {
        System.out.println("=== INIZIO action_modificaCategoria ===");
        
        WebMarketDataLayer dl = (WebMarketDataLayer) request.getAttribute("datalayer");
        if(dl == null) {
            System.err.println("=== DATALAYER NULL ===");
            response.sendRedirect("gestioneCategorie?error=Errore+database");
            return;
        }
        
        String idCategoriaStr = request.getParameter("idCategoria");
        String nomeCategoria = request.getParameter("nomeCategoria");
        String[] specifiche = request.getParameterValues("specifiche");
        
        System.out.println("=== idCategoria: " + idCategoriaStr + " ===");
        System.out.println("=== nomeCategoria: " + nomeCategoria + " ===");
        System.out.println("=== specifiche: " + (specifiche != null ? java.util.Arrays.toString(specifiche) : "null") + " ===");
        
        if(idCategoriaStr == null || nomeCategoria == null || nomeCategoria.trim().isEmpty()) {
            System.err.println("=== DATI OBBLIGATORI MANCANTI ===");
            response.sendRedirect("gestioneCategorie?error=Dati+obbligatori+mancanti");
            return;
        }
        
        int idCategoria = Integer.parseInt(idCategoriaStr);
        
        // DEBUG: Verifica che la categoria esista prima di modificarla
        try {
            Categoria categoriaEsistente = dl.getCategoriaDAO().getCategoriabyID(idCategoria);
            System.out.println("=== CATEGORIA ESISTENTE: " + (categoriaEsistente != null ? categoriaEsistente.getNomeCategoria() : "NULL") + " ===");
        } catch (Exception e) {
            System.err.println("=== ERRORE RECUPERO CATEGORIA ESISTENTE: " + e.getMessage() + " ===");
        }
        
        // Modifica la categoria nel database
        System.out.println("=== PRIMA DI updateCategoria ===");
        dl.getCategoriaDAO().updateCategoria(idCategoria, nomeCategoria.trim());
        System.out.println("=== DOPO updateCategoria ===");
        
        // Elimina le vecchie specifiche e inserisci le nuove
        if(specifiche != null) {
            System.out.println("=== PRIMA DI deleteSpecificheByCategoria ===");
            dl.getSpecificaCategoriaDAO().deleteSpecificheByCategoria(idCategoria);
            System.out.println("=== DOPO deleteSpecificheByCategoria ===");
            
            for(String nomeSpecifica : specifiche) {
                if(nomeSpecifica != null && !nomeSpecifica.trim().isEmpty()) {
                    System.out.println("=== INSERIMENTO SPECIFICA: " + nomeSpecifica.trim() + " ===");
                    dl.getSpecificaCategoriaDAO().insertSpecifica(idCategoria, nomeSpecifica.trim());
                }
            }
            System.out.println("=== FINITO INSERIMENTO SPECIFICHE ===");
        }
        
        System.out.println("=== REINDIRIZZAMENTO A SUCCESS ===");
        response.sendRedirect("gestioneCategorie?success=Categoria+modificata+con+successo");
        
    } catch (NumberFormatException e) {
        System.err.println("=== ERRORE NUMBER FORMAT: " + e.getMessage() + " ===");
        e.printStackTrace();
        response.sendRedirect("gestioneCategorie?error=ID+categoria+non+valido");
    } catch (Exception e) {
        System.err.println("=== ERRORE GENERICO: " + e.getMessage() + " ===");
        e.printStackTrace();
        response.sendRedirect("gestioneCategorie?error=Errore+durante+la+modifica");
    }
}
    
    private void action_eliminaCategoria(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException {
        
        try {
            WebMarketDataLayer dl = (WebMarketDataLayer) request.getAttribute("datalayer");
            if(dl == null) {
                response.sendRedirect("gestioneCategorie?error=Errore+database");
                return;
            }
            
            String idCategoriaStr = request.getParameter("idCategoria");
            if(idCategoriaStr == null) {
                response.sendRedirect("gestioneCategorie?error=ID+categoria+obbligatorio");
                return;
            }
            
            int idCategoria = Integer.parseInt(idCategoriaStr);
            
            // Elimina la categoria dal database (le specifiche verranno eliminate in cascade)
            // Assumendo che esista un metodo per eliminare una categoria
             dl.getCategoriaDAO().deleteCategoria(idCategoria);
            
            response.sendRedirect("gestioneCategorie?success=Categoria+eliminata+con+successo");
            
        } catch (NumberFormatException e) {
            response.sendRedirect("gestioneCategorie?error=ID+categoria+non+valido");
        } catch (Exception e) {
            throw new ServletException("Errore nell'eliminazione della categoria", e);
        }
    }
    
   private void action_preparaModifica(HttpServletRequest request, HttpServletResponse response) 
        throws TemplateManagerException, IOException, ServletException {
    
    try {
        TemplateResult result = new TemplateResult(getServletContext());
        Map<String, Object> datamodel = new HashMap<>();
        HttpSession session = SecurityHelpers.checkSession(request);
        
        // Aggiungi info utente al datamodel
        String username = (String) session.getAttribute("username");
        String type = (String) session.getAttribute("userType");
        datamodel.put("userType", type);
        datamodel.put("username", username);
        
        String idCategoriaStr = request.getParameter("idCategoria");
        System.out.println("=== ID CATEGORIA PER MODIFICA: " + idCategoriaStr + " ===");
        
        if(idCategoriaStr != null) {
            try {
                int idCategoria = Integer.parseInt(idCategoriaStr);
                WebMarketDataLayer dl = (WebMarketDataLayer) request.getAttribute("datalayer");
                
                if(dl != null) {
                    // Recupera la categoria da modificare
                    Categoria categoria = dl.getCategoriaDAO().getCategoriabyID(idCategoria);
                    if(categoria != null) {
                        System.out.println("=== CATEGORIA TROVATA: " + categoria.getNomeCategoria() + " ===");
                        
                        // Recupera le specifiche della categoria
                        List<SpecificaCategoria> specifiche = dl.getSpecificaCategoriaDAO().getListSpecificaCategoria(idCategoria);
                        categoria.setSpecifiche(specifiche);
                        
                        datamodel.put("categoriaModifica", categoria);
                    }
                }
            } catch (Exception e) {
                System.err.println("=== ERRORE RECUPERO CATEGORIA: " + e.getMessage() + " ===");
                e.printStackTrace();
            }
        }
        
        // Ricarica la lista delle categorie
        WebMarketDataLayer dl = (WebMarketDataLayer) request.getAttribute("datalayer");
        if(dl != null) {
            try {
                List<Categoria> categorie = dl.getCategoriaDAO().getAllCategorieWithSpecifiche();
                datamodel.put("categorie", categorie);
            } catch (Exception e) {
                datamodel.put("categorie", new ArrayList<>());
            }
        } else {
            datamodel.put("categorie", new ArrayList<>());
        }
        
        result.activate("gestioneCategorie.ftl.html", datamodel, request, response);
        
    } catch (Exception e) {
        throw new ServletException("Errore nella preparazione della modifica", e);
    }
}
}