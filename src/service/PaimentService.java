package service;

import dao.PaiementDao;
import model.entity.Paiement;
import model.enums.StatusPaiement;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PaimentService {
    private final PaiementDao paiementDao;
    public PaimentService(PaiementDao paiementDao) {this.paiementDao = paiementDao;}
    public void addPaiment(Paiement p){
        paiementDao.create(p);
    }
    public void updatePaiment(Paiement p){
        paiementDao.update(p);
    }
    public void deletePaiment(Paiement p){
        paiementDao.delete(p.getIdPaiement());
    }
    public Optional<Paiement> findPaiementById(String idPaiement) {
       return  paiementDao.findById(idPaiement);
    }
    public List<Paiement> findAllPaiements() {
        return paiementDao.findAll();
    }
    public List<Paiement> findByAbonnement(String idAbonnement){
        return paiementDao.findAll().stream()
                .filter(p -> p.getIdAbonnement().equals(idAbonnement))
                .collect(Collectors.toList());
    }
//    public List<Paiement> findByDateEcheance(String dateEcheance) {
//        return paiementDao.findAll().stream()
//                .filter(p -> p.getIdAbonnement().equals(id))
//    }
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
//    public List<Paiement> findPaiementByDate(String date) {
//        return
//    }

}
