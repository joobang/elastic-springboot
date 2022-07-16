package com.dojoohwan.springbootes.repository;

import java.io.IOException;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.ScriptSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.springframework.stereotype.Component;

import com.dojoohwan.springbootes.document.ProductDocument;
import com.dojoohwan.springbootes.document.ProductUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import reactor.core.publisher.Flux;

@Component
public class ElasticsearchTestRepository implements ProductUseCase {

    private final RestHighLevelClient restHighLevelClient;

    public ElasticsearchTestRepository(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    @Override
    public Flux<ProductDocument> searchAutuocompleteByName(String name) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // query
        searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.multiMatchQuery(name, "name_auto.keyword","name_auto.ngram","name_auto.edge")));
        
        // sort
        Script script = new Script("doc['name_auto.keyword'].value.length()");
        searchSourceBuilder.sort(SortBuilders.scriptSort(script, ScriptSortBuilder.ScriptSortType.NUMBER));
        // highlight
        HighlightBuilder highlightBuilder = new HighlightBuilder().field("name_auto.*");
        searchSourceBuilder.highlighter(highlightBuilder);

        //System.out.println(searchSourceBuilder);
        
        return getProductFlux(searchSourceBuilder);
    }

    private Flux<ProductDocument> getProductFlux(SearchSourceBuilder searchSourceBuilder) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(searchSourceBuilder);
        Gson gson = new GsonBuilder().create();

        return Flux.<ProductDocument>create(sink -> {
            ActionListener<SearchResponse> actionListener = new ActionListener<SearchResponse>() {
                @Override
                public void onResponse(SearchResponse searchResponse) {
                    
                    for(SearchHit hit : searchResponse.getHits()) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        try {
                            ProductDocument product = objectMapper.readValue(hit.getSourceAsString(), ProductDocument.class);
                            sink.next(product);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        
                    }
                    sink.complete();
                }

                @Override
                public void onFailure(Exception e) {
                }

            };

            restHighLevelClient.searchAsync(searchRequest, RequestOptions.DEFAULT, actionListener);
        });
    }

}



