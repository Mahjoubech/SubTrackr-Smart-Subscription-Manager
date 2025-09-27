package ui;
import model.entity.Abonnement;
import model.entity.Paiement;
import model.enums.StatusPaiement;
import service.AbonementService;
import service.PaimentService;
import util.Helper;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class PaiementMenu {
    private static PaimentService paiementService;
    private static AbonementService abonementService;

    public static void initServices(PaimentService paySrv, AbonementService abnSrv) {
        paiementService = paySrv;
        abonementService = abnSrv;
    }

    public static void affiche() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("╔═══════════════════════════════════════╗");
            System.out.println("║        === Menu Paiement ===          ║");
            System.out.println("╠═══════════════════════════════════════╣");
            System.out.println("║ 1. Afficher les paiements             ║");
            System.out.println("║   d'un abonnement                     ║");
            System.out.println("║ 2. Enregistrer un paiement            ║");
            System.out.println("║ 3. Modifier un paiement               ║");
            System.out.println("║ 4. Supprimer un paiement              ║");
            System.out.println("║ 5. Consulter les paiements manqués    ║");
            System.out.println("║   et le total impayé                  ║");
            System.out.println("║ 6. Afficher les 5 derniers paiements  ║");
            System.out.println("║ 0. Quitter                            ║");
            System.out.println("╚═══════════════════════════════════════╝");
            System.out.print("Choisissez une option : ");
            String choixStr = sc.nextLine();

            switch (choixStr) {
                case "1":
                    afficherPaiementsAbonnement();
                    break;
                case "2":
                    enregistrerPaiement();
                    break;
                case "3":
                    modifierPaiement();
                    break;
                case "4":
                    supprimerPaiement();
                    break;
                case "5":
                    consulterPaiementsManques();
                    break;
                case "6":
                    afficherDerniersPaiements();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Option invalide.");
            }
        }
    }

    public static void afficherPaiementsAbonnement() {
        List<Paiement> paiements = paiementService.findAllPaiements();
        if (paiements.isEmpty()) {
            System.out.println("Aucun paiement disponible.");
            return;
        }
        System.out.printf("%-3s | %-18s | %-12s | %-10s\n", "#", "Nom abonnement", "Date paiement", "Statut");
        for (int i = 0; i < paiements.size(); i++) {
            Paiement p = paiements.get(i);
            String nomAbonnement = "Inconnu";
            Optional<Abonnement> abnOpt = abonementService.getById(p.getIdAbonnement());
            if (abnOpt.isPresent()) nomAbonnement = abnOpt.get().getNomService();
            System.out.printf("%-3d | %-18s | %-12s | %-10s\n",
                    i + 1, nomAbonnement,
                    p.getDatePaiement() != null ? p.getDatePaiement() : "null",
                    p.getStatut() != null ? p.getStatut() : "NON_PAYE");
        }
    }

    public static void enregistrerPaiement() {
        List<Abonnement> abonnements = abonementService.getAll();
        if (abonnements.isEmpty()) {
            System.out.println("Aucun abonnement trouvé.");
            return;
        }
        System.out.printf("%-3s | %-18s | %-12s | %-18s\n", "#", "Nom", "Date début", "Type abonnement");
        for (int i = 0; i < abonnements.size(); i++) {
            Abonnement abn = abonnements.get(i);
            String type = (abn.getClass().getSimpleName().equals("AbonnementAvecEngagement")) ? "Avec engagement" : "Sans engagement";
            System.out.printf("%-3d | %-18s | %-12s | %-18s\n",
                    i + 1, abn.getNomService(), abn.getDateDebut(), type);
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("Choisissez le numéro de l'abonnement pour le paiement :");
        String choixStr = sc.nextLine();
        int choix;
        try {
            choix = Integer.parseInt(choixStr);
        } catch (NumberFormatException e) {
            System.out.println("Erreur de saisie.");
            return;
        }
        if (choix < 1 || choix > abonnements.size()) {
            System.out.println("Numéro invalide.");
            return;
        }
        Abonnement abnChoisi = abonnements.get(choix - 1);
        String idAbonnement = abnChoisi.getId();

        System.out.println("Type paiement (ex: CB, Virement, espèces, etc) :");
        String typePaiement = sc.nextLine();

        String datePaiementStr = LocalDate.now().toString();
        String dateEcheanceStr;
        if (abnChoisi.getClass().getSimpleName().equals("AbonnementAvecEngagement")) {
            int duree = ((model.entity.AbonnementAvecEngagement) abnChoisi).getDureeEngagementMois();
            dateEcheanceStr = LocalDate.now().plusMonths(duree).toString();
        } else {
            dateEcheanceStr = LocalDate.now().plusMonths(1).toString();
        }

        System.out.println("Statut paiement (PAYE, NON_PAYE, EN_ATTENTE) :");
        String statutStr = sc.nextLine().trim();
        if (statutStr.isEmpty()) statutStr = "PAYE";

        String idPaiement = Helper.generateAbonnementId();

        try {
            Paiement paiement = paiementService.addPaiement(idPaiement, idAbonnement, dateEcheanceStr, datePaiementStr, typePaiement, statutStr);
            System.out.println("Paiement enregistré pour l'abonnement '" + abnChoisi.getNomService() + "' !");
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    public static void modifierPaiement() {
        List<Paiement> paiements = paiementService.findAllPaiements();
        if (paiements.isEmpty()) {
            System.out.println("Aucun paiement disponible.");
            return;
        }
        System.out.printf("%-3s | %-18s | %-12s | %-10s\n", "#", "Nom abonnement", "Date paiement", "Statut");
        for (int i = 0; i < paiements.size(); i++) {
            Paiement p = paiements.get(i);
            String nomAbonnement = "Inconnu";
            Optional<Abonnement> abnOpt = abonementService.getById(p.getIdAbonnement());
            if (abnOpt.isPresent()) nomAbonnement = abnOpt.get().getNomService();
            System.out.printf("%-3d | %-18s | %-12s | %-10s\n",
                    i + 1, nomAbonnement,
                    p.getDatePaiement() != null ? p.getDatePaiement() : "null",
                    p.getStatut() != null ? p.getStatut() : "NON_PAYE");
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("Choisissez le numéro du paiement à modifier :");
        String choixStr = sc.nextLine();
        int choix;
        try {
            choix = Integer.parseInt(choixStr);
        } catch (NumberFormatException e) {
            System.out.println("Erreur de saisie.");
            return;
        }
        if (choix < 1 || choix > paiements.size()) {
            System.out.println("Numéro invalide.");
            return;
        }
        Paiement paiement = paiements.get(choix - 1);

        System.out.println("Nouveau type paiement (vide pour inchangé) :");
        String newTypePaiement = sc.nextLine();

        System.out.println("Nouveau statut (PAYE, NON_PAYE, EN_ATTENTE, vide pour inchangé) :");
        String newStatutStr = sc.nextLine();

        String newDatePaiementStr = "";
        String newDateEcheanceStr = "";

        // Optionally ask for new dates (not required)
        System.out.println("Nouvelle date paiement (AAAA-MM-JJ, vide pour inchangé) :");
        newDatePaiementStr = sc.nextLine();

        System.out.println("Nouvelle date échéance (AAAA-MM-JJ, vide pour inchangé) :");
        newDateEcheanceStr = sc.nextLine();

        try {
            paiementService.updatePaiement(paiement, newTypePaiement, newStatutStr, newDatePaiementStr, newDateEcheanceStr);
            System.out.println("Paiement mis à jour !");
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    public static void supprimerPaiement() {
        List<Paiement> paiements = paiementService.findAllPaiements();
        if (paiements.isEmpty()) {
            System.out.println("Aucun paiement disponible.");
            return;
        }
        System.out.printf("%-3s | %-30s | %-12s | %-12s | %-10s | %-10s\n", "#", "ID Paiement", "Date Paiement", "Date Échéance", "Type", "Statut");
        for (int i = 0; i < paiements.size(); i++) {
            Paiement p = paiements.get(i);
            System.out.printf("%-3d | %-30s | %-12s | %-12s | %-10s | %-10s\n",
                    i + 1, p.getIdPaiement(),
                    p.getDatePaiement() != null ? p.getDatePaiement() : "null",
                    p.getDateEcheance() != null ? p.getDateEcheance() : "null",
                    p.getTypePaiement() != null ? p.getTypePaiement() : "null",
                    p.getStatut() != null ? p.getStatut() : "null");
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("Choisissez le numéro du paiement à supprimer :");
        String choixStr = sc.nextLine();
        int choix;
        try {
            choix = Integer.parseInt(choixStr);
        } catch (NumberFormatException e) {
            System.out.println("Erreur de saisie.");
            return;
        }
        if (choix < 1 || choix > paiements.size()) {
            System.out.println("Numéro invalide.");
            return;
        }
        Paiement paiement = paiements.get(choix - 1);

        paiementService.deletePaiement(paiement);
        System.out.println("Paiement supprimé !");
    }

    public static void consulterPaiementsManques() {
        List<Abonnement> abonnements = abonementService.getAll();
        if (abonnements.isEmpty()) {
            System.out.println("Aucun abonnement trouvé.");
            return;
        }
        System.out.println("Choisissez un abonnement :");
        for (int i = 0; i < abonnements.size(); i++) {
            Abonnement abn = abonnements.get(i);
            String type = abn.getClass().getSimpleName().equals("AbonnementAvecEngagement") ? "Avec engagement"
                    : abn.getClass().getSimpleName().equals("AbonnementSansEngagement") ? "Sans engagement"
                    : "Inconnu";
            System.out.printf("%-3d | %-18s | %-12s | %-18s\n",
                    i + 1, abn.getNomService(), abn.getDateDebut(), type);
        }
        Scanner sc = new Scanner(System.in);
        String choixStr = sc.nextLine();
        int choix;
        try {
            choix = Integer.parseInt(choixStr);
        } catch (NumberFormatException e) {
            System.out.println("Erreur de saisie.");
            return;
        }
        if (choix < 1 || choix > abonnements.size()) {
            System.out.println("Numéro invalide.");
            return;
        }
        Abonnement abnChoisi = abonnements.get(choix - 1);
        String idAbonnement = abnChoisi.getId();

        List<Paiement> impayes = paiementService.findUnpaidByAbonnement(idAbonnement);
        if (impayes.isEmpty()) {
            System.out.println("Aucun paiement manqué pour cet abonnement.");
            return;
        }
        System.out.printf("\nPaiements manqués pour '%s':\n", abnChoisi.getNomService());
        System.out.printf("%-3s | %-12s | %-10s | %-10s\n", "#", "Date échéance", "Type", "Statut");
        for (int i = 0; i < impayes.size(); i++) {
            Paiement p = impayes.get(i);
            System.out.printf("%-3d | %-12s | %-10s | %-10s\n",
                    i + 1,
                    p.getDateEcheance() != null ? p.getDateEcheance() : "null",
                    p.getTypePaiement() != null ? p.getTypePaiement() : "null",
                    p.getStatut() != null ? p.getStatut() : "NON_PAYE");
        }
        System.out.println("Montant total impayé : N/A (pas de montant dans Paiement)");
    }

    public static void afficherDerniersPaiements() {
        List<Paiement> paiements = paiementService.findLastPayments();
        if (paiements.isEmpty()) {
            System.out.println("Aucun paiement trouvé.");
            return;
        }
        System.out.println("Les 5 derniers paiements :");
        System.out.printf("%-3s | %-18s | %-12s | %-10s | %-10s\n", "#", "Nom abonnement", "Date paiement", "Type", "Statut");
        for (int i = 0; i < Math.min(5, paiements.size()); i++) {
            Paiement p = paiements.get(i);
            String nomAbonnement = "Inconnu";
            Optional<Abonnement> abnOpt = abonementService.getById(p.getIdAbonnement());
            if (abnOpt.isPresent()) nomAbonnement = abnOpt.get().getNomService();
            System.out.printf("%-3d | %-18s | %-12s | %-10s | %-10s\n",
                    i + 1,
                    nomAbonnement,
                    p.getDatePaiement() != null ? p.getDatePaiement() : "null",
                    p.getTypePaiement() != null ? p.getTypePaiement() : "null",
                    p.getStatut() != null ? p.getStatut() : "NON_PAYE");
        }
    }
}