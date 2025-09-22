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