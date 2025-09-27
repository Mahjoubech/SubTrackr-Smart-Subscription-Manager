package service;

import dao.PaiementDao;
import model.entity.Paiement;
import model.enums.StatusPaiement;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PaimentService {
    private final PaiementDao paiementDao;
    public PaimentService(PaiementDao paiementDao) {this.paiementDao = paiementDao;}

    public Paiement addPaiement(String idPaiement, String idAbonnement, String dateEcheanceStr, String datePaiementStr, String typePaiement, String statutStr) {
        try {
            LocalDate dateEcheance = LocalDate.parse(dateEcheanceStr);
            LocalDate datePaiement = LocalDate.parse(datePaiementStr);
            StatusPaiement statut = StatusPaiement.valueOf(statutStr);
            Paiement paiement = new Paiement(idPaiement, idAbonnement, dateEcheance, datePaiement, typePaiement, statut);
            paiementDao.create(paiement);
            return paiement;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Format de date invalide. Utilisez AAAA-MM-JJ.");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Statut de paiement non valide.");
        } catch (Exception e) {
            throw new IllegalArgumentException("Erreur lors de la création du paiement : " + e.getMessage());
        }
    }

    public void updatePaiement(Paiement paiement, String newTypePaiement, String newStatutStr, String newDatePaiementStr, String newDateEcheanceStr) {
        try {
            if (newTypePaiement != null && !newTypePaiement.isEmpty())
                paiement.setTypePaiement(newTypePaiement);
            if (newStatutStr != null && !newStatutStr.isEmpty())
                paiement.setStatut(StatusPaiement.valueOf(newStatutStr));
            if (newDatePaiementStr != null && !newDatePaiementStr.isEmpty())
                paiement.setDatePaiement(LocalDate.parse(newDatePaiementStr));
            if (newDateEcheanceStr != null && !newDateEcheanceStr.isEmpty())
                paiement.setDateEcheance(LocalDate.parse(newDateEcheanceStr));
            paiementDao.update(paiement);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Format de date invalide.");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Statut de paiement non valide.");
        } catch (Exception e) {
            throw new IllegalArgumentException("Erreur lors de la modification du paiement : " + e.getMessage());
        }
    }

    public void deletePaiement(Paiement paiement) {
        paiementDao.delete(paiement.getIdPaiement());
    }

    public Optional<Paiement> findPaiementById(String idPaiement) {
        return paiementDao.findById(idPaiement);
    }

    public List<Paiement> findAllPaiements() {
        return paiementDao.findAll();
    }

    public List<Paiement> findByAbonnement(String idAbonnement){
        return paiementDao.findAll().stream()
                .filter(p -> p.getIdAbonnement().equals(idAbonnement))
                .collect(Collectors.toList());
    }

    public List<Paiement> findUnpaidByAbonnement(String idAbonnement) {
        return paiementDao.findAll().stream()
                .filter(p -> p.getIdAbonnement().equals(idAbonnement) && p.getStatut() != StatusPaiement.PAYE)
                .collect(Collectors.toList());
    }

    public List<Paiement> findLastPayments(){
        return paiementDao.findAll().stream()
                .sorted((p1 , p2) -> p2.getDatePaiement().compareTo(p1.getDatePaiement()))
                .collect(Collectors.toList());
    }

}