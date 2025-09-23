create database Abonnment ;
use Abonnment;

create table Abonnement (
    id VARCHAR(36) PRIMARY KEY,
    nomService VARCHAR(100) NOT NULL,
    montantMensuel DOUBLE NOT NULL,
    dateDebut DATE NOT NULL,
    dateFin DATE,
    statut ENUM('ACTIVE', 'SUSPENDU', 'RESILIE') NOT NULL
);

create table abonnement_avec_engagement (
    id VARCHAR(36) PRIMARY KEY,
    dureeEngagementMois INT NOT NULL,

    FOREIGN KEY (id) REFERENCES abonnement(id) ON DELETE CASCADE
);
create table abonnement_sans_engagement (
    id VARCHAR(36) PRIMARY KEY,

    FOREIGN KEY (id) REFERENCES abonnement(id) ON DELETE CASCADE
);

create table paiement (
    idPaiement VARCHAR(36) PRIMARY KEY,
    idAbonnement VARCHAR(36) NOT NULL,
    dateEcheance DATE NOT NULL,
    datePaiement DATE,
    typePaiement VARCHAR(50) NOT NULL,
    statut ENUM('PAYE', 'NON_PAYE', 'EN_RETARD') NOT NULL,
    FOREIGN KEY (idAbonnement) REFERENCES abonnement(id) ON DELETE CASCADE
);

alter table abonnement
add column typeAbonnenment enum('AVEC_ENGAG' , 'SANS_ENGAG') not null default 'SANS_ENGAG';