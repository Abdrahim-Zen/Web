/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.webmarket.data.dao.impl;

import it.univaq.webmarket.data.dao.ProdottoCandidatoDAO;
import it.univaq.webmarket.data.model.ProdottoCandidato;
import it.univaq.webmarket.data.model.impl.proxy.ProdottoCandidatoProxy;
import it.univaq.webmarket.framework.data.DAO;
import it.univaq.webmarket.framework.data.DataException;
import it.univaq.webmarket.framework.data.DataLayer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author abdrahimzeno
 */
public class ProdottoCandidatoDAO_MySQL extends DAO implements ProdottoCandidatoDAO {

    PreparedStatement sProdottoByUtente;
    PreparedStatement sProdottoByID;
    PreparedStatement sProdottoByRichiestaInCarico;
    PreparedStatement iProdottoCandidato;
  
    PreparedStatement uProdottoCandidatoAcc;
    PreparedStatement uProdottoCandidatoRif;

    public ProdottoCandidatoDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        super.init();
        try {
            
            sProdottoByUtente = connection.prepareStatement("SELECT p.ID, p.nome, p.descrizione, p.prezzo, "
                    + "p.ID_richiestaInCarico, p.data_proposta "
                    + "FROM prodottoCandidato p " + "JOIN richiesteInCarico rc ON p.ID_richiestaInCarico = rc.ID "
                    + "JOIN richiestaAcquisto r ON rc.ID_richiesta = r.ID "
                    + "WHERE r.ID_utente = ? AND p.stato = 'in_attesa' ");

            sProdottoByID = connection.prepareStatement("SELECT * FROM prodottoCandidato WHERE ID=? ");

           
            sProdottoByRichiestaInCarico = connection.prepareStatement("SELECT * FROM prodottoCandidato WHERE ID_richiestaInCarico = ? ORDER BY data_proposta DESC");

           
            iProdottoCandidato = connection.prepareStatement(
                    "INSERT INTO prodottoCandidato (nome, descrizione, prezzo, ID_richiestaInCarico, data_proposta) "
                    + "VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            
           
            uProdottoCandidatoAcc = connection.prepareStatement("UPDATE prodottoCandidato SET stato = 'accettato' WHERE ID = ?");
            uProdottoCandidatoRif = connection.prepareStatement("UPDATE prodottoCandidato SET stato = 'rifiutato' WHERE ID = ?");

        } catch (SQLException ex) {
            Logger.getLogger(ProdottoCandidatoDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<ProdottoCandidato> getProdottiCandidatiByUserID(int x) {
        List<ProdottoCandidato> prodottiCandidati = new ArrayList<>();
        try {
            sProdottoByUtente.setInt(1, x);
            ResultSet rs = sProdottoByUtente.executeQuery();
            while (rs.next()) {
                prodottiCandidati.add(createProdottoCandidato(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProdottoCandidatoDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return prodottiCandidati;
    }

    @Override
    public List<ProdottoCandidato> getProdottiCandidatiByRichiestaID(int richiestaInCaricoId) { // CORRETTO: ora è richiestaInCaricoId
        List<ProdottoCandidato> prodottiCandidati = new ArrayList<>();
        try {
            sProdottoByRichiestaInCarico.setInt(1, richiestaInCaricoId);
            ResultSet rs = sProdottoByRichiestaInCarico.executeQuery();
            while (rs.next()) {
                prodottiCandidati.add(createProdottoCandidato(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProdottoCandidatoDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return prodottiCandidati;
    }

    @Override
    public ProdottoCandidato getProdottoCandidatoByID(int x) throws DataException {
        ProdottoCandidato prodotto = null;
        try {
            if (dataLayer.getCache().has(ProdottoCandidato.class, x)) {
                prodotto = dataLayer.getCache().get(ProdottoCandidato.class, x);
            } else {
                sProdottoByID.setInt(1, x);
                ResultSet rs = sProdottoByID.executeQuery();
                if (rs.next()) {
                    prodotto = createProdottoCandidato(rs);
                    dataLayer.getCache().add(ProdottoCandidato.class, prodotto);
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Errore nel recupero del prodotto candidato con ID: " + x, ex);
        }
        return prodotto;
    }

    @Override
    public int insertProdottoCandidato(ProdottoCandidato prodotto) throws DataException {
        try {
            iProdottoCandidato.setString(1, prodotto.getNome());
            iProdottoCandidato.setString(2, prodotto.getDescrizone()); // CORREZIONE: getDescrizione, non getDescrizone
            iProdottoCandidato.setDouble(3, prodotto.getPrezzo());
            iProdottoCandidato.setInt(4, prodotto.getRichiestaKey()); // CORREZIONE: ora è ID_richiestaInCarico
            iProdottoCandidato.setTimestamp(5, prodotto.getDataProposta());
           
            int affectedRows = iProdottoCandidato.executeUpdate();
            
            if (affectedRows == 0) {
                throw new DataException("Inserimento fallito, nessuna riga interessata");
            }

            try ( ResultSet generatedKeys = iProdottoCandidato.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    prodotto.setKey(generatedId);
                    dataLayer.getCache().add(ProdottoCandidato.class, prodotto);
                    return generatedId;
                } else {
                    throw new DataException("Inserimento fallito, nessun ID ottenuto");
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Errore nell'inserimento del prodotto candidato", ex);
        }
    }

    private ProdottoCandidato createProdottoCandidato(ResultSet rs) throws SQLException {
        ProdottoCandidatoProxy prodotto = (ProdottoCandidatoProxy) createProdottoCandidato();
        prodotto.setKey(rs.getInt("ID"));
        prodotto.setDataProposta(rs.getTimestamp("data_proposta"));
        prodotto.setDescrizione(rs.getString("descrizione"));
        prodotto.setNome(rs.getString("nome"));
        prodotto.setPrezzo(rs.getDouble("prezzo"));
        prodotto.setRichiestaKey(rs.getInt("ID_richiestaInCarico")); // CORREZIONE: ora è ID_richiestaInCarico
        // RIMOSSO: prodotto.setTecnicoKey(rs.getInt("ID_tecnico")); - non esiste più
        return prodotto;
    }

    @Override
    public ProdottoCandidato createProdottoCandidato() {
        return new ProdottoCandidatoProxy(dataLayer);
    }

    public void sceltaProdottoCandidato(int id, String scelta) throws DataException {
        try {
            if (scelta.equals("accetta")) {
                uProdottoCandidatoAcc.setInt(1, id);
                uProdottoCandidatoAcc.executeUpdate();
            } else {
                uProdottoCandidatoRif.setInt(1, id);
                uProdottoCandidatoRif.executeUpdate();
            }

        } catch (SQLException ex) {
            throw new DataException("Errore durante l'eliminazione dell'ordine", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        try {
            if (sProdottoByUtente != null) {
                sProdottoByUtente.close();
            }
            if (sProdottoByID != null) {
                sProdottoByID.close();
            }
            if (sProdottoByRichiestaInCarico != null) {
                sProdottoByRichiestaInCarico.close();
            }
            if (iProdottoCandidato != null) {
                iProdottoCandidato.close();
            }
        } catch (SQLException ex) {
            throw new DataException("Errore nella chiusura delle prepared statements", ex);
        }
        super.destroy();
    }
}
