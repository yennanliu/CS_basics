package dev;

import java.util.ArrayList;
import java.util.List;

public class ListTest {

    public static void main(String[] args) {

        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        System.out.println("list = " + list); // list = [1, 2, 3]

        list.set(0, 0);
        System.out.println("(after op) list = " + list); // list = [0, 2, 3]


    }

}
