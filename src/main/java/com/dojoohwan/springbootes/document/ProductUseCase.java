package com.dojoohwan.springbootes.document;

import java.util.List;

import com.google.gson.JsonObject;

import reactor.core.publisher.Flux;

public interface ProductUseCase {
     Flux<ProductDocument> searchAutuocompleteByName(String name);
    //Flux<JsonObject> searchAutuocompleteByName(String name);
    //List<ProductDocument> search(String indexName);
}
