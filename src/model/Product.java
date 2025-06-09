package model;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String p_name;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int qty;

    @Column
    private boolean is_deleted;

    @Column
    private String p_uuid;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
