package com.ms.gestionHistoireTicket.gestionHistoireTicketService.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Sprint {
    private Long id;
    private Date dateLancement;
    private Date dateFin;
    private String objectif;
    private int velocite;
    private String etat;
    private ProductBacklog productBacklog;
    private Long productBacklogId;
}
