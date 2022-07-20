package com.shawn.study.deep.in.java.jdbc.jpa.serialize;

import java.io.IOException;

public interface Serializer<T> {
  byte[] serialize(T object) throws IOException;
}
