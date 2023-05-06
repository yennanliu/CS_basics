package LeetCodeJava.Array;

// https://leetcode.com/problems/shortest-word-distance/

import java.util.ArrayList;
import java.util.List;

public class ShortestWordDistance {

    // V0
    public int shortestDistance(String[] wordsDict, String word1, String word2) {

        List<Integer> list1 =  new ArrayList<>();
        List<Integer> list2 =  new ArrayList<>();
        int idx = 0;
        int minDiff = (int) Math.pow(10, 10);

        for (String str : wordsDict){
            if (word1.equals(str)){
                list1.add(idx);
            } else if (word2.equals(str)) {
                list2.add(idx);
            }
            idx += 1;
        }

        for (int i = 0; i < list1.toArray().length; i++){
            for (int j = 0; j < list2.toArray().length; j++){
                minDiff = Math.min(Math.abs(list1.get(i) - list2.get(j)), minDiff);
            }
        }

//        System.out.println("list1 = " + list1);
//        System.out.println("list2 = " + list2);
        return minDiff;
    }

    // V1
    // https://leetcode.com/problems/shortest-word-distance/solutions/66931/ac-java-clean-solution/
    public int shortestDistance_1(String[] words, String word1, String word2) {
        int p1 = -1, p2 = -1, min = Integer.MAX_VALUE;

        for (int i = 0; i < words.length; i++) {
            if (words[i].equals(word1))
                p1 = i;

            if (words[i].equals(word2))
                p2 = i;

            if (p1 != -1 && p2 != -1)
                min = Math.min(min, Math.abs(p1 - p2));
        }

        return min;
    }

    // V2
    // https://leetcode.com/problems/shortest-word-distance/solutions/3458164/java-easy-to-understand-solution/
    public int shortestDistance_2(String[] wordsDict, String word1, String word2) {
        int minDistance = Integer.MAX_VALUE;

        for(int i = 0; i< wordsDict.length; i++) {
            if(wordsDict[i].equals(word1)) {
                int k = i-1;
                while(k>=0) {
                    if(wordsDict[k].equals(word2)) minDistance = Math.min(minDistance,i-k);
                    k--;
                }
                k = i+1;
                while(k<wordsDict.length) {
                    if(wordsDict[k].equals(word2)) minDistance = Math.min(minDistance,k-i);
                    k++;
                }
            }
        }

        return minDistance;
    }

}
