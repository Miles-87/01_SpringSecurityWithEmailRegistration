package com.app.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private BigDecimal price;
    @ManyToOne(cascade = {CascadeType.PERSIST})
    // @JsonManagedReference
    @JoinColumn(name = "producerId")
    private Producer producer;
}
