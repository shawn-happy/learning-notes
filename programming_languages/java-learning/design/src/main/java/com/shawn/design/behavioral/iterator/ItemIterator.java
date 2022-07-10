package com.shawn.design.behavioral.iterator;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author shawn
 * @since 2020/8/10
 */
public class ItemIterator<Item> implements Iterator<Item> {

  private int cursor;

  private List<Item> list;

  public ItemIterator(List<Item> list) {
    this.list = list;
  }

  @Override
  public boolean hasNext() {
    return cursor != list.size();
  }

  @Override
  public Item next() {
    if (cursor >= list.size()) {
      throw new NoSuchElementException();
    }
    Item item = list.get(cursor);
    cursor++;
    return item;
  }

}
