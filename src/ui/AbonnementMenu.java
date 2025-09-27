package ui;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import model.entity.Abonnement;
import model.entity.Paiement;
import service.AbonementService;
import service.PaimentService;

public class AbonnementMenu {
    private static AbonementService abonementService;
    private static PaimentService paiementService;

    public static void initService(AbonementService abnSrv) {
        abonementService = abnSrv;
    }

    public static void initPaiementService(PaimentService paySrv) {
        paiementService = paySrv;
    }

    public static void affiche() {
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("╔════════════════════════════════════╗");
            System.out.println("║        === Menu Abonnement ===     ║");
            System.out.println("╠════════════════════════════════════╣");
            System.out.println("║ 1. Créer un abonnement             ║");
            System.out.println("║ 2. Modifier un abonnement          ║");
            System.out.println("║ 3. Supprimer un abonnement         ║");
            System.out.println("║ 4. Consulter la liste des abonn.   ║");
            System.out.println("║ 5. Afficher la somme payée         ║");
            System.out.println("║    d'un abonnement                 ║");
            System.out.println("║ 6. Présenter un abonnement         ║");
            System.out.println("║ 0. Quitter                         ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.print("Choisissez une option : ");

            String choixStr = sc.nextLine();
            switch(choixStr){
                case "1":
                    System.out.println("Choisissez un type d'abonnement :");
                    System.out.println(" 1 - Avec Engagement     2 - Sans Engagement");
                    String choix2 = sc.nextLine();
                    if(choix2.equals("1")){
                        addAbonnementAvecEngagement();
                    } else if(choix2.equals("2")){
                        addAbonnementSansEngagement();
                    } else {
                        System.out.println("Option invalide.");
                    }
                    break;
                case "2":
                    updateAbonnement();
                    break;
                case "3":
                    deleteAbonnement();
                    break;
                case "4":
                    afficherListeAbonnements();
                    break;
                case "5":
                    afficherSommePayeeAbonnement();
                    break;
                case "0": return;
                default:
                    System.out.println("Option invalide.");
            }
        }
    }

    public static void addAbonnementAvecEngagement() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Nom du service :");
        String nomService = sc.nextLine();

        System.out.println("Montant mensuel :");
        String montantStr = sc.nextLine();

        System.out.println("Date début (AAAA-MM-JJ) :");
        String dateDebutStr = sc.nextLine();

        System.out.println("Durée engagement (mois) :");
        String dureeStr = sc.nextLine();

        try {
            Abonnement abnEngag = abonementService.addAbonnementAvecEngagement(
                    nomService, montantStr, dateDebutStr, dureeStr
            );
            System.out.println("Abonnement avec engagement créé ! (ID: " + abnEngag.getId() + ")");
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    public static void addAbonnementSansEngagement() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Nom du service :");
        String nomService = sc.nextLine();

        System.out.println("Montant mensuel :");
        String montantStr = sc.nextLine();

        System.out.println("Date début (AAAA-MM-JJ) :");
        String dateDebutStr = sc.nextLine();

        System.out.println("Date fin (AAAA-MM-JJ) ou vide pour abonnement sans fin :");
        String dateFinStr = sc.nextLine();

        try {
            Abonnement abnSans = abonementService.addAbonnementSansEngagement(
                    nomService, montantStr, dateDebutStr, dateFinStr
            );
            System.out.println("Abonnement sans engagement créé ! (ID: " + abnSans.getId() + ")");
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    public static void afficherListeAbonnements() {
        List<Abonnement> abonnements = abonementService.getAll();
        if (abonnements.isEmpty()) {
            System.out.println("Aucun abonnement trouvé.");
            return;
        }
        System.out.printf("%-3s | %-36s | %-18s | %-10s | %-10s | %-10s | %-10s\n",
                "#", "ID", "Nom Service", "Montant", "Début", "Fin", "Statut");
        System.out.println("-----------------------------------------------------------------------------------------------");
        int i = 1;
        for (Abonnement ab : abonnements) {
            System.out.printf("%-3d | %-36s | %-18s | %-10.2f | %-10s | %-10s | %-10s\n",
                    i, ab.getId(), ab.getNomService(), ab.getMontantMensuel(),
                    ab.getDateDebut(), ab.getDateFin(), ab.getStatut());
            i++;
        }
    }

    public static void updateAbonnement() {
        List<Abonnement> abonnements = abonementService.getAll();
        if (abonnements.isEmpty()) {
            System.out.println("Aucun abonnement à modifier.");
            return;
        }
        afficherListeAbonnements();
        Scanner sc = new Scanner(System.in);
        System.out.println("Entrez le numéro du service à modifier :");
        String numStr = sc.nextLine();
        int num;
        try {
            num = Integer.parseInt(numStr);
        } catch (NumberFormatException e) {
            System.out.println("Erreur de saisie : veuillez entrer un numéro.");
            return;
        }
        if (num < 1 || num > abonnements.size()) {
            System.out.println("Numéro invalide.");
            return;
        }
        Abonnement abn = abonnements.get(num - 1);
        System.out.println("Modification de l'abonnement : " + abn.getNomService());
        System.out.println("Nouveau nom du service (laisser vide pour inchangé) :");
        String newNom = sc.nextLine();
        System.out.println("Nouveau montant (laisser vide pour inchangé) :");
        String montantStr = sc.nextLine();

        try {
            abonementService.updateAbonnement(abn, newNom, montantStr);
            System.out.println("Abonnement mis à jour !");
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    public static void deleteAbonnement() {
        List<Abonnement> abonnements = abonementService.getAll();
        if (abonnements.isEmpty()) {
            System.out.println("Aucun abonnement à supprimer.");
            return;
        }
        afficherListeAbonnements();
        Scanner sc = new Scanner(System.in);
        System.out.println("Entrez le numéro du service à supprimer :");
        String numStr = sc.nextLine();
        int num;
        try {
            num = Integer.parseInt(numStr);
        } catch (NumberFormatException e) {
            System.out.println("Erreur de saisie : veuillez entrer un numéro.");
            return;
        }
        if (num < 1 || num > abonnements.size()) {
            System.out.println("Numéro invalide.");
            return;
        }
        Abonnement abn = abonnements.get(num - 1);
        abonementService.deleteAbonnement(abn);
        System.out.println("Abonnement supprimé !");
    }

    public static void afficherSommePayeeAbonnement() {
        List<Abonnement> abonnements = abonementService.getAll();
        if (abonnements.isEmpty()) {
            System.out.println("Aucun abonnement trouvé.");
            return;
        }
        afficherListeAbonnements();
        Scanner sc = new Scanner(System.in);
        System.out.println("Entrez le numéro de l'abonnement :");
        String numStr = sc.nextLine();
        int num;
        try {
            num = Integer.parseInt(numStr);
        } catch (NumberFormatException e) {
            System.out.println("Erreur de saisie : veuillez entrer un numéro.");
            return;
        }
        if (num < 1 || num > abonnements.size()) {
            System.out.println("Numéro invalide.");
            return;
        }
        Abonnement abn = abonnements.get(num - 1);
        String idAbonnement = abn.getId();
        List<Paiement> paiements = paiementService.findByAbonnement(idAbonnement);
        int nbPaiementsPayes = 0;
        for (Paiement p : paiements) {
            if (p.getStatut() == model.enums.StatusPaiement.PAYE) {
                nbPaiementsPayes++;
            }
        }
        double montantMensuel = abn.getMontantMensuel();
        double total = nbPaiementsPayes * montantMensuel;

        System.out.println("Nombre de paiements PAYE : " + nbPaiementsPayes);
        System.out.println("Montant mensuel abonnement : " + montantMensuel + " Dh");
        System.out.println("Estimation somme totale payée : " + total + " Dh");
    }

}