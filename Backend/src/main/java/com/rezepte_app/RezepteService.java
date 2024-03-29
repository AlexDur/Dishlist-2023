package com.rezepte_app;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RezepteService {

    private static final Logger logger = LoggerFactory.getLogger(RezepteService.class);


    @Autowired
    private RezepteRepository rezepteRepository;

    // Im RezepteService
    public List<Rezept> fetchAlleRezepte() {
        List<Rezept> alleRezepte = rezepteRepository.findAll();
        logger.info("Anzahl der abgerufenen Rezepte: {}", alleRezepte.size());

        // Optional: Loggen Sie einige Details der abgerufenen Rezepte
        if (logger.isDebugEnabled()) {
            alleRezepte.forEach(rezept -> logger.debug("Rezept: {}", rezept));
        }

        return alleRezepte;
    }

    @Valid
    public Rezept createRezept(Rezept rezept) {
        // ...

        if (rezept.getName() == null) {
            throw new IllegalArgumentException("Der Name des Rezepts darf nicht null sein.");
        }

        return rezepteRepository.save(rezept);
    }


    public Optional<Rezept> updateRezept(Rezept rezept) {
        Optional<Rezept> existingRezeptOptional = Optional.ofNullable(rezepteRepository.findById(rezept.getId()));

        if (existingRezeptOptional.isPresent()) {
            Rezept existingRezept = existingRezeptOptional.get();
            // Setzen Sie hier die Felder von existingRezept basierend auf den Werten von rezept
            existingRezept.setName(rezept.getName());
            existingRezept.setOnlineAdresse(rezept.getOnlineAdresse());
            existingRezept.setDatum(rezept.getDatum());
         /*   existingRezept.setPerson(rezept.getPerson());*/
            existingRezept.setStatus(rezept.getStatus());
            existingRezept.setBewertung(rezept.getBewertung());

            return Optional.of(rezepteRepository.save(existingRezept));
        } else {
            logger.info("Rezept mit ID {} nicht gefunden.", rezept.getId());
            return Optional.empty();
        }
    }

    public boolean deleteRezept(int id) {
        Optional<Rezept> rezeptOptional = Optional.ofNullable(rezepteRepository.findById(id));

        if (rezeptOptional.isPresent()) {
            rezepteRepository.deleteById(id);
            return true;
        } else {
            return false; // Rezept mit der angegebenen ID wurde nicht gefunden
        }
    }



}
