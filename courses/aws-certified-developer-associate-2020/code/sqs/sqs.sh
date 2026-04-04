# get CLI help
aws sqs help

# list queues and specify the region
aws sqs list-queues --region us-east-1

# send a message
aws sqs send-message help
aws sqs send-message --queue-url https://queue.amazonaws.com/387124123361/MyFirstQueue --region us-east-1 --message-body hello-world

# receive a message
aws sqs receive-message help
aws sqs receive-message --region us-east-1  --queue-url https://queue.amazonaws.com/387124123361/MyFirstQueue --max-number-of-messages 10 --visibility-timeout 30 --wait-time-seconds 20

# delete a message
aws sqs delete-message help
aws sqs receive-message --region us-east-1  --queue-url https://queue.amazonaws.com/387124123361/MyFirstQueue --max-number-of-messages 10 --visibility-timeout 30 --wait-time-seconds 20
aws sqs delete-message --receipt-handle AQEBB+moMioWDaeaCZguaiMPXEqDe6n4JlGiUj/T0yUCLEKkL/tT1+68xyiZMe/ip7HBvgzSZJ6Gys8CCY8QO5qPypqZ9HSKdhl6sluJVl90x1igUHwz0gSEq/UbiLB8tNvFOKF90Dj4aH87mW3K7LLNUtv839z2Uu1Aeqd4kQDVB7SSqPzqCeaYFcLGquz+XIvT69vTAYP5HIsIjmwECx0faEiQF2JZ/KiVHq5n/ZEcG5UbIPMFmP+bg1n4ql8+2dUK+6G+gnIkMRPraZ4aweT9vUZmD5AXHDU5lnJBJNKj1QGuTbxtCjp/pzJvsul/uwsspUUWdRGP92ZpTlTDTL+WiJft3E9AUdqVhksc8NhExYDpdebWEqx43SbvzJMyJlrC --queue-url https://queue.amazonaws.com/387124123361/MyFirstQueue --region us-east-1