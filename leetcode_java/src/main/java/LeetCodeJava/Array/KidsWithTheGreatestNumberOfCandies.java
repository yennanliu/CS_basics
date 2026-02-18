package LeetCodeJava.Array;

// https://leetcode.com/problems/kids-with-the-greatest-number-of-candies/description/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 Code
 Testcase
 Testcase
 Test Result
 1431. Kids With the Greatest Number of Candies
 Easy
 Topics
 premium lock icon
 Companies
 Hint
 There are n kids with candies. You are given an integer array candies, where each candies[i] represents the number of candies the ith kid has, and an integer extraCandies, denoting the number of extra candies that you have.

 Return a boolean array result of length n, where result[i] is true if, after giving the ith kid all the extraCandies, they will have the greatest number of candies among all the kids, or false otherwise.

 Note that multiple kids can have the greatest number of candies.



 Example 1:

 Input: candies = [2,3,5,1,3], extraCandies = 3
 Output: [true,true,true,false,true]
 Explanation: If you give all extraCandies to:
 - Kid 1, they will have 2 + 3 = 5 candies, which is the greatest among the kids.
 - Kid 2, they will have 3 + 3 = 6 candies, which is the greatest among the kids.
 - Kid 3, they will have 5 + 3 = 8 candies, which is the greatest among the kids.
 - Kid 4, they will have 1 + 3 = 4 candies, which is not the greatest among the kids.
 - Kid 5, they will have 3 + 3 = 6 candies, which is the greatest among the kids.
 Example 2:

 Input: candies = [4,2,1,1,2], extraCandies = 1
 Output: [true,false,false,false,false]
 Explanation: There is only 1 extra candy.
 Kid 1 will always have the greatest number of candies, even if a different kid is given the extra candy.
 Example 3:

 Input: candies = [12,1,12], extraCandies = 10
 Output: [true,false,true]


 Constraints:

 n == candies.length
 2 <= n <= 100
 1 <= candies[i] <= 100
 1 <= extraCandies <= 50
 *
 *
 */
public class KidsWithTheGreatestNumberOfCandies {

     // V0
     public List<Boolean> kidsWithCandies(int[] candies, int extraCandies) {
         // edge
         List<Boolean> res = new ArrayList<>();

         int max = 0;
         for (int x : candies) {
             max = Math.max(x, max);
         }

         for (int i = 0; i < candies.length; i++) {
             //max = Math.max(x, max);
             int x = candies[i];
             if (x + extraCandies >= max) {
                 res.add(true);
             } else {
                 res.add(false);
             }
         }

         return res;
     }

     // V1
     // IDEA: AD HOC
     // https://leetcode.com/problems/kids-with-the-greatest-number-of-candies/editorial/
     public List<Boolean> kidsWithCandies_1(int[] candies, int extraCandies) {
         // Find out the greatest number of candies among all the kids.
         int maxCandies = 0;
         for (int candy : candies) {
             maxCandies = Math.max(candy, maxCandies);
         }
         // For each kid, check if they will have greatest number of candies
         // among all the kids.
         List<Boolean> result = new ArrayList<>();
         for (int candy : candies) {
             result.add(candy + extraCandies >= maxCandies);
         }

         return result;
     }

     // V2
     // https://leetcode.com/problems/kids-with-the-greatest-number-of-candies/solutions/3425582/easy-solutions-in-java-python-and-c-look-84uf/
     public List<Boolean> kidsWithCandies_2(int[] candies, int extraCandies) {
         int maxCandies = 0;
         for (int candy : candies) {
             maxCandies = Math.max(maxCandies, candy);
         }

         List<Boolean> result = new ArrayList<>();

         for (int candy : candies) {
             if (candy + extraCandies >= maxCandies) {
                 result.add(true);
             } else {
                 result.add(false);
             }
         }

         return result;
     }

     // V3
     // https://leetcode.com/problems/kids-with-the-greatest-number-of-candies/solutions/7250128/clean-simple-beats-easy-to-understand-ef-wu47/
     public List<Boolean> kidsWithCandies_3(int[] candies, int extraCandies) {
         int maxCandy = Arrays.stream(candies).max().getAsInt();
         List<Boolean> result = new ArrayList<>();

         for (int candy : candies) {
             result.add(candy + extraCandies >= maxCandy);
         }

         return result;
     }



}
