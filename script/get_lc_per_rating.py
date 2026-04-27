import csv
import random
import sys
from collections import defaultdict


def load_ratings(file_path):
    ratings = {}
    titles = {}
    with open(file_path, 'r', encoding='utf-8') as file:
        reader = csv.DictReader(file, delimiter='\t')
        for row in reader:
            slug = row['Title Slug']
            ratings[slug] = float(row['Rating'])
            titles[slug] = row['Title']
    return ratings, titles


def load_tags(file_path):
    tags = defaultdict(list)
    with open(file_path, 'r', encoding='utf-8') as file:
        for line in file:
            parts = line.strip().split(',')
            if len(parts) != 2:
                continue
            slug, tag = parts
            tags[slug].append(tag.lower())
    return tags


def filter_problems(ratings, tags, min_rating, max_rating):
    return [
        slug for slug, rating in ratings.items()
        if min_rating <= rating <= max_rating
        and 'bit manipulation' not in tags.get(slug, [])
    ]


def select_random_problems(problems, num_problems):
    if len(problems) < num_problems:
        print(f"Only {len(problems)} problems found. Returning all.")
        return problems
    return random.sample(problems, num_problems)


def main():
    ratings_file = 'rating.txt'
    tags_file = 'tags.txt'
    min_rating = float(sys.argv[1]) if len(sys.argv) > 1 else 100
    max_rating = float(sys.argv[2]) if len(sys.argv) > 2 else 1000
    num_problems = int(sys.argv[3]) if len(sys.argv) > 3 else 10

    ratings, titles = load_ratings(ratings_file)
    tags = load_tags(tags_file)
    filtered_slugs = filter_problems(ratings, tags, min_rating, max_rating)
    selected_slugs = select_random_problems(filtered_slugs, num_problems)

    print("\n🎯 Selected Problems:")
    for slug in selected_slugs:
        print(f"- {titles[slug]} (Rating: {ratings[slug]}) - https://leetcode.com/problems/{slug}/")


if __name__ == '__main__':
    main()
