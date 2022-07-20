package com.shawn.study.deep.in.java.jdbc.jpa.serialize;

import com.shawn.study.deep.in.java.jdbc.jpa.exception.DeserializationException;
import com.shawn.study.deep.in.java.jdbc.jpa.exception.SerializationException;
import java.io.IOException;
import javax.persistence.AttributeConverter;

public interface PersistenceConverter<T>
    extends Serializer<T>, Deserializer<T>, AttributeConverter<T, byte[]> {

  default byte[] convertToDatabaseColumn(T attribute) {
    try {
      return serialize(attribute);
    } catch (IOException ioe) {
      throw new SerializationException(ioe);
    }
  }

  default T convertToEntityAttribute(byte[] dbData) {
    try {
      return deserialize(dbData);
    } catch (IOException ioe) {
      throw new DeserializationException(ioe);
    }
  }
}
