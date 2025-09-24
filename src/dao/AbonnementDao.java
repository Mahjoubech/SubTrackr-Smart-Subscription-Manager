package dao;
import model.entity.Abonnement;
import DataBase.DbConn;
import model.entity.AbonnementAvecEngagement;
import model.entity.AbonnementSansEngagement;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import  java.sql.SQLException;

public class AbonnementDao implements CrudDao<Abonnement> {
    private final Connection conn;

    public AbonnementDao() {
        try{
            this.conn = DbConn.getInstance().getConn();
        }catch (SQLException ex){
            throw new RuntimeException("Erreur lors de la connexion à la base de données", ex);
        }


    }
  public void create(Abonnement abonnement) {
     String requet = "insert int abonnement (id, nomService, montantMensuel, dateDebut, dateFin, statut, typeAbonnenment) VALUES (?, ?, ?, ?, ?, ?, ?)";
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

}
