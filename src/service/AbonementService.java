package service;
import dao.AbonnementDao;
import model.entity.Abonnement;
import model.enums.StatusAbonnement;
import java.util.stream.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
public class AbonementService {
    private final AbonnementDao abonnementDao ;
    public AbonementService(AbonnementDao abonnementDao) { this.abonnementDao = abonnementDao; }
    public void addAbonnement(Abonnement abonnement) {abonnementDao.create(abonnement);}
    public void updteAbonnement(Abonnement abonnement) {abonnementDao.update(abonnement);}
    public void deleteAbonnement(Abonnement abonnement) {abonnementDao.delete(abonnement.getId());}
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
