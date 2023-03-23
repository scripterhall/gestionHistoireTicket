package com.ms.gestionHistoireTicket.gestionHistoireTicketService.controllers;

import com.ms.gestionHistoireTicket.gestionHistoireTicketService.entities.HistoireTicket;
import com.ms.gestionHistoireTicket.gestionHistoireTicketService.entities.ProductBacklog;
import com.ms.gestionHistoireTicket.gestionHistoireTicketService.entities.Ticket;
import com.ms.gestionHistoireTicket.gestionHistoireTicketService.models.Membre;
import com.ms.gestionHistoireTicket.gestionHistoireTicketService.repositories.HistoireTicketRepository;
import com.ms.gestionHistoireTicket.gestionHistoireTicketService.services.HistoireTicketService;
import com.ms.gestionHistoireTicket.gestionHistoireTicketService.services.MembreService;
import com.ms.gestionHistoireTicket.gestionHistoireTicketService.services.ProductBacklogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @GetMapping
    public List<HistoireTicket> getAllHistoireTickets() {
        return histoireTicketService.findAllHistoireTickets();
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
    @GetMapping("/membre/{membreId}")
    public ResponseEntity<List<HistoireTicket>> getHistoireTicketsByMembreId(@PathVariable Long membreId) {
        List<HistoireTicket> histoireTickets = this.histoireTicketService.getHistoireTicketsByMembreId(membreId);
        return ResponseEntity.ok(histoireTickets);
    }
    @GetMapping("/product-backlog/{productBacklogId}")
    public ResponseEntity<List<HistoireTicket>> getHistoireTicketsByProductBacklogId(@PathVariable Long productBacklogId) {
        List<HistoireTicket> histoireTickets = histoireTicketService.getHistoireTicketsByProductBacklogId(productBacklogId);
        return new ResponseEntity<>(histoireTickets, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public void deleteUserStoryById(@PathVariable Long id) {
        histoireTicketService.deleteUserStoryById(id);
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> removeUserStoryFromProductBacklog(@PathVariable Long id) {
        histoireTicketService.removeUserStoryFromProductBacklog(id);
        return ResponseEntity.ok("ProductBacklogId of user story's id " + id + " updated to null.");
    }
    @PutMapping("/{histoireTicketId}/sprint/{sprintId}")
    public HistoireTicket assignUserStoryToSprint(@PathVariable Long histoireTicketId, @PathVariable Long sprintId) {
        return histoireTicketService.assignUserStoryToSprint(histoireTicketId, sprintId);
    }
    @PutMapping("/{histoireTicketId}/productBacklog/{productBacklogId}")
    public HistoireTicket assignUserStoryToProductBacklog(@PathVariable Long histoireTicketId, @PathVariable Long productBacklogId) {
        return histoireTicketService.assignUserStoryToProductBacklog(histoireTicketId, productBacklogId);
    }
    @GetMapping("/{id}")
    public HistoireTicket getUserStoryById(@PathVariable Long id) {
        try {
            HistoireTicket histoireTicket =  histoireTicketService.findUserStoryById(id);
            return histoireTicket;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
    @PostMapping("/new")
    public HistoireTicket addUserStory(@RequestBody HistoireTicket userStory) {
        return histoireTicketService.addUserStory(userStory);
    }
}
