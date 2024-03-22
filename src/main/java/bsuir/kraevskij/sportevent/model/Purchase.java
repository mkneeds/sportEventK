package bsuir.kraevskij.sportevent.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "purchase")
public class Purchase {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "buyer_id", referencedColumnName = "id")
    private User buyer;
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    private User seller;
    @Getter
    @Setter
    @Column(name = "amount")
    private double amount;
    @Getter
    @Setter
    @Column(name = "purchase_date")
    private Date purchaseDate;

}

