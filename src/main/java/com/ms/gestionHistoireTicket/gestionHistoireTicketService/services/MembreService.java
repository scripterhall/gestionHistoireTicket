package com.ms.gestionHistoireTicket.gestionHistoireTicketService.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ms.gestionHistoireTicket.gestionHistoireTicketService.models.Membre;

@FeignClient(name="membre-service")
public interface MembreService {
    @GetMapping("/membres/{id}?projection=fullMembre")
    public Membre findMembreById(@PathVariable("id") Long id);
}
