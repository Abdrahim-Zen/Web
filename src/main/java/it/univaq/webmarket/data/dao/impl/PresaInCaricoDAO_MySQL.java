package it.univaq.webmarket.data.dao.impl;

import it.univaq.webmarket.data.dao.PresaInCaricoDAO;
import it.univaq.webmarket.data.model.PresaInCarico;
import it.univaq.webmarket.data.model.impl.proxy.PresaInCaricoProxy;
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

public class PresaInCaricoDAO_MySQL extends DAO implements PresaInCaricoDAO {

    private PreparedStatement sRichiesteInCaricoByTecnico;
    private PreparedStatement sRichiesteInCaricoByTecnicoRifiutati;
    private PreparedStatement sPresaInCaricoByID;
    private PreparedStatement sPresaInCaricoByRichiestaID;
    private PreparedStatement iPresaInCarico;
    private PreparedStatement uPresaInCarico;
    private PreparedStatement dPresaInCarico;
    private PreparedStatement uCompletato;

    public PresaInCaricoDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        super.init();
        try {

            sRichiesteInCaricoByTecnico = connection.prepareStatement(
                    "SELECT rc.ID, rc.data_incarico, rc.ID_tecnico, rc.ID_richiesta, "
                    + "r.note, r.importo_totale, rc.completato, "
                    + "u.nome as utenteNome, u.cognome as utenteCognome, "
                    + "c.nome as categoriaNome "
                    + "FROM richiesteInCarico rc "
                    + "JOIN richiestaAcquisto r ON rc.ID_richiesta = r.ID "
                    + "JOIN utente u ON r.ID_utente = u.ID "
                    + "JOIN categoria c ON r.ID_categoria = c.ID "
                    + "WHERE rc.ID_tecnico = ? AND rc.completato = FALSE "
                    + "ORDER BY rc.data_incarico DESC"
            );

            sRichiesteInCaricoByTecnicoRifiutati = connection.prepareStatement("SELECT rc.ID, rc.data_incarico, rc.ID_tecnico, rc.ID_richiesta, p.stato, p.motivazione, p.ID as prodotto,p.nome as prodottoNome, "
                    + "r.note, r.importo_totale, rc.completato, "
                    + "u.nome as utenteNome, u.cognome as utenteCognome, "
                    + "c.nome as categoriaNome "
                    + "FROM richiesteInCarico rc "
                    + "JOIN richiestaAcquisto r ON rc.ID_richiesta = r.ID "
                    + "JOIN prodottoCandidato P ON rc.ID = p.ID_richiestaInCarico "
                    + "JOIN utente u ON r.ID_utente = u.ID "
                    + "JOIN categoria c ON r.ID_categoria = c.ID "
                    + "WHERE rc.ID_tecnico = ? AND p.stato='rifiutato' "
                    + "ORDER BY rc.data_incarico DESC");

            sPresaInCaricoByID = connection.prepareStatement(
                    "SELECT * FROM richiesteInCarico WHERE ID = ?"
            );

            uCompletato = connection.prepareStatement(
                    "UPDATE richiesteInCarico SET completato = TRUE WHERE ID = ?"
            );

            sPresaInCaricoByRichiestaID = connection.prepareStatement(
                    "SELECT * FROM richiesteInCarico WHERE ID_richiesta = ?"
            );

            iPresaInCarico = connection.prepareStatement(
                    "INSERT INTO richiesteInCarico (data_incarico, ID_tecnico, ID_richiesta) "
                    + "VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            uPresaInCarico = connection.prepareStatement(
                    "UPDATE richiesteInCarico SET data_incarico = ?, "
                    + "ID_tecnico = ?, ID_richiesta = ? WHERE ID = ?"
            );

            dPresaInCarico = connection.prepareStatement(
                    "DELETE FROM richiesteInCarico WHERE ID = ?"
            );

        } catch (SQLException ex) {
            Logger.getLogger(PresaInCaricoDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataException("Errore nell'inizializzazione del DAO PresaInCarico", ex);
        }
    }

    @Override
    public List<PresaInCarico> getRichiesteInCaricoByTecnico(int idTecnico) throws DataException {
        List<PresaInCarico> richieste = new ArrayList<>();
        try {
            sRichiesteInCaricoByTecnico.setInt(1, idTecnico);
            ResultSet rs = sRichiesteInCaricoByTecnico.executeQuery();

            while (rs.next()) {
                PresaInCarico presa = createPresaInCarico(rs);
                richieste.add(presa);
            }

        } catch (SQLException ex) {
            throw new DataException("Errore nel recupero delle richieste in carico", ex);
        }
        return richieste;
    }

    @Override
    public List<PresaInCarico> getRichiesteRifiutateByTecnico(int idTecnico) throws DataException {
        List<PresaInCarico> richieste = new ArrayList<>();
        try {
            sRichiesteInCaricoByTecnicoRifiutati.setInt(1, idTecnico);
            ResultSet rs = sRichiesteInCaricoByTecnicoRifiutati.executeQuery();
            while (rs.next()) {
                PresaInCarico presa = createPresaInCarico(rs);
                richieste.add(presa);
            }
        } catch (SQLException ex) {
            throw new DataException("Errore nel recupero delle richieste in carico", ex);
        }
        return richieste;
    }

    @Override
    public PresaInCarico getPresaInCaricoByID(int id) throws DataException {
        PresaInCarico presa = null;
        try {
            if (dataLayer.getCache().has(PresaInCarico.class, id)) {
                presa = dataLayer.getCache().get(PresaInCarico.class, id);
            } else {
                sPresaInCaricoByID.setInt(1, id);
                ResultSet rs = sPresaInCaricoByID.executeQuery();
                if (rs.next()) {
                    presa = createPresaInCarico(rs);
                    dataLayer.getCache().add(PresaInCarico.class, presa);
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Errore nel recupero della presa in carico con ID: " + id, ex);
        }
        return presa;
    }

    @Override
    public void segnaComeCompletato(int idRichiestaInCarico) throws DataException {
        try {
            uCompletato.setInt(1, idRichiestaInCarico);
            uCompletato.executeUpdate();
        } catch (SQLException ex) {
            throw new DataException("Errore nell'aggiornamento dello stato della richiesta", ex);
        }
    }

    @Override
    public PresaInCarico getPresaInCaricoByRichiestaID(int idRichiesta) throws DataException {
        PresaInCarico presa = null;
        try {
            sPresaInCaricoByRichiestaID.setInt(1, idRichiesta);
            ResultSet rs = sPresaInCaricoByRichiestaID.executeQuery();
            if (rs.next()) {
                presa = createPresaInCarico(rs);
                dataLayer.getCache().add(PresaInCarico.class, presa);
            }
        } catch (SQLException ex) {
            throw new DataException("Errore nel recupero della presa in carico per richiesta ID: " + idRichiesta, ex);
        }
        return presa;
    }

    @Override
    public int insertPresaInCarico(PresaInCarico presa) throws DataException {
        try {

            iPresaInCarico.setTimestamp(1, presa.getDataIncarico());
            iPresaInCarico.setInt(2, presa.getTecnicoKey());
            iPresaInCarico.setInt(3, presa.getRichiestaKey());

            int affectedRows = iPresaInCarico.executeUpdate();
            if (affectedRows == 0) {
                throw new DataException("Inserimento fallito, nessuna riga interessata");
            }

            try ( ResultSet generatedKeys = iPresaInCarico.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    presa.setKey(generatedId);
                    dataLayer.getCache().add(PresaInCarico.class, presa);
                    return generatedId;
                } else {
                    throw new DataException("Inserimento fallito, nessun ID ottenuto");
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Errore nell'inserimento della presa in carico", ex);
        }
    }

    @Override
    public void updatePresaInCarico(PresaInCarico presa) throws DataException {
        try {

            uPresaInCarico.setTimestamp(1, presa.getDataIncarico());
            uPresaInCarico.setInt(2, presa.getTecnicoKey());
            uPresaInCarico.setInt(3, presa.getRichiestaKey());
            uPresaInCarico.setInt(4, presa.getKey());

            uPresaInCarico.executeUpdate();

            dataLayer.getCache().add(PresaInCarico.class, presa);
        } catch (SQLException ex) {
            throw new DataException("Errore nell'aggiornamento della presa in carico con ID: " + presa.getKey(), ex);
        }
    }

    @Override
    public void deletePresaInCarico(int id) throws DataException {
        try {
            dPresaInCarico.setInt(1, id);
            int affectedRows = dPresaInCarico.executeUpdate();
            if (affectedRows == 0) {
                throw new DataException("Eliminazione fallita, nessuna riga interessata per ID: " + id);
            }

            dataLayer.getCache().delete(PresaInCarico.class, id);
        } catch (SQLException ex) {
            throw new DataException("Errore nell'eliminazione della presa in carico con ID: " + id, ex);
        }
    }

    private PresaInCarico createPresaInCarico(ResultSet rs) throws SQLException {
        PresaInCaricoProxy presa = (PresaInCaricoProxy) createPresaInCarico();
        presa.setKey(rs.getInt("ID"));
        presa.setDataIncarico(rs.getTimestamp("data_incarico"));
        presa.setCompletato(rs.getBoolean("completato"));
        presa.setTecnicoKey(rs.getInt("ID_tecnico"));
        presa.setRichiestaKey(rs.getInt("ID_richiesta"));

        // Imposta i dati aggiuntivi se presenti (dalle JOIN)
        try {
            presa.setNoteRichiesta(rs.getString("note"));
            presa.setImportoTotale(rs.getDouble("importo_totale"));
            presa.setUtenteNome(rs.getString("utenteNome"));
            presa.setUtenteCognome(rs.getString("utenteCognome"));
            presa.setCategoriaNome(rs.getString("categoriaNome"));
            presa.setMotivazione(rs.getString("motivazione"));
            presa.setIdProdotto(rs.getString("prodotto"));
            presa.setNomeProdottoCandidato(rs.getString("prodottoNome"));
        } catch (SQLException e) {
            // I campi aggiuntivi potrebbero non essere presenti in tutte le query
            // Ignora l'eccezione se si tratta di colonne non trovate
            if (!e.getMessage().contains("not found")) {
                throw e;
            }
        }

        return presa;
    }

    @Override
    public PresaInCarico createPresaInCarico() {
        return new PresaInCaricoProxy(dataLayer);
    }

    @Override
    public void destroy() throws DataException {
        try {

            sRichiesteInCaricoByTecnico.close();

            sPresaInCaricoByID.close();

            sPresaInCaricoByRichiestaID.close();

            iPresaInCarico.close();

            uPresaInCarico.close();

            dPresaInCarico.close();

        } catch (SQLException ex) {
            throw new DataException("Errore nella chiusura delle prepared statements", ex);
        }
        super.destroy();
    }
}
