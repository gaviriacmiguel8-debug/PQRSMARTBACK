package Proyecto.PQRSMART.Controller;


import Proyecto.PQRSMART.Domain.Dto.DependenceDTO;
import Proyecto.PQRSMART.Domain.Service.DependenceServiceImpl;
import Proyecto.PQRSMART.Domain.Service.Interfaces.DependenceService;
import Proyecto.PQRSMART.Persistence.Entity.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/dependence")
public class DependenceController {
    @Autowired
    private DependenceService dependenceService;

    @PostMapping("/save")
    public DependenceDTO save(@RequestBody DependenceDTO dependenceDTO){
        return dependenceService.save(dependenceDTO);
    }

    @GetMapping("/get")
    public List<DependenceDTO> get(){return dependenceService.getAll();}

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody DependenceDTO dependenceDTO) {
        Optional<DependenceDTO> dependenceDTOOptional = dependenceService.findById(dependenceDTO.getIdDependence());
        if(dependenceDTOOptional.isPresent()) {
            dependenceService.save(dependenceDTO);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<DependenceDTO> delete(@PathVariable Long id){
        Optional<DependenceDTO> dependenceDTOOptional = dependenceService.findById(id);
        if(dependenceDTOOptional.isPresent()) {
            DependenceDTO dependenceDTO = dependenceDTOOptional.get();
            dependenceDTO.setState(new State(2l, "DESACTIVADO"));
            dependenceService.save(dependenceDTO);
            return ResponseEntity.ok(dependenceDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<DependenceDTO> activate(@PathVariable Long id){
        Optional<DependenceDTO> dependenceDTOOptional = dependenceService.findById(id);
        if(dependenceDTOOptional.isPresent()) {
            DependenceDTO dependenceDTO = dependenceDTOOptional.get();
            dependenceDTO.setState(new State(1l, "ACTIVADO"));
            dependenceService.save(dependenceDTO);
            return ResponseEntity.ok(dependenceDTO);
        }
        return ResponseEntity.notFound().build();
    }


}
