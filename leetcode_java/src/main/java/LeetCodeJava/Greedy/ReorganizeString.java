package LeetCodeJava.Greedy;

// https://leetcode.com/problems/reorganize-string/description/

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 767. Reorganize String
 * Solved
 * Medium
 * Topics
 * Companies
 * Hint
 * Given a string s, rearrange the characters of s so that any two adjacent characters are not the same.
 *
 * Return any possible rearrangement of s or return "" if not possible.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "aab"
 * Output: "aba"
 * Example 2:
 *
 * Input: s = "aaab"
 * Output: ""
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 500
 * s consists of lowercase English letters.
 *
 *
 */
public class ReorganizeString {

    // V0
    // IDEA : HASHMAP + HEAP
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Greedy/reorganize-string.py#L35
    public String reorganizeString(String S) {
        // Step 1: Count the frequency of each character
        Map<Character, Integer> charCountMap = new HashMap<>();
        for (char c : S.toCharArray()) {
            charCountMap.put(c, charCountMap.getOrDefault(c, 0) + 1);
        }

        // Step 2: Use a priority queue (max heap) to keep characters sorted by
        // frequency
        /** NOTE !!!
         *
         *  we use PQ to track the characters count sorted in order
         */
        PriorityQueue<Map.Entry<Character, Integer>> maxHeap = new PriorityQueue<>(
                (a, b) -> b.getValue() - a.getValue());
        maxHeap.addAll(charCountMap.entrySet());

        // Step 3: Initialize a StringBuilder with a placeholder to avoid indexing
        // errors
        StringBuilder result = new StringBuilder("#");

        // Step 4: While the heap is not empty, try to append characters
        while (!maxHeap.isEmpty()) {
            boolean stop = true;

            // Iterate over the heap to find the most common character that isn't the last
            // character in the result
            List<Map.Entry<Character, Integer>> tempList = new ArrayList<>();
            while (!maxHeap.isEmpty()) {
                Map.Entry<Character, Integer> entry = maxHeap.poll();
                char currentChar = entry.getKey();
                int count = entry.getValue();

                // If the current character is not the same as the last character in the result
                /**
                 *  NOTE !!!
                 *
                 *   we get last element of stringBuilder via
                 *
                 *   sb.charAt(sb.length() - 1)
                 */
                if (currentChar != result.charAt(result.length() - 1)) {
                    stop = false;
                    result.append(currentChar);

                    // Decrease the count and add it back to the heap if it's still > 0
                    if (count - 1 > 0) {
                        entry.setValue(count - 1);
                        tempList.add(entry);
                    }
                    break;
                } else {
                    tempList.add(entry);
                }
            }

            // Add back all remaining entries to the heap
            maxHeap.addAll(tempList);

            // If no valid character was found, break the loop
            if (stop) {
                break;
            }
        }

        // Step 5: Return the result or an empty string if reorganization is not
        // possible
        String reorganizedString = result.substring(1); // Remove the placeholder
        return reorganizedString.length() == S.length() ? reorganizedString : "";
    }

    // V0'
    // IDEA : HASHMAP
    // TODO : fix below
//    public String reorganizeString(String s) {
//
//        if (s.length() == 1){
//            return s;
//        }
//
//        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
//        for (String x : s.split("")){
//            map.put(x, map.getOrDefault(x, 0)+1);
//        }
//
//        System.out.println(">>> map = " + map);
//        StringBuilder sb = new StringBuilder();
//        String prev = null;
//        //String res = "";
//
//        while(!map.isEmpty()){
//            for(String k : map.keySet()){
//                System.out.println(">>> k = " + k + ", keySet = " + map.keySet() + ", prev = " + prev);
//                if (prev != null && prev.equals(k)){
//                    return "";
//                }
//                sb.append(k);
//                prev = k;
//                if (map.get(k) - 1 == 0){
//                    map.remove(k);
//                }else{
//                    map.put(k, map.get(k)-1);
//                }
//            }
//        }
//
//        return sb.toString();
//    }

    // V1
    // IDEA : HASHMAP + PriorityQueue
    // https://leetcode.com/problems/reorganize-string/solutions/943268/heap-fetch-2-at-once-with-very-detail-explanations/
    public String reorganizeString_1(String S) {

        // step 1:
        // build a hashmap to store characters and its frequencies:
        Map<Character, Integer> freq_map = new HashMap<>();
        for (char c : S.toCharArray()) {
            freq_map.put(c, freq_map.getOrDefault(c, 0) + 1);
        }
        // step 2:
        // put the char of freq_map into the maxheap with sorting the frequencies by
        // large->small
        /** NOTE !!!!
         *
         *  make PQ as descending order
         *
         *   (a, b) -> freq_map.get(b) - freq_map.get(a)
         */
        PriorityQueue<Character> maxheap = new PriorityQueue<>(
                (a, b) -> freq_map.get(b) - freq_map.get(a)
        );
        // addAll() is adding more then one element to heap
        maxheap.addAll(freq_map.keySet());

        // now maxheap has the most frequent character on the top

        // step 3:
        // obtain the character 2 by 2 from the maxheap to put in the result sb
        // until there is only one element(character) left in the maxheap
        // create a stringbuilder to build the result result
        StringBuilder sb = new StringBuilder();
        while (maxheap.size() > 1) {
            char first = maxheap.poll();
            char second = maxheap.poll();
            sb.append(first);
            sb.append(second);
            freq_map.put(first, freq_map.get(first) - 1);
            freq_map.put(second, freq_map.get(second) - 1);

            // insert the character back to the freq_map if the count in
            // hashmap of these two character are still > 0
            if (freq_map.get(first) > 0) {
                maxheap.offer(first);
            }
            if (freq_map.get(second) > 0) {
                maxheap.offer(second);
            }
        }

        if (!maxheap.isEmpty()) {
            // when there is only 1 element left in the maxheap
            // check the count, it should not be greater than 1
            // otherwise it would be impossible and should return ""
            if (freq_map.get(maxheap.peek()) > 1) {
                return "";
            } else {
                sb.append(maxheap.poll());
            }
        }

        return sb.toString();
    }

    // V2
    // https://leetcode.com/problems/reorganize-string/solutions/3948228/100-fast-priorityqueue-with-explanation-c-java-python-c/
    public String reorganizeString_2(String s) {
        int[] f = new int[26];
        int n = s.length();

        for (int i = 0; i < n; i++) {
            f[s.charAt(i) - 'a']++;
            if (f[s.charAt(i) - 'a'] > (n + 1) / 2) {
                return "";
            }
        }

        PriorityQueue<Pair> p = new PriorityQueue<>((a, b) -> b.freq - a.freq);
        for (int i = 0; i < 26; i++) {
            if (f[i] != 0) {
                p.offer(new Pair(f[i], (char) (i + 'a')));
            }
        }

        StringBuilder ans = new StringBuilder();
        while (p.size() >= 2) {
            Pair p1 = p.poll();
            Pair p2 = p.poll();
            ans.append(p1.ch);
            ans.append(p2.ch);
            if (p1.freq > 1) {
                p.offer(new Pair(p1.freq - 1, p1.ch));
            }
            if (p2.freq > 1) {
                p.offer(new Pair(p2.freq - 1, p2.ch));
            }
        }

        if (!p.isEmpty()) {
            ans.append(p.poll().ch);
        }

        return ans.toString();
    }

    class Pair {
        int freq;
        char ch;

        Pair(int freq, char ch) {
            this.freq = freq;
            this.ch = ch;
        }
    }


    // V3
    // https://leetcode.com/problems/reorganize-string/solutions/3948110/easy-solution-python3-c-c-java-python-with-image/
    public String reorganizeString_3(String s) {
        Map<Character, Integer> count = new HashMap<>();
        for (char c : s.toCharArray()) {
            count.put(c, count.getOrDefault(c, 0) + 1);
        }

        List<int[]> maxHeap = new ArrayList<>();
        for (Map.Entry<Character, Integer> entry : count.entrySet()) {
            maxHeap.add(new int[]{-entry.getValue(), entry.getKey()});
        }
        heapify(maxHeap);

        int[] prev = null;
        StringBuilder res = new StringBuilder();
        while (!maxHeap.isEmpty() || prev != null) {
            if (prev != null && maxHeap.isEmpty()) {
                return "";
            }

            int[] top = heapPop(maxHeap);
            res.append((char) top[1]);
            top[0]++;

            if (prev != null) {
                heapPush(maxHeap, prev);
                prev = null;
            }

            if (top[0] != 0) {
                prev = top;
            }
        }

        return res.toString();
    }

    private void heapify(List<int[]> heap) {
        int n = heap.size();
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapifyDown(heap, i);
        }
    }

    private void heapifyDown(List<int[]> heap, int index) {
        int n = heap.size();
        int left = 2 * index + 1;
        int right = 2 * index + 2;
        int largest = index;

        if (left < n && heap.get(left)[0] < heap.get(largest)[0]) {
            largest = left;
        }
        if (right < n && heap.get(right)[0] < heap.get(largest)[0]) {
            largest = right;
        }

        if (largest != index) {
            Collections.swap(heap, index, largest);
            heapifyDown(heap, largest);
        }
    }

    private int[] heapPop(List<int[]> heap) {
        int n = heap.size();
        int[] top = heap.get(0);
        heap.set(0, heap.get(n - 1));
        heap.remove(n - 1);
        heapifyDown(heap, 0);
        return top;
    }

    private void heapPush(List<int[]> heap, int[] element) {
        heap.add(element);
        heapifyUp(heap, heap.size() - 1);
    }

    private void heapifyUp(List<int[]> heap, int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (heap.get(index)[0] >= heap.get(parent)[0]) {
                break;
            }
            Collections.swap(heap, index, parent);
            index = parent;
        }
    }

    // V3
}
