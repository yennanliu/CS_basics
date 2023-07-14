package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/generate-parentheses/

import java.util.*;

public class GenerateParentheses {

//    private List<String> res = new ArrayList<String>();;
//
//    // V0
//    public List<String> generateParenthesis(int n) {
//
//        if (n == 0){
//            return null;
//        }
//
//        if (n == 1){
//            this.res.add("()");
//            return this.res;
//        }
//
//        Stack<String> stack = new Stack<>();
//        helper(n*2, stack);
//        System.out.println(">>>> this.res = " + this.res.toString());
//        return this.res;
//    }
//
//    private void helper(int n, Stack<String> stack){
//        List<String> _list = Arrays.asList("(", ")");
//        System.out.println("n = " + n + " stack = " + String.valueOf(stack));
//        if (n == 0){
//            if (validate(stack)){
//                this.res.add(stack2String(stack));
//            }
//            this.res.add(stack2String(stack));
//            return;
//        }
//        if (n < 0){
//            return;
//        }
//        while (n >= 0){
//            for (String i : _list){
//                stack.push(i);
//                n -= 1;
//                helper(n, stack);
//                // undo op
//                //stack.pop();
//            }
//        }
//    }
//
//    private String stack2String(Stack<String> input){
//        Stack<String> _input = input;
//        if (_input.isEmpty()){
//            return "";
//        }
//        String _res = "";
//        while(!_input.isEmpty()){
//            _res += _input.pop();
//        }
//        return _res;
//    }
//
//    private Boolean validate(Stack<String> input){
//        Stack<String> _input = input;
//        HashMap<String, String> lookup = new HashMap<>();
//        lookup.put("(", "(");
//        lookup.put("[", "]");
//        lookup.put("{", "}");
//        Stack<String> s = new Stack<>();
//        while(!_input.isEmpty()){
//            String cur = _input.pop();
//            if (!lookup.containsKey(cur) && s.isEmpty()){
//                return false;
//            }else if (lookup.containsKey(cur)){
//                s.push(cur);
//            }else{
//              String last = s.peek();
//              if (lookup.get(last) != cur){
//                  return false;
//              }
//            }
//        }
//
//        return s.isEmpty() ? true : false;
//    }

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
