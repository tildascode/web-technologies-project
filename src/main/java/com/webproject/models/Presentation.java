package com.webproject.models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @OneToMany(mappedBy = "presentation", fetch = FetchType.LAZY)
    List<Slide> slides;

}
