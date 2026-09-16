package org.jfl.block01;

import org.jfl.Utils.Player;
import org.jfl.Utils.Transaction;

import java.util.*;

public class Block1ArrayListSortComparator {
    public static int[] twoSumSorted(int[] nums, int target){
        int left = 0;
        int right = nums.length - 1;
        int[] ret = new int[2];

        while (right > left){
            int sum = nums[left]+nums[right];

            if (sum == target){
                ret[0] = left;
                ret[1] = right;
                break;
            } else if (sum < target) {
                left++;
            }else{
                right--;
            }

        }
        return ret;
    }

    public static void main(String[] args) {
        System.out.println("\n");
        System.out.println("_".repeat(50)+"ArrayList_Sort_Comparator"+"_".repeat(50));
        List<Integer> numbers = new ArrayList<>();
        numbers.add(10);
        numbers.add(20);
        numbers.add(30);

        for(Integer n: numbers){
            System.out.println(n);
        }

        Integer n2 = numbers.get(0);
        System.out.println("numbers[0] = "+n2);

        numbers.set(0,50);
        n2 = numbers.get(0);
        System.out.println("after replace numbers[0] = "+n2);

        Integer size = numbers.size();
        System.out.println("numbers.size() = "+size);

        Boolean empty = numbers.isEmpty();
        System.out.println("numbers.isEmpty() = "+empty);

        Boolean contains = numbers.contains(20);
        System.out.println("numbers.contains(20) = "+contains);

        System.out.println("\nSorting:");
        int [] nums = {5,2,8,1};
        Arrays.sort(nums);

        for(int n: nums){
            System.out.print(n+ " ");
        }
        System.out.println();

        List<Integer> nums_list = new ArrayList<>();
        nums_list.add(5);
        nums_list.add(2);
        nums_list.add(8);
        nums_list.add(1);

        Collections.sort(nums_list);
        //nums_list.stream().forEach(System.out::print);
        nums_list.stream().forEach(n -> System.out.print(n + " "));
        System.out.println();

        nums_list.sort(Comparator.naturalOrder());
        nums_list.stream().forEach(n -> System.out.print(n + " "));
        System.out.println();

        nums_list.sort(Comparator.reverseOrder());
        nums_list.stream().forEach(n -> System.out.print(n + " "));
        System.out.println();

        System.out.println("\nComparator:");

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction("T1", 250.75));
        transactions.add(new Transaction("T2", -40.00));
        transactions.add(new Transaction("T3", 1200.50));
        transactions.add(new Transaction("T4", 15.20));
        transactions.add(new Transaction("T5", -300.00));

        System.out.println("Ordering per amount:");
        transactions.sort(Comparator.comparingDouble(t -> t.amount));
        transactions.forEach(System.out::println);
        System.out.println();

        System.out.println("Ordering per amount, reverse order:");
        transactions.sort(Comparator.comparingDouble((Transaction t) -> t.amount).reversed());
        transactions.forEach(System.out::println);
        System.out.println();

        //For integers:
        //Comparator.comparingInt(...)

        //For strings/objects:
        //Comparator.comparing(...)

        //For long:
        //Comparator.comparingLong(...)

        System.out.println("\n5. Custom comparator");
        nums_list.sort((a, b) -> Integer.compare(a, b));
        nums_list.stream().forEach(n -> System.out.print(n + " "));
        System.out.println();

        nums_list.sort((a, b) -> Integer.compare(b, a));
        nums_list.stream().forEach(n -> System.out.print(n + " "));
        System.out.println();

        System.out.println("\n6. Multiple sorting criteria");
        List<Player> players = new ArrayList<>();
        players.add(new Player("Meryl Streep", 95));
        players.add(new Player("Viola Davis", 88));
        players.add(new Player("Emma Stone", 72));
        players.add(new Player("Zendaya", 64));
        players.add(new Player("Cate Blanchett", 91));

        //Sort by score descending. If scores are equal, sort by name ascending.
        players.sort(Comparator.comparing(Player::getScore)
                .reversed()
                .thenComparing(Player::getName)
        );
        players.forEach(System.out::println);
 //       int [] j = {2, 7, 11, 15};
        int [] j = {1, 2, 3, 4, 8};
        int []ret = twoSumSorted(j,5);
        System.out.println("ret: "+ret[0]+" "+ret[1]);


    }
}
