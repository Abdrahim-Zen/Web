/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.webmarket.data.dao.impl;

import it.univaq.webmarket.data.model.RichiestaAcquisto;
import it.univaq.webmarket.framework.data.DAO;
import it.univaq.webmarket.framework.data.DataException;
import it.univaq.webmarket.framework.data.DataItemProxy;
import it.univaq.webmarket.framework.data.DataLayer;
import it.univaq.webmarket.framework.data.OptimisticLockException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import it.univaq.webmarket.data.dao.RichiestaAcquistoDAO;
import it.univaq.webmarket.data.model.impl.proxy.RichiestaAcquistoProxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author abdrahimzeno
 */
public class RichiestaAcquistoDAO_MySQL extends DAO implements RichiestaAcquistoDAO {

    PreparedStatement iRichiesta;
    PreparedStatement uRichiesta;
    PreparedStatement sRichiestaByUtente;
    PreparedStatement sRichiestaById;
    PreparedStatement sRichiestaNonPreseInCarico;
    PreparedStatement sSpecificheByRichiestaId;
    PreparedStatement sRichiesteNonAssociateConSpecifiche;
    public RichiestaAcquistoDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        super.init();
        try {
            iRichiesta = connection.prepareStatement(
                    "INSERT INTO richiestaAcquisto (ID_utente, ID_categoria, importo_totale, data_inserimento, note) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            uRichiesta = connection.prepareStatement(
                    "UPDATE richiestaAcquisto SET ID_utente=?, ID_categoria=?, importo_totale=?, stato=?, version=?,note=?  "
                    + "WHERE ID=? AND version=? ");
            sRichiestaByUtente = connection.prepareStatement("SELECT * FROM richiestaAcquisto WHERE ID_utente=? ");
            sRichiestaById = connection.prepareStatement("SELECT * FROM richiestaAcquisto r JOIN categoria c ON r.ID_categoria = c.ID WHERE r.ID=? ");
            sRichiestaNonPreseInCarico = connection.prepareStatement(" SELECT * FROM  richiestaAcquisto WHERE stato = 'in_attesa' ");
            
                        // Aggiungi queste nuove prepared statements
            sSpecificheByRichiestaId = connection.prepareStatement(
                "SELECT sr.ID, sr.valore, sc.nome_specifica " +
                "FROM specifiche_richiesta sr " +
                "JOIN specifica_categoria sc ON sr.ID_specifica_categoria = sc.ID " +
                "WHERE sr.ID_richiesta = ?"
            );

            sRichiesteNonAssociateConSpecifiche = connection.prepareStatement(
                "SELECT ra.*, c.nome as nome_categoria " +
                "FROM richiestaAcquisto ra " +
                "JOIN categoria c ON ra.ID_categoria = c.ID " +
                "LEFT JOIN richiesteInCarico ric ON ra.ID = ric.ID_richiesta " +
                "WHERE ric.ID IS NULL AND ra.stato = 'in_attesa'"
            );
        } catch (SQLException ex) {
            Logger.getLogger(RichiestaAcquistoDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void storeRichiestaAcquisto(RichiestaAcquisto richiesta) throws DataException {
        try {
            if (richiesta.getKey() != null && richiesta.getKey() > 0) {
                if (richiesta instanceof DataItemProxy && !((DataItemProxy) richiesta).isModified()) {
                    return;
                }

                uRichiesta.setInt(1, richiesta.getUtenteRegistrato().getKey());
                uRichiesta.setInt(2, richiesta.getCategoria().getKey());

                if (richiesta.getImporto() != null) {
                    uRichiesta.setBigDecimal(3, BigDecimal.valueOf(richiesta.getImporto()));
                } else {
                    uRichiesta.setNull(3, java.sql.Types.DECIMAL);
                }

                uRichiesta.setTimestamp(4, (richiesta.getDataInserimento()));

                long current_version = richiesta.getVersion();
                long next_version = current_version + 1;

                uRichiesta.setLong(5, next_version);
                uRichiesta.setString(6, richiesta.getNote());
                uRichiesta.setInt(7, richiesta.getKey());
                uRichiesta.setLong(8, current_version);

                if (uRichiesta.executeUpdate() == 0) {
                    throw new OptimisticLockException(richiesta);
                } else {
                    richiesta.setVersion(next_version);
                }
            } else {
                iRichiesta.setInt(1, richiesta.getUtenteRegistrato().getKey());
                iRichiesta.setInt(2, richiesta.getCategoria().getKey());

                if (richiesta.getImporto() != null) {
                    iRichiesta.setBigDecimal(3, BigDecimal.valueOf(richiesta.getImporto()));
                } else {
                    iRichiesta.setNull(3, java.sql.Types.DECIMAL);
                }

                iRichiesta.setTimestamp(4, richiesta.getDataInserimento());

                iRichiesta.setString(5, richiesta.getNote());

                if (iRichiesta.executeUpdate() == 1) {
                    try (ResultSet keys = iRichiesta.getGeneratedKeys()) {
                        if (keys.next()) {
                            int key = keys.getInt(1);
                            richiesta.setKey(key);
                            dataLayer.getCache().add(RichiestaAcquisto.class, richiesta);
                        }
                    }
                }
            }

            if (richiesta instanceof DataItemProxy) {
                ((DataItemProxy) richiesta).setModified(false);
            }
        } catch (SQLException | OptimisticLockException ex) {
            throw new DataException("Unable to store richiesta acquisto", ex);
        }
    }
    
    @Override
        public List<Map<String, String>> getSpecificheByRichiestaId(int idRichiesta) throws DataException {
        List<Map<String, String>> specifiche = new ArrayList<>();
        try {
            sSpecificheByRichiestaId.setInt(1, idRichiesta);
            try (ResultSet rs = sSpecificheByRichiestaId.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> specifica = new HashMap<>();
                    specifica.put("nome_specifica", rs.getString("nome_specifica"));
                    specifica.put("valore", rs.getString("valore"));
                    specifiche.add(specifica);
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Errore nel caricamento delle specifiche della richiesta", ex);
        }
        return specifiche;
    }

    // Metodo per recuperare le richieste non associate con le loro specifiche
    public List<RichiestaAcquisto> getRichiesteNonAssociateConSpecifiche() throws DataException {
        List<RichiestaAcquisto> richieste = new ArrayList<>();
        try {
            try (ResultSet rs = sRichiesteNonAssociateConSpecifiche.executeQuery()) {
                while (rs.next()) {
                    RichiestaAcquisto richiesta = createRichiesta(rs);
                    // Recupera le specifiche per questa richiesta
                    List<Map<String, String>> specifiche = getSpecificheByRichiestaId(richiesta.getKey());
                    // Se il tuo modello RichiestaAcquisto supporta l'aggiunta di specifiche, aggiungile qui
                    // Altrimenti, le passeremo separatamente al datamodel
                    richieste.add(richiesta);
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Errore nel caricamento delle richieste non associate", ex);
        }
        return richieste;
    }

    @Override
    public RichiestaAcquisto createRichiesta() {
        return new RichiestaAcquistoProxy(dataLayer);
    }
    
    

    @Override
    public void updateRichiestaAcquisto(RichiestaAcquisto richiesta) throws DataException {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE richiestaAcquisto SET stato = ? WHERE ID = ?"
            );
            stmt.setString(1, richiesta.getStato());
            stmt.setInt(2, richiesta.getKey());

            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException ex) {
            throw new DataException("Errore nell'aggiornamento della richiesta acquisto", ex);
        }
    }

    @Override
    public RichiestaAcquisto getRichiestaAcquistoByID(int id) throws DataException {
        RichiestaAcquisto richiesta = null;
        try {
            // Controlla prima la cache
            if (dataLayer.getCache().has(RichiestaAcquisto.class, id)) {
                richiesta = dataLayer.getCache().get(RichiestaAcquisto.class, id);
            } else {
                PreparedStatement stmt = connection.prepareStatement(
                        "SELECT r.*, c.* "
                        + "FROM richiestaAcquisto r "
                        + "JOIN categoria c ON r.ID_categoria = c.ID "
                        + "WHERE r.ID = ?"
                );
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    richiesta = createRichiesta(rs);
                    dataLayer.getCache().add(RichiestaAcquisto.class, richiesta);
                }
                stmt.close();
            }
        } catch (SQLException ex) {
            throw new DataException("Errore nel recupero della richiesta acquisto con ID: " + id, ex);
        }
        return richiesta;
    }

    @Override
    public List<RichiestaAcquisto> getRichiesteByUtenteId(int utenteid) {
        List<RichiestaAcquisto> richieste = new ArrayList<>();

        try {
            sRichiestaByUtente.setInt(1, utenteid);
            ResultSet rs = sRichiestaByUtente.executeQuery();
            while (rs.next()) {
                richieste.add(getRichiestaById(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RichiestaAcquistoDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return richieste;
    }

    public RichiestaAcquisto getRichiestaById(int id) throws SQLException {
        RichiestaAcquisto richiesta = null;

        if (dataLayer.getCache().has(RichiestaAcquisto.class, id)) {
            richiesta = dataLayer.getCache().get(RichiestaAcquisto.class, id);
        } else {
            sRichiestaById.setInt(1, id);
            ResultSet rs = sRichiestaById.executeQuery();
            if (rs.next()) {
                richiesta = createRichiesta(rs);
                dataLayer.getCache().add(RichiestaAcquisto.class, richiesta);
            }
        }
        return richiesta;

    }

    @Override
    public List<RichiestaAcquisto> getRichiesteNonAssociate() {
        List<RichiestaAcquisto> richieste = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT r.*, c.*"
                    + "FROM richiestaAcquisto r "
                    + "JOIN categoria c ON r.ID_categoria = c.ID "
                    + "WHERE r.stato = 'in_attesa' "
                    + // Solo richieste in attesa
                    "AND NOT EXISTS ( "
                    + "    SELECT 1 FROM richiesteInCarico rc "
                    + "    WHERE rc.ID_richiesta = r.ID "
                    + ") "
                    + "ORDER BY r.data_inserimento DESC"
            );

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                RichiestaAcquisto richiesta = createRichiesta(rs);
                richieste.add(richiesta);
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(RichiestaAcquistoDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return richieste;
    }

    private RichiestaAcquisto createRichiesta(ResultSet rs) throws SQLException {
        RichiestaAcquistoProxy richiesta = (RichiestaAcquistoProxy) createRichiesta();
        richiesta.setKey(rs.getInt("ID"));
        richiesta.setOrdinate_key(rs.getInt("ID_utente"));
        richiesta.setCategory_key(rs.getInt("ID_categoria"));
        richiesta.setImporto(rs.getDouble("importo_totale"));
        richiesta.setNote(rs.getString("note"));
        richiesta.setStato(rs.getString("stato"));
        richiesta.setCategorai(rs.getString("nome"));
        richiesta.setDataInserimento(rs.getTimestamp("data_inserimento"));
        return richiesta;

    }

}
