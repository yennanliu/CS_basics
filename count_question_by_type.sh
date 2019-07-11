#!/bin/sh

#################################################################
# SCRIPT COUNT NUMBER OF LEETCODE QUESTIONS BY TYPE  
#################################################################
# https://unix.stackexchange.com/questions/80399/cat-all-lines-from-file-which-are-after-keyword
# dev 

question_type=$(cat README.md | grep '##')

for type in  "${question_type[@]}"
  do
     #cat README.md | sed '1,/^## Shell Script easy and medium$/d'
     echo $type && cat README.md | sed '1,/^$type$/d'
  done 
