#!/bin/sh

#################################################################
# SCRIPT COUNT NUMBER OF LEETCODE QUESTIONS BY TYPE  
#################################################################
# ref : get call for lines after word  :  https://unix.stackexchange.com/questions/80399/cat-all-lines-from-file-which-are-after-keyword
# ref : get lines after and before via bash : https://stackoverflow.com/questions/12444808/how-do-i-fetch-lines-before-after-the-grep-result-in-bash
# ref : print next element in list via bash : https://stackoverflow.com/questions/30462040/calling-the-current-and-next-item-in-a-bash-for-loop
# dev 

#question_type=$(cat README.md | grep '##')
question_type=('## Array Easy and Medium' 
               '## Hash Table Easy and Medium'
               '## Linked List Easy and Medium'
               '## Stack Easy and Medium'
               '## Tree Easy and Medium'
               '## Heap Easy and Medium'
               '## BitMap Easy and Medium'
               '## String Easy and Medium'
               '## Queue Easy and Medium'
               '## Math Easy and Medium'
               '## Sort Easy and Medium'
               '## Two Pointers Easy and Medium'
               '## Recursion Easy and Medium'
               '## Binary Search Easy and Medium'
               '## Binary Search Tree Easy and Medium'
               '## Breadth-First Search Easy and Medium'
               '## Depth-First Search Easy and Medium'
               '## Backtracking Easy and Medium'
               '## Dynamic Programming Easy and Medium'
               '## Greedy Easy and Medium'
               '## Graph Easy and Medium'
               '## Geometry Easy and Medium'
               '## Simulation Easy and Medium'
               '## SQL Easy and Medium'
               '## Shell Script Easy and Medium')

for q in  "${!question_type[@]}"; 
  do
     #cat README.md | sed '1,/^## Shell Script easy and medium$/d'
     #cat README.md | sed '1,/^## Heap Easy and Medium $/d'  | grep -i "BitMap Easy and Medium" -B  1000 | wc -l 
     #echo $type && cat README.md | sed '1,/^"$type"$/d' |  wc -l 
     #cat README.md | grep -i "SQL Easy and Medium" -A 1000 | grep -i "Shell Script Easy and Medium" -B  1000 | wc -l 
     echo ${question_type[$q]} && cat README.md | grep -i "${question_type[$q]}" -A 100 | grep -i "${question_type[$(( $q + 1))]}"  -B  100  | wc -l 

  done 
