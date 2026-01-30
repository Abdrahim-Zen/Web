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
            
            // Aggiungo info utente al datamodel
            String username = (String) session.getAttribute("username");
            String type = (String) session.getAttribute("userType");
            datamodel.put("userType", type);
            datamodel.put("username", username);
            
            // Recupero le categorie dal database
            WebMarketDataLayer dl = (WebMarketDataLayer) request.getAttribute("datalayer");
            if(dl != null) {
                
                 List<Categoria> categorie = dl.getCategoriaDAO().getAllCategorieWithSpecifiche();
                 datamodel.put("categorie", categorie);
                
                
            }
            
          
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
            

            int idCategoria = dl.getCategoriaDAO().insertCategoria(nomeCategoria.trim());
            
            
            if(specifiche != null) {
                for(String nomeSpecifica : specifiche) {
                    if(nomeSpecifica != null && !nomeSpecifica.trim().isEmpty()) {
                         dl.getSpecificaCategoriaDAO().insertSpecifica(idCategoria, nomeSpecifica.trim());
                    }
                }
            }
            
            
            response.sendRedirect("gestioneCategorie");
            
        } catch (Exception e) {
             response.sendRedirect("gestioneCategorie?error=1");
        }
    }
    
  private void action_modificaCategoria(HttpServletRequest request, HttpServletResponse response) 
        throws IOException, ServletException {
    
    try {
       
        
        WebMarketDataLayer dl = (WebMarketDataLayer) request.getAttribute("datalayer");
        if(dl == null) {
           
            response.sendRedirect("gestioneCategorie?error=Errore+database");
            return;
        }
        
        String idCategoriaStr = request.getParameter("idCategoria");
        String nomeCategoria = request.getParameter("nomeCategoria");
        String[] specifiche = request.getParameterValues("specifiche");
        
     
        
        if(idCategoriaStr == null || nomeCategoria == null || nomeCategoria.trim().isEmpty()) {
            
            response.sendRedirect("gestioneCategorie?error=2");
            return;
        }
        
        int idCategoria = Integer.parseInt(idCategoriaStr);
            
           
        
        
        dl.getCategoriaDAO().updateCategoria(idCategoria, nomeCategoria.trim());
        
        // Elimino le vecchie specifiche e inserisco le nuove
        if(specifiche != null) {
           
            dl.getSpecificaCategoriaDAO().deleteSpecificheByCategoria(idCategoria);
            
            for(String nomeSpecifica : specifiche) {
                if(nomeSpecifica != null && !nomeSpecifica.trim().isEmpty()) {
                    dl.getSpecificaCategoriaDAO().insertSpecifica(idCategoria, nomeSpecifica.trim());
                }
            }
           
        }
        
      
        response.sendRedirect("gestioneCategorie");
        
    } catch (NumberFormatException e) {
        
        response.sendRedirect("gestioneCategorie?error=2");
    } catch (Exception e) {
        
        response.sendRedirect("gestioneCategorie?error=2");
    }
}
    
    private void action_eliminaCategoria(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException {
        
        try {
            WebMarketDataLayer dl = (WebMarketDataLayer) request.getAttribute("datalayer");
            if(dl == null) {
                response.sendRedirect("gestioneCategorie?error=2");
                return;
            }
            
            String idCategoriaStr = request.getParameter("idCategoria");
            if(idCategoriaStr == null) {
                response.sendRedirect("gestioneCategorie?error=2");
                return;
            }
            
            int idCategoria = Integer.parseInt(idCategoriaStr);
             dl.getCategoriaDAO().deleteCategoria(idCategoria);
            
            response.sendRedirect("gestioneCategorie");
            
        } catch (NumberFormatException e) {
            response.sendRedirect("gestioneCategorie?error=2");
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
        String username = (String) session.getAttribute("username");
        String type = (String) session.getAttribute("userType");
        datamodel.put("userType", type);
        datamodel.put("username", username);
        
        String idCategoriaStr = request.getParameter("idCategoria");
      
        
        if(idCategoriaStr != null) {
            try {
                int idCategoria = Integer.parseInt(idCategoriaStr);
                WebMarketDataLayer dl = (WebMarketDataLayer) request.getAttribute("datalayer");
                
                if(dl != null) {
             
                    Categoria categoria = dl.getCategoriaDAO().getCategoriabyID(idCategoria);
                    if(categoria != null) {
             
                        
                 
                        List<SpecificaCategoria> specifiche = dl.getSpecificaCategoriaDAO().getListSpecificaCategoria(idCategoria);
                        categoria.setSpecifiche(specifiche);
                        
                        datamodel.put("categoriaModifica", categoria);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
  
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