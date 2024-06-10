package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/generate-parentheses/

import java.util.*;

public class GenerateParentheses {

    // V0
    // IDEA : BACKTRACK (fix by gpt)
    // https://www.youtube.com/watch?v=s9fokUqJ76A
    // https://github.com/neetcode-gh/leetcode/blob/main/java/0022-generate-parentheses.java
    List<String> res_ = new ArrayList<>();
    public List<String> generateParenthesis(int n) {
        if (n == 0) {
            return res_;
        }

        // Backtrack
        getParenthesis_(n, 0, 0, new StringBuilder());
        //System.out.println("this.res_ = " + this.res_);
        return this.res_;
    }

    // leftCnt : "(" count
    // rightCnt : ")" count
    private void getParenthesis_(int n, int leftCnt, int rightCnt, StringBuilder cur) {
        if (cur.length() == 2 * n) {
            res_.add(cur.toString());
            return;
        }

        if (leftCnt < n) {
            cur.append("(");
            getParenthesis_(n, leftCnt + 1, rightCnt, cur);
            cur.deleteCharAt(cur.length() - 1);
        }
        if (rightCnt < leftCnt) {
            cur.append(")");
            getParenthesis_(n, leftCnt, rightCnt + 1, cur);
            cur.deleteCharAt(cur.length() - 1);
        }
    }

    // V0'
    // https://github.com/neetcode-gh/leetcode/blob/main/java/0022-generate-parentheses.java
    // https://www.youtube.com/watch?v=s9fokUqJ76A
    // IDEA : BACKTRACK
    Stack<Character> stack = new Stack<>();
    List<String> res = new ArrayList<>();

    public List<String> generateParenthesis_0_1(int n) {
        backtrack(0, 0, n);
        return res;
    }

    private void backtrack(int openN, int closedN, int n) {
        if (openN == closedN && closedN == n) {
            Iterator vale = stack.iterator();
            String temp = "";
            while (vale.hasNext()) {
                temp = temp + vale.next();
            }
            res.add(temp);
        }
        if (openN < n) {
            stack.push('(');
            backtrack(openN + 1, closedN, n);
            stack.pop();
        }
        if (closedN < openN) {
            stack.push(')');
            backtrack(openN, closedN + 1, n);
            stack.pop();
        }
    }

    // V0'
    // IDEA : backtrack + valid parentheses (gpt)
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Backtracking/generate-parentheses.py#L27
    // Method to generate all combinations of well-formed parentheses
    public List<String> generateParenthesis_0_2(int n) {
        List<String> res = new ArrayList<>();
        //List<String> _list = List.of("(", ")");
        List<String> _list = new ArrayList<>();
        _list.add("(");
        _list.add(")");

        // Backtracking helper function
        generateParenthesisHelper(res, "", n, _list);
        return res;
    }

    // Helper function for backtracking
    private void generateParenthesisHelper(List<String> res, String tmp, int n, List<String> _list) {
        if (tmp.length() == n * 2) {
            if (isValid_0(tmp)) {
                res.add(tmp);
            }
            return;
        }

        for (String l : _list) {
            generateParenthesisHelper(res, tmp + l, n, _list);
        }
    }

    // Method to check if a string of parentheses is valid
    private boolean isValid_0(String s) {
        java.util.Map<Character, Character> lookup = new java.util.HashMap<>();
        lookup.put('(', ')');
        lookup.put('[', ']');
        lookup.put('{', '}');

        java.util.Stack<Character> q = new java.util.Stack<>();
        for (char i : s.toCharArray()) {
            if (!lookup.containsKey(i) && q.isEmpty()) {
                return false;
            } else if (lookup.containsKey(i)) {
                q.push(i);
            } else {
                char tmp = q.pop();
                if (lookup.get(tmp) != i) {
                    return false;
                }
            }
        }
        return q.isEmpty();
    }

    // V1
    // IDEA : Brute Force
    // https://leetcode.com/problems/generate-parentheses/editorial/
    private boolean isValid(String pString) {
        int leftCount = 0;
        for (char p : pString.toCharArray()) {
            if (p == '(') {
                leftCount++;
            } else {
                leftCount--;
            }

            if (leftCount < 0) {
                return false;
            }
        }
        return leftCount == 0;
    }

    public List<String> generateParenthesis_2(int n) {
        List<String> answer = new ArrayList<>();
        Queue<String> queue = new LinkedList<>(Arrays.asList(""));

        while (!queue.isEmpty()) {
            String curString = queue.poll();

            // If the length of curString is 2 * n, add it to `answer` if
            // it is valid.
            if (curString.length() == 2 * n) {
                if (isValid(curString)) {
                    answer.add(curString);
                }
                continue;
            }
            queue.offer(curString + ")");
            queue.offer(curString + "(");
        }

        return answer;
    }


    // V2
    // IDEA :  Backtracking, Keep Candidate Valid
    // https://leetcode.com/problems/generate-parentheses/editorial/
    public List<String> generateParenthesis_3(int n) {
        List<String> answer = new ArrayList<>();
        backtracking(answer, new StringBuilder(), 0, 0, n);

        return answer;
    }

    private void backtracking(List<String> answer, StringBuilder curString, int leftCount, int rightCount, int n) {
        if (curString.length() == 2 * n) {
            answer.add(curString.toString());
            return;
        }
        if (leftCount < n) {
            curString.append("(");
            backtracking(answer, curString, leftCount + 1, rightCount, n);
            curString.deleteCharAt(curString.length() - 1);
        }
        if (leftCount > rightCount) {
            curString.append(")");
            backtracking(answer, curString, leftCount, rightCount + 1, n);
            curString.deleteCharAt(curString.length() - 1);
        }
    }

    // V3
    // IDEA: Divide and Conquer
    // https://leetcode.com/problems/generate-parentheses/editorial/
    public List<String> generateParenthesis_4(int n) {
        if (n == 0) {
            return new ArrayList(Arrays.asList(""));
        }

        List<String> answer = new ArrayList();
        for (int leftCount = 0; leftCount < n; ++leftCount)
            for (String leftString: generateParenthesis_4(leftCount))
                for (String rightString: generateParenthesis_4(n - 1 - leftCount))
                    answer.add("(" + leftString + ")" + rightString);
        return answer;
    }

}
