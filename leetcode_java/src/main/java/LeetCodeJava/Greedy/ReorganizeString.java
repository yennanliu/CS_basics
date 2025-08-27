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
    // IDEA: HASHMAP + PQ (fixed by gpt)
    public String reorganizeString(String s) {
        if (s == null || s.isEmpty())
            return "";
        if (s.length() <= 2)
            return s;

        // Count frequency of each character
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : s.toCharArray()) {
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }

        // Max-heap: sort characters by frequency descending
        PriorityQueue<Character> pq = new PriorityQueue<>(
                (a, b) -> freqMap.get(b) - freqMap.get(a));
        pq.addAll(freqMap.keySet());

        StringBuilder res = new StringBuilder();
        Character prev = null;

        /**
         *  NOTE !!!
         *
         *   while `!pq.isEmpty()` (but NOT freqMap)
         *
         */
        while (!pq.isEmpty()) {

            /**
             *  NOTE !!! we `poll` to-add element
             */
            char cur = pq.poll();

            /**
             *  NOTE !!!
             *
             *   below `if` handles the case:
             *
             *     ->
             *      1) `cur res` is NOT null (prev != null)
             *      AND
             *      2) prev == cur (prev element equals to the to-add element)
             *         e.g.  res ="v", pq = [v,a,b]
             */
            if (prev != null && prev == cur) {
                // edge case
                if (pq.isEmpty()){
                    return ""; // cannot reorganize
                }
                /**
                 *  NOTE !!!
                 *
                 *   so, if we face the ` res ="v", pq = [v,a,b]` case
                 *   (e.g. cur poll val equals the prev element, but we
                 *         STILL have other candidates (to poll)
                 *
                 *
                 *   -> what we can do is: poll the next element from PQ
                 *     and try to add it to res
                 */
                char next = pq.poll();
                res.append(next);
                freqMap.put(next, freqMap.get(next) - 1);
                if (freqMap.get(next) > 0){
                    pq.add(next);
                }

                /**  NOTE !!!
                 *
                 *  DON'T forget to add `cur` back to PQ
                 *
                 *  -> since we append the `next` element to res,
                 *    instead of `cur`
                 *
                 */
                pq.add(cur); // push current back
                prev = next;
            }
            /**
             *  NOTE !!!
             *
             *   below `if` handles the case:
             *
             *     ->
             *      1) `cur res` is null
             *      OR
             *      2) `cur  (current poll element) NOT equals to the prev
             */
            else {
                res.append(cur);
                freqMap.put(cur, freqMap.get(cur) - 1);
                if (freqMap.get(cur) > 0)
                    pq.add(cur);
                prev = cur;
            }
        }

        return res.toString();
    }

    // V0-0-1
    // IDEA: HASHMAP + PQ
    public String reorganizeString_0_0_1(String s) {

        // edge
        if (s.isEmpty()) {
            return "";
        }
        if (s.length() <= 2) {
            return s;
        }

        // { val : cnt }
        Map<String, Integer> cnt_map = new HashMap<>();

        for (String x : s.split("")) {
            cnt_map.put(x, cnt_map.getOrDefault(x, 0) + 1);
        }

        // custom PQ: sort with cnt_map val (val : big -> small)
        PriorityQueue<String> pq = new PriorityQueue<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int diff = cnt_map.get(o2) - cnt_map.get(o1);
                return diff;
            }
        });

        // can use stringBuilder as well
        String res = "";

        // NOTE !!! add `key` to PQ
        pq.addAll(cnt_map.keySet());

        /**
         * NOTE !!! we define prev
         */
        String prev = null;

        while (!pq.isEmpty()) {

            String val = pq.poll();

            /**
             * case 1) prev has val and prev == cur val
             */
            if (prev != null && val == prev) {
                // edge case ???
                if (pq.isEmpty()) {
                    return "";
                }

                /**
                 * NOTE !!! we pop another prev val, as next
                 */
                String next = pq.poll();
                res += next;
                /**
                 * NOTE !!! we update cnt_map by prev key
                 */
                cnt_map.put(next, cnt_map.get(next) - 1);
                /**
                 * NOTE !!! ONLY add prev back to PQ if its remaining cnt still > 0
                 */
                if (cnt_map.get(next) > 0) {
                    pq.add(next);
                }
                /**
                 * NOTE !!! add val back to PQ, since it's NOT used
                 */
                pq.add(val);
                /**
                 * NOTE !!! update `prev`
                 */
                prev = next;
            }
            /**
             * case 2) prev is null or prev != cur val
             */
            else {
                res += val;
                cnt_map.put(val, cnt_map.get(val) - 1);
                /**
                 * NOTE !!! ONLY add val back to PQ if its remaining cnt still > 0
                 */
                if (cnt_map.get(val) > 0) {
                    pq.add(val);
                }
                /**
                 * NOTE !!! update `prev`
                 */
                prev = val;
            }

        }

        return res;
    }


    // V0-0-2
    // IDEA: PQ (PriorityQueue<Character>) + HASHMAP (fixed by gpt)
    public String reorganizeString_0_0_2(String s) {
        // Edge cases
        if (s == null || s.length() == 0) {
            return "";
        }
        if (s.length() == 1) {
            return s;
        }

        // Count the frequency of each character
        Map<Character, Integer> charCount = new HashMap<>();
        for (char c : s.toCharArray()) {
            charCount.put(c, charCount.getOrDefault(c, 0) + 1);
        }

        // Use a max-heap (PriorityQueue) to store characters based on their frequency
        // V1
        //PriorityQueue<Character> maxHeap = new PriorityQueue<>(Comparator.comparingInt(charCount::get).reversed());

        // V2
        PriorityQueue<Character> maxHeap = new PriorityQueue<>(new Comparator<Character>() {
            private final Map<Character, Integer> counts = charCount; // Access the charCount map

            @Override
            public int compare(Character char1, Character char2) {
                // Compare based on the frequencies from the charCount map (descending order)
                return counts.get(char2) - counts.get(char1);
            }
        });

        for (char c : charCount.keySet()) {
            maxHeap.offer(c);
        }

        StringBuilder result = new StringBuilder();
        Character prevChar = null;

        while (!maxHeap.isEmpty()) {
            Character currentChar = maxHeap.poll();
            result.append(currentChar);
            charCount.put(currentChar, charCount.get(currentChar) - 1);

            if (prevChar != null && charCount.get(prevChar) > 0) {
                maxHeap.offer(prevChar);
            }
            prevChar = currentChar;
        }

        // If the length of the reorganized string is not equal to the original length,
        // it means it was not possible to reorganize the string according to the rules.
        return result.length() == s.length() ? result.toString() : "";
    }


    // V0-1
    // IDEA : HASHMAP + HEAP ( PriorityQueue<Map.Entry<Character, Integer>> )
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Greedy/reorganize-string.py#L35
    public String reorganizeString_0_1(String S) {
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

    // V0-2
    // IDEA: PQ + HASHMAP (fixed by gpt)
    public String reorganizeString_0_2(String s) {
        // Edge cases
        if (s == null || s.length() == 0) {
            return "";
        }
        if (s.length() == 1) {
            return s;
        }

        // Count the frequency of each character
        Map<Character, Integer> charCount = new HashMap<>();
        for (char c : s.toCharArray()) {
            charCount.put(c, charCount.getOrDefault(c, 0) + 1);
        }

        // Use a max-heap (PriorityQueue) to store characters based on their frequency
        PriorityQueue<Map.Entry<Character, Integer>> maxHeap = new PriorityQueue<>(
                (a, b) -> b.getValue() - a.getValue());

        for (Map.Entry<Character, Integer> entry : charCount.entrySet()) {
            maxHeap.offer(entry);
        }

        StringBuilder result = new StringBuilder();
        Map.Entry<Character, Integer> prevEntry = null;

        while (!maxHeap.isEmpty()) {
            Map.Entry<Character, Integer> currentEntry = maxHeap.poll();
            result.append(currentEntry.getKey());
            currentEntry.setValue(currentEntry.getValue() - 1);

            if (prevEntry != null && prevEntry.getValue() > 0) {
                maxHeap.offer(prevEntry);
            }
            prevEntry = currentEntry;
        }

        // If the length of the reorganized string is not equal to the original length,
        // it means it was not possible to reorganize the string according to the rules.
        return result.length() == s.length() ? result.toString() : "";
    }

    // V1
    // https://www.youtube.com/watch?v=2g_b1aYTHeg
    // https://github.com/neetcode-gh/leetcode/blob/main/java%2F0767-reorganize-string.java
    public String reorganizeString_1(String s) {
        HashMap<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            map.put(s.charAt(i), map.getOrDefault(s.charAt(i), 0) + 1);
        }
        PriorityQueue<Map.Entry<Character, Integer>> pq = new PriorityQueue<>(
                (a, b) ->
                        b.getValue() - a.getValue()
        );
        pq.addAll(map.entrySet());

        StringBuilder sb = new StringBuilder();

        while (!pq.isEmpty()) {
            Map.Entry<Character, Integer> temp1 = pq.poll();
            //if the character at sb's end is different from the max frequency character or the string is empty
            if (
                    sb.length() == 0 || sb.charAt(sb.length() - 1) != temp1.getKey()
            ) {
                sb.append(temp1.getKey());
                //update the value
                temp1.setValue(temp1.getValue() - 1);
            } else { //the character is same
                //hold the current character and look for the 2nd most frequent character
                Map.Entry<Character, Integer> temp2 = pq.poll();
                //if there is no temp2 i.e. the temp1 was the only character in the heap then there is no way to avoid adjacent duplicate values
                if (temp2 == null) return "";
                //else do the same thing as above
                sb.append(temp2.getKey());
                //update the value
                temp2.setValue(temp2.getValue() - 1);
                //if still has some value left add again to the heap
                if (temp2.getValue() != 0) pq.offer(temp2);
            }
            if (temp1.getValue() != 0) pq.offer(temp1);
        }
        return sb.toString();
    }


    // V2
    // IDEA : HASHMAP + PriorityQueue
    // https://leetcode.com/problems/reorganize-string/solutions/943268/heap-fetch-2-at-once-with-very-detail-explanations/
    public String reorganizeString_2(String S) {

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

    // V3
    // IDEA : HASHMAP + array + 26 alphabet consideration (gpt)
    public String reorganizeString_3(String S) {
        // Step 1: Count the frequency of each character using an array
        int[] charCounts = new int[26];
        for (char c : S.toCharArray()) {
            charCounts[c - 'a']++;
        }

        // Step 2: Find the most frequent character
        int maxCount = 0, maxCharIndex = -1;
        for (int i = 0; i < 26; i++) {
            if (charCounts[i] > maxCount) {
                maxCount = charCounts[i];
                maxCharIndex = i;
            }
        }

        // Step 3: If the most frequent character is more than half the string length, return ""
        if (maxCount > (S.length() + 1) / 2) {
            return "";
        }

        // Step 4: Arrange characters
        char[] result = new char[S.length()];
        int index = 0;

        // Fill the most frequent character first
        while (charCounts[maxCharIndex] > 0) {
            result[index] = (char) (maxCharIndex + 'a');
            index += 2; // Fill at even indices first
            charCounts[maxCharIndex]--;
        }

        // Fill the remaining characters
        for (int i = 0; i < 26; i++) {
            while (charCounts[i] > 0) {
                if (index >= S.length()) {
                    index = 1; // Switch to odd indices
                }
                result[index] = (char) (i + 'a');
                index += 2;
                charCounts[i]--;
            }
        }

        return new String(result);
    }

    // V4
    // https://leetcode.com/problems/reorganize-string/solutions/3948228/100-fast-priorityqueue-with-explanation-c-java-python-c/
    public String reorganizeString_4(String s) {
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


    // V5
    // https://leetcode.com/problems/reorganize-string/solutions/3948110/easy-solution-python3-c-c-java-python-with-image/
    public String reorganizeString_5(String s) {
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

    // V5

}
