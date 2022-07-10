package com.shawn.design.behavioral.iterator;


import java.util.ArrayList;
import java.util.List;

/**
 * @author shawn
 * @since 2020/8/10
 */
public class ItemList {

  private List<Item> itemList;

  public ItemList(){
    itemList = new ArrayList<>();
  }

  public ItemList add(Item item){
    itemList.add(item);
    return this;
  }

  public Iterator<Item> iterator(){
    return new ItemIterator<>(itemList);
  }

}
