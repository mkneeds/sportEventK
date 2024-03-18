package bsuir.kraevskij.sportevent.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
public class Product {
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    @Getter
    private String name;
    @Setter
    @Getter
    private double price;
    @Setter
    @Getter
    private String imageUrl;
    @Setter
    @Getter
    private String description;
    @Setter
    @Getter
    private String date;
    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
