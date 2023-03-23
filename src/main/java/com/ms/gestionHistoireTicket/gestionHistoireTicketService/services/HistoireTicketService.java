package com.ms.gestionHistoireTicket.gestionHistoireTicketService.services;

import com.ms.gestionHistoireTicket.gestionHistoireTicketService.entities.HistoireTicket;
import com.ms.gestionHistoireTicket.gestionHistoireTicketService.entities.ProductBacklog;
import com.ms.gestionHistoireTicket.gestionHistoireTicketService.models.Sprint;
import com.ms.gestionHistoireTicket.gestionHistoireTicketService.repositories.HistoireTicketRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLException;
import java.util.List;

@Service
public class HistoireTicketService {
    private static final Logger logger = LogManager.getLogger(HistoireTicketService.class);
    @Autowired
    private HistoireTicketRepository histoireTicketRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Value("http://localhost:9999/gestion-sprints-service/sprints")
    private String sprintServiceUrl;
    @Value("http://localhost:9999/gestion-product-backlog/product-backlogs")
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
        histoireTicketRepository.save(histoireTicket);
    }
    public HistoireTicket assignUserStoryToSprint(Long histoireTicketId, Long sprintId) {
        HistoireTicket histoireTicket = histoireTicketRepository.findById(histoireTicketId).orElseThrow(() -> new RuntimeException("User story not found"));
        String sprintUrl = sprintServiceUrl + "/" + sprintId;
        Sprint sprint = restTemplate.getForObject(sprintUrl, Sprint.class);
        histoireTicket.setSprint(sprint);
        histoireTicket.setSprintId(sprint.getId());
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
    public HistoireTicket findUserStoryById(Long id) throws SQLException {
        return this.histoireTicketRepository.findById(id).get();
    }
    public HistoireTicket addUserStory(HistoireTicket userStory) {
        return histoireTicketRepository.save(userStory);
    }

}
