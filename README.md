# task01
목표
검색어 자동완성을 위한 Elasticsearch 인덱스를 생성하고 REST API를 구현 합니다.

환경
- Spring Boot 기반으로 REST API를 개발합니다.
- 로컬 PC에 Elasticsearch를 설치하고, 검색어 추천을 위한 인덱스를 생성 후 샘플 추천 검색어를 색인합니다.
- 추천 검색어는 자동완성 구현을 위한 최소 데이터 이기 때문에, 테스트 하는 특정 검색어 패턴에 의해 나올 수 있는 검색어 5개 정도를 만드시면 됩니다.
- "나" 입력시 노출 될  추천 검색어는 "나이키", "나이키 골프", "운동화 나이키", "브랜드 나이키" , "반다나" 이런 형태로 만드시면 됩니다.
- 추천 검색어는 검색어 길이가 짧은 순으로 정렬되어야 합니다.

# Request Body

// 인덱스 설정
// PUT _index_template/template_1
{
  "index_patterns": [
    "t1-*"
  ],
  "template": {
    "settings": {
      "number_of_replicas": 0,
      "number_of_shards": 1,
      "max_ngram_diff":"20",
      "analysis":{
        "analyzer":{
          "ngram_analyzer":{
            "filter":[
              "lowercase",
              "trim"
            ],
            "tokenizer":"ngram_tokenizer"
          },
          "edge_ngram_analyzer":{
            "filter":[
              "lowercase",
              "trim"
            ],
            "tokenizer":"edge_ngram_tokenizer"
          }
        },
        "tokenizer":{
          "ngram_tokenizer":{
            "type":"ngram",
            "min_gram":1,
            "max_gram":20,
            "token_chars":[
              "letter",
              "digit"
            ]
          },
          "edge_ngram_tokenizer":{
            "type":"edge_ngram",
            "min_gram":1,
            "max_gram":20,
            "token_chars":[
              "letter",
              "digit"
            ]
          }
        }
      }
    },
    "mappings": {
      "dynamic_templates": [
        {
          "suffix_auto":{
            "match":"*_auto",
            "mapping":{
              "type":"text",
              "fields":{
                "keyword":{
                  "type":"keyword"
                },
                "ngram":{
                  "type":"text",
                  "term_vector":"with_positions_offsets",
                  "analyzer":"ngram_analyzer"
                },
                "edge":{
                  "type":"text",
                  "term_vector":"with_positions_offsets",
                  "analyzer":"edge_ngram_analyzer"
                }
              }
            }
          }
        }
      ]
    }
  }
}

// 샘플 데이터 색인
//PUT t1-test01/_doc/1
{
  "name_auto":"나이키"
}
//PUT t1-test01/_doc/2
{
  "name_auto":"나이키 골프"
}
//PUT t1-test01/_doc/3
{
  "name_auto":"운동화 나이키"
}
//PUT t1-test01/_doc/4
{
  "name_auto":"브랜드 나이키"
}
//PUT t1-test01/_doc/5
{
  "name_auto":"반다나"
}
//PUT t1-test01/_doc/7
{
  "name_auto":"나이키 옷"
}
//PUT t1-test01/_doc/6
{
  "name_auto":"나이키 신발"
}
