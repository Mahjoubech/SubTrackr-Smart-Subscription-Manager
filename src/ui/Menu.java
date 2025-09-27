package ui;
import java.util.Scanner ;
import model.entity.AbonnementAvecEngagement;
import service.AbonementService;

public class Menu {
    public static void afiche() {
        while (true) {

            System.out.println("╔════════════════════════════════════╗");
            System.out.println("║        === Sub for live ===        ║");
            System.out.println("╠════════════════════════════════════╣");
            System.out.println("║ 1. Gestion des abonnements         ║");
            System.out.println("║ 2. Gestion des paiements           ║");
            System.out.println("║ 3. Rapports financiers             ║");
            System.out.println("║ 0. Quitter                         ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.println("Choisissez une option : ");
            Scanner sc = new Scanner(System.in);
            int choix = sc.nextInt();
            switch (choix) {
                case 1:
                    try {
                        System.out.println("Entrer Mot de passe :");
                        int motps = Integer.parseInt(new Scanner(System.in).nextLine());
                        if (motps == 1230) {
                            AbonnementMenu.affiche();
                        } else {
                            System.out.println("Mot de passe incorrect. Accès refusé.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Mot de passe doit être au format entier.");
                    }

                    break;
                case 2:
                    PaiementMenu.affiche();
                    break;
                case 3:
                    RapportMenu.affiche();
                    break;
                case 0:
                    System.out.println("Au revoir !");
                    return;
                default:
                    System.out.println("Option invalide, veuillez réessayer.");
            }
        }
    }



}
