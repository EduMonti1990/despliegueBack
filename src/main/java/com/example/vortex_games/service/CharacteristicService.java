package com.example.vortex_games.service;

import com.example.vortex_games.entity.Characteristic;
import com.example.vortex_games.repository.CharacteristicRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Service
public class CharacteristicService {

    @Autowired
    private CharacteristicRepository characteristicRepository;


    public List<Characteristic> listCharacteristics(){
        return characteristicRepository.findAll();
    }

    public Characteristic addCharacteristic(Characteristic characteristic){
        return characteristicRepository.save(characteristic);
    }

    public Optional<Characteristic> searchByName(String name){
        return characteristicRepository.findByName(name);
    }

    public Optional<Characteristic> searchById(Long id){
        return characteristicRepository.findById(id);
    }

    public void deleteById(Long id){
        characteristicRepository.deleteById(id);
    }

    public void deleteCharacteristic(Characteristic characteristic){
        characteristic.getProducts().forEach(product -> product.getCharacteristics().remove(characteristic));
        characteristicRepository.delete(characteristic);
    }

    public void updateCharacteristic(Characteristic characteristic){
        characteristicRepository.save(characteristic);
    }


}
