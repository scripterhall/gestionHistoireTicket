package com.ms.gestionHistoireTicket.controllers;

import com.ms.gestionHistoireTicket.entities.HistoireTicket;
import com.ms.gestionHistoireTicket.entities.ProductBacklog;
import com.ms.gestionHistoireTicket.entities.Sprint;
import com.ms.gestionHistoireTicket.entities.TicketHistoireStatus;
import com.ms.gestionHistoireTicket.models.Membre;
import com.ms.gestionHistoireTicket.services.HistoireTicketService;
import com.ms.gestionHistoireTicket.services.MembreService;
import com.ms.gestionHistoireTicket.services.ProductBacklogService;
import com.ms.gestionHistoireTicket.services.SprintFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.*;

@RestController
@RequestMapping("/histoireTickets")
public class HistoireTicketController {
    @Autowired
    private HistoireTicketService histoireTicketService;
    @Autowired
    private ProductBacklogService productBacklogService;

    @Autowired
    private MembreService membreService;
    

    @Autowired
    private SprintFeignClient sprintFeignClient;
    
    @GetMapping
    public List<HistoireTicket> getAllHistoireTickets() {
        return histoireTicketService.findAllHistoireTickets();
    }


    @GetMapping("/{id}")
    public HistoireTicket getTicketHistoireById(@PathVariable("id")Long id){

        HistoireTicket ht = this.histoireTicketService.getTicketHistoireById(id);
        if(ht.getSprintId()!=null){
          Sprint sp =  this.sprintFeignClient.findById(ht.getSprintId());
          ht.setSprint(sp);
        }
        return ht;
    }
    @GetMapping("/sprint/{id-sprint}")
    public List<HistoireTicket> getHistoireTicketsBySprint(@PathVariable("id-sprint") Long idSprint){
        List<HistoireTicket> hTickets = this.histoireTicketService.findTicketHistoireBySprintId(idSprint);
        Sprint sprint = this.sprintFeignClient.findById(idSprint);
        for(HistoireTicket ht:hTickets){
            if(ht.getDateFin() != null){
                ht.setDateFin(truncateDate(ht.getDateFin()));
            }
            if(ht.getMembreId()!=null){
                Membre membre = this.membreService.findMembreById((ht.getMembreId()));
                ht.setMembre(membre);
            }
            ht.setSprint(sprint);
        }
        Collections.sort(hTickets, Comparator.comparing(HistoireTicket::getDateFin, Comparator.nullsLast(Comparator.naturalOrder())));
        return hTickets;
    }

    @GetMapping("/productBacklog/{id}")
    public List<HistoireTicket> getHistoireTicketsByProductBacklog(@PathVariable(name="id") Long id) throws SQLException {
        ProductBacklog productBacklog  = this.productBacklogService.findProductBacklogById(id);
        List<HistoireTicket> histoireTickets = this.histoireTicketService.findAllHistoireTicketByProductBacklog(id);
        for(HistoireTicket histoireTicket:histoireTickets){
            histoireTicket.setProductBacklog(productBacklog);
        }
        Collections.sort(histoireTickets, Comparator.comparingInt(HistoireTicket::getPosition));
        return histoireTickets;
    }

    @PostMapping
    public ResponseEntity<HistoireTicket> addHistoireTicket(@RequestBody HistoireTicket histoireTicket) {
        histoireTicket.setStatus(TicketHistoireStatus.EN_ATTENTE);
        HistoireTicket newHistoireTicket = histoireTicketService.addHistoireTicket(histoireTicket);
        return ResponseEntity.ok(newHistoireTicket);
    }

    @GetMapping("/membre/{membreId}")
    public ResponseEntity<List<HistoireTicket>> getHistoireTicketsByMembreId(@PathVariable Long membreId) {
        List<HistoireTicket> histoireTickets = this.histoireTicketService.getHistoireTicketsByMembreId(membreId);
        return ResponseEntity.ok(histoireTickets);
    }
    private Date truncateDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
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
    @PostMapping("/new")
    public HistoireTicket addUserStory(@RequestBody HistoireTicket userStory) {
        return histoireTicketService.addUserStory(userStory);
    }

    @PutMapping
    public HistoireTicket detacherHistoireTicket(@RequestBody HistoireTicket ticket){
        return histoireTicketService.detacherHistoireTicket(ticket);
    }
    @PutMapping("/histoireTicket/{id}")
    public ResponseEntity<HistoireTicket> updateUserStory(@PathVariable Long id, @RequestBody HistoireTicket histoireTicket) {
        HistoireTicket existingHistoireTicket = histoireTicketService.getTicketHistoireById(id);
        if (existingHistoireTicket != null) {
            existingHistoireTicket.setStatus(histoireTicket.getStatus());
            existingHistoireTicket.setTitre(histoireTicket.getTitre());
            existingHistoireTicket.setDescription(histoireTicket.getDescription());
            
            existingHistoireTicket.setPriorite(histoireTicket.getPriorite());
            existingHistoireTicket.setEffort(histoireTicket.getEffort());
            existingHistoireTicket.setSprintId(histoireTicket.getSprintId());
            existingHistoireTicket.setPosition(histoireTicket.getPosition());
            if(histoireTicket.getProductBacklogId()!=null && existingHistoireTicket.getProductBacklogId()==null)
                existingHistoireTicket.setProductBacklogId(histoireTicket.getProductBacklogId());
            HistoireTicket updatedHistoireTicket = histoireTicketService.addHistoireTicket(existingHistoireTicket);
            return ResponseEntity.ok(updatedHistoireTicket);
        }
        return ResponseEntity.notFound().build();
    }


}
