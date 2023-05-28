package com.ms.gestionHistoireTicket.services;

import com.ms.gestionHistoireTicket.entities.HistoireTicket;
import com.ms.gestionHistoireTicket.entities.ProductBacklog;
import com.ms.gestionHistoireTicket.entities.Sprint;
import com.ms.gestionHistoireTicket.entities.TicketHistoireStatus;
import com.ms.gestionHistoireTicket.repositories.HistoireTicketRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Service
public class HistoireTicketService {
    private static final Logger logger = LogManager.getLogger(HistoireTicketService.class);
    @Autowired
    private HistoireTicketRepository histoireTicketRepository;
    @Autowired
    private TicketTacheFeignClient ticketTacheFeignClient;
    @Autowired
    private RestTemplate restTemplate;
    @Value("http://localhost:8089/sprints")
    private String sprintServiceUrl;
    @Value("http://localhost:8087/product-backlogs")
    private String productBacklogServiceUrl;
    public List<HistoireTicket> findAllHistoireTicketByProductBacklog(Long id){
        return this.histoireTicketRepository.findAllByProductBacklogId(id);
    }
    public List<HistoireTicket> findAllHistoireTickets(){
        return this.histoireTicketRepository.findAll();
    }
    public HistoireTicket addHistoireTicket(HistoireTicket histoireTicket) {
        return histoireTicketRepository.save(histoireTicket);
    }
    public List<HistoireTicket> getHistoireTicketsByMembreId(Long membreId) {
        return histoireTicketRepository.findByMembreId(membreId);
    }
    public List<HistoireTicket> getHistoireTicketsByProductBacklogId(Long productBacklogId) {
        return histoireTicketRepository.findByproductBacklogId(productBacklogId);
    }
    public void deleteUserStoryById(Long id) {
        histoireTicketRepository.deleteById(id);
    }
    public void removeUserStoryFromProductBacklog(Long id) {
        HistoireTicket histoireTicket = histoireTicketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User story with id " + id + " not found"));
        histoireTicket.setProductBacklogId(null);
        histoireTicket.setSprintId(null);
        histoireTicketRepository.save(histoireTicket);
    }
    public HistoireTicket assignUserStoryToSprint(Long histoireTicketId, Long sprintId) {
        HistoireTicket histoireTicket = histoireTicketRepository.findById(histoireTicketId).orElseThrow(() -> new RuntimeException("User story not found"));
        String sprintUrl = sprintServiceUrl + "/" + sprintId;
        Sprint sprint = restTemplate.getForObject(sprintUrl, Sprint.class);
        histoireTicket.setSprint(sprint);
        histoireTicket.setSprintId(sprint.getId());
        histoireTicket.setDateDebut(sprint.getDateLancement());
        histoireTicket.setDateFin(sprint.getDateFin());
        histoireTicketRepository.save(histoireTicket);
        return histoireTicket;
    }
    public HistoireTicket assignUserStoryToProductBacklog(Long histoireTicketId, Long productBacklogId) {
        HistoireTicket histoireTicket = histoireTicketRepository.findById(histoireTicketId).orElseThrow(() -> new RuntimeException("User story not found"));
        String productBacklogUrl = productBacklogServiceUrl + "/" + productBacklogId;
        ProductBacklog productBacklog = restTemplate.getForObject(productBacklogUrl, ProductBacklog.class);
        histoireTicket.setProductBacklog(productBacklog);
        histoireTicket.setProductBacklogId(productBacklog.getId());
        histoireTicketRepository.save(histoireTicket);
        return histoireTicket;
    }

    public HistoireTicket addUserStory(HistoireTicket userStory) {
        HistoireTicket existingUserStory = histoireTicketRepository
                .findByProductBacklogIdAndTitreAndDescriptionAndEffortAndPriorite(
                        userStory.getProductBacklogId(),
                        userStory.getTitre(),
                        userStory.getDescription(),
                        userStory.getEffort(),
                        userStory.getPriorite()
                );

        if(existingUserStory != null) {
            return existingUserStory;
        } else {
            userStory.setStatus(TicketHistoireStatus.EN_ATTENTE);
            return histoireTicketRepository.save(userStory);
        }
    }



    public List<HistoireTicket> findTicketHistoireBySprintId(Long idSprint){
        return this.histoireTicketRepository.findBySprintId(idSprint);
    }

    public HistoireTicket detacherHistoireTicket(HistoireTicket ticket){
        this.ticketTacheFeignClient.deleteAllByTicketHistoireId(ticket.getId());
        return this.histoireTicketRepository.save(ticket);
    }

    public HistoireTicket getTicketHistoireById(Long id){
        return this.histoireTicketRepository.findById(id).get();
    }
}
