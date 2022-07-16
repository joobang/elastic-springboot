package com.dojoohwan.springbootes.document;

import javax.persistence.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

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
@Setting(settingPath = "static/es-settings.json")
public class ProductDocument {
    
    @Field(type= FieldType.Text)
    private String name_auto;



    public void setName_auto(String name_auto) {
        this.name_auto = name_auto;
    }



    public String getName_auto() {
        return name_auto;
    }



}
