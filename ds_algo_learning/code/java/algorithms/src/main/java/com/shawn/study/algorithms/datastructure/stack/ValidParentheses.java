package com.shawn.study.algorithms.datastructure.stack;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author shawn
 * @since 2020/10/13
 */
public class ValidParentheses {

  public boolean isValid(String s) {
    if (null == s || s.isEmpty()) {
      return true;
    }
    int length = s.length();
    if (length % 2 == 1) {
      return false;
    }
    Deque<Character> stack = new ArrayDeque<>();
    char[] chars = s.toCharArray();
    for (char c : chars) {
      switch (c) {
        case '[':
        case '{':
        case '(':
        case '<':
          stack.push(c);
          break;
        case ')':
          if (!stack.isEmpty() && stack.pop() == '(') {
            break;
          } else {
            return false;
          }
        case '}':
          if (!stack.isEmpty() && stack.pop() == '{') {
            break;
          } else {
            return false;
          }
        case ']':
          if (!stack.isEmpty() && stack.pop() == '[') {
            break;
          } else {
            return false;
          }
        case '>':
          if (!stack.isEmpty() && stack.pop() == '<') {
            break;
          } else {
            return false;
          }
      }
    }
    return stack.isEmpty();
  }
}
