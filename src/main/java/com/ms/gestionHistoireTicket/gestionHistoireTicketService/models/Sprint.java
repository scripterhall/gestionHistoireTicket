package com.ms.gestionHistoireTicket.gestionHistoireTicketService.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Sprint {
    private Long id;
    private Date dateLancement;
    private Date dateFin;
    private String objectif;
    private int velocite;
    private String etat;
}
