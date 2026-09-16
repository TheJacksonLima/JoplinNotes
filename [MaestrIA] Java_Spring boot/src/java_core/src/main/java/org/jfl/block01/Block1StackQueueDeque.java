package org.jfl.block01;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

public class Block1StackQueueDeque {

    public static boolean isValid(String s){
        Deque<Character> stack = new ArrayDeque<>();
        for(Character c: s.toCharArray()){
            if (c == '('){
                stack.push('(');
            }
            else if (c == ')'){
                if (stack.isEmpty())
                    return false;

                if (stack.pop() != '(')
                    return false;
            }
            else if (c == '[') {
                stack.push('[');
            }
            else if (c == ']'){
                if (stack.isEmpty())
                    return false;

                if (stack.pop() != '[')
                    return false;
            }
            else if (c == '{') {
                stack.push('{');
            }
            else if (c == '}'){
                if (stack.isEmpty())
                    return false;

                if (stack.pop() != '{')
                    return false;
            }
        }

        return stack.isEmpty();
    }

    public static void main(String[] args) {
        /*
        3. What is Deque?
            Deque means: Double Ended Queue
            You can add/remove from both ends.
        */
        System.out.println("\nDeque:");
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(10);
        stack.push(20);
        stack.push(5);

        System.out.println(stack.toString());
        System.out.println("stack.peek(): "+stack.peek());
        stack.pop();
        stack.pop();
        System.out.println(stack.toString());

        System.out.println("\nDeque addFirst and addLast:");
        Deque<Integer> stack2 = new ArrayDeque<>();
        stack2.addFirst(10);
        stack2.addLast(20);
        System.out.println(stack2.toString());

        stack2.removeFirst();
        System.out.println(stack2.toString());
        stack2.removeLast();
        System.out.println(stack2.toString());

        System.out.println("\nDeque as queue:");
        Deque<Integer> stack3 = new ArrayDeque<>();
        stack3.offer(10);
        stack3.offer(20);
        stack3.offer(5);
        System.out.println(stack3.toString());


        System.out.println(".".repeat(50));

        System.out.println("\nQueue:");
        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(10);
        queue.offer(20);
        queue.offer(30);

        System.out.println(queue.toString());
        System.out.println("queue.peek(): "+queue.peek());
        queue.poll();
        queue.poll();
        System.out.println(queue.toString());


        System.out.println("Exercise — Valid Parentheses:\n");
        System.out.println(isValid("()"));
        System.out.println(isValid("()[]{}"));
        System.out.println(isValid("{[()]}"));
        System.out.println(isValid("(]"));
        System.out.println(isValid("([)]"));
        System.out.println(isValid("((("));
        System.out.println(isValid(""));


    }
}
