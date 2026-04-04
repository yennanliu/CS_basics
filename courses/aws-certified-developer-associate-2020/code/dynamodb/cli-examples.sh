#!/bin/bash

# Demo Projection Expression
aws dynamodb scan --table-name UserPosts --projection-expression "user_id, content"  --region us-east-1

# Demo Filter Expression
aws dynamodb scan --table-name UserPosts --filter-expression "user_id = :u" --expression-attribute-values '{ ":u": {"S":"124usersoi3"}}' --region us-east-1

# Page Size demo: will do 1 API call if you have 3 Items
aws dynamodb scan --table-name UserPosts --region us-east-1

# Will do 3 API calls if you have 3 Items
aws dynamodb scan --table-name UserPosts --region us-east-1 --page-size 1

# Max Item demo:
aws dynamodb scan --table-name UserPosts --region us-east-1 --max-items 1

# Fetch the next item
aws dynamodb scan --table-name UserPosts --region us-east-1 --max-items 1 --starting-token eyJFeGNsdXNpdmVTdGFydEtleSI6IG51bGwsICJib3RvX3RydW5jYXRlX2Ftb3VudCI6IDF9

# Fetch the next item
aws dynamodb scan --table-name UserPosts --region us-east-1 --max-items 1 --starting-token eyJFeGNsdXNpdmVTdGFydEtleSI6IG51bGwsICJib3RvX3RydW5jYXRlX2Ftb3VudCI6IDJ9
