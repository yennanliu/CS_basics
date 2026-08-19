package LeetCodeJava.Math;

// https://leetcode.com/problems/poor-pigs/

/**
 *  458. Poor Pigs
 *  Hard
 *
 *  There are buckets buckets of liquid, where exactly one of the buckets is poisonous.
 *  To figure out which one is poisonous, you feed some number of (poor) pigs the liquid
 *  to see whether they will die or not. Unfortunately, you only have minutesToTest minutes
 *  to determine which bucket is poisonous.
 *
 *  You can feed the pigs according to these steps:
 *    1. Choose some live pigs to feed.
 *    2. For each pig, choose which buckets to feed it (a pig can be fed any number of buckets).
 *       The pig will consume all the chosen buckets simultaneously and will take no time.
 *    3. Wait for minutesToDie minutes. Any pigs that have been fed the poisonous bucket will die.
 *    4. Repeat this process until you run out of time.
 *
 *  Given buckets, minutesToDie, and minutesToTest, return the minimum number of pigs needed
 *  to figure out which bucket is poisonous within the allotted time.
 *
 *  Example 1:
 *    Input: buckets = 1000, minutesToDie = 15, minutesToTest = 60
 *    Output: 5
 *
 *  Example 2:
 *    Input: buckets = 4, minutesToDie = 15, minutesToTest = 15
 *    Output: 2
 *
 *  Constraints:
 *    1 <= buckets <= 1000
 *    1 <= minutesToDie <= minutesToTest <= 100
 */
public class PoorPigs {

    // V0
    // IDEA: each pig gives (minutesToTest / minutesToDie + 1) distinguishable states,
    //       so with p pigs we can distinguish states^p buckets -> smallest p with states^p >= buckets
    /**
     * time = O(log(buckets))
     * space = O(1)
     */
    public int poorPigs(int buckets, int minutesToDie, int minutesToTest) {
        int states = minutesToTest / minutesToDie + 1;
        int pigs = 0;
        long reach = 1; // states^pigs
        while (reach < buckets) {
            reach *= states;
            pigs++;
        }
        return pigs;
    }
}
