package com.ms.gestionHistoireTicket.gestionHistoireTicketService.entities;

import com.ms.gestionHistoireTicket.gestionHistoireTicketService.models.Membre;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;


@Entity
@Table(name="histoire_ticket")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class HistoireTicket extends Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String priorite;

    @Enumerated(EnumType.STRING) 
    private TicketHistoireStatus status;
    private int effort;
    private Long productBacklogId;
    @Transient
    private ProductBacklog productBacklog;
    private int position;
    private Long membreId;
    @Transient
    private Membre membre;
    private Long sprintId;
    @Transient
    private Sprint sprint;
    private Date dateDebut;
    private Date dateFin;
}
