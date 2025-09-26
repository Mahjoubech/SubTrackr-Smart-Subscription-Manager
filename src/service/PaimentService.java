package service;

import dao.PaiementDao;
import model.entity.Paiement;

import java.util.List;
import java.util.Optional;

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

}
