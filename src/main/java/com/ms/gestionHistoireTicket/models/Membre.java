package com.ms.gestionHistoireTicket.models;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@ToString
public class Membre extends Personne{
    private Long id ;

    @Enumerated(EnumType.STRING)
    private MembreStatus status;
}
