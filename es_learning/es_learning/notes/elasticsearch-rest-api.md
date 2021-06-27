## elasticsearch操作

### 前期准备

通过logstash，往elasticsearch里导入了相关的测试数据，步骤如下：

1. 进入到logstash的安装目录

   ```shell
   cd /home/work/soft/logstash
   ```

2. 下载并解压测试数据

   ```shell
   wget https://files.grouplens.org/datasets/movielens/ml-latest-small.zip
   unzip ml-latest-small.zip
   ```

3. 创建并修改`logstash.conf`配置文件

   ```shell
   vim logstash.conf
   ```

   将下面的内容复制到`logstash.conf`文件中

   ```
   input {
     file {
       # path 为数据集的位置，需要你根据自己的位置去配置
       path => "/home/work/soft/logstash/ml-latest-small/movies.csv"
       start_position => "beginning"
       sincedb_path => "/dev/null"
     }
   }
   filter {
     csv {
       separator => ","
       columns => ["id","content","genre"]
     }
   
     mutate {
       split => { "genre" => "|" }
       remove_field => ["path", "host","@timestamp","message"]
     }
   
     mutate {
   
       split => ["content", "("]
       add_field => { "title" => "%{[content][0]}"}
       add_field => { "year" => "%{[content][1]}"}
     }
   
     mutate {
       convert => {
         "year" => "integer"
       }
       strip => ["title"]
       remove_field => ["path", "host","@timestamp","message","content"]
     }
   
   }
   output {
      elasticsearch {
        hosts => "http://1ocalhost:9200"
        index => "movies"
        document_id => "%{id}"
        }
     stdout {}
   }
   ```

4. 启动logstash导入数据

   ```shell
   ./bin/logstash -f ./logstash.conf
   ```

Index: movies

### 基本的CURD操作

#### REST API

##### Index apis

* 查看索引相关信息（Mapping & Setting）

  * 语法：`get <index_name>`

  * 举例：`get movies`

  * 结果：

    ```json
    {
      "movies" : {
        "aliases" : { },
        "mappings" : {
          "properties" : {
            "@version" : {
              "type" : "text",
              "fields" : {
                "keyword" : {
                  "type" : "keyword",
                  "ignore_above" : 256
                }
              }
            },
            "genre" : {
              "type" : "text",
              "fields" : {
                "keyword" : {
                  "type" : "keyword",
                  "ignore_above" : 256
                }
              }
            },
            "id" : {
              "type" : "text",
              "fields" : {
                "keyword" : {
                  "type" : "keyword",
                  "ignore_above" : 256
                }
              }
            },
            "title" : {
              "type" : "text",
              "fields" : {
                "keyword" : {
                  "type" : "keyword",
                  "ignore_above" : 256
                }
              }
            },
            "year" : {
              "type" : "long"
            }
          }
        },
        "settings" : {
          "index" : {
            "creation_date" : "1624690171977",
            "number_of_shards" : "1",
            "number_of_replicas" : "1",
            "uuid" : "HqFyAwvOQ8Ctfwy7Cbwz-A",
            "version" : {
              "created" : "7090299"
            },
            "provided_name" : "movies"
          }
        }
      }
    }
    ```

* 查看index mapping信息

  * 语法：`get <index_name>/_mappings`
  * 举例：`get movies/_mappings?pretty`

* 查看index settings信息

  * 语法：`get <index_name>/_settings`
  * 举例：`get <index_name>/_settings`

* 查看index document总数

  * 语法：`get <index_name>/_count`

  * 举例：`get movies/_count`

  * 结果：

    ```json
    {
      "count" : 9743,
      "_shards" : {
        "total" : 1,
        "successful" : 1,
        "skipped" : 0,
        "failed" : 0
      }
    }
    ```

* 查看document前10条数据

  * 语法：`get <index_name>/_search`

  * 举例：`get movies/_search`

  * 结果：

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
          "value" : 9743,
          "relation" : "eq"
        },
        "max_score" : 1.0,
        "hits" : [
          {
            "_index" : "movies",
            "_type" : "_doc",
            "_id" : "37475",
            "_score" : 1.0,
            "_source" : {
              "genre" : [
                "Drama"
              ],
              "id" : "37475",
              "year" : 2005,
              "title" : "Unfinished Life, An",
              "@version" : "1"
            }
          },
          {
            "_index" : "movies",
            "_type" : "_doc",
            "_id" : "37477",
            "_score" : 1.0,
            "_source" : {
              "genre" : [
                "Action",
                "Comedy",
                "Crime"
              ],
              "id" : "37477",
              "year" : 2005,
              "title" : "Man, The",
              "@version" : "1"
            }
          },
          {
            "_index" : "movies",
            "_type" : "_doc",
            "_id" : "37495",
            "_score" : 1.0,
            "_source" : {
              "genre" : [
                "Fantasy",
                "Mystery",
                "Romance",
                "Thriller"
              ],
              "id" : "37495",
              "year" : 2004,
              "title" : "Survive Style 5+",
              "@version" : "1"
            }
          },
          {
            "_index" : "movies",
            "_type" : "_doc",
            "_id" : "37545",
            "_score" : 1.0,
            "_source" : {
              "genre" : [
                "Drama"
              ],
              "id" : "37545",
              "year" : 1979,
              "title" : "Woyzeck",
              "@version" : "1"
            }
          },
          {
            "_index" : "movies",
            "_type" : "_doc",
            "_id" : "37720",
            "_score" : 1.0,
            "_source" : {
              "genre" : [
                "Crime",
                "Drama",
                "Horror",
                "Thriller"
              ],
              "id" : "37720",
              "year" : 2005,
              "title" : "Exorcism of Emily Rose, The",
              "@version" : "1"
            }
          },
          {
            "_index" : "movies",
            "_type" : "_doc",
            "_id" : "37727",
            "_score" : 1.0,
            "_source" : {
              "genre" : [
                "Action",
                "Drama",
                "Thriller"
              ],
              "id" : "37727",
              "year" : 2005,
              "title" : "Flightplan",
              "@version" : "1"
            }
          },
          {
            "_index" : "movies",
            "_type" : "_doc",
            "_id" : "37729",
            "_score" : 1.0,
            "_source" : {
              "genre" : [
                "Animation",
                "Comedy",
                "Fantasy",
                "Musical",
                "Romance"
              ],
              "id" : "37729",
              "year" : 2005,
              "title" : "Corpse Bride",
              "@version" : "1"
            }
          },
          {
            "_index" : "movies",
            "_type" : "_doc",
            "_id" : "37731",
            "_score" : 1.0,
            "_source" : {
              "genre" : [
                "Crime",
                "Drama"
              ],
              "id" : "37731",
              "year" : 0,
              "title" : "Green Street Hooligans",
              "@version" : "1"
            }
          },
          {
            "_index" : "movies",
            "_type" : "_doc",
            "_id" : "37733",
            "_score" : 1.0,
            "_source" : {
              "genre" : [
                "Action",
                "Crime",
                "Drama",
                "Thriller"
              ],
              "id" : "37733",
              "year" : 2005,
              "title" : "History of Violence, A",
              "@version" : "1"
            }
          },
          {
            "_index" : "movies",
            "_type" : "_doc",
            "_id" : "37736",
            "_score" : 1.0,
            "_source" : {
              "genre" : [
                "Drama"
              ],
              "id" : "37736",
              "year" : 2005,
              "title" : "Oliver Twist",
              "@version" : "1"
            }
          }
        ]
      }
    }
    
    ```

* 查看所有的index

  * 语法：`get /_cat/indices?v`

  * 结果：

    ```
    health status index                          uuid                   pri rep docs.count docs.deleted store.size pri.store.size
    yellow open   movies                         HqFyAwvOQ8Ctfwy7Cbwz-A   1   1       9743            0      1.2mb          1.2mb
    green  open   .kibana-event-log-7.9.2-000001 JXU4J2oNRraHF0TJV4EjEw   1   0          1            0      5.5kb          5.5kb
    green  open   .apm-custom-link               wAKXZ3fVQMWyZel5tWd3DA   1   0          0            0       208b           208b
    green  open   .kibana_task_manager_1         JhGKI5p0RsCnGPL1F5enJg   1   0          6        13457      1.4mb          1.4mb
    green  open   .apm-agent-configuration       1iFwpiNeTwSGfdZkjNgqpg   1   0          0            0       208b           208b
    green  open   .kibana_1                      F4EKDsXJT7qa9Iqzr0oEbw   1   0         39           16     10.6mb         10.6mb
    ```

* 查看某index的信息（支持正则）

  * 语法：`get /_cat/indices/mo*?v`

  * 结果：

    ```
    health status index  uuid                   pri rep docs.count docs.deleted store.size pri.store.size
    yellow open   movies HqFyAwvOQ8Ctfwy7Cbwz-A   1   1       9743            0      1.2mb          1.2mb
    ```

* 其余cat indices api可以参考[cat indices api](https://www.elastic.co/guide/en/elasticsearch/reference/7.9/cat-indices.html)

  举例：

  ```
  # 查看状态为绿色的索引
  get /_cat/indices?v&health=green
  
  # 按照文档个数排序
  get /_cat/indices?v&s=docs.count:desc
  
  # 查看具体的字段
  get /_cat/indices/mov*?pri&v&h=health,index,pri,rep,docs.count,mt
  
  # 查看index占用多少内存
  get /_cat/indices?v&h=i,tm&s=tm:desc
  ```

##### single document apis

* 创建文档

  * 语法：

    >语法1: 
    >
    >```
    >PUT <index_name>/_create/<document_id>
    >{
    >	"FIELD_NAME": "FIELD_VALUE"
    >}
    >```
    >
    >语法2:
    >
    >```
    >put <index_name>/_doc/<document_id>?op_type=create
    >{
    >	"FIELD_NAME": "FIELD_VALUE"
    >}
    >```
    >
    >语法3:
    >
    >```
    >post <index_name>/_doc
    >{
    >	"FIELD_NAME": "FIELD_VALUE"
    >}
    >```

    语法说明：

    1. 支持自动生成document id和指定document id两种方式
    2. 通过调用`post <index_name>/_doc`，系统自动生成document id
    3. 使用`PUT <index_name>/_create/<id>`或者使用`put <index_name>/_doc/<document_id>?op_type=create`创建时，此时如果该id的document已经存在，操作失败。

  * 举例：测试语法1，创建一个user文档

    ```
    PUT users/_create/1
    {
    	"username": "shawn",
    	"age": 26,
    	"address": "SHANGHAI",
    	"hobbies": ["programming", "reading"]
    }
    ```

  * 结果：

    ```json
    {
      "_index" : "users",
      "_type" : "_doc",
      "_id" : "1",
      "_version" : 1,
      "result" : "created",
      "_shards" : {
        "total" : 2,
        "successful" : 1,
        "failed" : 0
      },
      "_seq_no" : 0,
      "_primary_term" : 1
    }
    ```

  * 举例：测试语法2

    ```
    PUT users/_doc/1?op_type=create
    {
    	"username": "shawn",
    	"age": 26,
    	"address": "SHANGHAI",
    	"hobbies": ["programming", "reading"]
    }
    ```

  * 结果：报错

    ```json
    // 第二次执行结果
    {
      "error" : {
        "root_cause" : [
          {
            "type" : "version_conflict_engine_exception",
            "reason" : "[1]: version conflict, document already exists (current version [1])",
            "index_uuid" : "IoWTBCR2ScOvh0_1863s8A",
            "shard" : "0",
            "index" : "users"
          }
        ],
        "type" : "version_conflict_engine_exception",
        "reason" : "[1]: version conflict, document already exists (current version [1])",
        "index_uuid" : "IoWTBCR2ScOvh0_1863s8A",
        "shard" : "0",
        "index" : "users"
      },
      "status" : 409
    }
    ```

  * 举例：测试语法3，自动生成document id

    ```
    # 自动生成_id
    post users/_doc
    {
    	"username": "Jack",
    	"age": 27,
    	"address": "BeiJing",
    	"hobbies": ["writing", "reading"]
    }
    ```

  * 结果：

    ```json
    {
      "_index" : "users",
      "_type" : "_doc",
      "_id" : "OHJrSXoBNsAv1-Vpj_ji",
      "_version" : 1,
      "result" : "created",
      "_shards" : {
        "total" : 2,
        "successful" : 1,
        "failed" : 0
      },
      "_seq_no" : 1,
      "_primary_term" : 1
    }
    ```

* 根据document id查看document

  * 语法：`get <index_name>/_doc/<document_id>`

  * 举例：`get users/_docs/1`

  * 结果：

    ```json
    {
      "_index" : "users",
      "_type" : "_doc",
      "_id" : "1",
      "_version" : 1,
      "_seq_no" : 0,
      "_primary_term" : 1,
      "found" : true,
      "_source" : {
        "username" : "shawn",
        "age" : 26,
        "address" : "SHANGHAI",
        "hobbies" : [
          "programming",
          "reading"
        ]
      }
    }
    ```

  * 说明：

    * 找到对应的文档，返回http 200，找不到的话，则返回404
    * _index / _type / _id / _version / _seq_no / _primary_term属于document metadata

* Index document

  * 语法：

    ```
    put <index_name>/_doc/<document_id>
    {
    		"FIELD_NAME": "FIELD_VALUE"
    }
    ```

  * 举例：

    ```
    put users/_doc/1
    {
    	"age": 25
    }
    ```

  * 结果：

    ```json
    {
      "_index" : "users",
      "_type" : "_doc",
      "_id" : "1",
      "_version" : 2,
      "result" : "updated",
      "_shards" : {
        "total" : 2,
        "successful" : 1,
        "failed" : 0
      },
      "_seq_no" : 3,
      "_primary_term" : 1
    }
    
    // 再执行 get users/_doc/1
    {
      "_index" : "users",
      "_type" : "_doc",
      "_id" : "1",
      "_version" : 2,
      "_seq_no" : 3,
      "_primary_term" : 1,
      "found" : true,
      "_source" : {
        "age" : 25
      }
    }
    ```

  * 说明：index和create不一样的地方：如果文档不存在，就会索引新的文档，如果存在，则先删除原有的document，再生成新的文档被索引，version + 1

* update文档

  * 语法：

    ```
    post <index_name>/_update/1
    {
    	"doc": {
    		"FIELD_NAME": "FIELD_VALUE"
    	}
    }
    ```

  * 举例：

    ```
    post users/_update/2
    {
    	"doc": {
    		"age": 24
    	}
    }
    ```

  * 结果：

    ```json
    {
      "_index" : "users",
      "_type" : "_doc",
      "_id" : "2",
      "_version" : 2,
      "result" : "updated",
      "_shards" : {
        "total" : 2,
        "successful" : 1,
        "failed" : 0
      },
      "_seq_no" : 5,
      "_primary_term" : 1
    }
    ```

* delete document

  * 语法：`delete <index_name>/_doc/<document_id>`

  * 举例：`delete users/_doc/1`

  * 结果：

    ```json
    {
      "_index" : "users",
      "_type" : "_doc",
      "_id" : "2",
      "_version" : 3,
      "result" : "deleted",
      "_shards" : {
        "total" : 2,
        "successful" : 1,
        "failed" : 0
      },
      "_seq_no" : 6,
      "_primary_term" : 1
    }
    ```

##### Muiti document apis

###### Bulk APIs

1. 支持一次api调用中，对不同索引进行操作
2. 支持四种类型操作
   1. index
   2. create
   3. update
   4. delete
3. 可以在url中指定index，也可以在请求的payload中进行
4. 操作中单条操作失败，并不会影响其他操作
5. 返回结果包括了每一条操作执行的结果

```console
POST _bulk
{ "index" : { "_index" : "test", "_id" : "1" } }
{ "field1" : "value1" }
{ "delete" : { "_index" : "test", "_id" : "2" } }
{ "create" : { "_index" : "test", "_id" : "3" } }
{ "field1" : "value3" }
{ "update" : {"_id" : "1", "_index" : "test"} }
{ "doc" : {"field2" : "value2"} }
```

举例：

```
POST _bulk
{ "index" : { "_index" : "users", "_id" : "2" } }
{ "username": "jackson","age": 26,"address": "hangzhou"}
{ "delete" : { "_index" : "users", "_id" : "2" } }
{ "create" : { "_index" : "users", "_id" : "3" } }
{ "username" : "jackson","age": 26,"address": "hangzhou"}
{ "update" : {"_id" : "2", "_index" : "users"} }
{ "doc" : {"age" : "2"} }
```

结果：

```json
{
  "took" : 6,
  "errors" : true,
  "items" : [
    {
      "index" : {
        "_index" : "users",
        "_type" : "_doc",
        "_id" : "2",
        "_version" : 1,
        "result" : "created",
        "_shards" : {
          "total" : 2,
          "successful" : 1,
          "failed" : 0
        },
        "_seq_no" : 7,
        "_primary_term" : 1,
        "status" : 201
      }
    },
    {
      "delete" : {
        "_index" : "users",
        "_type" : "_doc",
        "_id" : "2",
        "_version" : 2,
        "result" : "deleted",
        "_shards" : {
          "total" : 2,
          "successful" : 1,
          "failed" : 0
        },
        "_seq_no" : 8,
        "_primary_term" : 1,
        "status" : 200
      }
    },
    {
      "create" : {
        "_index" : "users",
        "_type" : "_doc",
        "_id" : "3",
        "_version" : 1,
        "result" : "created",
        "_shards" : {
          "total" : 2,
          "successful" : 1,
          "failed" : 0
        },
        "_seq_no" : 9,
        "_primary_term" : 1,
        "status" : 201
      }
    },
    {
      "update" : {
        "_index" : "users",
        "_type" : "_doc",
        "_id" : "2",
        "status" : 404,
        "error" : {
          "type" : "document_missing_exception",
          "reason" : "[_doc][2]: document missing",
          "index_uuid" : "IoWTBCR2ScOvh0_1863s8A",
          "shard" : "0",
          "index" : "users"
        }
      }
    }
  ]
}
```

###### multi get

**批量读取-mget**，可以减少网络连接所产生的开销，提高性能。

语法：

> 语法1:
>
> ```
> GET /_mget
> {
> 	"docs": {
> 	
> 	}
> }
> ```
>
> 语法2:
>
> ```
> GET /<index>/_mget
> {
> 	"docs": {
> 	
> 	}
> }
> ```

举例：

```
get /_mget
{
  "docs": [
    {
      "_index": "users",
      "_id": 1
    },
    {
      "_index": "users",
      "_id": 2
    } 
  ]
}
```

结果：

```json
{
  "docs" : [
    {
      "_index" : "users",
      "_type" : "_doc",
      "_id" : "1",
      "_version" : 2,
      "_seq_no" : 3,
      "_primary_term" : 1,
      "found" : true,
      "_source" : {
        "age" : 25
      }
    },
    {
      "_index" : "users",
      "_type" : "_doc",
      "_id" : "2",
      "_version" : 1,
      "_seq_no" : 12,
      "_primary_term" : 1,
      "found" : true,
      "_source" : {
        "username" : "John",
        "age" : 26,
        "address" : "SHANGHAI",
        "hobbies" : [
          "programming",
          "reading"
        ]
      }
    }
  ]
}

```

**批量查询 - msearch**

语法：

```
GET /<index_name>/_msearch
{}
```

举例：

```
GET users/_msearch
{ }
{"query" : {"match" : { "username": "john"}}}
{"index": "users"}
{"query" : {"match_all" : {}}}
```

结果：

```json
{
  "took" : 4,
  "responses" : [
    {
      "took" : 2,
      "timed_out" : false,
      "_shards" : {
        "total" : 1,
        "successful" : 1,
        "skipped" : 0,
        "failed" : 0
      },
      "hits" : {
        "total" : {
          "value" : 1,
          "relation" : "eq"
        },
        "max_score" : 1.540445,
        "hits" : [
          {
            "_index" : "users",
            "_type" : "_doc",
            "_id" : "2",
            "_score" : 1.540445,
            "_source" : {
              "username" : "John",
              "age" : 26,
              "address" : "SHANGHAI",
              "hobbies" : [
                "programming",
                "reading"
              ]
            }
          }
        ]
      },
      "status" : 200
    },
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
          "value" : 5,
          "relation" : "eq"
        },
        "max_score" : 1.0,
        "hits" : [
          {
            "_index" : "users",
            "_type" : "_doc",
            "_id" : "OHJrSXoBNsAv1-Vpj_ji",
            "_score" : 1.0,
            "_source" : {
              "username" : "Jack",
              "age" : 27,
              "address" : "BeiJing",
              "hobbies" : [
                "writing",
                "reading"
              ]
            }
          },
          {
            "_index" : "users",
            "_type" : "_doc",
            "_id" : "kXJ5SXoBNsAv1-VpT_l8",
            "_score" : 1.0,
            "_source" : {
              "username" : "bob",
              "age" : 28,
              "address" : "BeiJing",
              "hobbies" : [
                "swimming",
                "reading"
              ]
            }
          },
          {
            "_index" : "users",
            "_type" : "_doc",
            "_id" : "1",
            "_score" : 1.0,
            "_source" : {
              "age" : 25
            }
          },
          {
            "_index" : "users",
            "_type" : "_doc",
            "_id" : "3",
            "_score" : 1.0,
            "_source" : {
              "username" : "jackson",
              "age" : 26,
              "address" : "hangzhou"
            }
          },
          {
            "_index" : "users",
            "_type" : "_doc",
            "_id" : "2",
            "_score" : 1.0,
            "_source" : {
              "username" : "John",
              "age" : 26,
              "address" : "SHANGHAI",
              "hobbies" : [
                "programming",
                "reading"
              ]
            }
          }
        ]
      },
      "status" : 200
    }
  ]
}
```
