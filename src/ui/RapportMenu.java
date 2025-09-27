package ui;

import model.entity.Paiement;
import service.AbonementService;
import service.RapportService;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class RapportMenu {
    private static RapportService rapportService;
    private static AbonementService abonementService;

    public static void initServices(RapportService rapSrv, AbonementService abnSrv) {
        rapportService = rapSrv;
        abonementService = abnSrv;
    }

    public static void affiche() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║         === Menu Rapport ===         ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║ 1. Rapport mensuel                   ║");
            System.out.println("║ 2. Rapport annuel                    ║");
            System.out.println("║ 3. Rapport des impayés               ║");
            System.out.println("║ 4. Total payé par mois               ║");
            System.out.println("║ 0. Quitter                           ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Choisissez une option : ");
            String choixStr = sc.nextLine();

            switch (choixStr) {
                case "1":
                    rapportMensuel();
                    break;
                case "2":
                    rapportAnnuel();
                    break;
                case "3":
                    rapportImpayes();
                    break;
                case "4":
                    rapportTotalPayeParMois();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Option invalide.");
            }
        }
    }

    public static void rapportMensuel() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Année à consulter (ex: 2025): ");
        String yearStr = sc.nextLine();
        try {
            Map<YearMonth, Long> rapport = rapportService.getPaiementsCountByMonth(yearStr);
            if (rapport.isEmpty()) {
                System.out.println("Aucun paiement trouvé pour cette année.");
            } else {
                System.out.println("Paiements mensuels:");
                rapport.forEach((ym, count) -> System.out.printf("%s: %d paiements\n", ym, count));
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    public static void rapportAnnuel() {
        Map<Integer, Long> rapport = rapportService.getPaiementsCountByYear();
        if (rapport.isEmpty()) {
            System.out.println("Aucun paiement trouvé.");
        } else {
            System.out.println("Paiements annuels:");
            rapport.forEach((year, count) -> System.out.printf("%d: %d paiements\n", year, count));
        }
    }

    public static void rapportImpayes() {
        List<Paiement> impayes = rapportService.getImpayes();
        if (impayes.isEmpty()) {
            System.out.println("Aucun paiement impayé.");
        } else {
            System.out.println("Paiements impayés:");
            for (Paiement p : impayes) {
                System.out.printf("ID: %s | Abonnement: %s | Date échéance: %s | Statut: %s\n",
                        p.getIdPaiement(), p.getIdAbonnement(), p.getDateEcheance(), p.getStatut());
            }
        }
    }

    public static void rapportTotalPayeParMois() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Année à consulter (ex: 2025): ");
        String yearStr = sc.nextLine();
        try {
            Map<YearMonth, Double> rapport = rapportService.getTotalPaidByMonth(yearStr , abonementService);
            if (rapport.isEmpty()) {
                System.out.println("Aucun paiement trouvé pour cette année.");
            } else {
                System.out.println("Total payé par mois:");
                rapport.forEach((ym, total) -> System.out.printf("%s: %.2f DH\n", ym, total));
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
}