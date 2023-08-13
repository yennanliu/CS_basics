package dev;

// https://blog.csdn.net/neweastsun/article/details/80294811
// https://blog.51cto.com/u_5650011/5386895

import javafx.util.Pair;

import java.util.LinkedList;
import java.util.Queue;

public class PairDemo1 {

    public static void main(String[] args) {

        // init
        Pair<Integer, String> p1 = new Pair<>(1, "one");
        // get key
        Integer k1 = p1.getKey();
        // get value
        String v1 = p1.getValue();

        // use with other data structure
        Queue<Pair<Integer, String>> q = new LinkedList<>();
        q.add(p1);

    }
}
