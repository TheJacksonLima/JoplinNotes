package org.jfl.block01;

import java.util.*;

public class Block01 {

    public static boolean isPalindrome(String s){
        String reverse = new StringBuilder(s).reverse().toString();
        return s.equals(reverse);
    }

    public static boolean isPalindrome_v2(String s) {
        int i = s.length() - 1;
        int j = 0;

        while( i != j){
            if (s.charAt(i) != s.charAt(j)){
                return false;
            }
            i-=1;
            j+=1;
        }

        return true;
    }

    public static void arrayInt(){
        int [] nums = {4,5,8,9};
        int numsSize = nums.length;

        System.out.println("=".repeat(50));

        for (int i = 0; i < numsSize ; i++) {
            System.out.println(nums[i]);
        }

        System.out.println(".".repeat(50));
        for(int n: nums){
            System.out.println(n);
        }
        System.out.println("=".repeat(50));

    }

    public static void strings()
    {
        System.out.println("\n");
        System.out.println("=".repeat(50));

        String s = "payment";
        System.out.println(s.length());
        System.out.println(s.charAt(0));
        System.out.println(s.substring(0,3));
        System.out.println(s.equals("payment"));

        char [] chars = s.toCharArray();
        System.out.println("chars: "+ Arrays.toString(chars));

        String reversed = new StringBuilder(s).reverse().toString();
        System.out.println("reversed: "+reversed);

        System.out.println("=".repeat(50));

    }

    public static void rashMap(){
        Map<String,Integer> map = new HashMap<>();
        map.put("java",10);
        System.out.println("map.get: "+map.get("java"));

        map.put("java",20);
        System.out.println("map.get: "+map.get("java"));

        System.out.println("map.getOrDefault: "+map.getOrDefault("java",0));
        System.out.println("map.containsKey: "+map.containsKey("java"));
        System.out.println("map.remove: "+map.remove("java"));
        System.out.println("map.size: "+map.size());
        System.out.println("map.isEmpty: "+map.isEmpty());


        map.put("java",10);
        map.put("c#",10);
        map.put("rust",20);
        map.put("react",30);
        System.out.println("\n");
        System.out.println(".".repeat(50));
        for(Map.Entry<String,Integer> entry : map.entrySet()){
            System.out.println(entry.getKey()+" : "+entry.getValue());
        }
    }

    public static Character mostFrequentChar(String s){
        Map<Character,Integer> freqMap = new HashMap<>();
        int maxCont = 0;
        Character maxChar = null;

        for(Character c: s.toCharArray()){
            //System.out.println(c);
            int charCont = freqMap.getOrDefault(c,0);
            charCont = charCont+1;
            freqMap.put(c,charCont);

            if (charCont>maxCont){
                maxChar = c;
                maxCont = charCont;
            }
        }
/*
        for(Map.Entry<Character,Integer> m: freqMap.entrySet()){
            System.out.println(m.getKey()+" : "+m.getValue());
        }
*/
        return maxChar;
    }

    public static void rashSet(){
        Set<Integer> numbers = new HashSet<>();
        numbers.add(10);
        numbers.add(20);
        numbers.add(10);
        System.out.println(numbers.size());

        System.out.println("numbers.contains(10): "+numbers.contains(10));
        System.out.println("numbers.contains(30): "+numbers.contains(30));
        numbers.remove(10);
        System.out.println("numbers.size: "+numbers.size());
        System.out.println("numbers.isEmpty: "+numbers.isEmpty());

        System.out.println("numbers.add(30) first time: "+numbers.add(30));
        System.out.println("numbers.add(30) second time: "+numbers.add(30));
    }

    public static boolean containsDuplicate(int [] nums){
        Set<Integer> uniqueNum = new HashSet<>();
        for(int n: nums){
           if(!uniqueNum.add(n))
           {
               return true;
           }

        }
        return false;
    }

    public static void main(String[] args) {
        arrayInt();
        strings();

        String s = "racecar";
        System.out.println("String "+s+" isPalindrome? " + isPalindrome(s));
        System.out.println("String "+s+" isPalindrome_v2? " + isPalindrome_v2(s));

        s = "bill.com";
        System.out.println("String "+s+" isPalindrome? " + isPalindrome(s));
        System.out.println("String "+s+" isPalindrome_v2 " + isPalindrome_v2(s));

        System.out.println("\n");
        System.out.println("_".repeat(50)+"HashMap"+"_".repeat(50));
        rashMap();
        System.out.println("mostFrequentChar: " +mostFrequentChar("java"));
        System.out.println("mostFrequentChar: " +mostFrequentChar("aaabbcccc"));
        System.out.println("mostFrequentChar: " +mostFrequentChar("ccc"));

        System.out.println("\n");
        System.out.println("_".repeat(50)+"HashSet"+"_".repeat(50));
        rashSet();
        int [] nums = {1,2,3,4,5,1};
        containsDuplicate(nums);
        System.out.println("containsDuplicate(nums) : " +containsDuplicate(nums));

    }

}
