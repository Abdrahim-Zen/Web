package it.univaq.webmarket.data.dao.impl;

import it.univaq.webmarket.data.dao.CategoriaDAO;
import it.univaq.webmarket.data.model.Categoria;
import it.univaq.webmarket.data.model.SpecificaCategoria;
import it.univaq.webmarket.data.model.impl.SpecificaCategoriaImpl;
import it.univaq.webmarket.data.model.impl.proxy.CategoriaProxy;
import it.univaq.webmarket.framework.data.DAO;
import it.univaq.webmarket.framework.data.DataException;
import it.univaq.webmarket.framework.data.DataLayer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO_MySQL extends DAO implements CategoriaDAO {

    private PreparedStatement sAllCategorie;
    private PreparedStatement sCategoriaById;
    private PreparedStatement iCategoria;
    private PreparedStatement uCategoria;
    private PreparedStatement dCategoria;
    private PreparedStatement sCategorieWithSpecifiche;

    public CategoriaDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();
            sAllCategorie = connection.prepareStatement("SELECT * FROM categoria   ");
            sCategoriaById = connection.prepareStatement("SELECT * FROM categoria WHERE ID = ?");
            iCategoria = connection.prepareStatement("INSERT INTO categoria (nome) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            uCategoria = connection.prepareStatement("UPDATE categoria SET nome = ? WHERE ID = ?");
            dCategoria = connection.prepareStatement("DELETE FROM categoria WHERE ID = ?");
            sCategorieWithSpecifiche = connection.prepareStatement(
                "SELECT c.*, sc.ID as spec_id, sc.nome_specifica " +
                "FROM categoria c " +
                "LEFT JOIN specifica_categoria sc ON c.ID = sc.ID_categoria " +
                "ORDER BY c.nome, sc.ID"
            );
        } catch (SQLException ex) {
            throw new DataException("Error initializing statements in CategoriaDAO_MySQL", ex);
        }
    }

    @Override
    public List<Categoria> getAllCategorie() throws DataException {
        List<Categoria> categorie = new ArrayList<>();

        try (ResultSet rs = sAllCategorie.executeQuery()) {
            while (rs.next()) {
                Categoria categoria = createCategoria(rs);
                categorie.add(categoria);
                dataLayer.getCache().add(Categoria.class, categoria);
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load all categorie", ex);
        }

        return categorie;
    }

    @Override
    public List<Categoria> getAllCategorieWithSpecifiche() throws DataException {
        List<Categoria> categorie = new ArrayList<>();
        
        try (ResultSet rs = sCategorieWithSpecifiche.executeQuery()) {
            Categoria currentCategoria = null;
            
            while (rs.next()) {
                int categoriaId = rs.getInt("ID");
                
                
                if (currentCategoria == null || currentCategoria.getKey() != categoriaId) {
                    currentCategoria = createCategoria(rs);
                    categorie.add(currentCategoria);
                    dataLayer.getCache().add(Categoria.class, currentCategoria);
                }
                
             
                int specId = rs.getInt("spec_id");
                if (!rs.wasNull()) {
                    SpecificaCategoria specifica = createSpecificaCategoria(rs);
                    currentCategoria.addSpecifica(specifica);
              
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load categorie with specifiche", ex);
        }
        
        return categorie;
    }

    private Categoria createCategoria(ResultSet rs) throws SQLException {
        CategoriaProxy categoria = new CategoriaProxy(dataLayer);
        categoria.setKey(rs.getInt("ID"));
        categoria.setNomeCategoria(rs.getString("nome"));
        return categoria;
    }
    
    private SpecificaCategoria createSpecificaCategoria(ResultSet rs) throws SQLException {
        SpecificaCategoria specifica = new SpecificaCategoriaImpl();
        specifica.setKey(rs.getInt("spec_id"));
        specifica.setNomeSpecifica(rs.getString("nome_specifica"));
        return specifica;
    }

    @Override
    public Categoria getCategoriabyID(int id) throws DataException {
        Categoria t = null;
        if (dataLayer.getCache().has(Categoria.class, id)) {
            t = dataLayer.getCache().get(Categoria.class, id);
        } else {
            try {
                sCategoriaById.setInt(1, id);
                try (ResultSet rs = sCategoriaById.executeQuery()) {
                    if (rs.next()) {
                        t = createCategoria(rs);
                        dataLayer.getCache().add(Categoria.class, t);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load categoria by ID", ex);
            }
        }
        return t;
    }

    @Override
    public int insertCategoria(String nome) throws DataException {
        try {
            iCategoria.setString(1, nome);
            
            int affectedRows = iCategoria.executeUpdate();
            if (affectedRows == 0) {
                throw new DataException("Creating categoria failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = iCategoria.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    dataLayer.getCache().add(Categoria.class, getCategoriabyID(id));
                    return id;
                } else {
                    throw new DataException("Creating categoria failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to insert categoria", ex);
        }
    }

    @Override
    public void updateCategoria(int id, String nome) throws DataException {
        try {
            uCategoria.setString(1, nome);
            uCategoria.setInt(2, id);
            
            int affectedRows = uCategoria.executeUpdate();
            if (affectedRows == 0) {
                throw new DataException("Updating categoria failed, no rows affected.");
            }
            
          
            dataLayer.getCache().delete(Categoria.class, id);
            
        } catch (SQLException ex) {
            throw new DataException("Unable to update categoria", ex);
        }
    }

    @Override
    public void deleteCategoria(int id) throws DataException {
        try {
            dCategoria.setInt(1, id);
            
            int affectedRows = dCategoria.executeUpdate();
            if (affectedRows == 0) {
                throw new DataException("Deleting categoria failed, no rows affected.");
            }
            
           
            dataLayer.getCache().delete(Categoria.class, id);
            
        } catch (SQLException ex) {
            throw new DataException("Unable to delete categoria", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        try {
           sAllCategorie.close();
            sCategoriaById.close();
            iCategoria.close();
            uCategoria.close();
            dCategoria.close();
           sCategorieWithSpecifiche.close();
        } catch (SQLException ex) {
            throw new DataException("Error closing statements", ex);
        }
        super.destroy();
    }
}