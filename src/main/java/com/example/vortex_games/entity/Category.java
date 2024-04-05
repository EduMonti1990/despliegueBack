package com.example.vortex_games.entity;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.EnableMBeanExport;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@RequiredArgsConstructor
@Table(name="categories")

public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(unique = true , nullable = true)
    private String title;

    @NonNull
    @Column(nullable = true)
    private String description;

    @OneToOne ( cascade={CascadeType.ALL} ,fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id")
    private Image image;

    @OneToMany (mappedBy = "category")
    @JsonIgnore
    private Set<Product> products=new HashSet<>();

    public Category(@NonNull String title, @NonNull String description, Image image) {
        this.title = title;
        this.description = description;
        this.image = image;
    }
}
