package service;

import model.entity.Abonnement;
import model.entity.Paiement;
import model.enums.StatusPaiement;
import dao.PaiementDao;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class RapportService {
    private final PaiementDao paiementDao;

    public RapportService(PaiementDao paiementDao) {
        this.paiementDao = paiementDao;
    }

    // Rapport mensuel: total des paiements par mois
    public Map<YearMonth, Long> getPaiementsCountByMonth(String yearStr) {
        int year;
        try {
            year = Integer.parseInt(yearStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Année invalide.");
        }
        return paiementDao.findAll().stream()
                .filter(p -> p.getDatePaiement() != null && p.getDatePaiement().getYear() == year)
                .collect(Collectors.groupingBy(
                        p -> YearMonth.of(p.getDatePaiement().getYear(), p.getDatePaiement().getMonth()),
                        Collectors.counting()
                ));
    }

    // Rapport annuel: total des paiements par année
    public Map<Integer, Long> getPaiementsCountByYear() {
        return paiementDao.findAll().stream()
                .filter(p -> p.getDatePaiement() != null)
                .collect(Collectors.groupingBy(
                        p -> p.getDatePaiement().getYear(),
                        Collectors.counting()
                ));
    }

    // Rapport des impayés: liste des paiements non payés
    public List<Paiement> getImpayes() {
        return paiementDao.findAll().stream()
                .filter(p -> p.getStatut() != StatusPaiement.PAYE)
                .collect(Collectors.toList());
    }

    // Rapport total payé par mois
    public Map<YearMonth, Double> getTotalPaidByMonth(String yearStr, AbonementService abonementService) {
        int year;
        try {
            year = Integer.parseInt(yearStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Année invalide.");
        }
        return paiementDao.findAll().stream()
                .filter(p -> p.getDatePaiement() != null
                        && p.getDatePaiement().getYear() == year
                        && p.getStatut() == StatusPaiement.PAYE)
                .collect(Collectors.groupingBy(
                        p -> YearMonth.of(p.getDatePaiement().getYear(), p.getDatePaiement().getMonth()),
                        Collectors.summingDouble(p -> {
                            // Get montantMensuel from linked Abonnement
                            Optional<Abonnement> abnOpt = abonementService.getById(p.getIdAbonnement());
                            return abnOpt.map(Abonnement::getMontantMensuel).orElse(0.0);
                        })
                ));
    }}