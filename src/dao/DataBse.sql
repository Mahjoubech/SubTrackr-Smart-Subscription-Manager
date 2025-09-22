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