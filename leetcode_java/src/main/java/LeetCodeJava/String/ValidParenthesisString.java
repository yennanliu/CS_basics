package LeetCodeJava.String;

// https://leetcode.com/problems/valid-parenthesis-string/description/

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * 678. Valid Parenthesis String
 * Medium
 * Topics
 * Companies
 * Hint
 * Given a string s containing only three types of characters: '(', ')' and '*', return true if s is valid.
 *
 * The following rules define a valid string:
 *
 * Any left parenthesis '(' must have a corresponding right parenthesis ')'.
 * Any right parenthesis ')' must have a corresponding left parenthesis '('.
 * Left parenthesis '(' must go before the corresponding right parenthesis ')'.
 * '*' could be treated as a single right parenthesis ')' or a single left parenthesis '(' or an empty string "".
 *
 *
 * Example 1:
 *
 * Input: s = "()"
 * Output: true
 * Example 2:
 *
 * Input: s = "(*)"
 * Output: true
 * Example 3:
 *
 * Input: s = "(*))"
 * Output: true
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 100
 * s[i] is '(', ')' or '*'.
 *
 */
public class ValidParenthesisString {

    // V0
    // IDEA: GREEDY
    // https://neetcode.io/problems/valid-parenthesis-string
    /**
     *  IDEA 1) GREEDY
     *
     *   NOTE !!!
     *
     *   - minParenCnt: The minimum possible number of `unmatched` open parens seen so far.
     *   - bigParenCnt: The maximum possible number of `unmatched` open parens seen so far.
     *
     *  step 1) maintain 2 var
     *        - minParenCnt
     *        - bigParenCnt
     *
     *  step 2) within loop
     *     - if "(",  minParenCnt += 1, bigParenCnt += 1
     *     - if ")",  minParenCnt -= 1, bigParenCnt -= 1
     *     - if "*", `wild card`
     *         - minParenCnt -= 1
     *         - bigParenCnt += 1
     *     - NOTE !!
     *         - if minParenCnt < 0, we reset it as 0
     *         - if bigParenCnt < 0, return false directly
     *
     *    step 3) check if 0 == minParenCnt
     *
     */
    public boolean checkValidString(String s) {
        // edge
        if(s == null || s.isEmpty()){
            return true;
        }
        if(s.length() == 1){
            return s.equals("*"); // only "*", return true, otherwise return false
        }

        /** NOTE !!!
         *
         *  we init, maintain 2 var:
         *
         *  (consider there are 3 possible cases of `wild card` "*"
         *  -> e.g. "*" can be either ")" or "(" or ""
         *
         *   - minParenCnt
         *   - maxParenCnt
         */
        int minParenCnt = 0; // minimum possible number of `unmatched` open parens seen so far.
        int maxParenCnt = 0; // max possible number of `unmatched` open parens seen so far.

        for(String x: s.split("")){
            // case 1) "("
            if(x.equals("(")){
                minParenCnt += 1;
                maxParenCnt += 1;
            }
            // case 2) ")"
            else if(x.equals(")")){
                minParenCnt -= 1;
                maxParenCnt -= 1;
            }else{
                // case 3) "*" (wildcard)
                minParenCnt -= 1;
                maxParenCnt += 1;
            }

            // NOTE !!! below
            /** NOTE !!!
             *
             *  1) if minParenCnt < 0, reset it as 0,
             *     since it's possible to `keep 0 for minParenCnt`
             *
             *  2) if maxParenCnt < 0, reset false directly,
             *     since it does NOT make sense to use negative val as maxParenCnt
             */
            if(minParenCnt < 0){
                minParenCnt = 0;
            }
            if(maxParenCnt < 0){
                return false;
            }
        }

        /** NOTE !!!
         *
         *  final validation as below:
         *
         *   if s = "(((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((("
         *
         *   -> need below logic, then can verify successfully
         */
        return minParenCnt == 0; // ???
    }


  // V0-0-1
  // IDEA: 2D DP (gemini)
  /**  NOTE !!!
   *
   *  DP def:
   *
   *  boolean dp[i][j]:
   *     "Is the substring from index i to j valid?"
   *
   *  DP eq:
   *
   *  1. Base Case: If the length is 1, it's only true if the character is
   *  2. Base Case: If the length is 2, it's true if the pair is (), (*, *), or
   *
   *  3. Recursive Step:
   *     - If s[i] and s[j] can form a pair
   *       (e.g., ( and ), or * and *),
   *       then dp[i][j] is true
   *
   *     - if the inner part dp[i+1][j-1] is true.
   *       Split Point: We can also split the string at any index
   *       $k$. If dp[i][k] is true and dp[k+1][j] is true,
   *       then the whole thing is true.
   *
   */
  public boolean checkValidString_0_0_1(String s) {
      int n = s.length();
      if (n == 0)
          return true;

      /**  NOTE !!!
       *
       *  DP def:
       *
       *  boolean dp[i][j]:
       *     "Is the substring from index i to j valid?"
       *
       */
      // dp[i][j] means substring s[i...j] is valid
      boolean[][] dp = new boolean[n][n];

      // 1. Base Case: Length 1
      for (int i = 0; i < n; i++) {
          if (s.charAt(i) == '*')
              dp[i][i] = true;
      }


      /**  NOTE !!!
       *  DP eq:
       *
       *  1. Base Case: If the length is 1, it's only true if the character is
       *  2. Base Case: If the length is 2, it's true if the pair is (), (*, *), or
       *
       *  3. Recursive Step:
       *     - If s[i] and s[j] can form a pair
       *       (e.g., ( and ), or * and *),
       *       then dp[i][j] is true
       *
       *     - if the inner part dp[i+1][j-1] is true.
       *       Split Point: We can also split the string at any index
       *       $k$. If dp[i][k] is true and dp[k+1][j] is true,
       *       then the whole thing is true.
       *
       */
      // 2. Fill the table for lengths 2 to n
      for (int len = 2; len <= n; len++) {
          for (int i = 0; i <= n - len; i++) {
              int j = i + len - 1;

              // Option A: Check if s[i] and s[j] can be a matching pair
              // s[i] must be '(' or '*' AND s[j] must be ')' or '*'
              if ((s.charAt(i) == '(' || s.charAt(i) == '*') &&
                      (s.charAt(j) == ')' || s.charAt(j) == '*')) {
                  // If the substring between them is valid (or they are adjacent)
                  if (len == 2 || dp[i + 1][j - 1]) {
                      dp[i][j] = true;
                  }
              }

              // Option B: If not already true, try splitting the range at k
              // This handles cases like "()()" or "(**)(*)"
              if (!dp[i][j]) {
                  for (int k = i; k < j; k++) {
                      if (dp[i][k] && dp[k + 1][j]) {
                          dp[i][j] = true;
                          break;
                      }
                  }
              }
          }
      }

      return dp[0][n - 1];
  }


  // V0-1
  // IDEA: GREEDY (fixed by gpt)
  /**
   *  IDEA:
   *
   *  🧠 Explanation:
   * 	•	low = min number of unmatched '('
   * 	•	high = max number of unmatched '('
   * 	•	When you see a '*', it could be '(', ')', or nothing. So:
   * 	   •	low decreases (optimistically assume it becomes ')')
   * 	   •	high increases (assume it becomes '(')
   * 	•	If high < 0 at any point → too many unmatched ')', return false
   * 	•	If low == 0 at the end → valid string
   *
   */
  public boolean checkValidString_0_1(String s) {
        int low = 0; // min possible open parens
        int high = 0; // max possible open parens

        for (char c : s.toCharArray()) {
            if (c == '(') {
                low++;
                high++;
            } else if (c == ')') {
                if (low > 0){
                    low --;
                }
                high--;
            } else { // '*'
                if (low > 0){
                    low--; // treat * as ')'
                }
                high++; // treat * as '('
            }

            // Too many closing parens
            if (high < 0){
                return false;
            }

        }

        // low must be 0, meaning all '(' can be matched
        return low == 0;
    }

    // TODO: fix below:
//    public boolean checkValidString(String s) {
//        // edge
//        if(s == null || s.length() == 0){
//            return true;
//        }
//        if(s.length() == 1){
//            if(s != "*"){
//                return false;
//            }
//        }
//
//        Queue<String> leftParen = new LinkedList<>();
//        Queue<String> rightParen = new LinkedList<>();
//        Queue<String> starParen = new LinkedList<>();
//
//        String[] s_arr = s.split("");
//
//        for(int i = 0; i < s_arr.length; i++){
//            String cur = s_arr[i];
//            if(cur.equals("(")){
//                leftParen.add(cur);
//            }else if (cur.equals(")")){
//                if(!leftParen.isEmpty()){
//                    leftParen.poll();
//                }else if(!starParen.isEmpty()){
//                    starParen.poll();
//                }else{
//                    return false;
//                }
//            }else{
//                starParen.add(cur);
//            }
//        }
//
//        //return leftParen.isEmpty() && rightParen.isEmpty();
//        return leftParen.isEmpty();
//    }

    // V0-2
    // IDEA: 2D DP (gpt)
    public boolean checkValidString_0_2(String s) {
        int n = s.length();
        boolean[][] dp = new boolean[n + 1][n + 1];

        dp[0][0] = true; // empty string, 0 open

        for (int i = 1; i <= n; i++) {
            char c = s.charAt(i - 1);
            for (int j = 0; j <= n; j++) {
                if (c == '(') {
                    if (j > 0)
                        // V1
                        //dp[i][j] |= dp[i - 1][j - 1];
                        dp[i][j] = dp[i - 1][j - 1] || dp[i][j];
                } else if (c == ')') {
                    if (j < n)
                        //dp[i][j] |= dp[i - 1][j + 1];
                        dp[i][j] = dp[i - 1][j + 1] || dp[i][j];
                } else { // '*'
                    // empty
                    //dp[i][j] |= dp[i - 1][j];
                    dp[i][j] = dp[i - 1][j] || dp[i][j];
                    // '('
                    if (j > 0)
                        //dp[i][j] |= dp[i - 1][j - 1];
                        dp[i][j] = dp[i - 1][j - 1] || dp[i][j];
                    // ')'
                    if (j < n)
                        //dp[i][j] |= dp[i - 1][j + 1];
                        dp[i][j] = dp[i - 1][j + 1] ||  dp[i][j];
                }
            }
        }

        return dp[n][0];
    }


    // V1-1
    // https://neetcode.io/problems/valid-parenthesis-string
    // IDEA: RECURSION
    public boolean checkValidString_1_1(String s) {

        return dfs(0, 0, s);
    }

    private boolean dfs(int i, int open, String s) {
        if (open < 0) return false;
        if (i == s.length()) return open == 0;

        if (s.charAt(i) == '(') {
            return dfs(i + 1, open + 1, s);
        } else if (s.charAt(i) == ')') {
            return dfs(i + 1, open - 1, s);
        } else {
            return dfs(i + 1, open, s) ||
                    dfs(i + 1, open + 1, s) ||
                    dfs(i + 1, open - 1, s);
        }
    }

    // V1-2
    // https://neetcode.io/problems/valid-parenthesis-string
    // IDEA: DP (TOP DOWN)
    public boolean checkValidString_1_2(String s) {
        int n = s.length();
        Boolean[][] memo = new Boolean[n + 1][n + 1];
        return dfs(0, 0, s, memo);
    }

    private boolean dfs(int i, int open, String s, Boolean[][] memo) {
        if (open < 0) return false;
        if (i == s.length()) return open == 0;

        if (memo[i][open] != null) return memo[i][open];

        boolean result;
        if (s.charAt(i) == '(') {
            result = dfs(i + 1, open + 1, s, memo);
        } else if (s.charAt(i) == ')') {
            result = dfs(i + 1, open - 1, s, memo);
        } else {
            result = (dfs(i + 1, open, s, memo) ||
                    dfs(i + 1, open + 1, s, memo) ||
                    dfs(i + 1, open - 1, s, memo));
        }

        memo[i][open] = result;
        return result;
    }


    // V1-3
    // https://neetcode.io/problems/valid-parenthesis-string
    // IDEA: DP (BOTTOM UP)
    public boolean checkValidString_1_3(String s) {
        int n = s.length();
        boolean[][] dp = new boolean[n + 1][n + 1];
        dp[n][0] = true;

        for (int i = n - 1; i >= 0; i--) {
            for (int open = 0; open < n; open++) {
                boolean res = false;
                if (s.charAt(i) == '*') {
                    res |= dp[i + 1][open + 1];
                    if (open > 0) res |= dp[i + 1][open - 1];
                    res |= dp[i + 1][open];
                } else {
                    if (s.charAt(i) == '(') {
                        res |= dp[i + 1][open + 1];
                    } else if (open > 0) {
                        res |= dp[i + 1][open - 1];
                    }
                }
                dp[i][open] = res;
            }
        }
        return dp[0][0];
    }


    // V1-4
    // https:/neetcode.io/problems/valid-parenthesis-string
    // IDEA: DP (SPACE OPTIMIZED)
    public boolean checkValidString_1_4(String s) {
        int n = s.length();
        boolean[] dp = new boolean[n + 1];
        dp[0] = true;

        for (int i = n - 1; i >= 0; i--) {
            boolean[] newDp = new boolean[n + 1];
            for (int open = 0; open < n; open++) {
                if (s.charAt(i) == '*') {
                    newDp[open] = dp[open + 1] ||
                            (open > 0 && dp[open - 1]) || dp[open];
                } else if (s.charAt(i) == '(') {
                    newDp[open] = dp[open + 1];
                } else if (open > 0) {
                    newDp[open] = dp[open - 1];
                }
            }
            dp = newDp;
        }
        return dp[0];
    }


    // V1-5
    // https://neetcode.io/problems/valid-parenthesis-string
    // IDEA: STACK
    public boolean checkValidString_1_5(String s) {
        Stack<Integer> left = new Stack<>();
        Stack<Integer> star = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '(') {
                left.push(i);
            } else if (ch == '*') {
                star.push(i);
            } else {
                if (left.isEmpty() && star.isEmpty()) return false;
                if (!left.isEmpty()) {
                    left.pop();
                } else{
                    star.pop();
                }
            }
        }
        while (!left.isEmpty() && !star.isEmpty()) {
            if (left.pop() > star.pop())
                return false;
        }
        return left.isEmpty();
    }

    // V1-6
    // https://neetcode.io/problems/valid-parenthesis-string
    // IDEA: GREEDY
    public boolean checkValidString_1_6(String s) {
        int leftMin = 0, leftMax = 0;

        for (char c : s.toCharArray()) {
            if (c == '(') {
                leftMin++;
                leftMax++;
            } else if (c == ')') {
                leftMin--;
                leftMax--;
            } else {
                leftMin--;
                leftMax++;
            }
            if (leftMax < 0) {
                return false;
            }
            if (leftMin < 0) {
                leftMin = 0;
            }
        }
        return leftMin == 0;
    }


    // V2
    // IDEA: GREEDY (fixed by gpt)
    /**
     *  We can approach the solution with a greedy method that counts the number of open parentheses ( and ensures that the number of * can balance the parentheses. We'll perform two passes:
     *
     *  1) Left-to-right pass:
     *      Count ( and * to ensure that we don't have more )
     *      than we can potentially balance.
     *
     *  2) Right-to-left pass:
     *     Count ) and * to ensure that we don't have more
     *     ( than we can balance with *.
     *
     *
     *  Explanation:
     *
     *  1) Left-to-right pass:
     *      We check if the number of ) ever exceeds (.
     *      If it does, we attempt to use a * as an opening parenthesis.
     *
     *
     *
     *  2) Right-to-left pass: Similarly, we check
     *       if the number of ( exceeds ) and use * as a closing
     *       parenthesis if needed.
     *
     */
    public boolean checkValidString_2(String s) {
        // Left to right pass
        int leftBalance = 0;
        int starCount = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == '(') {
                leftBalance++;
            } else if (c == ')') {
                leftBalance--;
            } else { // c == '*'
                starCount++;
            }

            // If at any point we have more ')' than '(', then it's invalid
            if (leftBalance < 0) {
                if (starCount > 0) {
                    leftBalance++; // Treat a '*' as an open parenthesis '('
                    starCount--; // Use one star as '('
                } else {
                    return false;
                }
            }
        }

        // Right to left pass
        int rightBalance = 0;
        starCount = 0; // Reset star count for the second pass
        for (int i = s.length() - 1; i >= 0; i--) {
            char c = s.charAt(i);

            if (c == ')') {
                rightBalance++;
            } else if (c == '(') {
                rightBalance--;
            } else { // c == '*'
                starCount++;
            }

            // If at any point we have more '(' than ')', then it's invalid
            if (rightBalance < 0) {
                if (starCount > 0) {
                    rightBalance++; // Treat a '*' as a closing parenthesis ')'
                    starCount--; // Use one star as ')'
                } else {
                    return false;
                }
            }
        }

        return true; // If we passed both passes, the string is valid
    }



}
