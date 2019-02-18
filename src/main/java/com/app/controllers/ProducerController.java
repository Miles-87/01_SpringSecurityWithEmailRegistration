package com.app.controllers;

import com.app.dto.ProducerDto;
import com.app.service.MyServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/producers")
public class ProducerController {

    private MyServiceImpl myService;

    public ProducerController(MyServiceImpl myService) {
        this.myService = myService;
    }


    @GetMapping
    public ResponseEntity<List<ProducerDto>> getAllProducers() {
        return new ResponseEntity<>(myService.getAllProducers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProducerDto> getOneProducer(@PathVariable Long id) {
        return new ResponseEntity<>(myService.getProducerById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProducerDto> addProducer(RequestEntity<ProducerDto> request) {
        System.out.println("HEADERS: " + request.getHeaders());
        ProducerDto addedProducer = myService.addProducer(request.getBody());
        return new ResponseEntity<>(addedProducer, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ProducerDto> updateProducer(RequestEntity<ProducerDto> request) {
        ProducerDto producerDto = myService.updateProducer(request.getBody());
        return new ResponseEntity<>(producerDto, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<ProducerDto> deleteProducer(RequestEntity<Long> request) {
        ProducerDto producerDto = myService.deleteProducer(request.getBody());
        return new ResponseEntity<>(producerDto, HttpStatus.OK);
    }
}
