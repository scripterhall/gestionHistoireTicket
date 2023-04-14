package com.ms.gestionHistoireTicket.models;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Personne {
    private String nom ;
    private String prenom;
    private String adresse ;
    private String telephone ;
    private String email ;

    private String pwd ;
    private Date dateInscription ;

    private String username;
}