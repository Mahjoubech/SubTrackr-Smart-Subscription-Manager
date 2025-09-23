//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
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
    }
}