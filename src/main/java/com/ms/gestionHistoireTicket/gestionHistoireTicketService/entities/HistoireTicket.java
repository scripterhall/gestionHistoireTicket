package com.ms.gestionHistoireTicket.gestionHistoireTicketService.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ms.gestionHistoireTicket.gestionHistoireTicketService.models.Membre;
import com.ms.gestionHistoireTicket.gestionHistoireTicketService.models.Sprint;
import jakarta.persistence.*;
import lombok.*;
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
    private int effort;
    @Column(nullable = true,name = "productBacklogId")
    private Long productBacklogId;
    @Transient
    private ProductBacklog productBacklog;
    private int position;
    private Long membreId;
    @Transient
    private Membre membre;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long sprintId;
    @Transient
    private Sprint sprint;
}
