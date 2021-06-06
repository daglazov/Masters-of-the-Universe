package ru.jobtest.masters.mastersoftheuniverse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.jobtest.masters.mastersoftheuniverse.domain.Master;
import ru.jobtest.masters.mastersoftheuniverse.domain.Planet;
import ru.jobtest.masters.mastersoftheuniverse.domain.PlanetBuilder;
import ru.jobtest.masters.mastersoftheuniverse.repository.CommonRepository;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class MastersAndPlanetesController {

    private CommonRepository<Master> masterRepository;
    private CommonRepository<Planet> planetRepository;

    @Autowired
    public MastersAndPlanetesController(CommonRepository<Master> masters, CommonRepository<Planet> planetes){
        this.masterRepository = masters;
        this.planetRepository = planetes;
    }

    @GetMapping("/test")
    public String testController(){
        return "I'm working";
    }

    @GetMapping("/masters")
    public ResponseEntity<Iterable<Master>> getMasters(){
        return ResponseEntity.ok(masterRepository.findAll());
    }

    @GetMapping("/planetes")
    public ResponseEntity<Iterable<Planet>> getPlanetes(){
        return ResponseEntity.ok(planetRepository.findAll());
    }

    @GetMapping("/masters/idle")
    public ResponseEntity<Iterable<Master>> getIdleMasters(){
        return ResponseEntity.ok(masterRepository.findIdle());
    }

    @GetMapping("/masters/young")
    public ResponseEntity<Iterable<Master>> getMostYoung(){
        return ResponseEntity.ok(masterRepository.findTopYoung());
    }

    @RequestMapping(value = "/masters", method = {RequestMethod.PUT, RequestMethod.POST})
    public ResponseEntity<Master> addMaster(@RequestBody Master master, Errors errors){
        if (errors.hasErrors()) return ResponseEntity.badRequest().build();

        Master result = masterRepository.save(master);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @RequestMapping(value = "/planetes", method = {RequestMethod.PUT, RequestMethod.POST})
    public ResponseEntity<Planet> addPlanet(@RequestBody Planet planet, Errors errors){
        if (errors.hasErrors()) return ResponseEntity.badRequest().build();

        Planet result = planetRepository.save(planet);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/id")
                .buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/planetes/{id}")
    public ResponseEntity<Planet> setMaster(@PathVariable String id, @RequestParam String masterId){
        Planet result = planetRepository.findById(id);
            if (result.getMasterId() != null) return ResponseEntity.badRequest().build();
        result.setMasterId(masterId);
        planetRepository.save(result);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(result.getId()).toUri();
        return ResponseEntity.ok().header("Location", location.toString()).build();
    }

    @DeleteMapping("/planetes/{id}")
    public ResponseEntity<Planet> delPlanetById(@PathVariable String id){
        planetRepository.delete(PlanetBuilder.create().withId(id).build());
        return ResponseEntity.ok().build();
    }


//    Здесь должен быть использован не реализованный в этот раз обработчик исключений
//    @ExceptionHandler
//    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//    public MyValidationError handleExeption(Exception e){
//        return new MyValidationError(e.getMessage());
//    }
}
