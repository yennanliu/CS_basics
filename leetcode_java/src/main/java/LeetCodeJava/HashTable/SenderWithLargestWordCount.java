package LeetCodeJava.HashTable;

// https://leetcode.com/problems/sender-with-largest-word-count/description/

import java.util.*;

/**
 * 2284. Sender With Largest Word Count
 * Solved
 * Medium
 * Topics
 * Companies
 * Hint
 * You have a chat log of n messages. You are given two string arrays messages and senders where messages[i] is a message sent by senders[i].
 *
 * A message is list of words that are separated by a single space with no leading or trailing spaces. The word count of a sender is the total number of words sent by the sender. Note that a sender may send more than one message.
 *
 * Return the sender with the largest word count. If there is more than one sender with the largest word count, return the one with the lexicographically largest name.
 *
 * Note:
 *
 * Uppercase letters come before lowercase letters in lexicographical order.
 * "Alice" and "alice" are distinct.
 *
 *
 * Example 1:
 *
 * Input: messages = ["Hello userTwooo","Hi userThree","Wonderful day Alice","Nice day userThree"], senders = ["Alice","userTwo","userThree","Alice"]
 * Output: "Alice"
 * Explanation: Alice sends a total of 2 + 3 = 5 words.
 * userTwo sends a total of 2 words.
 * userThree sends a total of 3 words.
 * Since Alice has the largest word count, we return "Alice".
 * Example 2:
 *
 * Input: messages = ["How is leetcode for everyone","Leetcode is useful for practice"], senders = ["Bob","Charlie"]
 * Output: "Charlie"
 * Explanation: Bob sends a total of 5 words.
 * Charlie sends a total of 5 words.
 * Since there is a tie for the largest word count, we return the sender with the lexicographically larger name, Charlie.
 *
 *
 * Constraints:
 *
 * n == messages.length == senders.length
 * 1 <= n <= 104
 * 1 <= messages[i].length <= 100
 * 1 <= senders[i].length <= 10
 * messages[i] consists of uppercase and lowercase English letters and ' '.
 * All the words in messages[i] are separated by a single space.
 * messages[i] does not have leading or trailing spaces.
 * senders[i] consists of uppercase and lowercase English letters only.
 *
 *
 *
 */
public class SenderWithLargestWordCount {

  // V0
  // IDEA: HASH MAP (LC 347)
  public String largestWordCount(String[] messages, String[] senders) {
      // edge
      if (messages.length == 0 || senders.length == 0) {
          return null;
      }

      // map: {sender, word_cnt}
      Map<String, Integer> map = new HashMap<>();
      for (int i = 0; i < messages.length; i++) {
          String sender = senders[i];
          int wordCount = messages[i].split("\\s+").length; // Fix: Correct word split
          map.put(sender, map.getOrDefault(sender, 0) + wordCount);
      }

      // get key list
      List<String> keyList = new ArrayList<>(map.keySet());
      //System.out.println(">>> before sort keyList = " + keyList);

      // sort
      Collections.sort(keyList, new Comparator<String>() {
          @Override
          public int compare(String o1, String o2) {
              int diff = map.get(o2) - map.get(o1);
              if (diff == 0) {
                  return o2.compareTo(o1);
              }
              return diff;
          }
      });

      //System.out.println(">>> after sort keyList = " + keyList);
      return keyList.get(0);
  }

  // V1
  // IDEA: HASHMAP (GPT)
  public String largestWordCount_1(String[] messages, String[] senders) {
      // Edge case: Empty input
      if (messages.length == 0 || senders.length == 0) {
          return "";
      }

      // Map: {sender -> word_count}
      Map<String, Integer> wordCountMap = new HashMap<>();

      for (int i = 0; i < messages.length; i++) {
          String sender = senders[i];
          int wordCount = messages[i].split("\\s+").length; // Fix: Correct word split
          wordCountMap.put(sender, wordCountMap.getOrDefault(sender, 0) + wordCount);
      }

      // Find sender with highest word count
      String maxSender = "";
      int maxCount = 0;

      for (Map.Entry<String, Integer> entry : wordCountMap.entrySet()) {
          String sender = entry.getKey();
          int count = entry.getValue();

          // Update if a sender has more words or is lexicographically larger in a tie
          if (count > maxCount || (count == maxCount && sender.compareTo(maxSender) > 0)) {
              maxSender = sender;
              maxCount = count;
          }
      }

      return maxSender;
  }

  // V2
}
