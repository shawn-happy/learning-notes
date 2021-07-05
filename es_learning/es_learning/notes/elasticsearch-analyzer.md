## elasticsear analyzer

### 什么是Analysis

顾名思义，文本分析就是**把全文本转换成一系列单词（term/token）的过程**，也叫**分词**。在 ES 中，Analysis 是通过**分词器（Analyzer）** 来实现的，可使用 ES 内置的分析器或者按需定制化分析器。

比如输入的文本是`thinking in elasticsearch`，通过分词器，可以分成三个单词，分别是`thinking, in, elasticsearch`，

### 分词器的组成

* **Character Filters:** 针对原始文本处理，比如去除 html 标签
* **Tokenizer:** 按照规则切分为单词，比如按照空格切分
* **Token Filter**: 将切分的单词进行加工，比如大写转小写，删除 stopwords，增加同义语

同时 Analyzer 三个部分也是有顺序的，从上到下依次经过 `Character Filters`，`Tokenizer` 以及 `Token Filters`，这个顺序比较好理解，一个文本进来，需要先经过`Character Filters`进行文本数据处理，然后进入`Tokenizer`进行分词操作，最后对分词的结果进行过滤。

### Elasticsearch内置分词器

#### Standard Analyzer

* elasticsearch内置的标准分词器，如果没有指定，这就是默认选择的分词器。

* 按词分配，支持多种语言

* 小写处理，它删除大多数标点符号、小写术语，并支持指定需要删除的词语。

* Standard Tokenizer

* Lower Case Token Filter

* Stop Token Filter (默认是禁止使用的)

  * 默认需要删除的词语有：

    `a`, `an`, `and`, `are`, `as`, `at`, `be`, `but`, `by`, `for`, `if`, `in`, `into`, `is`, `it`, `no`, `not`, `of`, `on`, `or`, `such`, `that`, `the`, `their`, `then`, `there`, `these`, `they`, `this`, `to`, `was`, `will`,` with`

#### Simple Analyzer

* 小写处理
* 如果文本中包含了非字母字符（数字，撇号，空格，连字符等），则会丢弃。
* Lower Case Token Filter

#### Whitespace Analyzer

* 按照空格切分
* Whitespace Filter

#### Stop Analyzer

* 相比Simple Analyzer多了Stop Filter，会把`a`, `an`, `and`, `are`, `as`, `at`, `be`, `but`, `by`, `for`, `if`, `in`, `into`, `is`, `it`, `no`, `not`, `of`, `on`, `or`, `such`, `that`, `the`, `their`, `then`, `there`, `these`, `they`, `this`, `to`, `was`, `will`,` with`等修饰词去除。

#### Keyword Analyzer

* 不分词，直接将输入当一个term输出
* Keyword Filter

#### Pattern Analyzer

* 通过正则表达式进行分词。
* 默认是`\W+`，非字符的符号进行隔离
* Pattern Tokenizer
* Lower Case Token Filter
* Stop Token Filter（disable by default）

#### Language Analyzer

A set of analyzers aimed at analyzing specific language text. The following types are supported: [`arabic`](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#arabic-analyzer), [`armenian`](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#armenian-analyzer), [`basque`](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#basque-analyzer), [`bengali`](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#bengali-analyzer), [`brazilian`](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#brazilian-analyzer), [`bulgarian`](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#bulgarian-analyzer), [`catalan`](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#catalan-analyzer), [`cjk`](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#cjk-analyzer), [`czech`](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#czech-analyzer), [`danish`](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#danish-analyzer), [`dutch`](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#dutch-analyzer), [`english`](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#english-analyzer), [`estonian`](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#estonian-analyzer), [`finnish`](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#finnish-analyzer), [`french`](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#french-analyzer), [`galician`](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#galician-analyzer), [`german`](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#german-analyzer), [`greek`](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#greek-analyzer), [`hindi`](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#hindi-analyzer), [`hungarian`](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#hungarian-analyzer), [`indonesian`](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#indonesian-analyzer), [`irish`](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#irish-analyzer), [`italian`](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#italian-analyzer), [`latvian`](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#latvian-analyzer), [`lithuanian`](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#lithuanian-analyzer), [`norwegian`](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#norwegian-analyzer), [`persian`](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#persian-analyzer), [`portuguese`](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#portuguese-analyzer), [`romanian`](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#romanian-analyzer), [`russian`](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#russian-analyzer), [`sorani`](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#sorani-analyzer), [`spanish`](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#spanish-analyzer), [`swedish`](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#swedish-analyzer), [`turkish`](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#turkish-analyzer), [`thai`](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#thai-analyzer).

#### Fingerprint Analyzer

* 指纹分词器，转小写，删除扩展字符，排序，删除重复字符

### Custom Analyzer

#### 中文分词的难点

* 中文句子，切分成一个个词（不是一个个子）
* 英文中，单词与单词之间有空格作为分割，而中文的语句中，词与词没有什么可以自然分割的。
* 一些中文语句，在不同的上下文中有不同的理解
  * 这个苹果，不大好吃/这个苹果，不大，好吃

#### 中文分词器

##### ICU Analyzer

* 需要安装plugin
  * `Elasticsearch-plugin install analysis-icu`
* 提供了unicode的支持，更好的支持亚洲语言
* `Normalization Character Filter`
* `ICU Tokenizer`
* `Normalization Token Filter`
* `Folding Token Filter`
* `Collation Token Filter`
* `Transform Token Filter`

##### IK Analyzer

* 基于java语言开发的轻量级的中文分词工具包
* 支持自定义词库，支持热更新分词字典。
* 支持细粒度和智能分词两种切分模式。
* https://github.com/medcl/elasticsearch-analysis-ik

参考资料：[medcl github](https://github.com/medcl)，提供了很多custom analyzer

### 分词器API

#### Standard Analyzer

* 默认配置

语句：

```
post _analyze
{
  "analyzer": "standard",
  "text": "The 2 QUICK Brown-Foxes jumped over the lazy dog's bone."
}
```

结果：

```json
{
  "tokens" : [
    {
      "token" : "the",
      "start_offset" : 0,
      "end_offset" : 3,
      "type" : "<ALPHANUM>",
      "position" : 0
    },
    {
      "token" : "2",
      "start_offset" : 4,
      "end_offset" : 5,
      "type" : "<NUM>",
      "position" : 1
    },
    {
      "token" : "quick",
      "start_offset" : 6,
      "end_offset" : 11,
      "type" : "<ALPHANUM>",
      "position" : 2
    },
    {
      "token" : "brown",
      "start_offset" : 12,
      "end_offset" : 17,
      "type" : "<ALPHANUM>",
      "position" : 3
    },
    {
      "token" : "foxes",
      "start_offset" : 18,
      "end_offset" : 23,
      "type" : "<ALPHANUM>",
      "position" : 4
    },
    {
      "token" : "jumped",
      "start_offset" : 24,
      "end_offset" : 30,
      "type" : "<ALPHANUM>",
      "position" : 5
    },
    {
      "token" : "over",
      "start_offset" : 31,
      "end_offset" : 35,
      "type" : "<ALPHANUM>",
      "position" : 6
    },
    {
      "token" : "the",
      "start_offset" : 36,
      "end_offset" : 39,
      "type" : "<ALPHANUM>",
      "position" : 7
    },
    {
      "token" : "lazy",
      "start_offset" : 40,
      "end_offset" : 44,
      "type" : "<ALPHANUM>",
      "position" : 8
    },
    {
      "token" : "dog's",
      "start_offset" : 45,
      "end_offset" : 50,
      "type" : "<ALPHANUM>",
      "position" : 9
    },
    {
      "token" : "bone",
      "start_offset" : 51,
      "end_offset" : 55,
      "type" : "<ALPHANUM>",
      "position" : 10
    }
  ]
}
```

* 修改配置，启用stop token filter

语句：

```
PUT standard-demo
{
  "settings": {
    "analysis": {
      "analyzer": {
        "my_english_analyzer": {
          "type": "standard",
          "max_token_length": 5,
          "stopwords": "_english_"
        }
      }
    }
  }
}

POST standard-demo/_analyze
{
  "analyzer": "my_english_analyzer",
  "text": "The 2 QUICK Brown-Foxes jumped over the lazy dog's bone."
}
```

结果：

```json
{
  "tokens" : [
    {
      "token" : "2",
      "start_offset" : 4,
      "end_offset" : 5,
      "type" : "<NUM>",
      "position" : 1
    },
    {
      "token" : "quick",
      "start_offset" : 6,
      "end_offset" : 11,
      "type" : "<ALPHANUM>",
      "position" : 2
    },
    {
      "token" : "brown",
      "start_offset" : 12,
      "end_offset" : 17,
      "type" : "<ALPHANUM>",
      "position" : 3
    },
    {
      "token" : "foxes",
      "start_offset" : 18,
      "end_offset" : 23,
      "type" : "<ALPHANUM>",
      "position" : 4
    },
    {
      "token" : "jumpe",
      "start_offset" : 24,
      "end_offset" : 29,
      "type" : "<ALPHANUM>",
      "position" : 5
    },
    {
      "token" : "d",
      "start_offset" : 29,
      "end_offset" : 30,
      "type" : "<ALPHANUM>",
      "position" : 6
    },
    {
      "token" : "over",
      "start_offset" : 31,
      "end_offset" : 35,
      "type" : "<ALPHANUM>",
      "position" : 7
    },
    {
      "token" : "lazy",
      "start_offset" : 40,
      "end_offset" : 44,
      "type" : "<ALPHANUM>",
      "position" : 9
    },
    {
      "token" : "dog's",
      "start_offset" : 45,
      "end_offset" : 50,
      "type" : "<ALPHANUM>",
      "position" : 10
    },
    {
      "token" : "bone",
      "start_offset" : 51,
      "end_offset" : 55,
      "type" : "<ALPHANUM>",
      "position" : 11
    }
  ]
}
```

#### Simple Analyzer

语句：

```
POST _analyze
{
  "analyzer": "simple",
  "text": "The 2 QUICK Brown-Foxes jumped over the lazy dog's bone."
}
```

结果：

```json
{
  "tokens" : [
    {
      "token" : "the",
      "start_offset" : 0,
      "end_offset" : 3,
      "type" : "word",
      "position" : 0
    },
    {
      "token" : "quick",
      "start_offset" : 6,
      "end_offset" : 11,
      "type" : "word",
      "position" : 1
    },
    {
      "token" : "brown",
      "start_offset" : 12,
      "end_offset" : 17,
      "type" : "word",
      "position" : 2
    },
    {
      "token" : "foxes",
      "start_offset" : 18,
      "end_offset" : 23,
      "type" : "word",
      "position" : 3
    },
    {
      "token" : "jumped",
      "start_offset" : 24,
      "end_offset" : 30,
      "type" : "word",
      "position" : 4
    },
    {
      "token" : "over",
      "start_offset" : 31,
      "end_offset" : 35,
      "type" : "word",
      "position" : 5
    },
    {
      "token" : "the",
      "start_offset" : 36,
      "end_offset" : 39,
      "type" : "word",
      "position" : 6
    },
    {
      "token" : "lazy",
      "start_offset" : 40,
      "end_offset" : 44,
      "type" : "word",
      "position" : 7
    },
    {
      "token" : "dog",
      "start_offset" : 45,
      "end_offset" : 48,
      "type" : "word",
      "position" : 8
    },
    {
      "token" : "s",
      "start_offset" : 49,
      "end_offset" : 50,
      "type" : "word",
      "position" : 9
    },
    {
      "token" : "bone",
      "start_offset" : 51,
      "end_offset" : 55,
      "type" : "word",
      "position" : 10
    }
  ]
}
```

#### Stop Analyzer

语句：

```
POST _analyze
{
  "analyzer": "stop",
  "text": "The 2 QUICK Brown-Foxes jumped over the lazy dog's bone."
}
```

结果：

```json
{
  "tokens" : [
    {
      "token" : "quick",
      "start_offset" : 6,
      "end_offset" : 11,
      "type" : "word",
      "position" : 1
    },
    {
      "token" : "brown",
      "start_offset" : 12,
      "end_offset" : 17,
      "type" : "word",
      "position" : 2
    },
    {
      "token" : "foxes",
      "start_offset" : 18,
      "end_offset" : 23,
      "type" : "word",
      "position" : 3
    },
    {
      "token" : "jumped",
      "start_offset" : 24,
      "end_offset" : 30,
      "type" : "word",
      "position" : 4
    },
    {
      "token" : "over",
      "start_offset" : 31,
      "end_offset" : 35,
      "type" : "word",
      "position" : 5
    },
    {
      "token" : "lazy",
      "start_offset" : 40,
      "end_offset" : 44,
      "type" : "word",
      "position" : 7
    },
    {
      "token" : "dog",
      "start_offset" : 45,
      "end_offset" : 48,
      "type" : "word",
      "position" : 8
    },
    {
      "token" : "s",
      "start_offset" : 49,
      "end_offset" : 50,
      "type" : "word",
      "position" : 9
    },
    {
      "token" : "bone",
      "start_offset" : 51,
      "end_offset" : 55,
      "type" : "word",
      "position" : 10
    }
  ]
}
```

自定义过滤条件：

```
PUT stop-demo
{
  "settings": {
    "analysis": {
      "analyzer": {
        "my_stop_analyzer": {
          "type": "stop",
          "stopwords": ["the", "over"]
        }
      }
    }
  }
}

POST stop-demo/_analyze
{
  "analyzer": "my_stop_analyzer",
  "text": "The 2 QUICK Brown-Foxes jumped over the lazy dog's bone."
}
```

结果：

```json
{
  "tokens" : [
    {
      "token" : "quick",
      "start_offset" : 6,
      "end_offset" : 11,
      "type" : "word",
      "position" : 1
    },
    {
      "token" : "brown",
      "start_offset" : 12,
      "end_offset" : 17,
      "type" : "word",
      "position" : 2
    },
    {
      "token" : "foxes",
      "start_offset" : 18,
      "end_offset" : 23,
      "type" : "word",
      "position" : 3
    },
    {
      "token" : "jumped",
      "start_offset" : 24,
      "end_offset" : 30,
      "type" : "word",
      "position" : 4
    },
    {
      "token" : "lazy",
      "start_offset" : 40,
      "end_offset" : 44,
      "type" : "word",
      "position" : 7
    },
    {
      "token" : "dog",
      "start_offset" : 45,
      "end_offset" : 48,
      "type" : "word",
      "position" : 8
    },
    {
      "token" : "s",
      "start_offset" : 49,
      "end_offset" : 50,
      "type" : "word",
      "position" : 9
    },
    {
      "token" : "bone",
      "start_offset" : 51,
      "end_offset" : 55,
      "type" : "word",
      "position" : 10
    }
  ]
}
```

#### Keyword Analyzer

语句：

```
POST _analyze
{
  "analyzer": "keyword",
  "text": "The 2 QUICK Brown-Foxes jumped over the lazy dog's bone."
}
```

结果：

```json
{
  "tokens" : [
    {
      "token" : "The 2 QUICK Brown-Foxes jumped over the lazy dog's bone.",
      "start_offset" : 0,
      "end_offset" : 56,
      "type" : "word",
      "position" : 0
    }
  ]
}
```

#### Pattern Analyzer

语句：

```
POST _analyze
{
  "analyzer": "pattern",
  "text": "The 2 QUICK Brown-Foxes jumped over the lazy dog's bone."
}
```

结果：

```json
{
  "tokens" : [
    {
      "token" : "the",
      "start_offset" : 0,
      "end_offset" : 3,
      "type" : "word",
      "position" : 0
    },
    {
      "token" : "2",
      "start_offset" : 4,
      "end_offset" : 5,
      "type" : "word",
      "position" : 1
    },
    {
      "token" : "quick",
      "start_offset" : 6,
      "end_offset" : 11,
      "type" : "word",
      "position" : 2
    },
    {
      "token" : "brown",
      "start_offset" : 12,
      "end_offset" : 17,
      "type" : "word",
      "position" : 3
    },
    {
      "token" : "foxes",
      "start_offset" : 18,
      "end_offset" : 23,
      "type" : "word",
      "position" : 4
    },
    {
      "token" : "jumped",
      "start_offset" : 24,
      "end_offset" : 30,
      "type" : "word",
      "position" : 5
    },
    {
      "token" : "over",
      "start_offset" : 31,
      "end_offset" : 35,
      "type" : "word",
      "position" : 6
    },
    {
      "token" : "the",
      "start_offset" : 36,
      "end_offset" : 39,
      "type" : "word",
      "position" : 7
    },
    {
      "token" : "lazy",
      "start_offset" : 40,
      "end_offset" : 44,
      "type" : "word",
      "position" : 8
    },
    {
      "token" : "dog",
      "start_offset" : 45,
      "end_offset" : 48,
      "type" : "word",
      "position" : 9
    },
    {
      "token" : "s",
      "start_offset" : 49,
      "end_offset" : 50,
      "type" : "word",
      "position" : 10
    },
    {
      "token" : "bone",
      "start_offset" : 51,
      "end_offset" : 55,
      "type" : "word",
      "position" : 11
    }
  ]
}
```

自定义正则表达式：

```
PUT pattern-demo
{
  "settings": {
    "analysis": {
      "analyzer": {
        "my_email_analyzer": {
          "type":      "pattern",
          "pattern":   "\\W|_", 
          "lowercase": true
        }
      }
    }
  }
}

POST pattern-demo/_analyze
{
  "analyzer": "my_email_analyzer",
  "text": "John_Smith@foo-bar.com"
}

```

结果：

```json
{
  "tokens" : [
    {
      "token" : "john",
      "start_offset" : 0,
      "end_offset" : 4,
      "type" : "word",
      "position" : 0
    },
    {
      "token" : "smith",
      "start_offset" : 5,
      "end_offset" : 10,
      "type" : "word",
      "position" : 1
    },
    {
      "token" : "foo",
      "start_offset" : 11,
      "end_offset" : 14,
      "type" : "word",
      "position" : 2
    },
    {
      "token" : "bar",
      "start_offset" : 15,
      "end_offset" : 18,
      "type" : "word",
      "position" : 3
    },
    {
      "token" : "com",
      "start_offset" : 19,
      "end_offset" : 22,
      "type" : "word",
      "position" : 4
    }
  ]
}
```

#### Language Analyzer(English Analyzer)

语句：

```
POST _analyze
{
  "analyzer": "english",
  "text": "The 2 QUICK Brown-Foxes jumped over the lazy dog's bone."
}
```

结果：

```json
{
  "tokens" : [
    {
      "token" : "2",
      "start_offset" : 4,
      "end_offset" : 5,
      "type" : "<NUM>",
      "position" : 1
    },
    {
      "token" : "quick",
      "start_offset" : 6,
      "end_offset" : 11,
      "type" : "<ALPHANUM>",
      "position" : 2
    },
    {
      "token" : "brown",
      "start_offset" : 12,
      "end_offset" : 17,
      "type" : "<ALPHANUM>",
      "position" : 3
    },
    {
      "token" : "fox",
      "start_offset" : 18,
      "end_offset" : 23,
      "type" : "<ALPHANUM>",
      "position" : 4
    },
    {
      "token" : "jump",
      "start_offset" : 24,
      "end_offset" : 30,
      "type" : "<ALPHANUM>",
      "position" : 5
    },
    {
      "token" : "over",
      "start_offset" : 31,
      "end_offset" : 35,
      "type" : "<ALPHANUM>",
      "position" : 6
    },
    {
      "token" : "lazi",
      "start_offset" : 40,
      "end_offset" : 44,
      "type" : "<ALPHANUM>",
      "position" : 8
    },
    {
      "token" : "dog",
      "start_offset" : 45,
      "end_offset" : 50,
      "type" : "<ALPHANUM>",
      "position" : 9
    },
    {
      "token" : "bone",
      "start_offset" : 51,
      "end_offset" : 55,
      "type" : "<ALPHANUM>",
      "position" : 10
    }
  ]
}
```

#### ICU Analyzer

语句：

```
POST _analyze
{
  "analyzer": "icu_analyzer",
  "text": "他说的确实在理"
}
```

结果：

```json
{
  "tokens" : [
    {
      "token" : "他",
      "start_offset" : 0,
      "end_offset" : 1,
      "type" : "<IDEOGRAPHIC>",
      "position" : 0
    },
    {
      "token" : "说的",
      "start_offset" : 1,
      "end_offset" : 3,
      "type" : "<IDEOGRAPHIC>",
      "position" : 1
    },
    {
      "token" : "确实",
      "start_offset" : 3,
      "end_offset" : 5,
      "type" : "<IDEOGRAPHIC>",
      "position" : 2
    },
    {
      "token" : "在",
      "start_offset" : 5,
      "end_offset" : 6,
      "type" : "<IDEOGRAPHIC>",
      "position" : 3
    },
    {
      "token" : "理",
      "start_offset" : 6,
      "end_offset" : 7,
      "type" : "<IDEOGRAPHIC>",
      "position" : 4
    }
  ]
}
```

### 中文分词器

### 参考资料

* https://www.elastic.co/guide/en/elasticsearch/reference/7.9/analysis-analyzers.html

