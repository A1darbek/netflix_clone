package com.ayderbek.netflxSpring.netflix_clone.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "_user")
public class User {
    @Id
    @SequenceGenerator(
            name = "_user_id_sequence",
            sequenceName = "_user_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "_use_id_sequence"
    )
    private Long id;
    @Column(name = "auth0_id")
    private String auth0Id;
    private String name;
    private String email;
    private String password;

    private Boolean emailVerified;
    private String picture;

    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    private List<PlayList> playlists;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Genre> favoriteGenres;
}
