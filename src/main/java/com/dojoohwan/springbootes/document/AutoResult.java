package com.dojoohwan.springbootes.document;

import java.util.List;
import java.util.Map;

import javax.persistence.Entity;

import org.springframework.data.elasticsearch.annotations.Document;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@Document(indexName = "t1-test01")
public class AutoResult {
    private Long total;

    private List<Map<String,String>> hits;

    private String highLightString;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }


    public String getHighLightString() {
        return highLightString;
    }

    public void setHighLightString(String highLightString) {
        this.highLightString = highLightString;
    }

    public List<Map<String,String>> getHits() {
        return hits;
    }

    public void setHits(List<Map<String, String>> hits) {
        this.hits = hits;
    }

}
