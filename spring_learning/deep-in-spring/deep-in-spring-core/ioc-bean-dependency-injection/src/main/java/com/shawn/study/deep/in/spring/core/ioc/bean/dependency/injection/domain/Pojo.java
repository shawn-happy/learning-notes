package com.shawn.study.deep.in.spring.core.ioc.bean.dependency.injection.domain;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.core.io.Resource;

public class Pojo {

  private byte b;
  private short s;
  private int i;
  private long l;
  private float f;
  private double d;
  private char c;
  private boolean flag;

  private Byte b2;
  private Short s2;
  private Integer i2;
  private Long l2;
  private Float f2;
  private Double d2;
  private Character c2;
  private Boolean flag2;

  private String str;
  private Date date;
  private Optional<String> optional;
  private Language language;

  private Resource resource;

  private Language[] languages;
  private List<Language> languageList;
  private Map<String, Language> languageMap;

  public byte getB() {
    return b;
  }

  public void setB(byte b) {
    this.b = b;
  }

  public short getS() {
    return s;
  }

  public void setS(short s) {
    this.s = s;
  }

  public int getI() {
    return i;
  }

  public void setI(int i) {
    this.i = i;
  }

  public long getL() {
    return l;
  }

  public void setL(long l) {
    this.l = l;
  }

  public float getF() {
    return f;
  }

  public void setF(float f) {
    this.f = f;
  }

  public double getD() {
    return d;
  }

  public void setD(double d) {
    this.d = d;
  }

  public char getC() {
    return c;
  }

  public void setC(char c) {
    this.c = c;
  }

  public boolean isFlag() {
    return flag;
  }

  public void setFlag(boolean flag) {
    this.flag = flag;
  }

  public Byte getB2() {
    return b2;
  }

  public void setB2(Byte b2) {
    this.b2 = b2;
  }

  public Short getS2() {
    return s2;
  }

  public void setS2(Short s2) {
    this.s2 = s2;
  }

  public Integer getI2() {
    return i2;
  }

  public void setI2(Integer i2) {
    this.i2 = i2;
  }

  public Long getL2() {
    return l2;
  }

  public void setL2(Long l2) {
    this.l2 = l2;
  }

  public Float getF2() {
    return f2;
  }

  public void setF2(Float f2) {
    this.f2 = f2;
  }

  public Double getD2() {
    return d2;
  }

  public void setD2(Double d2) {
    this.d2 = d2;
  }

  public Character getC2() {
    return c2;
  }

  public void setC2(Character c2) {
    this.c2 = c2;
  }

  public Boolean getFlag2() {
    return flag2;
  }

  public void setFlag2(Boolean flag2) {
    this.flag2 = flag2;
  }

  public String getStr() {
    return str;
  }

  public void setStr(String str) {
    this.str = str;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Optional<String> getOptional() {
    return optional;
  }

  public void setOptional(Optional<String> optional) {
    this.optional = optional;
  }

  public Language getLanguage() {
    return language;
  }

  public void setLanguage(Language language) {
    this.language = language;
  }

  public Resource getResource() {
    return resource;
  }

  public void setResource(Resource resource) {
    this.resource = resource;
  }

  public Language[] getLanguages() {
    return languages;
  }

  public void setLanguages(Language[] languages) {
    this.languages = languages;
  }

  public List<Language> getLanguageList() {
    return languageList;
  }

  public void setLanguageList(List<Language> languageList) {
    this.languageList = languageList;
  }

  public Map<String, Language> getLanguageMap() {
    return languageMap;
  }

  public void setLanguageMap(Map<String, Language> languageMap) {
    this.languageMap = languageMap;
  }

  @Override
  public String toString() {
    return "Pojo{"
        + "b="
        + b
        + ", s="
        + s
        + ", i="
        + i
        + ", l="
        + l
        + ", f="
        + f
        + ", d="
        + d
        + ", c="
        + c
        + ", flag="
        + flag
        + ", b2="
        + b2
        + ", s2="
        + s2
        + ", i2="
        + i2
        + ", l2="
        + l2
        + ", f2="
        + f2
        + ", d2="
        + d2
        + ", c2="
        + c2
        + ", flag2="
        + flag2
        + ", str='"
        + str
        + '\''
        + ", date="
        + date
        + ", optional="
        + optional
        + ", language="
        + language
        + ", resource="
        + resource
        + ", languages="
        + Arrays.toString(languages)
        + ", languageList="
        + languageList
        + ", languageMap="
        + languageMap
        + '}';
  }
}
