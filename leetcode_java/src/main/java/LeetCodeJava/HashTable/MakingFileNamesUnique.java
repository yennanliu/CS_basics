package LeetCodeJava.HashTable;

// https://leetcode.com/problems/making-file-names-unique/description/

import java.util.*;

/**
 * 1487. Making File Names Unique
 * Medium
 *
 * Given an array of strings names of size n. You will create n folders in your file system such
 * that, at the ith minute, you will create a folder with the name names[i].
 *
 * Since two files cannot have the same name, if you enter a folder name that was previously used,
 * the system will have a suffix addition to its name in the form of (k), where, k is the smallest
 * positive integer such that the obtained name remains unique.
 *
 * Return an array of strings of length n where ans[i] is the actual name the system will assign
 * to the ith folder when you create it.
 *
 *
 * Example 1:
 *
 * Input: names = ["pes","fifa","gta","pes(2019)"]
 * Output: ["pes","fifa","gta","pes(2019)"]
 *
 * Example 2:
 *
 * Input: names = ["gta","gta(1)","gta","avalon"]
 * Output: ["gta","gta(1)","gta(2)","avalon"]
 * Explanation: "gta" is reserved and so is "gta(1)", so the third folder gets k = 2: "gta(2)".
 *
 * Example 3:
 *
 * Input: names = ["onepiece","onepiece(1)","onepiece(2)","onepiece(3)","onepiece"]
 * Output: ["onepiece","onepiece(1)","onepiece(2)","onepiece(3)","onepiece(4)"]
 *
 *
 * Constraints:
 *
 * 1 <= names.length <= 5 * 10^4
 * 1 <= names[i].length <= 20
 * names[i] consists of lowercase English letters, digits, and/or round brackets.
 *
 */
public class MakingFileNamesUnique {

    // V0
    // IDEA: HASH TABLE (taken name -> next suffix k worth trying; never restart k from 1)
    /**
     * time = O(L) amortised, L = total length of all names
     * space = O(L)
     */
    public String[] getFolderNames(String[] names) {
        Map<String, Integer> nextSuffix = new HashMap<>();
        String[] result = new String[names.length];

        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            if (!nextSuffix.containsKey(name)) {
                nextSuffix.put(name, 1);
                result[i] = name;
                continue;
            }

            // resume from the remembered k, so identical names do not rescan 1..k each time
            int k = nextSuffix.get(name);
            String candidate = name + "(" + k + ")";
            // a literal input like "gta(1)" may already hold this candidate
            while (nextSuffix.containsKey(candidate)) {
                k++;
                candidate = name + "(" + k + ")";
            }
            nextSuffix.put(name, k + 1);
            nextSuffix.put(candidate, 1);
            result[i] = candidate;
        }
        return result;
    }
}
