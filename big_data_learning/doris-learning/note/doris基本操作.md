# 建表语句

```sql
CREATE TABLE [IF NOT EXISTS] [database.]table
(
    column_definition_list,
    [index_definition_list]
)
[engine_type]
[keys_type]
[table_comment]
[partition_info]
distribution_desc
[rollup_list]
[properties]
[extra_properties]
```

* `column_definition_list`定义如下：
  * `column_name column_type [KEY] [aggr_type] [NULL] [default_value] [column_comment]`
  * 多个使用`,`隔开。
* `index_definition_list`定义如下：
  * `INDEX index_name (col_name) [USING BITMAP] COMMENT 'xxxxxx'`
  * 多个使用`,`隔开。
  * 如果没有索引，则不用填写，可选参数
* `engine_type`:
  * 可选参数，不填默认为：`ENGINE=olap`
  * 外部表支持：` MYSQL、BROKER、HIVE、ICEBERG 、HUDI`
* `keys_type`:
  * 支持的数据模型
  * `DUPLICATE KEY`：其后指定的列为排序列。默认数据模型
  * `AGGREGATE KEY`：其后指定的列为维度列。
  * `UNIQUE KEY`：其后指定的列为主键列。

* `table_comment`:

  * `COMMENT "This is my first DORIS table"`

* `partition_info`:

  * 定义分区信息，不填则默认只有一个分区

  * `LESS THAN`：仅定义分区上界。下界由上一个分区的上界决定。

    ```sql
    PARTITION BY RANGE(col1[, col2, ...])
    (
        PARTITION partition_name1 VALUES LESS THAN MAXVALUE|("value1", "value2", ...),
        PARTITION partition_name2 VALUES LESS THAN MAXVALUE|("value1", "value2", ...)
    )
    ```

  * `FIXED RANGE`：定义分区的左闭右开区间。

    ```sql
    PARTITION BY RANGE(col1[, col2, ...])
    (
        PARTITION partition_name1 VALUES [("k1-lower1", "k2-lower1", "k3-lower1",...), ("k1-upper1", "k2-upper1", "k3-upper1", ...)),
        PARTITION partition_name2 VALUES [("k1-lower1-2", "k2-lower1-2", ...), ("k1-upper1-2", MAXVALUE, ))
    )
    ```

  * `MULTI RANGE`：批量创建RANGE分区，定义分区的左闭右开区间，设定时间单位和步长，时间单位支持年、月、日、周和小时。

    ```sql
    PARTITION BY RANGE(col)
    (
       FROM ("2000-11-14") TO ("2021-11-14") INTERVAL 1 YEAR,
       FROM ("2021-11-14") TO ("2022-11-14") INTERVAL 1 MONTH,
       FROM ("2022-11-14") TO ("2023-01-03") INTERVAL 1 WEEK,
       FROM ("2023-01-03") TO ("2023-01-14") INTERVAL 1 DAY
    )
    ```

  * `MULTI RANGE`：批量创建数字类型的RANGE分区，定义分区的左闭右开区间，设定步长。

    ```sql
    PARTITION BY RANGE(int_col)
    (
        FROM (1) TO (100) INTERVAL 10
    )
    ```

* `distribution_desc`
  * 分桶信息，必填项
  * Hash 分桶 语法： `DISTRIBUTED BY HASH (k1[,k2 ...]) [BUCKETS num|auto]` 说明： 使用指定的 key 列进行哈希分桶。
  * Random 分桶 语法： `DISTRIBUTED BY RANDOM [BUCKETS num|auto]` 说明： 使用随机数进行分桶。
* `rollup_list`：
  * `rollup_name (col1[, col2, ...]) [DUPLICATE KEY(col1[, col2, ...])] [PROPERTIES("key" = "value")]`
  * 多个用`,`隔开
* `properties`:

