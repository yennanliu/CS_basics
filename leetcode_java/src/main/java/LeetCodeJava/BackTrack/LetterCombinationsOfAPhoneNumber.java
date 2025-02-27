package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/letter-combinations-of-a-phone-number/
/**
 * 17. Letter Combinations of a Phone Number
 * Solved
 * Medium
 * Topics
 * Companies
 * Given a string containing digits from 2-9 inclusive, return all possible letter combinations that the number could represent. Return the answer in any order.
 *
 * A mapping of digits to letters (just like on the telephone buttons) is given below. Note that 1 does not map to any letters.
 *
 *
 *
 *
 * Example 1:
 *
 * Input: digits = "23"
 * Output: ["ad","ae","af","bd","be","bf","cd","ce","cf"]
 * Example 2:
 *
 * Input: digits = ""
 * Output: []
 * Example 3:
 *
 * Input: digits = "2"
 * Output: ["a","b","c"]
 *
 *
 * Constraints:
 *
 * 0 <= digits.length <= 4
 * digits[i] is a digit in the range ['2', '9'].
 *
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LetterCombinationsOfAPhoneNumber {

    // V0
    List<String> _res = new ArrayList<String>();
    public List<String> letterCombinations(String _digits) {

        if (_digits.length() == 0){
            return new ArrayList<>();
        }

        HashMap<java.lang.String, java.lang.String> letters = new HashMap<>();
        letters.put("2", "abc");
        letters.put("3", "def");
        letters.put("4", "ghi");
        letters.put("5", "jkl");
        letters.put("6", "mno");
        letters.put("7", "pqrs");
        letters.put("8", "tuv");
        letters.put("9", "wxyz");

        _letter_builder(letters, 0, _digits, new StringBuilder());
        return this._res;
    }

    private void _letter_builder(HashMap<String, String> map, int idx, String digits, StringBuilder builder){

        if (builder.length() == digits.length()){
            this._res.add(builder.toString()); // NOTE this
            return;
        }

        String _digit = String.valueOf(digits.toCharArray()[idx]); // NOTE this
        String _alphabets = map.get(_digit);

        // backtrack
        for (char a : _alphabets.toCharArray()){
            builder.append(a);
            _letter_builder(map, idx+1, digits, builder);
            // undo
            // builder.deleteCharAt(0); // NOTE !!! in backtrack, we remove LAST element (idx = len - 1), instead of first element
            builder.deleteCharAt(builder.toString().length() - 1);
        }
    }

    // V0-1
    // IDEA: BACKTRACK (fixed by gpt)
    public List<String> letterCombinations_0_1(String digits) {
        // Mapping of digit to letters
        HashMap<Character, String> letters = new HashMap<>();
        letters.put('2', "abc");
        letters.put('3', "def");
        letters.put('4', "ghi");
        letters.put('5', "jkl");
        letters.put('6', "mno");
        letters.put('7', "pqrs");
        letters.put('8', "tuv");
        letters.put('9', "wxyz");

        // Edge case: if digits is empty, return an empty list
        if (digits == null || digits.length() == 0) {
            return new ArrayList<>();
        }

        // List to store the final result
        List<String> letterRes = new ArrayList<>();

        // Backtracking to find combinations
        backtrack(digits, 0, new StringBuilder(), letterRes, letters);

        return letterRes;
    }

    private void backtrack(String digits, int idx, StringBuilder current, List<String> letterRes,
                           HashMap<Character, String> letters) {
        // Base case: if the current combination length matches the digits length, add
        // to result
        if (current.length() == digits.length()) {
            letterRes.add(current.toString());
            return;
        }

        // Get the letters corresponding to the current digit
        char digit = digits.charAt(idx);
        String possibleLetters = letters.get(digit);

        // Explore each possible letter for the current digit
        for (char letter : possibleLetters.toCharArray()) {
            current.append(letter); // Choose the letter
            backtrack(digits, idx + 1, current, letterRes, letters); // Recurse for the next digit
            current.deleteCharAt(current.length() - 1); // Backtrack, undo the choice
        }
    }

    /**
     *  NOTE !!!
     *
     *   below approach is WRONG
     *   -> we CAN'T `loop over digit value` inside the backtrack call
     *
     *
     *   1) Reason:
     *
     *   1. Recursive Nature of Backtracking:
     *       Backtracking works by exploring one possibility at a time,
     *       progressively building a solution. At each step,
     *       you pick one choice (a letter in this case),
     *       move on to the next step (next digit), and explore the next possibility.
     *
     *       -> If you loop over all digits inside the backtrack method,
     *          you will break this structure by making multiple recursive
     *          calls in one iteration, which would mix up the exploration process.
     *          You want the recursion to handle the exploration one step at a time.
     *
     *   2. Index (idx) Ensures You Stay on Track:
     *      The idx variable tells the backtrack method
     *      which digit you are currently working on.
     *      By passing the index and incrementing it for each recursive call,
     *      you ensure that the recursion goes down one path at a time,
     *      and the algorithm processes one digit at a time in order.
     *
     *      -> Without the idx, if you looped over the digits inside backtrack,
     *         you'd be calling backtrack recursively on the same level (same idx),
     *         which means you'd repeatedly explore the same digit and its letters
     *         without properly progressing through the string.
     *
     *   3. Preventing Redundancy:
     *      By passing idx and not looping over digits, you avoid unnecessary redundancy.
     *      For example, if you looped over all digits inside the backtrack function,
     *      you would re-explore the same digits multiple times, which is inefficient.
     *
     *
     *  2) Why below is Incorrect:
     *
     *    - Redundant Exploration:
     *       You are looping over all digits, meaning for each recursion, you would be picking multiple characters for each position in the final combination (e.g., picking multiple characters for the first digit of the combination at the same time).
     *       You'd end up recursively exploring the same combinations multiple times, leading to redundant and incorrect results.
     *
     *
     *   - Infinite Loop Potential:
     *        This would mean that you're not progressing through the digits in the correct order. Instead of processing one digit and moving forward, you're processing the same digits repeatedly, leading to wrong results or even infinite recursion.
     *
     *
     *  3)  Summary:
     *   To summarize, passing idx to track the current position is essential
     *   for the recursive backtracking approach. It ensures that each digit
     *   is processed in the correct order and that the recursion explores each
     *   possibility one step at a time. Without idx,
     *   you'd end up re-exploring the same digits in the wrong order,
     *   leading to inefficiency and incorrect results.
     *
     */
//    private void backtrack(String digits, StringBuilder current, List<String> letterRes, HashMap<Character, String> letters) {
//        // Base case: if the current combination length matches the digits length, add to result
//        if (current.length() == digits.length()) {
//            letterRes.add(current.toString());
//            return;
//        }
//
//        // Looping over all digits for the current position
//        for (int i = 0; i < digits.length(); i++) {  // This loops over all digits!
//            char digit = digits.charAt(i);
//            String possibleLetters = letters.get(digit);
//
//            for (char letter : possibleLetters.toCharArray()) {
//                current.append(letter); // Choose the letter
//                backtrack(digits, current, letterRes, letters); // Recurse without progressing through the digits!
//                current.deleteCharAt(current.length() - 1); // Backtrack, undo the choice
//            }
//        }
//    }


    // V1
    private Map<Character, String> digitToChar = new HashMap<>();
    public List<String> letterCombinations_1(String digits) {
        if (digits.length() == 0) {
            return new ArrayList();
        }

        List<String> ans = new ArrayList();
        String cur = "";
        backtrack(digits, ans, cur, 0);
        return ans;
    }

    public void backtrack(
            String digits,
            List<String> ans,
            String cur,
            int index
    ) {
        if (cur.length() == digits.length()) {
            ans.add(cur);
            return;
        } else if (index >= digits.length()) {
            return;
        } else {
            String digit = digitToChar.get(digits.charAt(index));
            for (char c : digit.toCharArray()) {
                backtrack(digits, ans, cur + c, index + 1);
            }
        }
    }

    // V2
    // IDEA : BACKTRACK
    // https://leetcode.com/problems/letter-combinations-of-a-phone-number/editorial/
    private List<String> combinations = new ArrayList<>();
    private String phoneDigits;

    public List<String> letterCombinations_2(String digits) {
        // If the input is empty, immediately return an empty answer array
        if (digits.length() == 0) {
            return combinations;
        }

        // Initiate backtracking with an empty path and starting index of 0
        phoneDigits = digits;
        backtrack(0, new StringBuilder());
        return combinations;
    }

    private void backtrack(int index, StringBuilder path) {

//        private Map<Character, String> letters = Map.of(
//                '2', "abc", '3', "def", '4', "ghi", '5', "jkl",
//                '6', "mno", '7', "pqrs", '8', "tuv", '9', "wxyz");

        HashMap<String,String> letters = new HashMap<>();
        letters.put("3", "def");
        letters.put("4", "ghi");
        letters.put("5", "jkl");
        letters.put("6", "mno");
        letters.put("7", "pqrs");
        letters.put("8", "tuv");
        letters.put("9", "wxyz");

        // If the path is the same length as digits, we have a complete combination
        if (path.length() == phoneDigits.length()) {
            combinations.add(path.toString());
            return; // Backtrack
        }

        // Get the letters that the current digit maps to, and loop through them
        String possibleLetters = letters.get(phoneDigits.charAt(index));
        for (char letter: possibleLetters.toCharArray()) {
            // Add the letter to our current path
            path.append(letter);
            // Move on to the next digit
            backtrack(index + 1, path);
            // Backtrack by removing the letter before moving onto the next
            path.deleteCharAt(path.length() - 1);
        }
    }

}
