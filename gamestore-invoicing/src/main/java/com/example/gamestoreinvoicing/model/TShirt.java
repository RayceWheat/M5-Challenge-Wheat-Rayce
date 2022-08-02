package com.example.gamestoreinvoicing.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "tshirt")
public class TShirt implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tshirt_id")
    private long id;

    @NotEmpty(message = "T-Shirt size is required")
    private String size;

    @NotEmpty(message = "T-Shirt color is required")
    private String color;

    @NotEmpty(message = "T-Shirt description is required")
    private String description;


    @NotNull(message = "T-Shirt price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Min Price is $0.0")
    @DecimalMax(value = "999.99", inclusive = true, message = "Max Price is $999.99")
    private BigDecimal price;


    @NotNull(message = "T-Shirt quantity is required")
    @Min(value = 1, message = "Min Quantity 1")
    @Max(value = 50000, message = "Max Quantity is 50,000")
    private long quantity;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TShirt tShirt = (TShirt) o;
        return id == tShirt.id && quantity == tShirt.quantity && Objects.equals(size, tShirt.size) && Objects.equals(color, tShirt.color) && Objects.equals(description, tShirt.description) && Objects.equals(price, tShirt.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, size, color, description, price, quantity);
    }
}
