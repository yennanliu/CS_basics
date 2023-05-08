package LeetCodeJava.String;

// https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/

public class FindTheIndexOfTheFirstOccurrenceInString {

    // V0
    public int strStr(String haystack, String needle) {

        if (haystack.equals(null) || needle.equals(null)){
            return -1;
        }

        if(! haystack.contains(needle)){
            return -1;
        }

        return haystack.indexOf(needle);
    }

}
