package com.ms.gestionHistoireTicket.services;

import com.ms.gestionHistoireTicket.models.Membre;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="membre-service")
public interface MembreService {
    @GetMapping("/membres/{id}?projection=fullMembre")
    public Membre findMembreById(@PathVariable("id") Long id);
}
