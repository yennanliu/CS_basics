package LeetCodeJava.HashTable;

// https://leetcode.com/problems/compare-strings-by-frequency-of-the-smallest-character/solutions/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CompareStringsByFrequencyOfTheSmallestCharacter {

    // V0
    // IDEA : HASH MAP + SORT + BINARY SEARCH (fixed by gpt)
    public int[] numSmallerByFrequency(String[] queries, String[] words) {

        int[] wordsFrequency = new int[words.length];
        Map<String, Integer> frequencyMap = new HashMap<>();

        // Calculate the frequency of the smallest character for each word and store in map
        for (int i = 0; i < words.length; i++) {
            wordsFrequency[i] = getFrequencyOfSmallestCharacter_(words[i], frequencyMap);
        }

        //System.out.println(">>> (before sort) wordsFrequency = " + Arrays.toString(wordsFrequency));

        // sort, for binary search
        Arrays.sort(wordsFrequency);
        //System.out.println(">>> (after sort) wordsFrequency = " + Arrays.toString(wordsFrequency));

        int[] result = new int[queries.length];

        for (int i = 0; i < queries.length; i++) {
            int queryFrequency = getFrequencyOfSmallestCharacter_(queries[i], frequencyMap);
            result[i] = countGreater_(wordsFrequency, queryFrequency);
        }

        System.out.println(">>> result = " + Arrays.toString(result));

        return result;
    }

    public int getFrequencyOfSmallestCharacter_(String word, Map<String, Integer> map){
        int cnt = 1;
        String prev = null;
        for (String w : word.split("")){
            //System.out.println("w = " + w + ", prev = " + prev + ", cnt = " + cnt);
            if (prev == null){
                prev = w;
                cnt = 1;
            }
            else if (prev.compareTo(w) > 0){
                prev = w;
                cnt = 1;
            }
            else if (prev.equals(w)){
                cnt += 1;
            }
        }
        return cnt;
    }

    public int countGreater_(int[] wordFreq, int freq) {
        // binary search (find bigger num idx)
        int l = 0;
        int r = wordFreq.length - 1;

        while (l < r) {
            int mid = (l + r) / 2;
            if (wordFreq[mid] <= freq) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }

        // After exiting the loop, `l` should be pointing to the first element greater than `freq`.
        // If all elements are less than or equal to `freq`, `l` will be at the end of the array.
        if (wordFreq[l] <= freq) {
            return 0;
        }

        return wordFreq.length - l;
    }

    // V1
    // IDEA : HASHMAP (gpt)
    public int[] numSmallerByFrequency_1(String[] queries, String[] words) {
        int[] wordsFrequency = new int[words.length];
        Map<String, Integer> frequencyMap = new HashMap<>();

        // Calculate the frequency of the smallest character for each word and store in map
        for (int i = 0; i < words.length; i++) {
            wordsFrequency[i] = getFrequencyOfSmallestCharacter(words[i], frequencyMap);
        }

        // Sort the frequencies for words to enable binary search
        Arrays.sort(wordsFrequency);

        int[] result = new int[queries.length];

        // For each query, calculate the frequency of the smallest character
        // and count the number of words with greater frequency
        for (int i = 0; i < queries.length; i++) {
            int queryFrequency = getFrequencyOfSmallestCharacter(queries[i], frequencyMap);
            result[i] = countGreater(wordsFrequency, queryFrequency);
        }

        return result;
    }

    // Helper method to calculate the frequency of the smallest character in a string
    private int getFrequencyOfSmallestCharacter(String s, Map<String, Integer> frequencyMap) {
        if (frequencyMap.containsKey(s)) {
            return frequencyMap.get(s);
        }

        char smallestChar = 'z';
        int count = 0;

        for (char c : s.toCharArray()) {
            if (c < smallestChar) {
                smallestChar = c;
                count = 1;
            } else if (c == smallestChar) {
                count++;
            }
        }

        frequencyMap.put(s, count);
        return count;
    }

    // Helper method to count the number of elements in the array that are greater than the given value
    private int countGreater(int[] arr, int value) {
        int left = 0, right = arr.length;

        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] <= value) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }

        return arr.length - left;
    }


    // V1-1
    // IDEA : ARRAY + BINARY SEARCH (gpt)
    public int[] numSmallerByFrequency_1_1(String[] queries, String[] words) {
        int[] wordsFrequency = new int[words.length];

        // Calculate the frequency of the smallest character for each word
        for (int i = 0; i < words.length; i++) {
            wordsFrequency[i] = getFrequencyOfSmallestCharacter(words[i]);
        }

        // NOTE !!! Sort the frequencies for words to enable binary search
        // binary search
        Arrays.sort(wordsFrequency);

        int[] result = new int[queries.length];

        // For each query, calculate the frequency of the smallest character
        // and count the number of words with greater frequency
        for (int i = 0; i < queries.length; i++) {
            /**
             *  NOTE !!!
             *
             *   (func parameter)
             *
             *   private int countGreater(int[] arr, int value)
             */
            int queryFrequency = getFrequencyOfSmallestCharacter(queries[i]);
            result[i] = countGreater_1(wordsFrequency, queryFrequency);
        }

        return result;
    }

    // Helper method to calculate the frequency of the smallest character in a string
    private int getFrequencyOfSmallestCharacter(String s) {
        char smallestChar = 'z';
        int count = 0;

        for (char c : s.toCharArray()) {
            if (c < smallestChar) {
                smallestChar = c;
                count = 1;
            } else if (c == smallestChar) {
                count++;
            }
        }

        return count;
    }

    // Helper method to count the number of elements in the array that are greater than the given value
    // binary search
    private int countGreater_1(int[] arr, int value) {
        int left = 0, right = arr.length;

        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] <= value) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }

        return arr.length - left;
    }


    // V2
    // https://leetcode.com/problems/compare-strings-by-frequency-of-the-smallest-character/submissions/1312707199/
    public int[] numSmallerByFrequency_2(String[] queries, String[] words) {
        int[] minCount = new int[11];
        int[] aggregated = new int[11];
        int[] result = new int[queries.length];

        //build array with count of words by min count character. O(NK)
        for (int i = 0; i < words.length; i++) {
            minCount[minFunc(words[i])]++;
        }

        //build presum by count. O(1)
        for (int i = 1; i < aggregated.length; i++) {
            aggregated[i] += aggregated[i - 1] + minCount[i];
        }

        //find min count character in queries and set in result using presum O(MT)
        for (int i = 0; i < queries.length; i++) {
            int count = minFunc(queries[i]);
            result[i] = aggregated[aggregated.length - 1] - aggregated[count];
        }

        return result;
    }

    private int minFunc(String s) {
        int[] chars = new int[26];
        int minCount = 0;
        for (char c : s.toCharArray()) {
            chars[c - 'a']++;
        }

        int i = 0;
        while (minCount == 0) {
            minCount = chars[i++];
        }

        return minCount;
    }

    // V3
    // https://leetcode.com/problems/compare-strings-by-frequency-of-the-smallest-character/solutions/1200429/java-extremely-easy-solution-using-normal-strings-and-arraylist/
    public int[] numSmallerByFrequency_3(String[] queries, String[] words) {

        ArrayList<Integer> temp = new ArrayList<Integer>();
        int count = 0;

        for (int i = 0; i < queries.length; i++) {
            for (int j = 0; j < words.length; j++) {

                char temp1 = SmallestCharacter(queries[i]);
                int freq1 = freqOfSmallestCharacter(queries[i], temp1);
                char temp2 = SmallestCharacter(words[j]);
                int freq2 = freqOfSmallestCharacter(words[j], temp2);

                if (freq1 < freq2) {
                    count++;
                }
            }
            temp.add(count);
            count = 0;
        }
        int size = temp.size();
        int[] ans = new int[size];
        for (int i = 0; i < size; i++) {
            ans[i] = temp.get(i);
        }
        return ans;
    }

    public int freqOfSmallestCharacter(String str, char a) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == a) {
                count++;
            }
        }
        return count;
    }

    public char SmallestCharacter(String str) {
        char temp = str.charAt(0);
        for (int i = 0; i < str.length(); i++) {
            if (temp > str.charAt(i)) {
                temp = str.charAt(i);
            }
        }
        return temp;
    }

}
