package dev;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  1. define 2 classes: TrieNode, Trie
 *  2. use TrieNode in Trie
 *  3. implement class method in trie
 *      - insert
 *      - contains
 *      - startWith
 *
 *   ex 1) word = "abc"
 *
 *    -> trie:
 *
 *       a
 *        |
 *        b
 *        |
 *        c
 *
 *
 *
 */
public class MyTrie1 {

    class TrieNode{
        // attr
        String val; // ??
        Boolean isEnd;
        Map<String, List<String>> children;

        // constructor
        TrieNode(){
            this.val = "";
            this.isEnd = false;
            this.children = new HashMap<>();
        }

        TrieNode(String val){
            this.val = val;
            this.isEnd = false;
            this.children = new HashMap<>();
        }
    }

    // attr
    TrieNode node;


    // constructor
    MyTrie1(){
        this.node = new TrieNode();
    }

    // method
    public void insert(String word){
        //TrieNode node = this.node;
        for(String x: word.split("")){
            List<String> list = new ArrayList<>();
            if(this.node.children.containsKey(x)){
                //this.node.children.put(String.valueOf(x), list);
                list = this.node.children.get(x);
            }
            list.add(x);
            this.node.children.put(x, list);
            this.node = new TrieNode(this.node.children.get(x).toString());
        }
    }

    public boolean contains(String word){
        for(String x: word.split("")){
            if(!this.node.children.containsKey(x)){
                return false;
            }
            this.node = new TrieNode(this.node.children.get(x).toString());
        }
        return this.node.isEnd;
    }

    public boolean startWith(String word){
        for(String x: word.split("")){
            if(!this.node.children.containsKey(x)){
                return false;
            }
            this.node = new TrieNode(this.node.children.get(x).toString());
        }
        return true;
    }



}
