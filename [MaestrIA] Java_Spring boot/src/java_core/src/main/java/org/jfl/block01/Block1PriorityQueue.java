package org.jfl.block01;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Block1PriorityQueue {
    public static int findKthLargest(int[] nums, int k){
      PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.reverseOrder());
      int ret;

      for(int n: nums){
          pq.offer(n);
      }

      while (k > 1) {
          pq.poll();
          k=k-1;
      }


      if (pq.isEmpty()){
          ret =0;
      }
      else {
          ret = pq.poll();
      }

      return ret;

    }

    public static int findKthLargest_v2(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();

        for (int n : nums) {
            minHeap.offer(n);

            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }

        return minHeap.peek();
    }

    public static void main(String[] args) {
        /*PriorityQueue — what you need for HackerRank
            A PriorityQueue is useful when you repeatedly need the smallest or largest element without sorting everything repeatedly.
            Java's PriorityQueue is a min-heap by default
            Don't think of it as a completely sorted collection.
            Think: "peek() always gives me the smallest element."

            !!!! The internal order of every other element isn't something you should rely on. !!!!
        */

        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.offer(30);
        pq.offer(20);
        pq.offer(10);
        pq.offer(5);
        System.out.println("pq: "+pq.toString());
        pq.offer(4);
        System.out.println("pq: "+pq.toString());
        pq.offer(6);

        System.out.println("pq.peek(): "+pq.peek());
        System.out.println("pq: "+pq.toString());
        System.out.println("pq.poll(): "+pq.poll());
        System.out.println("pq.poll(): "+pq.poll());
        System.out.println("pq.poll(): "+pq.poll());
        System.out.println("pq.poll(): "+pq.poll());
        System.out.println("pq.poll(): "+pq.poll());
        System.out.println("pq.poll(): "+pq.poll());

        System.out.println("\nMax Heap");
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        maxHeap.offer(30);
        maxHeap.offer(100);
        maxHeap.offer(20);
        maxHeap.offer(200);
        System.out.println("maxHeap.poll(): "+maxHeap.poll());
        System.out.println("maxHeap.poll(): "+maxHeap.poll());
        System.out.println("maxHeap.poll(): "+maxHeap.poll());
        System.out.println("maxHeap.poll(): "+maxHeap.poll());

        int[] nums = {3,2,1,5,6,4};
        int k = 2;
        int ret = findKthLargest(nums,k);
        System.out.println("ret: "+ret);

        int[] nums2 = {7,10,4,3,20,15};
        int k2 = 3;
        ret = findKthLargest(nums2,k2);
        System.out.println("ret: "+ret);

    }
}
