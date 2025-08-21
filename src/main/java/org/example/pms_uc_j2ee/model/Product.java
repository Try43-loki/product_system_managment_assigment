package org.example.pms_uc_j2ee.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import java.math.BigDecimal;

/**
 * Represents the Product entity, mapping to the 'Product' table in the database.
 * This model is designed for products like mobile phones.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "Product")
public class Product {

    /**
     * The unique identifier for the product.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId;

    /**
     * The name of the product (e.g., "Galaxy S25").
     */
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    /**
     * A detailed description of the product. Mapped to a TEXT column for longer content.
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * The price of the product. BigDecimal is used for accurate currency representation.
     */
    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    /**
     * The quantity of the product currently in stock.
     */
    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    /**
     * The color of the product (e.g., "Phantom Black", "Cloud Blue").
     */
    @Column(name = "product_color", length = 50)
    private String productColor;

    /**
     * A URL or path to the product's image.
     * Stored as a string.
     */
    @Column(name = "image_url")
    private String imageUrl;

    /**
     * The admin who created or manages this product.
     * This creates a many-to-one relationship with the Admin entity.
     * The 'admin_id' column in the 'Product' table will be the foreign key.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    @ToString.Exclude // Exclude from toString to prevent potential infinite loops
    private Admin admin;

}
