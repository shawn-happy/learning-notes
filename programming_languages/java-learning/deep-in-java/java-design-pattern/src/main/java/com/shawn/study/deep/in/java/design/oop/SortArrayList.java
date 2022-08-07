package com.shawn.study.deep.in.java.design.oop;

/**
 * @author shawn
 * @param <E>
 */
public class SortArrayList<E> extends ArrayList<E>{

	@Override
	public void add(int index, E e) {
		ensureCapacity();
		E[] datas = getDatas();
		int i = size - 1;
		while (i >= 0){
			E data = datas[i];
			if(data instanceof Comparable){
				Comparable comp = (Comparable)data;
				String s = comp.toString();
				if(((Comparable)e).compareTo(Integer.valueOf(s)) < 0){
					datas[i + 1] = datas[i];
				}else{
					break;
				}
			}else{
				throw new RuntimeException("this element's type is not Comparable!");
			}

			i --;
		}
		datas[i + 1] = e;
		++ size;
	}
}
