package com.example.vortex_games.controller;

import com.example.vortex_games.entity.Characteristic;
import com.example.vortex_games.exception.ExistingProductException;
import com.example.vortex_games.exception.ResourceNotFoundException;
import com.example.vortex_games.service.CharacteristicService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@Log4j2
@RestController
@RequestMapping("/characteristics")
public class CharacteristicController {

    @Autowired
    private CharacteristicService characteristicService;

    @GetMapping("/list-characteristics")//<<--- Lista todas las caracteristicas
    public ResponseEntity<List<Characteristic>> getCharacteristics(){
        return ResponseEntity.ok(characteristicService.listCharacteristics());
    }

    @GetMapping("/search-name/{name}")//<<----- Busca una caracteristica en particular por su nombre
    public ResponseEntity<Characteristic> searchCharacteristicByName(@PathVariable String name) throws ResourceNotFoundException {
        log.info("el nombre es: "+name);
        Optional<Characteristic> characteristicSearched = characteristicService.searchByName(name);
        if(characteristicSearched.isPresent()) return ResponseEntity.ok(characteristicSearched.get());
        else throw new ResourceNotFoundException("No se encontro la caracteristica con nombre "+name);
    }

    @GetMapping("/search-id/{id}")//<<------ Busca una caracteristica en particular por su id
    public ResponseEntity<Characteristic> searchCharacteristicById(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<Characteristic> characteristicSearched = characteristicService.searchById(id);
        if(characteristicSearched.isPresent()) return ResponseEntity.ok(characteristicSearched.get());
        else throw new ResourceNotFoundException("No se encontro la caracteristica con id "+id);
    }

    @PostMapping("/add-characteristic")//<<----- registrar una caracteristia nueva en la base de datos
    public ResponseEntity<String>  add(@RequestBody Characteristic characteristic) throws ExistingProductException {
        Optional<Characteristic> characteristicSearched = characteristicService.searchByName(characteristic.getName());
        if(characteristicSearched.isEmpty()){
            Characteristic characteristicSaved = characteristicService.addCharacteristic(characteristic);
            return ResponseEntity.ok("Se agrego correctamente la caracteristica con id: "+characteristicSaved.getId()+" nombre: "+characteristicSaved.getDescription());
        }else{
            throw new ExistingProductException("No se puede agregar la caracteristica, el sistema ya existe una con el mismo nombre");
        }
    }

    @DeleteMapping("/delete")//<<----- Elimina una caracteristica de la base de datos pasandole la caracteristica completa
    public ResponseEntity<String> remove(@RequestBody Characteristic characteristic) throws ResourceNotFoundException {
        Optional<Characteristic> characteristicSearched = characteristicService.searchByName(characteristic.getName());
        if(characteristicSearched.isPresent()){
            characteristicService.deleteCharacteristic(characteristicSearched.get());
            return ResponseEntity.ok("Se elimino correctamente la caracteristica: "+characteristic.getName());
        }else{
            throw new ResourceNotFoundException("No existe una caracteristica en el sistema con esos datos para ser borrada");
        }

    }
    @DeleteMapping("/delete/{id}")//<<----- Elimina una caracteristica de la base de datos solo por id
    public ResponseEntity<String> removeById(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<Characteristic> characteristicSearched = characteristicService.searchById(id);
        if(characteristicSearched.isPresent()){
            characteristicService.deleteCharacteristic(characteristicSearched.get());
            return ResponseEntity.ok("Se elimino correctamente la caracteristica");
        }else{
            throw new ResourceNotFoundException("No existe una caracteristica en el sistema con esos datos para ser borrada");
        }

    }



    @PutMapping("/update")//<<------ Actualizar la caracteriztica en cuestion
    public ResponseEntity<String> update(@RequestBody Characteristic characteristic) throws ResourceNotFoundException{
        Optional<Characteristic> characteristicSearched = characteristicService.searchById(characteristic.getId());
        if(characteristicSearched.isPresent()){
            characteristicService.updateCharacteristic(characteristic);
            return ResponseEntity.ok("Se actualizo la caracteristica con los datos nuevos");
        }else{
            throw new ResourceNotFoundException("No se encontro una caracteristica con esos datos, debe agregar una primero");
        }
    }

}
