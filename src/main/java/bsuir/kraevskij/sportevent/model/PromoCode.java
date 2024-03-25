package bsuir.kraevskij.sportevent.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
public class PromoCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter @Getter
    private Long id;
    @Setter @Getter
    private String code;
    @Setter @Getter
    private Date expirationDate;
    @Setter @Getter
    private double discountPercentage;
    @Setter @Getter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
