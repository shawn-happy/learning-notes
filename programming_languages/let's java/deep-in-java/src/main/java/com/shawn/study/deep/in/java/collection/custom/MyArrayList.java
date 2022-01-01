package com.shawn.study.deep.in.java.collection.custom;

import java.util.AbstractList;

public class MyArrayList<E> extends AbstractList<E> {

    @Override
    public boolean add(E e) {
        return super.add(e);
    }

    @Override
    public E set(int index, E element) {
        return super.set(index, element);
    }

    @Override
    public E remove(int index) {
        return super.remove(index);
    }

    @Override
    public E get(int index) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }
}
