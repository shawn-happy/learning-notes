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

