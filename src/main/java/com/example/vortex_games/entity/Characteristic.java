package com.example.vortex_games.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name="characteristics")

public class Characteristic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(unique = true)
    private String name;


    @Column(nullable = true)
    private String description;

    @ManyToMany (mappedBy = "characteristics", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Product> products=new HashSet<>();

    public Characteristic(@NonNull String name, String description) {
        this.name = name;
        this.description = description;
    }
}
