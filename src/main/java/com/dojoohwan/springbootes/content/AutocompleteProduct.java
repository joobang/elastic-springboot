import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
 
@Data
@Document(indexName = "autocomplete_product")
@Setting(settingPath = "elastic-setting.json")
public class ReservationIndex {
   
    @Id
    @Field(type = FieldType.Long)
    private int autoId;
 
    @Field(type = FieldType.Keyword)
    private String productName;
 
}