package org.jfl;

import java.util.ArrayList;

public class Interview {
/*
    You are given an integer array coins representing coins of different denominations and an integer amount representing a total amount of money.
    Return the fewest number of coins that you need to make up that amount. If that amount of money cannot be made up by any combination of the coins, return -1.
    You may assume that you have an infinite number of each kind of coin.

    Example 1:
    Input: coins = [1,2,5], amount = 11 Output: 3 Explanation: 11 = 5 + 5 + 1

    1 #coins = 11 => 11/1 = 11
    2 #coins = 11 => 11/2 = 5 11% 2 = 1 5 of 2 and 1 of 1

    Example 2:
    Input: coins = [2], amount = 3 Output: -1

    Example 3:
    Input: coins = [1], amount = 0 Output: 0

    Constraints:

            1 <= coins.length <= 12
            1 <= coins[i] <= 231 - 1
            0 <= amount <= 104

 */

    public static void main(String[] args) {
        ArrayList<Integer> listCoins = new ArrayList<>();
 //       Integer amount = 11;
        /*listCoins.add(1);
        listCoins.add(2);
        listCoins.add(5);
*/
/*
        listCoins.add(2);
        Integer amount = 3;
       /*
        listCoins.add(1);
        Integer amount = 0;
       */

        listCoins.add(2);
        listCoins.add(5);
        listCoins.add(10);
        Integer amount = 27;
        //Input: coins = [2, 5, 10], amount = 27
        //
        //Output: 4
        //



        Integer q1,q2,qtyCoins,minQtyCoins=100;
        Boolean notPossible = Boolean.TRUE;

        if (amount == 0 || listCoins.isEmpty()){
            System.out.println("Minimal qty of coins, edge case amount is zero or list of coins is empty: "+0);
            return;
        }

        for(Integer coin: listCoins) {
          q1 = (amount/coin);
          q2 = (amount%coin);
          qtyCoins = q1+q2;
          if (minQtyCoins > qtyCoins){
              minQtyCoins = qtyCoins;

             if(listCoins.contains(q1) && listCoins.contains(q2)){
                 //minQtyCoins = -1;
                 System.out.println(listCoins.contains(q1));
                 System.out.println(listCoins.contains(q2));
                 notPossible = Boolean.FALSE;

             }

              System.out.println("listCoins: "+listCoins.toString()+" coin: "+coin+" q1: "+q1+ " q2: "+q2+" qtyCoins:"+ (qtyCoins)+ " amount: "+amount+" minQtyCoins: "+minQtyCoins +" notPossible: "+notPossible);
          }
        }
        if(notPossible ){
            minQtyCoins = -1;
        }

        System.out.println("Minimal qty of coins: "+minQtyCoins);



    }
}
