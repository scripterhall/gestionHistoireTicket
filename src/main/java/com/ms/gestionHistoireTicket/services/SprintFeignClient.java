package com.ms.gestionHistoireTicket.services;

import com.ms.gestionHistoireTicket.entities.Sprint;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("gestion-sprints-service")
public interface SprintFeignClient {
    
    @GetMapping("/sprints/{id-sprint}")
    public Sprint findById(@PathVariable("id-sprint") Long id );
}
