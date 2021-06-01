package com.shawn.design.structural.decorator;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author shawn
 * @description:
 * @since 2020/7/28
 */
public class LowerCaseInputStream extends FilterInputStream {

  public LowerCaseInputStream(InputStream inputStream) {
    super(inputStream);
  }

  @Override
  public int read() throws IOException {
    int read = super.read();
    return (read == -1 ? read : Character.toLowerCase((char) read));
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    int result = super.read(b, off, len);
    for (int i = off; i < off + result; i++) {
      b[i] = (byte) Character.toLowerCase((char) b[i]);
    }
    return result;
  }
}
