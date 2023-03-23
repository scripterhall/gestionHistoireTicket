package com.ms.gestionHistoireTicket.gestionHistoireTicketService.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ms.gestionHistoireTicket.gestionHistoireTicketService.entities.Sprint;

@FeignClient("gestion-sprints-service")
public interface SprintFeignClient {
    
    @GetMapping("/sprints/{id-sprint}")
    public Sprint findById(@PathVariable("id-sprint") Long id );
}
