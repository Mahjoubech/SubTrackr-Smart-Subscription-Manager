package dao;
import model.entity.Abonnement;
import DataBase.DbConn;
import model.entity.AbonnementAvecEngagement;
import model.entity.AbonnementSansEngagement;
import model.enums.StatusAbonnement;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AbonnementDao implements CrudDao<Abonnement> {
    private final Connection conn;

    public AbonnementDao() {
        try{
            this.conn = DbConn.getInstance().getConn();
        }catch (SQLException ex){
            throw new RuntimeException("Erreur lors de la connexion à la base de données", ex);
        }


    }
    @Override
  public void create(Abonnement abonnement) {
     String requet = "insert into abonnement (id, nomService, montantMensuel, dateDebut, dateFin, statut, typeAbonnenment) VALUES (?, ?, ?, ?, ?, ?, ?)";
     try(PreparedStatement stmt = conn.prepareStatement(requet)){
         stmt.setString(1 , abonnement.getId());
         stmt.setString(2 , abonnement.getNomService());
         stmt.setDouble(3 , abonnement.getMontantMensuel());
         stmt.setDate(4 , Date.valueOf(abonnement.getDateDebut()));
         stmt.setDate(5 , abonnement.getDateFin() != null ? Date.valueOf(abonnement.getDateFin()) : null);
         stmt.setString(6 , abonnement.getStatut().name());
         String type = abonnement instanceof AbonnementAvecEngagement ? "AVEC_ENGAG" : "SANS_ENGAG";
         stmt.setString(7, type);
         stmt.executeUpdate();
         if(abonnement instanceof AbonnementAvecEngagement){
             AbonnementAvecEngagement AbnAvcEg = (AbonnementAvecEngagement) abonnement;
             String AbnSql = "insert into abonnement_avec_engagement (id , dureeEngagementMois) values (?, ?)";
             try (PreparedStatement stmt2 = conn.prepareStatement(AbnSql)) {
                 stmt2.setString(1, AbnAvcEg.getId());
                 stmt2.setInt(2, AbnAvcEg.getDureeEngagementMois());
                 stmt2.executeUpdate();
             } catch (SQLException e) {
                 throw new RuntimeException("Erreur lors de la sauvegarde de l'engagement: " + e.getMessage());
             }
         }else if (abonnement instanceof AbonnementSansEngagement) {
             String sansSql = "INSERT INTO abonnement_sans_engagement (id) VALUES (?)";
             try (PreparedStatement stmt3 = conn.prepareStatement(sansSql)) {
                 stmt3.setString(1, abonnement.getId());
                 stmt3.executeUpdate();
             } catch (SQLException e) {
                 throw new RuntimeException("Erreur lors de la sauvegarde du sans engagement: " + e.getMessage());
             }
         }
     }catch (SQLException ex){
         throw new RuntimeException(ex);
     };

  }
  @Override
  public void update(Abonnement abonnement) {
        String requet = "UPDATE abonnement SET nomService=?, montantMensuel=?, dateDebut=?, dateFin=?, statut=?, typeAbonnenment=? WHERE id=?";
      try(PreparedStatement stmt = conn.prepareStatement(requet)){
          stmt.setString(1 , abonnement.getNomService());
          stmt.setDouble(2 , abonnement.getMontantMensuel());
          stmt.setDate(3 , Date.valueOf(abonnement.getDateDebut()));
          stmt.setDate(4 , abonnement.getDateFin() != null ? Date.valueOf(abonnement.getDateFin()) : null);
          stmt.setString(5 , abonnement.getStatut().name());
          String type = abonnement instanceof AbonnementAvecEngagement ? "AVEC_ENGAG" : "SANS_ENGAG";
          stmt.setString(6, type);
          stmt.setString(7 , abonnement.getId());
          stmt.executeUpdate();
          if(abonnement instanceof AbonnementAvecEngagement){
              AbonnementAvecEngagement AbnAvcEg = (AbonnementAvecEngagement) abonnement;
              String AbnSql = "UPDATE abonnement_avec_engagement SET dureeEngagementMois=?  WHERE id=?";
              try (PreparedStatement stmt2 = conn.prepareStatement(AbnSql)) {
                  stmt2.setString(2, AbnAvcEg.getId());
                  stmt2.setInt(1, AbnAvcEg.getDureeEngagementMois());
                  stmt2.executeUpdate();
              } catch (SQLException e) {
                  throw new RuntimeException("Erreur lors de la mise à jour de l'abonnement: " + e.getMessage());
              }
          }
      }catch (SQLException ex){
          throw new RuntimeException(ex);
      };
  }
  @Override
  public void delete(String id) {
        String requet = "DELETE FROM abonnement WHERE id=?";
        try(PreparedStatement stmt = conn.prepareStatement(requet)){
            stmt.setString(1, id);
            stmt.executeUpdate();
        }catch (SQLException ex){
            throw new RuntimeException("Erreur lors de la suppression de l'abonnement: " , ex);
        }
  }
  @Override
  public Optional<Abonnement> findById(String id) {
        String requet = "SELECT * FROM abonnement WHERE id=?";
        try(PreparedStatement stmt = conn.prepareStatement(requet)){
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return Optional.of(mapAbonnement(rs));
            }
        }catch (SQLException ex){
            throw new RuntimeException("Erreur lors de la recherche de l'abonnement: " + ex.getMessage());
        }
        return Optional.empty();
  }
 @Override
 public List<Abonnement> findAll(){
        String requet = "SELECT * FROM abonnement";
     List<Abonnement> abonnements = new ArrayList<>();
        try(PreparedStatement stmt = conn.prepareStatement(requet)){
            stmt.executeQuery();
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                abonnements.add(mapAbonnement(rs));
            }
        }catch (SQLException ex){
            throw new RuntimeException("Erreur lors de get all Abonnements: " + ex.getMessage());
        };
        return abonnements;
 }
//add methode maoAbonnement pour mapping abonnmnt
    private Abonnement mapAbonnement(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String nomService = rs.getString("nomService");
        double montantMensuel = rs.getDouble("montantMensuel");
        LocalDate dateDebut = rs.getDate("dateDebut").toLocalDate();
        LocalDate dateFin = rs.getDate("dateFin") != null ? rs.getDate("dateFin").toLocalDate() : null;
        StatusAbonnement statut = StatusAbonnement.valueOf(rs.getString("statut"));
        String type = rs.getString("typeAbonnenment");

        if ("AVEC_ENGAG".equals(type)) {
            String engSql = "SELECT dureeEngagementMois FROM abonnement_avec_engagement WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(engSql)) {
                ps.setString(1, id);
                ResultSet ers = ps.executeQuery();
                int duree = 0;
                if (ers.next()) {
                    duree = ers.getInt("dureeEngagementMois");
                }
                return new AbonnementAvecEngagement(id,nomService, montantMensuel, dateDebut, dateFin, statut, duree);
            }
        } else {
            return new AbonnementSansEngagement(id ,nomService, montantMensuel, dateDebut, dateFin, statut);
        }
    }
    public void delete(){
        String requet = "DELETE FROM abonnement";
        try(PreparedStatement stmt = conn.prepareStatement(requet)){
            stmt.executeUpdate();
        }catch (SQLException ex){
            throw new RuntimeException("Erreur lors de la delete abonnement: " + ex.getMessage());
        }
    }
}
