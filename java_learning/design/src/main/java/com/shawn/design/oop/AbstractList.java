package com.shawn.design.oop;

/**
 * @author shawn
 * @param <E>
 */
public abstract class AbstractList<E> implements List<E>{

	protected AbstractList(){

	}

	@Override
	public void add(E e) {
		add(size(), e);
	}

	protected void checkIndexForAdd(int index) {
		if (index < 0 || index > size()) {
			throw new IndexOutOfBoundsException("index is error: " + index);
		}
	}

	protected abstract E[] getDatas();

	@Override
	public String toString() {
		E[] datas = getDatas();
		StringBuilder builder = new StringBuilder("[");
		for (int i = 0; i < size(); i++) {
			builder.append(datas[i]);
			if (i != size() - 1){
				builder.append(",");
			}
		}
		builder.append("]\n");
		return builder.toString();
	}
}
