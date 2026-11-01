package LeetCodeJava.HashTable;

// https://leetcode.com/problems/naming-a-company/description/

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 2306. Naming a Company
 * Hard
 *
 * You are given an array of strings ideas that represents a list of names to be used in the process of naming a company. The process of naming a company is as follows:
 *
 * Choose 2 distinct names from ideas, call them ideaA and ideaB.
 * Swap the first letters of ideaA and ideaB with each other.
 * If both of the new names are not found in the original ideas, then the name ideaA ideaB (the concatenation of ideaA and ideaB, separated by a space) is a valid company name.
 * Otherwise, it is not a valid name.
 *
 * Return the number of distinct valid names for the company.
 *
 *
 * Example 1:
 *
 * Input: ideas = ["coffee","donuts","time","toffee"]
 * Output: 6
 * Explanation: The following selections are valid:
 * - ("coffee", "donuts"): The company name created is "doffee conuts".
 * - ("donuts", "coffee"): The company name created is "conuts doffee".
 * - ("donuts", "time"): The company name created is "tonuts dime".
 * - ("donuts", "toffee"): The company name created is "tonuts doffee".
 * - ("time", "donuts"): The company name created is "dime tonuts".
 * - ("toffee", "donuts"): The company name created is "doffee tonuts".
 * Therefore, there are a total of 6 distinct company names.
 *
 * The following are some examples of invalid selections:
 * - ("coffee", "time"): The name "toffee" formed after swapping already exists in the original array.
 * - ("time", "toffee"): Both names are still the same after swapping and exist in the original array.
 * - ("coffee", "toffee"): Both names formed after swapping already exist in the original array.
 *
 * Example 2:
 *
 * Input: ideas = ["lack","back"]
 * Output: 0
 * Explanation: There are no valid selections. Therefore, 0 is returned.
 *
 *
 * Constraints:
 *
 * 2 <= ideas.length <= 5 * 10^4
 * 1 <= ideas[i].length <= 10
 * ideas[i] consists of lowercase English letters.
 * All the strings in ideas are unique.
 *
 */
public class NamingACompany {

    // V0
    // IDEA: GROUP SUFFIXES BY FIRST LETTER + COUNT THE OVERLAP OF EACH LETTER PAIR
    //   for letters a != b, swapping first letters of (a + x, b + y) is valid iff
    //   suffix x is not in group b and suffix y is not in group a. so with
    //   shared = |group a ∩ group b|, the pair contributes
    //   2 * (|a| - shared) * (|b| - shared)  (x2 for the two orders).
    /**
     * time = O(26^2 * N)
     * space = O(N)
     */
    public long distinctNames(String[] ideas) {
        List<Set<String>> groups = new ArrayList<>();
        for (int letter = 0; letter < 26; letter++) {
            groups.add(new HashSet<>());
        }
        for (String idea : ideas) {
            groups.get(idea.charAt(0) - 'a').add(idea.substring(1));
        }

        long names = 0;
        for (int a = 0; a < 26; a++) {
            for (int b = a + 1; b < 26; b++) {
                Set<String> groupA = groups.get(a);
                Set<String> groupB = groups.get(b);

                // suffixes in both groups break the swap on both sides
                int shared = 0;
                for (String suffix : groupA) {
                    if (groupB.contains(suffix)) {
                        shared++;
                    }
                }
                long usableA = groupA.size() - shared;
                long usableB = groupB.size() - shared;
                names += 2 * usableA * usableB;
            }
        }
        return names;
    }
}
