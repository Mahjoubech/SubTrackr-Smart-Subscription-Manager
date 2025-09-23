package model.entity;
import model.enums.StatusAbonnement;
import java.time.LocalDate;

public class AbonnementSansEngagement extends Abonnement{

    public AbonnementSansEngagement(String nomService, double montantMensuel, LocalDate dateDebut, LocalDate dateFin, StatusAbonnement statut) {
        super(nomService, montantMensuel, dateDebut, dateFin, statut);
    }
    @Override
    public String toString() {
        return super.toString() + " AbonnementSansEngagement{}";
    }
}
