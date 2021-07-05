## elasticsearch Search APIs

### URL Search API

语法：

```
get <index_name>/_search

post <index_name>/_search
{

}
```

说明：

* `<index_name>/_search`可以省略不写，如果不写，查询范围整个集群的所有索引
* `<index_name>/_search`支持通配符，比如`user*`表示查询范围所有以`user`开头的索引
* `<index_name>/_search`支持多个，中间以英文（半角）逗号隔开，比如`user1,user2/_search`表示查询范围是`user1,user2`这两个索引

* get请求可以在URL上加上请求参数，使用Query String Syntax
* post/get请求可以添加Request Body，使用Query Domain Specific Language(DSL)
* 具体可以参考[Search API](https://www.elastic.co/guide/en/elasticsearch/reference/7.9/search-search.html)

#### Query String Syntax

demo:

```
# 获取2012的电影
get movies/_search?q=2012&df=year&sort=year:desc&from=0&size=10&timeout=1s
```

* q指定查询语句，使用Query String Syntax
* df指定查询的字段
* sort指定排序规则
* from和size用于分页
* q可以指定字段，精确查询，模糊查询。
  * 单字段精确查询，`q=k:v`， 例如：`q=year:2012`
  * 泛查询，正对`_all`，所有字段：`q=v`，例如：`get movies/_search?q=2012`
  * Term查询 `Beautiful Mind`等效于`Beautiful OR Mind`
  * Phrase查询：`"Beautiful Mind"`等效于`Beautiful AND Mind`。要求前后顺序一致
  * 条件组合查询：
    * 单条件查询：`q=+k1:v1 -k2:v2 k3:v3`，`+` 前缀表示必须与查询条件匹配；类似地，`-` 前缀表示一定不与查询条件匹配；没有 `+` 或者 `-` 地所有其他条件都是可选的，匹配的越多，文档就越相关。例如：`get movies/_search?q=+year:2012 -title:"Bullet to the Head"`
    * 多条件组合查询：`AND / OR / NOT` 或者 `&& / || / !`，注意：必须是大写。
  * 范围查询：
    * 区间表示：[]闭区间，{}开区间
      * `year:{2019 TO 2018]`
      * `year:[* TO 2018]`
    * 算数表示：
      * `year:>2012`
      * `year:(>2012 && <=2018)`
      * `year:(+>2010 +<=2018)`
  * 通配符查询（通配符查询效率低，占用内存大，不建议使用。特别是放在最前面）
    * ?表示1个字符，*表示0个或多个字符：例如`GET /movies/_search?q=title:b*`
  * 正则表达式查询（查询效率低，不建议使用）：`GET /movies/_search?q=title:[bt]oy`
  * 模糊查询与近似查询：
    * 用 `~` 表示搜索单词可能有一两个字母写的不对，按照相似度返回结果，最多可以模糊 2 个距离。
      * `GET /movies/_search?q=title:beautifl~1`
      * `GET /movies/_search?q=title:"Lord Rings"~2`

#### Query Domain Specific Language(DSL)

举例：

```
# 查询2005年上映的电影
get movies/_search?q=year:2005

post movies/_search
{
  "query":{
    "match": {"year": 2005}
  }
}

```

* 分页查询：

  * ```json
    {
        "from": 10,
        "size": 20,
        "query": {
            "match_all": {}
        }
    }
    ```

  * from从0开始，默认返回10个结果，获取靠后的翻页成本较高。

* 排序

  * 最好是数字类型或者日期类型的字段排序

  * 因为对于多值类型或分析过的字段排序，系统会选一个值，无法得知该值

  * ```json
    {
        "sort": [{"order_date": "desc"}]
    }
    ```

* `_source filtering`

  * 如果`_source`没有存储，那就只返回匹配的文档的元数据

  * `_source`支持使用通配符： `_source["name*","desc*"]`

  * ```json
    {
        "_source": ["order_date", "order_date","category_keyword"]
    }
    ```

* 脚本字段

  * ```json
    {
        "script_field": {
            "new_field": {
                "script":{
                    "lang": "painless",
                    "source": "doc['order_date'].value+'hello'"
                }
            }
        }
    }
    ```

  * 用例：订单中有不同的汇率，需要结合汇率对订单价格进行排序。

### Term-Level Queries

* Term是表达语意的最小单位。搜索和利用统计语言模型进行自然语言处理都需要处理Term
* Term Level Query: Term Query / Range Query / Exists Query / Prefix Query / Wildcard Query
* 在Es中，Term Query，对输入不做分词。会将输入作为一个整体，在倒排索引中查找准确的词项，并且使用相关度算分公式为每个包含该词项的文档进行相关性算分
* 可以通过Constant Score将查询转换成一个Filtering，避免算分，并利用缓存，提高性能。

案例：

创建一个products的index，并插入3条数据

```
DELETE products
PUT products
{
  "settings": {
    "number_of_shards": 1
  }
}


POST /products/_bulk
{ "index": { "_id": 1 }}
{ "productID" : "XHDK-A-1293-#fJ3","desc":"iPhone" }
{ "index": { "_id": 2 }}
{ "productID" : "KDKE-B-9947-#kL5","desc":"iPad" }
{ "index": { "_id": 3 }}
{ "productID" : "JODL-X-1937-#pV7","desc":"MBP" }
```

#### Term Query

使用Term Query，查看desc的值是`iPhone`

```
POST /products/_search
{
  "query": {
    "term": {
      "desc": {
        "value":"iPhone"
      }
    }
  }
}
```

**结果：**

```json
{
  "took" : 0,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 0,
      "relation" : "eq"
    },
    "max_score" : null,
    "hits" : [ ]
  }
}
```

**思考：**document里明明有desc的值是`iPhone`的，为什么查不到数据呢？

**答案：**

由于插入一条document的时候，会做分词处理，使用的是Standard Analyzer，默认会转成小写字母，但是使用Term Query的时候，输入不会做分词处理，所以大写的P不会转成小写的p。如果查询的值是`iphone`就能得到结果

```
POST /products/_search
{
  "query": {
    "term": {
      "desc": {
        "value":"iphone"
      }
    }
  }
}
```

* 使用Term Query，根据productId查看

  ```
  POST /products/_search
  {
    "query": {
      "term": {
        "productID": {
          "value": "XHDK-A-1293-#fJ3"
        }
      }
    }
  }
  ```

  **结果：**

  ```json
  {
    "took" : 1,
    "timed_out" : false,
    "_shards" : {
      "total" : 1,
      "successful" : 1,
      "skipped" : 0,
      "failed" : 0
    },
    "hits" : {
      "total" : {
        "value" : 0,
        "relation" : "eq"
      },
      "max_score" : null,
      "hits" : [ ]
    }
  }
  ```

  **思考：**为什么查不到数据？

  **答案：**

  如果我们使用的分词器的语法对`XHDK-A-1293-#fJ3`这个text进行分词

  ```
  post _analyze
  {
    "analyzer": "standard",
    "text": "XHDK-A-1293-#fJ3"
  }
  ```

  **结果：**

  ```json
  {
    "tokens" : [
      {
        "token" : "xhdk",
        "start_offset" : 0,
        "end_offset" : 4,
        "type" : "<ALPHANUM>",
        "position" : 0
      },
      {
        "token" : "a",
        "start_offset" : 5,
        "end_offset" : 6,
        "type" : "<ALPHANUM>",
        "position" : 1
      },
      {
        "token" : "1293",
        "start_offset" : 7,
        "end_offset" : 11,
        "type" : "<NUM>",
        "position" : 2
      },
      {
        "token" : "fj3",
        "start_offset" : 13,
        "end_offset" : 16,
        "type" : "<ALPHANUM>",
        "position" : 3
      }
    ]
  }
  ```

  还是因为Term Query对输入不做分词的缘故，导致查询结果不符合预期。

  如果执行的是如下语句：

  ```
  POST /products/_search
  {
    "query": {
      "term": {
        "productID": {
          "value": "xhdk"
        }
      }
    }
  }
  ```

  则会返回对应的结果。

  如果想要全文匹配，可以执行如下语句：

  ```
  POST /products/_search
  {
    "query": {
      "term": {
        "productID.keyword": {
          "value": "XHDK-A-1293-#fJ3"
        }
      }
    }
  }
  ```

  为什么加上`keyword`就能全文匹配呢？

  这实际上index mapping的配置。

  ```
  GET /products/_mapping
  ```

  结果：

  ```json
  {
    "products" : {
      "mappings" : {
        "properties" : {
          "desc" : {
            "type" : "text",
            "fields" : {
              "keyword" : {
                "type" : "keyword",
                "ignore_above" : 256
              }
            }
          },
          "productID" : {
            "type" : "text",
            "fields" : {
              "keyword" : {
                "type" : "keyword",
                "ignore_above" : 256
              }
            }
          }
        }
      }
    }
  }
  ```

* 由于Term Query还会返回Score，比较影响性能，可以跳过算分的步骤

  * 将Query转成Filter，忽略TF-IDF计算，避免相关性算分的开销
  * Filter可以有效利用缓存

  ```
  POST /products/_search
  {
    "explain": true,
    "query": {
      "constant_score": {
        "filter": {
          "term": {
            "productID.keyword": "XHDK-A-1293-#fJ3"
          }
        }
      }
    }
  }
  ```

#### Structured Search

* 对结构化数据的搜索
  * 日期，bool类型和数字都是结构化的
* 文本也可以是结构化的
  * 如彩色笔可以有离散的颜色集合：red、green、blue
  * 一个blog可能被标记了tag：distributed 、search
  * 电商网站上的商品都有upcs(通用产品码universal product codes)或其他的唯一标识，它们都需要遵从严格规定的、结构化的格式。
* 布尔，时间，日期和数字这类结构化数据：有精确的格式，我们可以对这些格式进行逻辑操作。
* 结构化的文本可以做精确匹配或部分匹配
  * Term Query  /  Prefix Query
* 结构化结果只有“是”或“否”两个值
  * 根据场景需要，可以决定结构化搜索是否需要打分。

##### Boolean

数据准备：

```
DELETE products
POST /products/_bulk
{ "index": { "_id": 1 }}
{ "price" : 10,"avaliable":true,"date":"2018-01-01", "productID" : "XHDK-A-1293-#fJ3" }
{ "index": { "_id": 2 }}
{ "price" : 20,"avaliable":true,"date":"2019-01-01", "productID" : "KDKE-B-9947-#kL5" }
{ "index": { "_id": 3 }}
{ "price" : 30,"avaliable":true, "productID" : "JODL-X-1937-#pV7" }
{ "index": { "_id": 4 }}
{ "price" : 30,"avaliable":false, "productID" : "QQPX-R-3956-#aD8" }

GET products/_mapping
```

案例：

```
#对布尔值 match 查询，有算分
POST products/_search
{
  "profile": "true",
  "explain": true,
  "query": {
    "term": {
      "avaliable": true
    }
  }
}

#对布尔值，通过constant score 转成 filtering，没有算分
POST products/_search
{
  "profile": "true",
  "explain": true,
  "query": {
    "constant_score": {
      "filter": {
        "term": {
          "avaliable": true
        }
      }
    }
  }
}
```

##### Numeric Range

* gt 大于
* lt 小于
* gte 大于等于
* lte 小于等于

```
#数字类型 Term
POST products/_search
{
  "profile": "true",
  "explain": true,
  "query": {
    "term": {
      "price": 30
    }
  }
}

#数字类型 terms
POST products/_search
{
  "query": {
    "constant_score": {
      "filter": {
        "terms": {
          "price": [
            "20",
            "30"
          ]
        }
      }
    }
  }
}

#数字 Range 查询
GET products/_search
{
    "query" : {
        "constant_score" : {
            "filter" : {
                "range" : {
                    "price" : {
                        "gte" : 20,
                        "lte"  : 30
                    }
                }
            }
        }
    }
}
```

##### Date Range

| 表达式 | 说明    |
| ------ | ------- |
| `y`    | Years   |
| `M`    | Months  |
| `w`    | Weeks   |
| `d`    | Days    |
| `h`    | Hours   |
| `H`    | Hours   |
| `m`    | Minutes |
| `s`    | Seconds |

假设`now`表示现在时间是`2021-07-04 12:00:00`

| 表达式                | 说明                  |
| --------------------- | --------------------- |
| `now+1h`              | `2021-07-04 13:00:00` |
| `now-1h`              | `2021-07-04 11:00:00` |
| `2021.07.04\|\|+1M/d` | `2021-08-04 00:00:00` |

案列：

```
POST products/_search
{
    "query" : {
        "constant_score" : {
            "filter" : {
                "range" : {
                    "date" : {
                      "gte" : "now-5y"
                    }
                }
            }
        }
    }
}
```

##### Exists

如下情况，调用exists方法时不会返回结果

* 如果该字段不存在，对应的值为null或者[]

* 如果该字段存在，存在如下情况：
  * 空字符串`""`或者`"-"`
  * 数组中包含null，`[null, "foo"]`
  * 自定义了`null-value`，在定义index mapping的时候

```
POST products/_search
{
  "query": {
    "constant_score": {
      "filter": {
        "exists": {
          "field": "date"
        }
      }
    }
  }
}

POST products/_search
{
  "query": {
    "constant_score": {
      "filter": {
        "bool": {
          "must_not": {
            "exists": {
              "field": "date"
            }
          }
        }
      }
    }
  }
}
```

##### Terms

查找**包含**多个精确值，注意**包含而不是相等**

```

PUT my-index-000001
{
  "mappings": {
    "properties": {
      "color": { "type": "keyword" }
    }
  }
}

PUT my-index-000001/_bulk
{"index": {"_id": 1}}
{"color": ["blue", "green"]}
{"index": {"_id": 2}}
{"color": "blue"}

GET my-index-000001/_search?pretty
{
  "query": {
    "terms": {
        "color" : {
            "index" : "my-index-000001",
            "id" : "2",
            "path" : "color"
        }
    }
  }
}

POST movies/_search
{
  "query": {
    "constant_score": {
      "filter": {
        "term": {
          "genre.keyword": "Comedy"
        }
      }
    }
  }
}

POST products/_search
{
  "query": {
    "constant_score": {
      "filter": {
        "terms": {
          "productID.keyword": [
            "QQPX-R-3956-#aD8",
            "JODL-X-1937-#pV7"
          ]
        }
      }
    }
  }
}
```



### Full Text Query

* Full Text Query的分类
  * Match Query
  * Match Phrase Query
  * Query String Query
  * Multi Match Query
  * Simple Query String Query
* 特点
  * 索引和搜索时都会进行分词，查询字符串先传递到一个合适的分词器，然后生成一个供查询的词项列表。
  * 查询时候，先会对输入的查询进行分词，然后每个词项逐个进行底层的查询，最终将结果进行合并。并未每个文档生成一个算分。

#### Query String Query 

类似[URL Search](#URL Search API)

* Query String Query

  * ```json
    GET /movies/_search
    {
    	"profile": true,
    	"query":{
    		"query_string":{
    			"default_field": "title",
    			"query": "Beautiful AND Mind"
    		}
    	}
    }
    ```

  * ```json
    GET /movies/_search
    {
    	"profile": true,
    	"query":{
    		"query_string":{
    			"fields":[
    				"title",
    				"year"
    			],
    			"query": "2012"
    		}
    	}
    }
    ```

#### Simple Query String Query

* 类似Query String，但是会忽略错误的语法。
* 只支持部分查询语句
* 不支持AND OR NOT，会被当做字符串处理
* Term之间默认的关系是OR，可以指定Operator
* 支持部分逻辑
  * +替代AND
  * |替代OR
  * -替代NOT

```
GET /movies/_search
{
	"profile":true,
	"query":{
		"simple_query_string":{
			"query":"Beautiful +mind",
			"fields":["title"]
		}
	}
}
```

#### Match Query

```
# 查看title里包含Beautiful OR Mind的电影
POST movies/_search
{
  "query": {
    "match": {
      "title": {
        "query": "Beautiful Mind"
      }
    }
  }
}
# 查看title里包含Beautiful AND Mind的电影
POST movies/_search
{
  "query": {
    "match": {
      "title": {
        "query": "Beautiful Mind",
        "operator": "AND"
      }
    }
  }
}
```

#### Match Phrase Query

与Match Query不同的是，不会对查询的text进行分词，还是作为一个完整的短语。

```
POST movies/_search
{
  "query": {
    "match_phrase": {
      "title":{
        "query": "one I love"

      }
    }
  }
}

POST movies/_search
{
  "query": {
    "match_phrase": {
      "title":{
        "query": "one love",
        "slop": 1
      }
    }
  }
}
```

这种精确匹配在大部分情况下显得太严苛了，有时我们想要包含 ""I like swimming and riding!"" 的文档也能够匹配 "I like riding"。这时就要以用到 "slop" 参数来控制查询语句的灵活度。

`slop` 参数告诉 `match_phrase` 查询词条相隔多远时仍然能将文档视为匹配 什么是相隔多远？ 意思是说为了让查询和文档匹配你需要移动词条多少次？

#### Multi Match Query

`multi_match` 查询建立在 `match` 查询之上，重要的是它允许对多个字段查询。

| 类型            | 说明                                                         | 备注                                                         |
| --------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| `Best Fields`   | 查找匹配任何字段的文档，但使用来自最佳字段的 _score          | 当字段之间相互竞争，又相互关联。评分来自最匹配的字段。       |
| `Most Fields`   | 多个字段都包含相同的文本的场合，会将所有字段的评分合并起来   | 处理英文内容时：一种常见的手段是，在主字段(Engilsh Analyzer)，抽取词干，以匹配更多的文档。相同的文本，加入子字段（Standard Analyzer），以提供更加精确的匹配。其他字段作为匹配文档提高相关度的信号。匹配字段越多则越好。<br />无法使用Operator<br />可以用copy_to解决，但需要额外的存储空间 |
| `Cross Fields`  | 首先分析查询字符串并生成一个词列表，然后**从所有字段中依次搜索每个词**，只要查询到，就算匹配上。 | 对于某些实体，例如人名，地址，图书信息。需要在多个字段中确定信息，单个字段只能作为整体的一部分。希望在任何这些列出的字段中找到尽可能多的词。<br />支持operator<br />与copy_to相比，它可以在搜索时为单个字段提升权重 |
| `phrase`        | 同match_phrase + best_field                                  |                                                              |
| `phrase_prefix` | 同match_phrase_prefix + best_field                           |                                                              |
| `bool_prefix`   | 同match_bool_prefix + most field                             |                                                              |

```
POST blogs/_search
{
    "query": {
        "dis_max": {
            "queries": [
                { "match": { "title": "Quick pets" }},
                { "match": { "body":  "Quick pets" }}
            ],
            "tie_breaker": 0.2
        }
    }
}

POST blogs/_search
{
  "query": {
    "multi_match": {
      "type": "best_fields",
      "query": "Quick pets",
      "fields": ["title","body"],
      "tie_breaker": 0.2,
      "minimum_should_match": "20%"
    }
  }
}



POST books/_search
{
    "multi_match": {
        "query":  "Quick brown fox",
        "fields": "*_title"
    }
}


POST books/_search
{
    "multi_match": {
        "query":  "Quick brown fox",
        "fields": [ "*_title", "chapter_title^2" ]
    }
}

DELETE /titles
PUT /titles
{
  "mappings": {
    "properties": {
      "title": {
        "type": "text",
        "analyzer": "english",
        "fields": {"std": {"type": "text","analyzer": "standard"}}
      }
    }
  }
}

POST titles/_bulk
{ "index": { "_id": 1 }}
{ "title": "My dog barks" }
{ "index": { "_id": 2 }}
{ "title": "I see a lot of barking dogs on the road " }

GET /titles/_search
{
   "query": {
        "multi_match": {
            "query":  "barking dogs",
            "type":   "most_fields",
            "fields": [ "title", "title.std" ]
        }
    }
}

GET /titles/_search
{
   "query": {
        "multi_match": {
            "query":  "barking dogs",
            "type":   "most_fields",
            "fields": [ "title^10", "title.std" ]
        }
    }
}
```

### Compound queries

#### Query Context & Filter Context

* 高级搜索的功能：支持多项文本输入，针对多个字段进行搜索。
* 搜索引擎一般也提供基于时间，价格等条件的过滤
* 在es中，有Query和Filter两种不同的Context
  * Query Context：相关性算分
  * Filter Context: 不需要算分，可以利用Cache，获得更好的性能

#### Boolean Query

案例：

* 假设要搜索一本电影，包含了以下一些条件
  * 评论中包含了Guitar，用户打分高于3分，同时上映日期要在1993与2000年之间
* 这个搜索包含了3段逻辑
  * 评论字段中要包含Guitar
  * 用户评分字段要高于3分
  * 上映日期字段需要在给定的范围

特点：

* 一个boolean Query，是一个或多个查询子句的组合
  * 总共包括4种子句，其中2种影响算分，2种不影响
    * `must:` 必须匹配，贡献算分
    * `should:`选择性匹配，贡献算分
    * `must_not:`Filter Context 查询子句，必须不能匹配，不贡献算分
    * `filter:`Filter Context必须匹配，但是不贡献算分
* 相关性并不只是全文检索的专利，也适用于`yes|no`的子句，匹配的子句越多，相关性评分越高。如果多条查询子句被合并为一条复合查询语句，比如boolean query，则每个查询子句计算得出的评分会被合到总的相关性评分中。
* 同一层级下的竞争字段，具有相同的权重
* 通过嵌套boolean query，可以改变对算分的影响
* should里嵌套must_not子查询，可以实现should not的逻辑

语法：

* 子查询可以任意顺序出现
* 可以嵌套子查询
* 如果没有Must条件，should中必须满足其中一条查询，使用数组

```
POST /products/_search
{
  "query": {
    "bool" : {
      "must" : {
        "term" : { "price" : "30" }
      },
      "filter": {
        "term" : { "avaliable" : "true" }
      },
      "must_not" : {
        "range" : {
          "price" : { "lte" : 10 }
        }
      },
      "should" : [
        { "term" : { "productID.keyword" : "JODL-X-1937-#pV7" } },
        { "term" : { "productID.keyword" : "XHDK-A-1293-#fJ3" } }
      ],
      "minimum_should_match" :1
    }
  }
}
```

如何解决Terms Query遗留下来的问题，包含而不是相等。

增加count字段，使用boolean query解决

```
#改变数据模型，增加字段。解决数组包含而不是精确匹配的问题
POST /newmovies/_bulk
{ "index": { "_id": 1 }}
{ "title" : "Father of the Bridge Part II","year":1995, "genre":"Comedy","genre_count":1 }
{ "index": { "_id": 2 }}
{ "title" : "Dave","year":1993,"genre":["Comedy","Romance"],"genre_count":2 }

#must，有算分
POST /newmovies/_search
{
  "query": {
    "bool": {
      "must": [
        {"term": {"genre.keyword": {"value": "Comedy"}}},
        {"term": {"genre_count": {"value": 1}}}

      ]
    }
  }
}

#Filter。不参与算分，结果的score是0
POST /newmovies/_search
{
  "query": {
    "bool": {
      "filter": [
        {"term": {"genre.keyword": {"value": "Comedy"}}},
        {"term": {"genre_count": {"value": 1}}}
        ]

    }
  }
}

#Query Context
POST /products/_search
{
  "query": {
    "bool": {
      "should": [
        {
          "term": {
            "productID.keyword": {
              "value": "JODL-X-1937-#pV7"}}
        },
        {"term": {"avaliable": {"value": true}}
        }
      ]
    }
  }
}

#嵌套，实现了 should not 逻辑
POST /products/_search
{
  "query": {
    "bool": {
      "must": {
        "term": {
          "price": "30"
        }
      },
      "should": [
        {
          "bool": {
            "must_not": {
              "term": {
                "avaliable": "false"
              }
            }
          }
        }
      ],
      "minimum_should_match": 1
    }
  }
}

#Controll the Precision
POST _search
{
  "query": {
    "bool" : {
      "must" : {
        "term" : { "price" : "30" }
      },
      "filter": {
        "term" : { "avaliable" : "true" }
      },
      "must_not" : {
        "range" : {
          "price" : { "lte" : 10 }
        }
      },
      "should" : [
        { "term" : { "productID.keyword" : "JODL-X-1937-#pV7" } },
        { "term" : { "productID.keyword" : "XHDK-A-1293-#fJ3" } }
      ],
      "minimum_should_match" :2
    }
  }
}
```

#### Boosting Query

* Boosting 是控制相关度的一种手段
  * 索引，字段或者查询子条件
* 参数boost的含义
  * 当boost > 1时，打分的相关度相对性提升
  * 当0<boost<1时，打分的权重相对性降低
  * 当boost<0时，贡献负分
* 希望包含了某项内容的结果不是不出现，而是排序靠后。

案例：

```
DELETE blogs
POST /blogs/_bulk
{ "index": { "_id": 1 }}
{"title":"Apple iPad", "content":"Apple iPad,Apple iPad" }
{ "index": { "_id": 2 }}
{"title":"Apple iPad,Apple iPad", "content":"Apple iPad" }


POST blogs/_search
{
  "query": {
    "bool": {
      "should": [
        {"match": {
          "title": {
            "query": "apple,ipad",
            "boost": 1.1
          }
        }},

        {"match": {
          "content": {
            "query": "apple,ipad",
            "boost": 2
          }
        }}
      ]
    }
  }
}


DELETE news
POST /news/_bulk
{ "index": { "_id": 1 }}
{ "content":"Apple Mac" }
{ "index": { "_id": 2 }}
{ "content":"Apple iPad" }
{ "index": { "_id": 3 }}
{ "content":"Apple employee like Apple Pie and Apple Juice" }


POST news/_search
{
  "query": {
    "bool": {
      "must": {
        "match":{"content":"apple"}
      }
    }
  }
}

POST news/_search
{
  "query": {
    "bool": {
      "must": {
        "match":{"content":"apple"}
      },
      "must_not": {
        "match":{"content":"pie"}
      }
    }
  }
}

POST news/_search
{
  "query": {
    "boosting": {
      "positive": {
        "match": {
          "content": "apple"
        }
      },
      "negative": {
        "match": {
          "content": "pie"
        }
      },
      "negative_boost": 0.5
    }
  }
}
```

* positive: 必须存在,查询对象,指定希望执行的查询子句,返回的结果都将满足该子句指定的条件
* negative:必须存在,查询对象,指定的查询子句用于降低匹配文档的相似度分
*  negative_boost：必须存在,浮点数,介于0与1.0之间的浮点数,用于降低匹配文档的相似分

#### Constant Score Query

#### Disjunction Max Query

单字符串查询的实例

```
PUT /blogs/_doc/1
{
    "title": "Quick brown rabbits",
    "body":  "Brown rabbits are commonly seen."
}

PUT /blogs/_doc/2
{
    "title": "Keeping pets healthy",
    "body":  "My quick brown fox eats rabbits on a regular basis."
}

POST /blogs/_search
{
    "query": {
        "bool": {
            "should": [
                { "match": { "title": "Brown fox" }},
                { "match": { "body":  "Brown fox" }}
            ]
        }
    }
}
```

预期：

title:文档1中出现了Brown

body:文档1中出现了Brown，文档2中出现了Brown fox，并且保持和查询一致的顺序，目测应该是文档2的相关性算分最高。

结果：

文档1的算分比文档2的高。

```json
{
  "took" : 0,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 2,
      "relation" : "eq"
    },
    "max_score" : 0.90425634,
    "hits" : [
      {
        "_index" : "blogs",
        "_type" : "_doc",
        "_id" : "1",
        "_score" : 0.90425634,
        "_source" : {
          "title" : "Quick brown rabbits",
          "body" : "Brown rabbits are commonly seen."
        }
      },
      {
        "_index" : "blogs",
        "_type" : "_doc",
        "_id" : "2",
        "_score" : 0.77041256,
        "_source" : {
          "title" : "Keeping pets healthy",
          "body" : "My quick brown fox eats rabbits on a regular basis."
        }
      }
    ]
  }
}
```

算分过程：

* 查询should语句中的两个查询
* 两个查询的评分相加
* 乘以匹配语句的总数
* 除以所以语句的总数

可以使用explain看一下查询结果和分析

title和body相互竞争，不应该将分数简单叠加，而是应该找到单个最佳匹配的字段的评分。Disjunction Max Query将任何与任意查询匹配的文档作为结果返回。采用字段上最匹配的评分最终评分返回。

```
POST blogs/_search
{
    "query": {
        "dis_max": {
            "queries": [
                { "match": { "title": "Brown fox" }},
                { "match": { "body":  "Brown fox" }}
            ]
        }
    }
}
```

这样返回的结果就会符合预期。

tie_breaker参数：

* 获得最佳匹配语句的评分_score。
* 将其他匹配语句的评分与tie_breaker相乘
* 对以上评分求和并规范化
* 是一个介于0-1之间的浮点数。0代表使用最佳匹配，1代表所有语句同等重要。

#### Function Score Query

算分与排序

* Elasticsearch默认会以文档的相关度算分进行排序
* 可以指定一个或多个字段进行排序
* 使用相关度算分排序，不能满足某些特定条件
  * 无法针对相关度，对排序实现更多的控制

Function Score Query

* 可以在查询后，对每个匹配的文档进行一系列重新算分，根据新生成的分数重新排序。
* function
  * weight: 为每一个文档设置一个简单而不被规范化的权重
  * Field Value Factor:使用该数值来修改_score，例如将”热度“和”点赞数“作为算分的参考因素
  * Random Score：为每一个用户使用不同的，随机算分结果
  * 衰减函数：以某个字段的值为标准，距离某个值越近，得分越高
  * Script Score：自定义脚本完全控制所需逻辑
* Boost Mode
  * Multiply: 算分与函数值的成绩
  * Sum:算分与函数的和
  * Min/Max: 算分与函数取最小、最大值
  * Replace: 使用函数值取代算分
* Max Boost可以将算分控制在一个最大值
* 一致性随机函数：
  * 使用场景：网站的广告需要提供展现率
  * 具体需求：让每个用户能看到不同的随机数，也希望同一个用户访问的时候，结果的相对顺序一致

```
DELETE blogs
PUT /blogs/_doc/1
{
  "title":   "About popularity",
  "content": "In this post we will talk about...",
  "votes":   0
}

PUT /blogs/_doc/2
{
  "title":   "About popularity",
  "content": "In this post we will talk about...",
  "votes":   100
}

PUT /blogs/_doc/3
{
  "title":   "About popularity",
  "content": "In this post we will talk about...",
  "votes":   1000000
}


POST /blogs/_search
{
  "query": {
    "function_score": {
      "query": {
        "multi_match": {
          "query":    "popularity",
          "fields": [ "title", "content" ]
        }
      },
      "field_value_factor": {
        "field": "votes"
      }
    }
  }
}

POST /blogs/_search
{
  "query": {
    "function_score": {
      "query": {
        "multi_match": {
          "query":    "popularity",
          "fields": [ "title", "content" ]
        }
      },
      "field_value_factor": {
        "field": "votes",
        "modifier": "log1p"
      }
    }
  }
}


POST /blogs/_search
{
  "query": {
    "function_score": {
      "query": {
        "multi_match": {
          "query":    "popularity",
          "fields": [ "title", "content" ]
        }
      },
      "field_value_factor": {
        "field": "votes",
        "modifier": "log1p" ,
        "factor": 0.1
      }
    }
  }
}


POST /blogs/_search
{
  "query": {
    "function_score": {
      "query": {
        "multi_match": {
          "query":    "popularity",
          "fields": [ "title", "content" ]
        }
      },
      "field_value_factor": {
        "field": "votes",
        "modifier": "log1p" ,
        "factor": 0.1
      },
      "boost_mode": "sum",
      "max_boost": 3
    }
  }
}

POST /blogs/_search
{
  "query": {
    "function_score": {
      "random_score": {
        "seed": 911119
      }
    }
  }
}
```



### Search Template

* Elasticsearch的查询语句
  * 对相关性算分、查询性能都至关重要
* 在开发初期，虽说可以明确查询参数，但是往往不能最终定义查询的DSL的具体结构
  * 通过Search Template定义一个Contract
* 各司其职，解耦
  * 开发人员、搜索工程师，性能工程师

```console
GET _search/template
{
  "source" : {
    "query": { "match" : { "{{my_field}}" : "{{my_value}}" } },
    "size" : "{{my_size}}"
  },
  "params" : {
    "my_field" : "message",
    "my_value" : "foo",
    "my_size" : 5
  }
}
```

### Suggester API

* 什么是搜索建议
  * 现代的搜索引擎，一般都会提供Suggest as you type的功能
  * 帮助用户在输入搜索的过程中，进行自动补全或者纠错，通过协助用户输入更加精准的关键词，提高后续搜索阶段文档匹配的程度
  * 在google上搜索，一开始会自动补全，当输入到一定长度，如因为单词拼写错误无法补全，就会开始提示相似的词或者句子
* API
  * 搜索引擎中类似的功能，es是通过Suggester API实现的
  * 原理：将输入的文本分为Token，然后在索引的字典里查找相似的Term并返回。
  * Term Suggester（纠错补全，输入错误的情况下补全正确的单词）
  * Phrase Suggester（自动补全短语，输入一个单词补全整个短语）
  * Complete Suggester(完成补全单词，输出如前半部分，补全整个单词）
  * Context Suggester（上下文补全）
* Suggestion Mode
  * Missing-如索引中已经存在，就不提供建议
  * Popular-推荐出现频率更高的词
  * Always-无论是否存在，都提供建议
* 精准度和召回率比较
  * 精准度
    * completion > phrase > term
  * 召回率
    * term > phrase > completion
  * 性能
    * completion > phrase > term

#### Term Suggester && Prase Suggester

Term Suggester 先将搜索词进行分词，然后逐个与指定的索引数据进行比较，计算出编辑距离再返回建议词。


编辑距离：这里使用了叫做Levenstein edit distance的算法，核心思想就是一个词改动多少次就可以和另外的词一致。比如说为了从elasticseach得到elasticsearch，就必须加入1个字母 r ，也就是改动1次，所以这两个词的编辑距离就是1。

Prase Suggester在Term Suggester上增加了一些逻辑

Prase Suggester常用参数里`max errors`：最多可以拼错的Terms数，`confidence`：限制返回结果数，默认为1

```
DELETE articles
PUT articles
{
  "mappings": {
    "properties": {
      "title_completion":{
        "type": "completion"
      }
    }
  }
}

POST articles/_bulk
{ "index" : { } }
{ "title_completion": "lucene is very cool"}
{ "index" : { } }
{ "title_completion": "Elasticsearch builds on top of lucene"}
{ "index" : { } }
{ "title_completion": "Elasticsearch rocks"}
{ "index" : { } }
{ "title_completion": "elastic is the company behind ELK stack"}
{ "index" : { } }
{ "title_completion": "Elk stack rocks"}
{ "index" : {} }


POST articles/_search?pretty
{
  "size": 0,
  "suggest": {
    "article-suggester": {
      "prefix": "elk ",
      "completion": {
        "field": "title_completion"
      }
    }
  }
}

DELETE articles

POST articles/_bulk
{ "index" : { } }
{ "body": "lucene is very cool"}
{ "index" : { } }
{ "body": "Elasticsearch builds on top of lucene"}
{ "index" : { } }
{ "body": "Elasticsearch rocks"}
{ "index" : { } }
{ "body": "elastic is the company behind ELK stack"}
{ "index" : { } }
{ "body": "Elk stack rocks"}
{ "index" : {} }
{  "body": "elasticsearch is rock solid"}


POST _analyze
{
  "analyzer": "standard",
  "text": ["Elk stack  rocks rock"]
}

POST /articles/_search
{
  "size": 1,
  "query": {
    "match": {
      "body": "lucen rock"
    }
  },
  "suggest": {
    "term-suggestion": {
      "text": "lucen rock",
      "term": {
        "suggest_mode": "missing",
        "field": "body"
      }
    }
  }
}


POST /articles/_search
{

  "suggest": {
    "term-suggestion": {
      "text": "lucen rock",
      "term": {
        "suggest_mode": "popular",
        "field": "body"
      }
    }
  }
}


POST /articles/_search
{

  "suggest": {
    "term-suggestion": {
      "text": "lucen rock",
      "term": {
        "suggest_mode": "always",
        "field": "body",
      }
    }
  }
}


POST /articles/_search
{

  "suggest": {
    "term-suggestion": {
      "text": "lucen hocks",
      "term": {
        "suggest_mode": "always",
        "field": "body",
        "prefix_length":0,
        "sort": "frequency"
      }
    }
  }
}


POST /articles/_search
{
  "suggest": {
    "my-suggestion": {
      "text": "lucne and elasticsear rock hello world ",
      "phrase": {
        "field": "body",
        "max_errors":2,
        "confidence":0,
        "direct_generator":[{
          "field":"body",
          "suggest_mode":"always"
        }],
        "highlight": {
          "pre_tag": "<em>",
          "post_tag": "</em>"
        }
      }
    }
  }
}
```

#### Complection Suggester

* Complection Suggester提供了Auto Complete的功能。用户每输入一个字符，就需要即时发送一个查询请求到后端查找匹配项。
* 对性能要求比较苛刻，elasticsearch采用了不同的数据结构，并非通过倒排索引来完成的，而是将Analyzer的数据编码成FST和索引一起存放，FST会被ES整个加载到内存，速度很快
* FST只能用于前缀查找
* 定义mapping, 使用completion type
* 索引数据
* 运行suggest查询

#### context Suggester

* 扩展了Completion Suggester
* 可以在搜索中加入更多的上下文信息，例如输入“star”
  * 咖啡相关：建议“starbucks”
  * 电影相关：建议”star wars“
* 定义两种类型的context
  * Category-任意的字符串
  * Geo-地理信息
* 定义mapping
  * type
  * name
* 索引数据，并且为每个document加入context信息
* 结合context进行suggestion查询

```
DELETE articles
PUT articles
{
  "mappings": {
    "properties": {
      "title_completion":{
        "type": "completion"
      }
    }
  }
}

POST articles/_bulk
{ "index" : { } }
{ "title_completion": "lucene is very cool"}
{ "index" : { } }
{ "title_completion": "Elasticsearch builds on top of lucene"}
{ "index" : { } }
{ "title_completion": "Elasticsearch rocks"}
{ "index" : { } }
{ "title_completion": "elastic is the company behind ELK stack"}
{ "index" : { } }
{ "title_completion": "Elk stack rocks"}
{ "index" : {} }


POST articles/_search?pretty
{
  "size": 0,
  "suggest": {
    "article-suggester": {
      "prefix": "elk ",
      "completion": {
        "field": "title_completion"
      }
    }
  }
}


DELETE comments
PUT comments
PUT comments/_mapping
{
  "properties": {
    "comment_autocomplete":{
      "type": "completion",
      "contexts":[{
        "type":"category",
        "name":"comment_category"
      }]
    }
  }
}

POST comments/_doc
{
  "comment":"I love the star war movies",
  "comment_autocomplete":{
    "input":["star wars"],
    "contexts":{
      "comment_category":"movies"
    }
  }
}

POST comments/_doc
{
  "comment":"Where can I find a Starbucks",
  "comment_autocomplete":{
    "input":["starbucks"],
    "contexts":{
      "comment_category":"coffee"
    }
  }
}


POST comments/_search
{
  "suggest": {
    "MY_SUGGESTION": {
      "prefix": "sta",
      "completion":{
        "field":"comment_autocomplete",
        "contexts":{
          "comment_category":"coffee"
        }
      }
    }
  }
}
```

### Cross Cluster Search

水平扩展的痛点：

* 单集群：
  * 当水平扩展时，节点数不能无限增加
  * 当集群的meta信息（节点，索引，集群状态）过多，会导致更新压力变大，单个active master会成为性能瓶颈，导致整个集群无法正常工作
* 早期版本，会通过tribe node可以实现多集群访问的需求，但是还存在一定的问题
  * tribe node会以client node的方式加入每个cluster，cluster中的master node的任务变更需要tribe node的回应才能继续
  * tribe node不能保存cluster state信息，一旦restart cluster，初始化很慢
  * 当多个cluster存在索引重名的情况下，只能设置一种prefer规则

Cross Cluster Search

* 早期tribe node的方案存在一定的问题，所以被deprecated
* es5.3引入了cross cluster search功能
  * 允许任何节点扮演federated节点，以轻量的方式，将搜搜请求进行代理
  * 不需要以client node 的形式加入其它集群

案例：

```
//启动3个集群

bin/elasticsearch -E node.name=cluster0node -E cluster.name=cluster0 -E path.data=cluster0_data -E discovery.type=single-node -E http.port=9200 -E transport.port=9300
bin/elasticsearch -E node.name=cluster1node -E cluster.name=cluster1 -E path.data=cluster1_data -E discovery.type=single-node -E http.port=9201 -E transport.port=9301
bin/elasticsearch -E node.name=cluster2node -E cluster.name=cluster2 -E path.data=cluster2_data -E discovery.type=single-node -E http.port=9202 -E transport.port=9302


//在每个集群上设置动态的设置
PUT _cluster/settings
{
  "persistent": {
    "cluster": {
      "remote": {
        "cluster0": {
          "seeds": [
            "127.0.0.1:9300"
          ],
          "transport.ping_schedule": "30s"
        },
        "cluster1": {
          "seeds": [
            "127.0.0.1:9301"
          ],
          "transport.compress": true,
          "skip_unavailable": true
        },
        "cluster2": {
          "seeds": [
            "127.0.0.1:9302"
          ]
        }
      }
    }
  }
}

#cURL
curl -XPUT "http://localhost:9200/_cluster/settings" -H 'Content-Type: application/json' -d'
{"persistent":{"cluster":{"remote":{"cluster0":{"seeds":["127.0.0.1:9300"],"transport.ping_schedule":"30s"},"cluster1":{"seeds":["127.0.0.1:9301"],"transport.compress":true,"skip_unavailable":true},"cluster2":{"seeds":["127.0.0.1:9302"]}}}}}'

curl -XPUT "http://localhost:9201/_cluster/settings" -H 'Content-Type: application/json' -d'
{"persistent":{"cluster":{"remote":{"cluster0":{"seeds":["127.0.0.1:9300"],"transport.ping_schedule":"30s"},"cluster1":{"seeds":["127.0.0.1:9301"],"transport.compress":true,"skip_unavailable":true},"cluster2":{"seeds":["127.0.0.1:9302"]}}}}}'

curl -XPUT "http://localhost:9202/_cluster/settings" -H 'Content-Type: application/json' -d'
{"persistent":{"cluster":{"remote":{"cluster0":{"seeds":["127.0.0.1:9300"],"transport.ping_schedule":"30s"},"cluster1":{"seeds":["127.0.0.1:9301"],"transport.compress":true,"skip_unavailable":true},"cluster2":{"seeds":["127.0.0.1:9302"]}}}}}'


#创建测试数据
curl -XPOST "http://localhost:9200/users/_doc" -H 'Content-Type: application/json' -d'
{"name":"user1","age":10}'

curl -XPOST "http://localhost:9201/users/_doc" -H 'Content-Type: application/json' -d'
{"name":"user2","age":20}'

curl -XPOST "http://localhost:9202/users/_doc" -H 'Content-Type: application/json' -d'
{"name":"user3","age":30}'


#查询
GET /users,cluster1:users,cluster2:users/_search
{
  "query": {
    "range": {
      "age": {
        "gte": 20,
        "lte": 40
      }
    }
  }
}

```

### resources

[REST APIs](https://www.elastic.co/guide/en/elasticsearch/reference/7.9/rest-apis.html)

[Search APIs](https://www.elastic.co/guide/en/elasticsearch/reference/7.9/search.html)

[Query DSL](https://www.elastic.co/guide/en/elasticsearch/reference/7.9/query-dsl.html)

