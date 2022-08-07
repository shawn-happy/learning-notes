package com.shawn.study.deep.in.java.design.behavioral.iterator;

import java.util.Date;

/**
 * @author shawn
 * @since 2020/8/10
 */
public class TestMain {

  public static void main(String[] args) {
    demoItemList();
    demonstrateBstIterator();
  }

  private static void demoItemList() {
    System.out.println("------------------------");
    System.out.println("item list Iterator: ");
    ItemList list = new ItemList();
    list.add(new Item(1, "shawn_1", "1", new Date(), 1))
        .add(new Item(2, "shawn_2", "2", new Date(), 2))
        .add(new Item(3, "shawn_3", "3", new Date(), 3))
        .add(new Item(4, "shawn_4", "4", new Date(), 4))
        .add(new Item(5, "shawn_5", "5", new Date(), 5));
    Iterator<Item> iterator = list.iterator();
    while (iterator.hasNext()) {
      System.out.printf("item: %s\n", iterator.next());
    }
  }

  private static void demonstrateBstIterator() {
    System.out.println("------------------------");
    System.out.println("BST Iterator: ");
    TreeNode<Integer> root = buildIntegerBst();
    BstIterator bstIterator = new BstIterator<>(root);
    while (bstIterator.hasNext()) {
      System.out.printf("Next node: %s\n", bstIterator.next());
    }
  }

  private static TreeNode<Integer> buildIntegerBst() {
    TreeNode<Integer> root = new TreeNode<>(8);

    root.insert(3);
    root.insert(10);
    root.insert(1);
    root.insert(6);
    root.insert(14);
    root.insert(4);
    root.insert(7);
    root.insert(13);

    return root;
  }
}
