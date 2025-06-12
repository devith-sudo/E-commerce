package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductDTO {
    // Getters
    private String uuid;
    private String name;
    private String category;
    private double price;

//    public ProductDTO(String uuid, String name, String category, double price) {
//        this.uuid = uuid;
//        this.name = name;
//        this.category = category;
//        this.price = price;
//    }

    // Optional: toString for debug
    @Override
    public String toString() {
        return String.format("[%s] %s - $%.2f (%s)", uuid, name, price, category);
    }
}
