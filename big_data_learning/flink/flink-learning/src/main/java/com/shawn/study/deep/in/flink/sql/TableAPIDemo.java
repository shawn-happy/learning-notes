package com.shawn.study.deep.in.flink.sql;

import org.apache.flink.connector.datagen.table.DataGenConnectorOptions;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableDescriptor;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.TableResult;

public class TableAPIDemo {

  public static void main(String[] args) throws Exception {
    TableEnvironment tableEnv = TableEnvironment.create(EnvironmentSettings.newInstance().build());

    // Create a source table
    tableEnv.createTemporaryTable(
        "SourceTable",
        TableDescriptor.forConnector("datagen")
            .schema(Schema.newBuilder().column("f0", DataTypes.STRING()).build())
            .option(DataGenConnectorOptions.ROWS_PER_SECOND, 100L)
            .build());

    // Create a sink table (using SQL DDL)
    tableEnv.executeSql(
        "CREATE TEMPORARY TABLE SinkTable WITH ('connector' = 'blackhole') LIKE SourceTable (EXCLUDING OPTIONS) ");

    // Create a Table object from a Table API query
    Table table1 = tableEnv.from("SourceTable");

    // Create a Table object from a SQL query
    Table table2 = tableEnv.sqlQuery("SELECT * FROM SourceTable");
    table2.printSchema();

    // Emit a Table API result Table to a TableSink, same for SQL result
    TableResult tableResult = table1.insertInto("SinkTable").execute();

    tableResult.print();
  }
}
