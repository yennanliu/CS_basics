package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/letter-combinations-of-a-phone-number/

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


    // V0
    // IDEA : BACKTRACK
//    public List<String> letterCombinations(String digits) {
//
//        List<String> res = new ArrayList<>();
//
//        HashMap<String,String> map = new HashMap<>();
//        // d =  {'2': 'abc', '3': 'def', '4': 'ghi', '5': 'jkl', '6': 'mno', '7': 'pqrs', '8': 'tuv', '9': 'wxyz'}
//        map.put("2", "abc");
//        map.put("3", "def");
//        map.put("4", "ghi");
//        map.put("5", "jkl");
//        map.put("6", "mno");
//        map.put("7", "pqrs");
//        map.put("8", "tuv");
//        map.put("9", "wxyz");
//
//        if (digits.length()==0){
//            return res;
//        }
//
//        if (digits.length()==1){
//            String _str = map.get(digits);
//            char[] _strArray = _str.toCharArray();
//            for (int x = 0; x < _strArray.length; x ++){
//                res.add(String.valueOf(_strArray[x]));
//            }
//        }
//
//        String cur = "";
//        _helper(digits, 0, map, cur, res);
//        return res;
//    }
//
//    private void _helper(String digits, int idx, HashMap<String, String> map, String cur, List<String> res){
//
//        if (cur.length() > digits.length()){
//            return;
//        }
//
//        if (cur.length() == digits.length()){
//            if (!res.contains(cur)){
//                StringBuilder sb = new StringBuilder();
//                sb.append(cur);
//                res.add(sb.toString());
//            }
//        }
//
//        map.get()
//        String _curVal = map.get(digits[idx]).toCharArray();
//        for (char x : map.get(digits[idx]).toCharArray()){
//            char c = digitsArray[i];
//            cur += c;
//            _helper(digits, i+1, map, cur, res);
//            // backtrack
//            cur.substring(0, cur.length()-2);
//        }
//
//    }

    // V1

//    private Map<Character, String> digitToChar = Map.of(
//            '2',
//            "abc",
//            '3',
//            "def",
//            '4',
//            "ghi",
//            '5',
//            "jkl",
//            '6',
//            "mno",
//            '7',
//            "pqrs",
//            '8',
//            "tuv",
//            '9',
//            "wxyz"
//    );

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
