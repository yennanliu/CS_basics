#!/bin/sh

COMPANY=$1
OUTPUT_FILE=$2

echo 'COMPANY = ' $COMPANY
echo 'OUTPUT_FILE = ' $OUTPUT_FILE 

if [[ ! $COMPANY || ! $OUTPUT_FILE ]]; then
    echo "either COMPANY or OUTPUT_FILE is unset";
    exit 1 ; 
fi

header1='|  #  | Title           |  Solution       |  Time           | Space           | Difficulty    | Note           | Status|'
header2='|-----|---------------- | --------------- | --------------- | --------------- | ------------- |--------------|-----|'

echo $header1 >> $OUTPUT_FILE
echo $header2 >> $OUTPUT_FILE

cat README.md | grep $COMPANY >> $OUTPUT_FILE