package dev;

import java.util.*;

public class workspace9 {


    // LC 22
    // 10.13 - 10.23 am
    /**
     *  IDEA 1) BRUTE FORCE
     *  IDEA 2) BACKTRACK ???
     *
     *     - backtrack get all possible candidates
     *     - collect the validated ones
     *
     */
    // ???
    List<String> collected1 = new ArrayList<>();
    public List<String> generateParenthesis(int n) {
        List<String> res = new ArrayList<>();
        // edge
        if(n == 0){
            return res;
        }
        if(n == 1){
            res.add("()");
            return res;
        }

        // backtrack
        backtrackHelper(n, new ArrayList<>());
        // validate
        for(String c: collected1){
            if(validParentheses(c)){
                res.add(c);
            }
        }
        return res;
    }

    public void backtrackHelper(int n, List<String> cache){
        int size = cache.size(); //sb.toString().length();
        if(size == n * 2){
            // ???
            List<String> newCache = new ArrayList<>(cache);
            collected1.add(newCache.toString()); // ???
        }
        if(size > n * 2){
            //sb = new StringBuilder(); // ??
            return; // ??
        }
        String[] src = new String[]{"(", ")"};
        for(int i = 0; i < src.length; i++){
            cache.add(src[i]);
            backtrackHelper(n, cache);
            // undo
            cache.remove(cache.size()-1); // ??
        }
    }

    public boolean validParentheses(String input){
//        int l = 0;
//        int r = input.length() - 1;
        Queue<String> q = new LinkedList<>();
        Map<String, String> map = new HashMap<>();
        map.put("(", ")");
        for(String x: input.split("")){
            if(q.isEmpty() && x.equals(")")){
                return false;
            }
            if(x.equals("(")){
                q.add(x);
            }else{
                String tmp = q.poll();
                if(q.equals(x)){
                    return true;
                }
                return false;
            }

        }

        return true;
    }


}
