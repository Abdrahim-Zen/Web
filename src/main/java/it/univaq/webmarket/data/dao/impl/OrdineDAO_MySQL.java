package it.univaq.webmarket.data.dao.impl;

import it.univaq.webmarket.data.dao.OrdineDAO;
import it.univaq.webmarket.data.model.Ordine;
import it.univaq.webmarket.data.model.impl.proxy.OrdineProxy;
import it.univaq.webmarket.framework.data.DAO;
import it.univaq.webmarket.framework.data.DataException;
import it.univaq.webmarket.framework.data.DataLayer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrdineDAO_MySQL extends DAO implements OrdineDAO {

    private PreparedStatement sAllOrdini;
    private PreparedStatement sOrdineById;
    private PreparedStatement sOrdiniByStato;
    private PreparedStatement sOrdiniByUtente;
    private PreparedStatement iOrdine;
    private PreparedStatement uStatoOrdine;
    private PreparedStatement uStatoOrdineConMotivazione;
    private PreparedStatement dOrdine;

    public OrdineDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        super.init();
        try {

            sAllOrdini = connection.prepareStatement(
                    "SELECT o.*, "
                    + "u.email as email_utente, "
                    + "ut.email as email_tecnico "
                    + "FROM ordine o "
                    + "JOIN utenteRegistrato ur ON o.ID_utente = ur.ID "
                    + "JOIN utente u ON ur.ID = u.ID "
                    + "JOIN prodottoCandidato pc ON o.ID_prodotto_candidato = pc.ID "
                    + "LEFT JOIN richiesteInCarico ric ON pc.ID_richiestaInCarico = ric.ID "
                    + "LEFT JOIN tecnico t ON ric.ID_tecnico = t.ID "
                    + "LEFT JOIN utente ut ON t.ID = ut.ID  WHERE t.ID = ? "
                    + "ORDER BY o.data_ordine DESC "
            );

            sOrdineById = connection.prepareStatement(
                    "SELECT o.*, "
                    + "u.email as email_utente, "
                    + "ut.email as email_tecnico "
                    + "FROM ordine o "
                    + "JOIN utenteRegistrato ur ON o.ID_utente = ur.ID "
                    + "JOIN utente u ON ur.ID = u.ID "
                    + "JOIN prodottoCandidato pc ON o.ID_prodotto_candidato = pc.ID "
                    + "LEFT JOIN richiesteInCarico ric ON pc.ID_richiestaInCarico = ric.ID "
                    + "LEFT JOIN tecnico t ON ric.ID_tecnico = t.ID "
                    + "LEFT JOIN utente ut ON t.ID = ut.ID "
                    + "WHERE o.ID = ?"
            );

            sOrdiniByStato = connection.prepareStatement(
                    "SELECT o.*, u.email as email_utente "
                    + "FROM ordine o "
                    + "JOIN utenteRegistrato ur ON o.ID_utente = ur.ID "
                    + "JOIN utente u ON ur.ID = u.ID "
                    + "WHERE o.stato = ? ORDER BY o.data_ordine DESC"
            );

            sOrdiniByUtente = connection.prepareStatement(
                    "SELECT * FROM ordine WHERE ID_utente = ? ORDER BY data_ordine DESC"
            );

            iOrdine = connection.prepareStatement(
                    "INSERT INTO ordine (ID_utente, ID_prodotto_candidato) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            uStatoOrdine = connection.prepareStatement(
                    "UPDATE ordine SET stato = ? WHERE ID = ?"
            );

            uStatoOrdineConMotivazione = connection.prepareStatement(
                    "UPDATE ordine SET stato = ?, motivazione_rifiuto = ? WHERE ID = ?"
            );

            dOrdine = connection.prepareStatement(
                    "DELETE FROM ordine WHERE ID = ?"
            );

        } catch (SQLException ex) {
            Logger.getLogger(OrdineDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataException("Errore inizializzazione OrdineDAO", ex);
        }
    }

    @Override
    public List<Ordine> getAllOrdiniWithDetails(int id) throws DataException {
        List<Ordine> ordini = new ArrayList<>();
        try {
            sAllOrdini.setInt(1, id);
            try ( ResultSet rs = sAllOrdini.executeQuery()) {
                while (rs.next()) {

                    ordini.add(getOrdineById(rs.getInt("ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Errore nel caricamento degli ordini", ex);
        }
        return ordini;
    }

    @Override
    public Ordine getOrdineById(int id) throws DataException {
        Ordine ordine = null;

        if (dataLayer.getCache().has(Ordine.class, id)) {
            ordine = dataLayer.getCache().get(Ordine.class, id);
        } else {
            try {
                sOrdineById.setInt(1, id);
                try ( ResultSet rs = sOrdineById.executeQuery()) {
                    if (rs.next()) {
                        ordine = createOrdine(rs);
                        dataLayer.getCache().add(Ordine.class, ordine);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Errore nel caricamento dell'ordine", ex);
            }
        }
        return ordine;
    }

    @Override
    public List<Ordine> getOrdiniByStato(String stato) throws DataException {
        List<Ordine> ordini = new ArrayList<>();
        try {
            sOrdiniByStato.setString(1, stato);
            try ( ResultSet rs = sOrdiniByStato.executeQuery()) {
                while (rs.next()) {
                    ordini.add(getOrdineById(rs.getInt("ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Errore nel caricamento degli ordini per stato", ex);
        }
        return ordini;
    }

    @Override
    public List<Ordine> getOrdiniByUtente(int idUtente) throws DataException {
        List<Ordine> ordini = new ArrayList<>();
        try {
            sOrdiniByUtente.setInt(1, idUtente);
            try ( ResultSet rs = sOrdiniByUtente.executeQuery()) {
                while (rs.next()) {
                    ordini.add(getOrdineById(rs.getInt("ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Errore nel caricamento degli ordini per utente", ex);
        }
        return ordini;
    }

    @Override
    public int insertOrdine(int idUtente, int idProdottoCandidato) throws DataException {
        try {

            iOrdine.setInt(1, idUtente);
            iOrdine.setInt(2, idProdottoCandidato);

            int affectedRows = iOrdine.executeUpdate();
            if (affectedRows == 0) {
                throw new DataException("Inserimento ordine fallito, nessuna riga modificata.");
            }

            try ( ResultSet generatedKeys = iOrdine.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idGenerato = generatedKeys.getInt(1);

                    dataLayer.getCache().delete(Ordine.class, idGenerato);
                    return idGenerato;
                } else {
                    throw new DataException("Inserimento ordine fallito, nessun ID ottenuto.");
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Errore durante l'inserimento dell'ordine", ex);
        }
    }

    @Override
    public void updateStatoOrdine(int idOrdine, String stato) throws DataException {
        try {
            uStatoOrdine.setString(1, stato);
            uStatoOrdine.setInt(2, idOrdine);

            int affectedRows = uStatoOrdine.executeUpdate();
            if (affectedRows == 0) {
                throw new DataException("Aggiornamento stato ordine fallito, nessuna riga modificata.");
            }

            // Invalida la cache
            dataLayer.getCache().delete(Ordine.class, idOrdine);

        } catch (SQLException ex) {
            throw new DataException("Errore durante l'aggiornamento dello stato ordine", ex);
        }
    }

    @Override
    public void updateStatoOrdine(int idOrdine, String stato, String motivazione) throws DataException {
        try {
            uStatoOrdineConMotivazione.setString(1, stato);
            uStatoOrdineConMotivazione.setString(2, motivazione);
            uStatoOrdineConMotivazione.setInt(3, idOrdine);

            int affectedRows = uStatoOrdineConMotivazione.executeUpdate();
            if (affectedRows == 0) {
                throw new DataException("Aggiornamento stato ordine fallito, nessuna riga modificata.");
            }

            dataLayer.getCache().delete(Ordine.class, idOrdine);

        } catch (SQLException ex) {
            throw new DataException("Errore durante l'aggiornamento dello stato ordine con motivazione", ex);
        }
    }

    @Override
    public void deleteOrdine(int idOrdine) throws DataException {
        try {
            dOrdine.setInt(1, idOrdine);

            int affectedRows = dOrdine.executeUpdate();
            if (affectedRows == 0) {
                throw new DataException("Eliminazione ordine fallita, nessuna riga modificata.");
            }

            dataLayer.getCache().delete(Ordine.class, idOrdine);

        } catch (SQLException ex) {
            throw new DataException("Errore durante l'eliminazione dell'ordine", ex);
        }
    }

    @Override
    public Ordine createOrdine() {
        return new OrdineProxy(getDataLayer());
    }

    private Ordine createOrdine(ResultSet rs) throws SQLException, DataException {
        OrdineProxy ordine = (OrdineProxy) createOrdine();
        ordine.setKey(rs.getInt("ID"));

        Timestamp timestamp = rs.getTimestamp("data_ordine");
        if (timestamp != null) {
            ordine.setDataOrdine(timestamp.toLocalDateTime());
        }

        ordine.setStato(rs.getString("stato"));
        ordine.setMotivazioneRifiuto(rs.getString("motivazione_rifiuto"));

        ordine.setIdUtente(rs.getInt("ID_utente"));
        ordine.setIdProdottoCandidato(rs.getInt("ID_prodotto_candidato"));

        ordine.setEmailUtente(rs.getString("email_utente"));

        ordine.setEmailTecnico(rs.getString("email_tecnico"));

        return ordine;
    }

    @Override
    public void destroy() throws DataException {
        try {
            sAllOrdini.close();
            sOrdineById.close();
            sOrdiniByStato.close();
            sOrdiniByUtente.close();
            iOrdine.close();
            uStatoOrdine.close();
            uStatoOrdineConMotivazione.close();
            dOrdine.close();
        } catch (SQLException ex) {
            throw new DataException("Errore nella chiusura degli statement", ex);
        }
        super.destroy();
    }
}
