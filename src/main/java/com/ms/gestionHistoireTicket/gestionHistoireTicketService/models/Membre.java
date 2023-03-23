package com.ms.gestionHistoireTicket.gestionHistoireTicketService.models;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Membre {
    private Long id ;

    @Enumerated(EnumType.STRING)
    private MembreStatus status;
}
