package com.shawn.study.algorithms.datastructure.stack;

interface Stack<T> extends Iterable<T>{

  // 返回栈中的元素个数
  int size();

  // 检查栈是否为空
  default boolean isEmpty(){
   return size() == 0;
  }

  // 从栈中弹出一个元素
  // 如果栈空就抛出一个异常
  T pop();

  // 查看栈顶元素(并不移除)
  // 如果栈空就抛出一个异常
  T peek();

  // 将一个元素入栈
  void push(T elem);
}
