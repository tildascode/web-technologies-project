package com.webproject.models;

import java.util.List;
import javax.persistence.*;

import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Presentation {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String tags;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "presentation", fetch = FetchType.LAZY)
    List<Slide> slides;

}
