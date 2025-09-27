package model.entity;
import model.enums.StatusPaiement;
import java.time.LocalDate;
import java.util.UUID;
public class Paiement {
    private String idPaiement;
    private String idAbonnement;
    private LocalDate dateEcheance;
    private LocalDate datePaiement;
    private String typePaiement;
    private StatusPaiement statut;

    public Paiement( String idPaiement , String idAbonnement, LocalDate dateEcheance, LocalDate datePaiement, String typePaiement, StatusPaiement statut) {
        this.idPaiement = idPaiement;
        this.idAbonnement = idAbonnement;
        this.dateEcheance = dateEcheance;
        this.datePaiement = datePaiement;
        this.typePaiement = typePaiement;
        this.statut = statut;
    }
    public String getIdPaiement() { return idPaiement; }
    public String getIdAbonnement() { return idAbonnement; }
    public void setIdAbonnement(String idAbonnement) { this.idAbonnement = idAbonnement; }

    public LocalDate getDateEcheance() { return dateEcheance; }
    public void setDateEcheance(LocalDate dateEcheance) { this.dateEcheance = dateEcheance; }

    public LocalDate getDatePaiement() { return datePaiement; }
    public void setDatePaiement(LocalDate datePaiement) { this.datePaiement = datePaiement; }

    public String getTypePaiement() { return typePaiement; }
    public void setTypePaiement(String typePaiement) { this.typePaiement = typePaiement; }

    public StatusPaiement getStatut() { return statut; }
    public void setStatut(StatusPaiement statut) { this.statut = statut; }


    @Override
    public String toString() {
        return "Paiement{" +
                "idPaiement='" + idPaiement + '\'' +
                ", idAbonnement='" + idAbonnement + '\'' +
                ", dateEcheance=" + dateEcheance +
                ", datePaiement=" + datePaiement +
                ", typePaiement='" + typePaiement + '\'' +
                ", statut=" + statut +
                '}';
    }
}
