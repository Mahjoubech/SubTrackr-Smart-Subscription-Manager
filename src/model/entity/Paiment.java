package model.entity;
import model.enums.StatusPaiement;
import java.time.LocalDate;
import java.util.UUID;

public class Paiment {
    private String idPaiement;
    private String idAbonnement;
    private LocalDate dateEcheance;
    private LocalDate datePaiement;
    private String typePaiement;
    private StatusPaiement statut;

    public Paiment(String idAbonnement, LocalDate dateEcheance, LocalDate datePaiement, String typePaiement, StatusPaiement statut) {
        this.idPaiement = UUID.randomUUID().toString();
        this.idAbonnement = idAbonnement;
        this.dateEcheance = dateEcheance;
        this.datePaiement = datePaiement;
        this.typePaiement = typePaiement;
        this.statut = statut;
    }
}
