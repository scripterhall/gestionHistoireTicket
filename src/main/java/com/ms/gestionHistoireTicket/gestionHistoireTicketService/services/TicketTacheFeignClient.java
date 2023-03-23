package com.ms.gestionHistoireTicket.gestionHistoireTicketService.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="ticket-tache-service")
public interface TicketTacheFeignClient  {

    @DeleteMapping("/ticket-taches/ticket-histoire/{id}")
    public void deleteAllByTicketHistoireId(@PathVariable("id") Long id);
    
}
