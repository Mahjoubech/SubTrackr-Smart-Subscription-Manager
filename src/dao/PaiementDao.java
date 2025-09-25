package dao;
import model.entity.Paiement;
import model.enums.StatusPaiement;
import DataBase.DbConn;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaiementDao implements CrudDao<Paiement> {
    private final Connection conn;

    public PaiementDao() {
        try {
            this.conn = DbConn.getInstance().getConn();
        } catch (SQLException ex) {
            throw new RuntimeException("Erreur lors de la connexion à la base de données", ex);
        }
    }

    @Override
    public void create(Paiement paiement) {
        String sql = "INSERT INTO paiement (idPaiement, idAbonnement, dateEcheance, datePaiement, typePaiement, statut) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, paiement.getIdPaiement());
            stmt.setString(2, paiement.getIdAbonnement());
            stmt.setDate(3, Date.valueOf(paiement.getDateEcheance()));
            stmt.setDate(4, paiement.getDatePaiement() != null ? Date.valueOf(paiement.getDatePaiement()) : null);
            stmt.setString(5, paiement.getTypePaiement());
            stmt.setString(6, paiement.getStatut().name());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Erreur lors de la création du paiement: " + ex.getMessage());
        }
    }

    @Override
    public void update(Paiement paiement) {
        String sql = "UPDATE paiement SET idAbonnement=?, dateEcheance=?, datePaiement=?, typePaiement=?, statut=? WHERE idPaiement=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, paiement.getIdAbonnement());
            stmt.setDate(2, Date.valueOf(paiement.getDateEcheance()));
            stmt.setDate(3, paiement.getDatePaiement() != null ? Date.valueOf(paiement.getDatePaiement()) : null);
            stmt.setString(4, paiement.getTypePaiement());
            stmt.setString(5, paiement.getStatut().name());
            stmt.setString(6, paiement.getIdPaiement());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Erreur lors de la mise à jour du paiement: " + ex.getMessage());
        }
    }

    @Override
    public void delete(String idPaiement) {
        String sql = "DELETE FROM paiement WHERE idPaiement=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idPaiement);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Erreur lors de la suppression du paiement: " + ex.getMessage());
        }
    }


}