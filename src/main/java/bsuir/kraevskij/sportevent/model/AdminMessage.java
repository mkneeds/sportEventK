package bsuir.kraevskij.sportevent.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
public class AdminMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    @Getter
    private Long id;
    @Setter
    @Getter
    private String date;
    @Setter
    @Getter
    private String title;
    @Setter
    @Getter
    private String description;
    @Setter
    @Getter
    private String duration;
    @ManyToOne
    @Setter @Getter
    private User user;
}

