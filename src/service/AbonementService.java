package service;
import dao.AbonnementDao;
import model.entity.Abonnement;
import model.entity.AbonnementAvecEngagement;
import model.entity.AbonnementSansEngagement;
import model.enums.StatusAbonnement;
import util.Helper;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AbonementService {
    private final AbonnementDao abonnementDao ;
    public AbonementService(AbonnementDao abonnementDao) { this.abonnementDao = abonnementDao; }

    public Abonnement addAbonnementAvecEngagement(
            String nomService, String montantStr, String dateDebutStr, String dureeStr
    ) {
        try {
            double montant = Double.parseDouble(montantStr);
            LocalDate dateDebut = LocalDate.parse(dateDebutStr);
            int duree = Integer.parseInt(dureeStr);
            String id = Helper.generateAbonnementId();
            LocalDate dateFin = dateDebut.plusMonths(duree);
            AbonnementAvecEngagement abnEngag = new AbonnementAvecEngagement(
                    id, nomService, montant, dateDebut, dateFin, StatusAbonnement.ACTIVE, duree
            );
            abonnementDao.create(abnEngag);
            return abnEngag;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Montant ou durée non valide.");
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Format de date invalide. Utilisez AAAA-MM-JJ.");
        } catch (Exception e) {
            throw new IllegalArgumentException("Erreur : " + e.getMessage());
        }
    }

    public Abonnement addAbonnementSansEngagement(
            String nomService, String montantStr, String dateDebutStr, String dateFinStr
    ) {
        try {
            double montant = Double.parseDouble(montantStr);
            LocalDate dateDebut = LocalDate.parse(dateDebutStr);
            LocalDate dateFin = null;
            if (dateFinStr != null && !dateFinStr.isEmpty()) {
                dateFin = LocalDate.parse(dateFinStr);
            }
            String id = Helper.generateAbonnementId();
            AbonnementSansEngagement abnSans = new AbonnementSansEngagement(
                    id, nomService, montant, dateDebut, dateFin, StatusAbonnement.ACTIVE
            );
            abonnementDao.create(abnSans);
            return abnSans;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Montant non valide.");
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Format de date invalide. Utilisez AAAA-MM-JJ.");
        } catch (Exception e) {
            throw new IllegalArgumentException("Erreur : " + e.getMessage());
        }
    }

    public void updateAbonnement(Abonnement abn, String newNom, String montantStr) {
        try {
            if (newNom != null && !newNom.isEmpty()) abn.setNomService(newNom);
            if (montantStr != null && !montantStr.isEmpty()) {
                double montant = Double.parseDouble(montantStr);
                abn.setMontantMensuel(montant);
            }
            abonnementDao.update(abn);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Montant non valide.");
        } catch (Exception e) {
            throw new IllegalArgumentException("Erreur : " + e.getMessage());
        }
    }
    public void deleteAbonnement(Abonnement abonnement) {
        abonnementDao.delete(abonnement.getId());
    }
    public Optional<Abonnement> getById(String id){return abonnementDao.findById(id);}
    public List<Abonnement> getAll(){return abonnementDao.findAll();}
    public List<Abonnement> getByActiveAbonnement(){
        return abonnementDao.findAll().stream()
                .filter(abn -> abn.getStatut().equals(StatusAbonnement.ACTIVE)).collect(Collectors.toList());
    }
    public List<Abonnement> getByTypeAbonnement(String typeAbonnement){
        return abonnementDao.findAll().stream()
                .filter(abn ->  abn.getClass().getSimpleName().equals(typeAbonnement) ).collect(Collectors.toList());
    }
}