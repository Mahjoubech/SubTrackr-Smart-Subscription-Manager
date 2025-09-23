//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import dao.AbonnementDao;
import model.entity.AbonnementAvecEngagement;
import util.DbConn;
public class Main {
    public static void main(String[] args) {
        try {
            DbConn db = DbConn.getInstance();
            if (db.getConn() != null && !db.getConn().isClosed()) {
                System.out.println("Connexion réussie !");
            } else {
                System.out.println("Échec de la connexion.");
            }
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
        AbonnementDao dao = new AbonnementDao();
        AbonnementAvecEngagement ab = new AbonnementAvecEngagement();
        ab.setNom("Test Abonnement");
        dao.create(ab);
        // Affiche la liste pour vérifier l’ajout
        System.out.println("Liste des abonnements : " + dao.getAbonnements());
    }
    }

