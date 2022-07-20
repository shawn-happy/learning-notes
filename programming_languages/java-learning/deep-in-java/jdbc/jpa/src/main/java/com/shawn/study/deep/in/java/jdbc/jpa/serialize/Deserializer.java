package com.shawn.study.deep.in.java.jdbc.jpa.serialize;

import java.io.IOException;

public interface Deserializer<T> {
  T deserialize(byte[] bytes) throws IOException;
}
