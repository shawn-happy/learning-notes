package com.shawn.design.oop;

/**
 * @author shawn
 * @param <E>
 */
public interface List<E> {

	void add(E e);

	void add(int index, E e);

	void ensureCapacity();

	E get(int index);

	int size();

	int capacity();
}
