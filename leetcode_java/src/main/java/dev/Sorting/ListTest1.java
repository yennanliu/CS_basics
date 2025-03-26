package dev.Sorting;

import java.util.ArrayList;
import java.util.List;

public class ListTest1 {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.remove(1);
        System.out.println(">>> list = " + list);
    }
}
