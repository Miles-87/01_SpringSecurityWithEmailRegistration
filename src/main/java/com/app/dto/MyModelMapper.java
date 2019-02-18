package com.app.dto;

import com.app.dto.security.UserDto;
import com.app.model.Producer;
import com.app.model.Product;
import com.app.model.security.User;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class MyModelMapper {
    public ProducerDto fromProducerToProducerDto(Producer producer) {
        return producer == null ? null : ProducerDto.builder()
                .id(producer.getId())
                .name(producer.getName())
                .country(producer.getCountry())
                .build();
    }

    public Producer fromProducerDtoToProducer(ProducerDto producerDto) {
        return producerDto == null ? null : Producer.builder()
                .id(producerDto.getId())
                .name(producerDto.getName())
                .country(producerDto.getCountry())
                .products(new HashSet<>())
                .build();
    }

    public ProductDto fromProductToProductDto(Product product) {
        return product == null ? null : ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .producerDto(product.getProducer() == null ? null : fromProducerToProducerDto(product.getProducer()))
                .build();
    }

    public Product fromProductDtoToProduct(ProductDto productDto) {
        return productDto == null ? null : Product.builder()
                .id(productDto.getId())
                .name(productDto.getName())
                .price(productDto.getPrice())
                .producer(productDto.getProducerDto() == null ? null : fromProducerDtoToProducer(productDto.getProducerDto()))
                .build();
    }

    public User fromUserDtoToUser(UserDto userDto) {
        return userDto == null ? null : User.builder()
                .id(userDto.getId())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .role(userDto.getRole())
                .enabled(userDto.getEnabled())
                .build();
    }

    public UserDto fromUserToUserDto(User user) {
        return user == null ? null : UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .enabled(user.getEnabled())
                .build();
    }
}
