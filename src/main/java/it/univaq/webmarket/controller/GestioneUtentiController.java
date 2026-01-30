package it.univaq.webmarket.controller;

import it.univaq.webmarket.application.ApplicationBaseController;
import it.univaq.webmarket.data.dao.impl.WebMarketDataLayer;
import it.univaq.webmarket.data.model.Tecnico;
import it.univaq.webmarket.data.model.Utente;
import it.univaq.webmarket.data.model.UtenteRegistrato;
import it.univaq.webmarket.framework.data.DataException;
import it.univaq.webmarket.framework.security.SecurityHelpers;
import it.univaq.webmarket.framework.view.TemplateManagerException;
import it.univaq.webmarket.framework.view.TemplateResult;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author abrah
 */
public class GestioneUtentiController extends ApplicationBaseController {

    @Override
    public void init(ServletConfig config) throws jakarta.servlet.ServletException {
        super.init(config);
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DataException, TemplateManagerException, SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
        HttpSession session = SecurityHelpers.checkSession(request);

        if (session == null) {
            response.sendRedirect("login?error=3");
            return;
        }

        String userType = (String) session.getAttribute("userType");
        if (!"amministratore".equals(userType)) {
            response.sendRedirect("login?error=4");
            return;
        }

        String tipoUtente = request.getParameter("tipoUtente");
        if ("tecnico".equals(tipoUtente)) {
            aggiungiTecnico(request, response);
        } else if ("utenteRegistrato".equals(tipoUtente)) {
            aggiungiUtente(request, response);
        }
        String azione = request.getParameter("azione");
        if ("elimina".equals(azione)) {
            eliminaUtente(request, response);
        } else {
            action_default(request, response);
        }

    }

    private void eliminaUtente(HttpServletRequest request, HttpServletResponse response) throws DataException, SQLException, IOException {
        String email = request.getParameter("emailUtente");
        WebMarketDataLayer dl = (WebMarketDataLayer) request.getAttribute("datalayer");
        dl.getUtenteRegistratoDAO().deliteUtenteByEmail(email);
        response.sendRedirect("gestioneUtenti");

    }

    private void aggiungiUtente(HttpServletRequest request, HttpServletResponse response) throws NoSuchAlgorithmException, InvalidKeySpecException, DataException, SQLException, IOException {
        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String cognome = request.getParameter("cognome");
        String password = SecurityHelpers.getPasswordHashPBKDF2(request.getParameter("password"));
        HttpSession session = SecurityHelpers.checkSession(request);
        int idAdmin = (int) session.getAttribute("userid");
        WebMarketDataLayer dl = (WebMarketDataLayer) request.getAttribute("datalayer");
        try {
            dl.getUtenteDAO().addUtente(email, nome, cognome, password, idAdmin);

            Utente u = dl.getUtenteDAO().getUtentebyEmail(email);
            dl.getUtenteRegistratoDAO().addUtentebyAdmin(u.getKey());
        } catch (SQLException e) {
            response.sendRedirect("amministratore?error=1");
        }

    }

    private void aggiungiTecnico(HttpServletRequest request, HttpServletResponse response) throws NoSuchAlgorithmException, DataException, InvalidKeySpecException, ServletException, SQLException, IOException {
        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String cognome = request.getParameter("cognome");
        String data = request.getParameter("dataAssunzione");
        java.sql.Date dataAssunzione;
        try {
            dataAssunzione = java.sql.Date.valueOf(data);
        } catch (IllegalArgumentException e) {
            // Gestisci l'errore se il formato non è valido
            throw new ServletException("Formato data non valido." + data, e);
        }
        String password = SecurityHelpers.getPasswordHashPBKDF2(request.getParameter("password"));
        HttpSession session = SecurityHelpers.checkSession(request);
        int idAdmin = (int) session.getAttribute("userid");
        WebMarketDataLayer dl = (WebMarketDataLayer) request.getAttribute("datalayer");
        try {
            dl.getUtenteDAO().addUtente(email, nome, cognome, password, idAdmin);

            Utente u = dl.getUtenteDAO().getUtentebyEmail(email);
            dl.getTecnicoDAO().addTecnicobyAdmin(u.getKey(), dataAssunzione);
        } catch (SQLException e) {
            response.sendRedirect("amministratore?error=1");
        }

    }

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws DataException, TemplateManagerException {
        WebMarketDataLayer dl = (WebMarketDataLayer) request.getAttribute("datalayer");
        HttpSession session = SecurityHelpers.checkSession(request);
        String type = (String) session.getAttribute("userType");
        String username = (String) session.getAttribute("username");
        Integer idAmministratore = (Integer) session.getAttribute("userid");
        List<UtenteRegistrato> utenti = dl.getUtenteRegistratoDAO().getUtentiRegistratiCreatiDa(idAmministratore);
        List<Tecnico> tecnici = dl.getTecnicoDAO().getTecnicoCreatiDa(idAmministratore);
        Map<String, Object> datamodel = new HashMap<>();

        datamodel.put("utenti", utenti);
        datamodel.put("tecnici", tecnici);
        datamodel.put("userType", type);
        datamodel.put("username", username);

        TemplateResult result = new TemplateResult(getServletContext());

        result.activate("gestioneUtenti.ftl.html", datamodel, request, response);
    }

}
