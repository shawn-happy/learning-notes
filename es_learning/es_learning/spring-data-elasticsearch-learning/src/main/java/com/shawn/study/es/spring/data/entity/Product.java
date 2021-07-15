package com.shawn.study.es.spring.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.time.Instant;

import static org.springframework.data.elasticsearch.annotations.DateFormat.epoch_millis;
import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;
import static org.springframework.data.elasticsearch.annotations.FieldType.Long;
import static org.springframework.data.elasticsearch.annotations.FieldType.Double;

@Setting(replicas = 0)
@Document(indexName = "products")
public class Product {

  @Id private String id;

  @Field(type = Keyword)
  private String name;

  @Field(type = Double)
  private double price;

  @Field(value = "publish_time", format = epoch_millis, type = Long)
  private Instant publishTime;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public Instant getPublishTime() {
    return publishTime;
  }

  public void setPublishTime(Instant publishTime) {
    this.publishTime = publishTime;
  }
}
