package io.github.shawn.datastructure.bitmap;

public class BitMap {
    private char[] bytes;
    private int bit;

    public BitMap(int bit) {
        this.bit = bit;
        this.bytes = new char[bit / 16 + 1];
    }

    public void set(int k) {
        if (k > bit) {
            return;
        }
        int byteIndex = k / 16;
        int bitIndex = k % 16;
        bytes[byteIndex] |= (1 << bitIndex);
    }

    public boolean get(int k) {
        if (k > bit) {
            return false;
        }
        int byteIndex = k / 16;
        int bitIndex = k % 16;
        return (bytes[byteIndex] & (1 << bitIndex)) != 0;
    }
}
