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

    @Override
    public Optional<Paiement> findById(String idPaiement) {
        String sql = "SELECT * FROM paiement WHERE idPaiement=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idPaiement);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapPaiement(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Erreur lors de la recherche du paiement: " + ex.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Paiement> findAll() {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM paiement";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                paiements.add(mapPaiement(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Erreur lors de la récupération des paiements: " + ex.getMessage());
        }
        return paiements;
    }

    // Helper for mapping ResultSet to Paiement
    private Paiement mapPaiement(ResultSet rs) throws SQLException {
        String idPaiement = rs.getString("idPaiement");
        String idAbonnement = rs.getString("idAbonnement");
        LocalDate dateEcheance = rs.getDate("dateEcheance").toLocalDate();
        LocalDate datePaiement = rs.getDate("datePaiement") != null ? rs.getDate("datePaiement").toLocalDate() : null;
        String typePaiement = rs.getString("typePaiement");
        StatusPaiement statut = StatusPaiement.valueOf(rs.getString("statut"));

        return new Paiement(idPaiement , idAbonnement, dateEcheance, datePaiement, typePaiement, statut);
    }
}