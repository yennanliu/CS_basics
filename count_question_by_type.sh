#!/bin/sh

#################################################################
# SCRIPT COUNT NUMBER OF LEETCODE QUESTIONS BY TYPE  
#################################################################
# get call for lines after word  :  https://unix.stackexchange.com/questions/80399/cat-all-lines-from-file-which-are-after-keyword
# get lines after and before via bash : https://stackoverflow.com/questions/12444808/how-do-i-fetch-lines-before-after-the-grep-result-in-bash
# dev 

#question_type=$(cat README.md | grep '##')
question_type=('## Array Easy and Medium' 
               '## Hash Table Easy and Medium'
               '## Linked List Easy and Medium')

for type in  "${question_type[@]}"
  do
     #cat README.md | sed '1,/^## Shell Script easy and medium$/d'
     #cat README.md | sed '1,/^## Heap Easy and Medium $/d'  | grep -i "BitMap Easy and Medium" -B  1000 | wc -l 
     #echo $type && cat README.md | sed '1,/^"$type"$/d' |  wc -l 
     #cat README.md | grep -i "SQL Easy and Medium" -A 1000 | grep -i "Shell Script Easy and Medium" -B  1000 | wc -l 
     echo $type && cat README.md | grep -i "$type" -A 1000 | wc -l 

  done 
