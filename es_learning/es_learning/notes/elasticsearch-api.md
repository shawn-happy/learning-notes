### 查看index信息
```
get movies
```

### 查看index mapping信息
```
get movies/_mappings
```

### 查看index setting信息
```
get movies/_settings
```

### 查看index document总数nt
```
get movies/_count
```

### 查看前10条文档，了解文档格式
```
get movies/_search
{

}
```

### 查看所有的index
```
get /_cat/indices?v

get /_cat/indices/mo*?v&s=index
```

### 查看状态为绿色的索引
```
get /_cat/indices?v&health=green
```

### 按照文档个数排序
```
get /_cat/indices?v&s=docs.count:desc
```

### 查看具体的字段
```
get /_cat/indices/mov*?pri&v&h=health,index,pri,rep,docs.count,mt
```

### 查看index占用多少内存
```
get /_cat/indices?v&h=i,tm&s=tm:desc
```

### 创建一个users的文档
```
PUT users/_create/1
{
	"username": "shawn",
	"age": 26,
	"address": "SHANGHAI",
	"hobbies": ["programming", "reading"]
}

PUT users/_doc/2?op_type=create
{
	"username": "John",
	"age": 26,
	"address": "SHANGHAI",
	"hobbies": ["programming", "reading"]
}
```

### 自动生成_id
```
post users/_doc
{
	"username": "bob",
	"age": 28,
	"address": "BeiJing",
	"hobbies": ["swimming", "reading"]
}
```


### 查看document记录
```
get users/_doc/2
```

### index document
```
put users/_doc/1
{
	"age": 25
}
```

### update document
```
post users/_update/2
{
	"doc": {
		"age": 24
	}
}
```

### delete document
```
delete users/_doc/2
```

### bulk api
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

### mget
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

### msearch
```
GET users/_msearch
{ }
{"query" : {"match" : { "username": "john"}}}
{"index": "users"}
{"query" : {"match_all" : {}}}
```









