package model.entity;
import model.enums.StatusAbonnement;
import java.time.LocalDate;

public class AbonnementAvecEngagement extends Abonnement{
    private int dureeEngagementMois;

    public AbonnementAvecEngagement(String nomService, double montantMensuel, LocalDate dateDebut, LocalDate dateFin, StatusAbonnement statut, int dureeEngagementMois) {
        super(nomService, montantMensuel, dateDebut, dateFin, statut);
        this.dureeEngagementMois = dureeEngagementMois;
    }

}
