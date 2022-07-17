package com.dojoohwan.springbootes.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.ScriptSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dojoohwan.springbootes.document.AutoResult;
import com.google.gson.Gson;

@RestController
@RequestMapping("/es/search")
public class SearchAPIController {
    private Gson gson = new Gson();
    
    /*
     * connection create method
     */
    public RestHighLevelClient createConnection() {
        return new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost("127.0.0.1",9200,"http")
                    )
                );
    }

    @RequestMapping("/auto")
    @ResponseStatus(HttpStatus.OK)
    public String autoComplete(@RequestParam(name="name") String name) {
        String aliasName = "t1-test01";
        
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // query
        //searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.multiMatchQuery(name, "name_auto.keyword","name_auto.ngram","name_auto.edge")));
        searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.multiMatchQuery(name)
                                                                                 .field("name_auto.keyword",3.0f)
                                                                                .field("name_auto.edge")
                                                                                 .field("name_auto.ngram")));
        //searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.multiMatchQuery(name, "name_auto.ngram")));

        // sort
        // sort by score
        searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC)); 
        // sort by length
        Script script = new Script("doc['name_auto.keyword'].value.length()");
        
        searchSourceBuilder.sort(SortBuilders.scriptSort(script, ScriptSortBuilder.ScriptSortType.NUMBER));
        // highlight
        HighlightBuilder highlightBuilder = new HighlightBuilder().field("name_auto.*");
        searchSourceBuilder.highlighter(highlightBuilder);
        
        SearchRequest request = new SearchRequest(aliasName);
        request.source(searchSourceBuilder);

        //System.out.println(searchSourceBuilder);
        
        SearchResponse response = null;
        SearchHits searchHits = null;
        AutoResult resultMap = new AutoResult();
        
        try(RestHighLevelClient client = createConnection();){
            
            response = client.search(request, RequestOptions.DEFAULT);
            searchHits = response.getHits();
            
            TotalHits totalHits = searchHits.getTotalHits();
            long numHits = totalHits.value;
            resultMap.setTotal(numHits);
            List<Map<String,String>> resultList = new ArrayList();
            for( SearchHit hit : searchHits) {
                
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                Map<String, String> tmp_hits = new HashMap<>();
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                HighlightField highlight = highlightFields.get("name_auto.ngram");
                Text[] fragments = highlight.fragments();
                String fragmentString = fragments[0].string();
                
                tmp_hits.put("name_auto", sourceAsMap.get("name_auto")+"");
                tmp_hits.put("highlight", fragmentString);
                resultList.add(tmp_hits);
                                
            }
            resultMap.setHits(resultList);
        }catch (Exception e) {
            /*
             * 예외처리
             */
            e.printStackTrace();
        }
        
        return gson.toJson(resultMap); 
    }

}
