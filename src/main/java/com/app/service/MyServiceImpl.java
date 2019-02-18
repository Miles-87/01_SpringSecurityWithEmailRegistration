package com.app.service;

import com.app.dto.MyModelMapper;
import com.app.dto.ProducerDto;
import com.app.dto.ProductDto;
import com.app.exception.MyException;
import com.app.model.Producer;
import com.app.model.Product;
import com.app.repository.ProducerRepository;
import com.app.repository.ProductRepository;
import com.app.validators.ProducerValidator;
import com.app.validators.ProductValidator;
import com.app.validators.ValidationErrors;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MyServiceImpl{

    private ProducerRepository producerRepository;
    private ProductRepository productRepository;
    private MyModelMapper modelMapper;
    private ProducerValidator producerValidator;
    private ProductValidator productValidator;

    public MyServiceImpl(ProducerRepository producerRepository, ProductRepository productRepository, MyModelMapper modelMapper, ProducerValidator producerValidator, ProductValidator productValidator) {
        this.producerRepository = producerRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.producerValidator = producerValidator;
        this.productValidator = productValidator;
    }

    public ProducerDto addProducer(ProducerDto producerDto) {
        try {
            // WALIDACJA
            ValidationErrors validationErrors = producerValidator.validate(producerDto);
            if (validationErrors.hasErrors()) {
                throw new IllegalArgumentException("VALIDATION ERRORS: " + validationErrors.errors());
            }

            Producer producer = modelMapper.fromProducerDtoToProducer(producerDto);
            Producer addedProducer = producerRepository.save(producer);
            return modelMapper.fromProducerToProducerDto(addedProducer);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("SERVICE, ADD PRODUCER EXCEPTION", LocalDateTime.now());
        }
    }

    public ProducerDto updateProducer(ProducerDto producerDto) {
        try {

            // WALIDACJA
            ValidationErrors validationErrors = producerValidator.validate(producerDto);
            if (validationErrors.hasErrors()) {
                throw new IllegalArgumentException("VALIDATION ERRORS: " + validationErrors.errors());
            }

            Producer producer = producerRepository.findById(producerDto.getId()).orElseThrow(NullPointerException::new);
            producer.setName(producerDto.getName() == null ? producer.getName() : producerDto.getName());
            producer.setCountry(producerDto.getCountry() == null ? producer.getCountry() : producerDto.getCountry());
            Producer updatedProducer = producerRepository.save(producer);
            return modelMapper.fromProducerToProducerDto(updatedProducer);
        } catch (Exception e) {
            throw new MyException("SERVICE, UPDATE PRODUCER EXCEPTION", LocalDateTime.now());
        }
    }

    public List<ProducerDto> getAllProducers() {
        try {
            return producerRepository
                    .findAll() // zwracamy produkty ale jako kolekcje typu Producer
                    .stream() // na podstawie kolekcji generujemy strumien
                    .map(modelMapper::fromProducerToProducerDto) // mapowanie pozwoli nam zrobic strumien ProducerDto
                    .collect(Collectors.toList()); // zamieniam strumien na kolekcje ProducerDto
        } catch (Exception e) {
            throw new MyException("SERVICE, GET ALL PRODUCERS EXCEPTION", LocalDateTime.now());
        }
    }

    public ProducerDto getProducerById(Long id) {
        try {
            Producer producer = producerRepository
                    .findById(id) // zwracam z bazy danych obiekt po id
                    // ale jako Optional
                    .orElseThrow(NullPointerException::new); // kiedy w Optional nic nie ma, bo np
            // podales id ktore nie wystepuje w db to rzuci nam wyjatek,
            // ktory okreslimy w () a jezeli nie to zwrocic obiekt o tym
            // id

            // teraz maja obiekt Producer musismy go przekonwertowac na
            // obiekt ProducerDto bo taki mamy typ zwracany
            return modelMapper.fromProducerToProducerDto(producer);
        } catch (Exception e) {
            throw new MyException("SERVICE, GET PRODUCER BY ID", LocalDateTime.now());
        }
    }

    public ProducerDto deleteProducer(Long id) {
        try {
            Producer producer = producerRepository.findById(id).orElseThrow(NullPointerException::new);
            deleteAllProductsForProducer(id);
            producerRepository.deleteById(id);
            return modelMapper.fromProducerToProducerDto(producer);
        } catch (Exception e) {
            throw new MyException("SERVICE, DELETE PRODUCER", LocalDateTime.now());
        }
    }

    public ProductDto addProduct(ProductDto productDto) {
        try {

            ValidationErrors productValidationErrors = productValidator.validate(productDto);
            if (productValidationErrors.hasErrors()) {
                throw new IllegalArgumentException("PRODUCT VALIDATION ERRORS: " + productValidationErrors.errors());
            }

            ValidationErrors producerValidationErrors = producerValidator.validate(productDto.getProducerDto());
            if (producerValidationErrors.hasErrors()) {
                throw new IllegalArgumentException("PRODUCER VALIDATION ERRORS: " + producerValidationErrors.errors());
            }


            Product product = modelMapper.fromProductDtoToProduct(productDto);

            Producer producer = null;
            if (productDto.getProducerDto().getId() == null) {
                producer = producerRepository.findByName(product.getProducer().getName());
            } else {
                producer = producerRepository.findById(product.getProducer().getId()).orElseThrow(NullPointerException::new);
            }
            product.setProducer(producer);
            Product addedProduct = productRepository.save(product);
            return modelMapper.fromProductToProductDto(addedProduct);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("SERVICE, ADD PRODUCT", LocalDateTime.now());
        }
    }

    public ProductDto updateProduct(ProductDto productDto) {
        try {

            ValidationErrors validationErrors = productValidator.validate(productDto);
            if (validationErrors.hasErrors()) {
                throw new IllegalArgumentException("PRODUCT VALIDATION ERRORS: " + validationErrors.errors());
            }

            // kiedy nie ma producera to zostaje stary producer
            // kiedy jest producer to standardowo sprawdzamy czy ma id lub name i po tym
            // dokonujemy aktualizacji
            Product product = productRepository.findById(productDto.getId()).orElseThrow(NullPointerException::new);

            if (productDto.getProducerDto() != null) {
                Producer producer;
                ValidationErrors producerValidationErrors = producerValidator.validate(productDto.getProducerDto());
                if (producerValidationErrors.hasErrors()) {
                    throw new IllegalArgumentException("PRODUCER VALIDATION ERRORS: " + producerValidationErrors.errors());
                }

                if (productDto.getProducerDto().getId() == null) {
                    producer = producerRepository.findByName(productDto.getProducerDto().getName());
                } else {
                    producer = producerRepository.findById(productDto.getProducerDto().getId()).orElseThrow(NullPointerException::new);
                }
                product.setProducer(producer);
            }

            product.setName(productDto.getName());
            product.setPrice(productDto.getPrice());
            System.out.println(product);
            Product updatedProduct = productRepository.save(product);
            return modelMapper.fromProductToProductDto(updatedProduct);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("SERVICE, ADD PRODUCT", LocalDateTime.now());
        }
    }

    public List<ProductDto> getAllProducts() {
        try {
            return productRepository
                    .findAll()
                    .stream()
                    .map(modelMapper::fromProductToProductDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("SERVICE, GET ALL PRODUCTS", LocalDateTime.now());
        }
    }

    public ProductDto getProductById(Long id) {
        try {
            return productRepository
                    .findById(id)
                    .map(modelMapper::fromProductToProductDto)
                    .orElseThrow(NullPointerException::new);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("SERVICE, GET PRODUCT BY ID", LocalDateTime.now());
        }
    }

    public void deleteProduct(Long id) {
        try {
            productRepository.deleteById(id);
        } catch (Exception e) {
            throw new MyException("SERVICE, DELETE PRODUCT", LocalDateTime.now());
        }
    }

    public void deleteAllProductsForProducer(Long producerId) {
        try {
            List<Product> products = productRepository
                    .findAll()
                    .stream()
                    .filter(p -> p.getProducer() != null && p.getProducer().getId().equals(producerId))
                    .collect(Collectors.toList());
            productRepository.deleteAll(products);
         } catch (Exception e) {
            throw new MyException("SERVICE, DELETE PRODUCTS FOR PRODUCER", LocalDateTime.now());
        }
    }
}
