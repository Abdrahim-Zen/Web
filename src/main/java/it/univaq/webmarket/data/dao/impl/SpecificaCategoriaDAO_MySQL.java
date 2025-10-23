package it.univaq.webmarket.data.dao.impl;

import it.univaq.webmarket.data.dao.SpecificaCategoriaDAO;
import it.univaq.webmarket.data.model.SpecificaCategoria;
import it.univaq.webmarket.data.model.impl.proxy.SpecificaCategoriaProxy;
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

public class SpecificaCategoriaDAO_MySQL extends DAO implements SpecificaCategoriaDAO {

    private PreparedStatement sSpecificheByCategoriaId;
    private PreparedStatement sSpecificaById;
    private PreparedStatement iSpecifica;
    private PreparedStatement uSpecifica;
    private PreparedStatement dSpecifica;
    private PreparedStatement dSpecificheByCategoria;

    public SpecificaCategoriaDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        super.init();
        try {
            // Query per ottenere le specifiche per una categoria
            sSpecificheByCategoriaId = connection.prepareStatement(
                "SELECT * FROM specifica_categoria WHERE ID_categoria = ?"
            );

            // Query per ottenere una specifica per id
            sSpecificaById = connection.prepareStatement(
                "SELECT * FROM specifica_categoria WHERE ID = ?"
            );

            // INSERT per aggiungere una nuova specifica
            iSpecifica = connection.prepareStatement(
                "INSERT INTO specifica_categoria (ID_categoria, nome_specifica) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );

            // UPDATE per modificare una specifica
            uSpecifica = connection.prepareStatement(
                "UPDATE specifica_categoria SET nome_specifica = ? WHERE ID = ?"
            );

            // DELETE per eliminare una specifica
            dSpecifica = connection.prepareStatement(
                "DELETE FROM specifica_categoria WHERE ID = ?"
            );

            // DELETE per eliminare tutte le specifiche di una categoria
            dSpecificheByCategoria = connection.prepareStatement(
                "DELETE FROM specifica_categoria WHERE ID_categoria = ?"
            );

        } catch (SQLException ex) {
            Logger.getLogger(SpecificaCategoriaDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataException("Errore inizializzazione SpecificaCategoriaDAO", ex);
        }
    }

    @Override
    public List<SpecificaCategoria> getListSpecificaCategoria(int idCategoria) throws DataException {
        List<SpecificaCategoria> specifiche = new ArrayList<>();
        try {
            sSpecificheByCategoriaId.setInt(1, idCategoria);
            try (ResultSet rs = sSpecificheByCategoriaId.executeQuery()) {
                while (rs.next()) {
                    // usa l'id della specifica per caricare tramite getSpecificaCategoria()
                    specifiche.add(getSpecificaCategoria(rs.getInt("ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Errore nel caricamento delle specifiche della categoria", ex);
        }
        return specifiche;
    }

    @Override
    public SpecificaCategoria getSpecificaCategoria(int id) throws DataException {
        SpecificaCategoria spec = null;

        if (dataLayer.getCache().has(SpecificaCategoria.class, id)) {
            spec = dataLayer.getCache().get(SpecificaCategoria.class, id);
        } else {
            try {
                sSpecificaById.setInt(1, id);
                try (ResultSet rs = sSpecificaById.executeQuery()) {
                    if (rs.next()) {
                        spec = createSpecificaCategoria(rs);
                        dataLayer.getCache().add(SpecificaCategoria.class, spec);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Errore nel caricamento della specifica categoria", ex);
            }
        }
        return spec;
    }

    @Override
    public int insertSpecifica(int idCategoria, String nomeSpecifica) throws DataException {
        try {
            iSpecifica.setInt(1, idCategoria);
            iSpecifica.setString(2, nomeSpecifica);
            
            int affectedRows = iSpecifica.executeUpdate();
            if (affectedRows == 0) {
                throw new DataException("Inserimento specifica fallito, nessuna riga modificata.");
            }
            
            // Recupera l'ID generato
            try (ResultSet generatedKeys = iSpecifica.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idGenerato = generatedKeys.getInt(1);
                    // Invalida la cache per forzare il ricaricamento
                    dataLayer.getCache().delete(SpecificaCategoria.class, idGenerato);
                    return idGenerato;
                } else {
                    throw new DataException("Inserimento specifica fallito, nessun ID ottenuto.");
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Errore durante l'inserimento della specifica", ex);
        }
    }

    @Override
    public void updateSpecifica(int idSpecifica, String nomeSpecifica) throws DataException {
        try {
            uSpecifica.setString(1, nomeSpecifica);
            uSpecifica.setInt(2, idSpecifica);
            
            int affectedRows = uSpecifica.executeUpdate();
            if (affectedRows == 0) {
                throw new DataException("Aggiornamento specifica fallito, nessuna riga modificata.");
            }
            
            // Invalida la cache
            dataLayer.getCache().delete(SpecificaCategoria.class, idSpecifica);
            
        } catch (SQLException ex) {
            throw new DataException("Errore durante l'aggiornamento della specifica", ex);
        }
    }

    @Override
    public void deleteSpecifica(int idSpecifica) throws DataException {
        try {
            dSpecifica.setInt(1, idSpecifica);
            
            int affectedRows = dSpecifica.executeUpdate();
            if (affectedRows == 0) {
                throw new DataException("Eliminazione specifica fallita, nessuna riga modificata.");
            }
            
            // Invalida la cache
            dataLayer.getCache().delete(SpecificaCategoria.class, idSpecifica);
            
        } catch (SQLException ex) {
            throw new DataException("Errore durante l'eliminazione della specifica", ex);
        }
    }

    @Override
    public void deleteSpecificheByCategoria(int idCategoria) throws DataException {
        try {
            dSpecificheByCategoria.setInt(1, idCategoria);
            dSpecificheByCategoria.executeUpdate();
            
         
        } catch (SQLException ex) {
            throw new DataException("Errore durante l'eliminazione delle specifiche della categoria", ex);
        }
    }

    public SpecificaCategoria createSpecificaCategoria() {
        return new SpecificaCategoriaProxy(getDataLayer());
    }

    private SpecificaCategoria createSpecificaCategoria(ResultSet rs) throws SQLException, DataException {
        SpecificaCategoriaProxy spec = (SpecificaCategoriaProxy) createSpecificaCategoria();
        spec.setKey(rs.getInt("ID"));
        spec.setCategoriaKey(rs.getInt("ID_categoria"));
        spec.setNomeSpecifica(rs.getString("nome_specifica"));
        return spec;
    }

    @Override
    public void destroy() throws DataException {
        try {
            if (sSpecificheByCategoriaId != null) sSpecificheByCategoriaId.close();
            if (sSpecificaById != null) sSpecificaById.close();
            if (iSpecifica != null) iSpecifica.close();
            if (uSpecifica != null) uSpecifica.close();
            if (dSpecifica != null) dSpecifica.close();
            if (dSpecificheByCategoria != null) dSpecificheByCategoria.close();
        } catch (SQLException ex) {
            throw new DataException("Errore nella chiusura degli statement", ex);
        }
        super.destroy();
    }
}