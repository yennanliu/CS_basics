import random

# Load ratings from ratings.txt
def load_ratings(file_path):
    ratings = {}
    with open(file_path, 'r') as file:
        for line in file:
            parts = line.strip().split(' - ')
            if len(parts) == 2:
                name, rating = parts
                ratings[name] = int(rating)
    return ratings

# Load problem tags from CSV file
def load_tags(file_path):
    tags = {}
    with open(file_path, 'r') as file:
        for line in file:
            parts = line.strip().split(',')
            if len(parts) >= 2:
                name, tag = parts[0], parts[1]
                if name not in tags:
                    tags[name] = []
                tags[name].append(tag)
    return tags

# Filter problems based on rating and exclude bit manipulation problems
def filter_problems(ratings, tags, min_rating, max_rating):
    filtered = [
        name for name, rating in ratings.items()
        if min_rating <= rating <= max_rating and 'Bit Manipulation' not in tags.get(name, [])
    ]
    return filtered

# Randomly select a specified number of problems
def select_random_problems(problems, num_problems):
    return random.sample(problems, num_problems)

# Main function
def main():
    ratings_file = 'ratings.txt'  # Path to your ratings.txt file
    tags_file = 'tags.csv'        # Path to your CSV file containing problem tags

    min_rating = int(input("Enter minimum rating: "))
    max_rating = int(input("Enter maximum rating: "))
    num_problems = int(input("Enter number of problems to select: "))

    ratings = load_ratings(ratings_file)
    tags = load_tags(tags_file)
    filtered_problems = filter_problems(ratings, tags, min_rating, max_rating)
    selected_problems = select_random_problems(filtered_problems, num_problems)

    print("\nSelected Problems:")
    for problem in selected_problems:
        print(f"- {problem} (Rating: {ratings[problem]})")

if __name__ == '__main__':
    main()
