package model;


import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "orders")
public class Order {
     @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    @Column(name = "order_code", nullable = false, unique = true)
    private String orderCode;
}
