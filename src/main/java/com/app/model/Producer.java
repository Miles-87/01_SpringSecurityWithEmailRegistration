package com.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "producers")
public class Producer {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String country;
    // @JsonBackReference
    @OneToMany(/*cascade = CascadeType.ALL,*/ mappedBy = "producer", fetch = FetchType.EAGER)
    private Set<Product> products = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Producer)) return false;
        Producer producer = (Producer) o;
        return Objects.equals(getId(), producer.getId()) &&
                Objects.equals(getName(), producer.getName()) &&
                Objects.equals(getCountry(), producer.getCountry());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getName(), getCountry());
    }

    @Override
    public String toString() {
        return "Producer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
