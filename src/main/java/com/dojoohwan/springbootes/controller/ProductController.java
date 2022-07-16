package com.dojoohwan.springbootes.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dojoohwan.springbootes.document.ProductDocument;
import com.dojoohwan.springbootes.document.ProductUseCase;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductUseCase productUseCase;

    public ProductController(ProductUseCase productUseCase) {
        this.productUseCase = productUseCase;
    }
    @GetMapping("/auto")
    @ResponseStatus(HttpStatus.OK)
    public Flux<ProductDocument> searchAutuocompleteByName(@RequestParam(name="name") String name){

        return productUseCase.searchAutuocompleteByName(name);
    }

}