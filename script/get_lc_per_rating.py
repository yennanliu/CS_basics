import csv
import random

# Load ratings from rating.txt
def load_ratings(file_path):
    ratings = {}
    titles = {}
    with open(file_path, 'r', encoding='utf-8') as file:
        reader = csv.DictReader(file, delimiter='\t')
        for row in reader:
            slug = row['Title Slug']
            title = row['Title']
            rating = float(row['Rating'])
            ratings[slug] = rating
            titles[slug] = title
    return ratings, titles

# Load tags from tags.txt
def load_tags(file_path):
    tags = {}
    with open(file_path, 'r', encoding='utf-8') as file:
        for line in file:
            parts = line.strip().split(',')
            if len(parts) != 2:
                continue
            slug, tag = parts
            if slug not in tags:
                tags[slug] = []
            tags[slug].append(tag.lower())
    return tags

# Filter problems based on rating and exclude bit manipulation
def filter_problems(ratings, tags, min_rating, max_rating):
    filtered = []
    for slug, rating in ratings.items():
        if min_rating <= rating <= max_rating:
            problem_tags = tags.get(slug, [])
            if 'bit manipulation' not in problem_tags:
                filtered.append(slug)
    return filtered

# Randomly select a specified number of problems
def select_random_problems(problems, num_problems):
    if len(problems) < num_problems:
        print(f"Only {len(problems)} problems found. Returning all.")
        return problems
    return random.sample(problems, num_problems)

# Main execution
def main():
    ratings_file = 'rating.txt'
    tags_file = 'tags.txt'

    # min_rating = float(input("Enter minimum rating: "))
    # max_rating = float(input("Enter maximum rating: "))
    # num_problems = int(input("Enter number of problems to select: "))

    min_rating = 100
    max_rating = 1000
    num_problems = 10


    ratings, titles = load_ratings(ratings_file)
    tags = load_tags(tags_file)
    filtered_slugs = filter_problems(ratings, tags, min_rating, max_rating)
    selected_slugs = select_random_problems(filtered_slugs, num_problems)

    print("\n🎯 Selected Problems:")
    for slug in selected_slugs:
        print(f"- {titles[slug]} (Rating: {ratings[slug]}) - https://leetcode.com/problems/{slug}/")

if __name__ == '__main__':
    main()
