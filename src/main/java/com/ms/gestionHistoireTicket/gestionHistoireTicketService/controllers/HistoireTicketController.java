package com.ms.gestionHistoireTicket.gestionHistoireTicketService.controllers;

import com.ms.gestionHistoireTicket.gestionHistoireTicketService.entities.HistoireTicket;
import com.ms.gestionHistoireTicket.gestionHistoireTicketService.entities.ProductBacklog;
import com.ms.gestionHistoireTicket.gestionHistoireTicketService.entities.Sprint;
import com.ms.gestionHistoireTicket.gestionHistoireTicketService.repositories.HistoireTicketRepository;
import com.ms.gestionHistoireTicket.gestionHistoireTicketService.services.HistoireTicketService;
import com.ms.gestionHistoireTicket.gestionHistoireTicketService.services.ProductBacklogService;
import com.ms.gestionHistoireTicket.gestionHistoireTicketService.services.SprintFeignClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/histoireTickets")
public class HistoireTicketController {
    @Autowired
    private HistoireTicketService histoireTicketService;
    @Autowired
    private ProductBacklogService productBacklogService;
    @Autowired
    private HistoireTicketRepository histoireTicketRepository;

    @Autowired
    private SprintFeignClient sprintFeignClient;
    
    @GetMapping
    public List<HistoireTicket> getAllHistoireTickets() {
        return histoireTicketService.findAllHistoireTickets();
    }


    @GetMapping("/{id}")
    public HistoireTicket getTicketHistoireById(@PathVariable("id")Long id){
        return this.histoireTicketService.getTicketHistoireById(id);
    }

    @GetMapping("/sprint/{id-sprint}")
    public List<HistoireTicket> getHistoireTicketsBySprint(@PathVariable("id-sprint") Long idSprint){
        List<HistoireTicket> hTickets = this.histoireTicketService.findTicketHistoireBySprintId(idSprint);
        Sprint sprint = this.sprintFeignClient.findById(idSprint);
        for(HistoireTicket ht:hTickets){
            ht.setSprint(sprint);
        }
        return hTickets;
    }

    @GetMapping("/productBacklog/{id}")
    public List<HistoireTicket> getHistoireTicketsByProductBacklog(@PathVariable(name="id") Long id) throws SQLException {

        ProductBacklog productBacklog  = this.productBacklogService.findProductBacklogById(id);
        List<HistoireTicket> histoireTickets = this.histoireTicketService.findAllHistoireTicketByProductBacklog(id);
        for(HistoireTicket histoireTicket:histoireTickets){
            histoireTicket.setProductBacklog(productBacklog);
        }
        return histoireTickets;
    }

    @PostMapping
    public ResponseEntity<HistoireTicket> addHistoireTicket(@RequestBody HistoireTicket histoireTicket) {
        HistoireTicket newHistoireTicket = histoireTicketService.addHistoireTicket(histoireTicket);
        return ResponseEntity.ok(newHistoireTicket);
    }

    @PutMapping("/position")
    public ResponseEntity<?> updateTicketPosition(@RequestBody Map<String, Object> request) {
        Long ticketId = Long.parseLong(request.get("id").toString());
        int newPosition = Integer.parseInt(request.get("position").toString());
        Optional<HistoireTicket> optionalTicket = histoireTicketRepository.findById(ticketId);
        if (optionalTicket.isPresent()) {
            HistoireTicket ticket = optionalTicket.get();
            ticket.setPosition(newPosition);
            histoireTicketRepository.save(ticket);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping
    public HistoireTicket modifierHistoireTicket(@RequestBody HistoireTicket ticket){
        return histoireTicketService.detacherHistoireTicket(ticket);
    }


}
