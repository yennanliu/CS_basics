#!/bin/bash

###############################################################################
# Script: get_must_problems.sh
# Description: Scan README.md for LeetCode problems marked with "MUST"
#              and output to CLI and save to file
# Usage: ./get_must_problems.sh
###############################################################################

# Color codes for better CLI output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Get script directory and project root
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"

# Define paths
README_FILE="$PROJECT_ROOT/README.md"
OUTPUT_FILE="$PROJECT_ROOT/data/must_problems.txt"

# Check if README.md exists
if [ ! -f "$README_FILE" ]; then
    echo -e "${RED}Error: README.md not found at $README_FILE${NC}"
    exit 1
fi

# Print header
echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  LeetCode Problems with MUST marker  ${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# Extract lines containing "MUST" and save to variable
MUST_LINES=$(grep -i "MUST" "$README_FILE")

# Count total problems found
TOTAL_COUNT=$(echo "$MUST_LINES" | wc -l)

# Print to CLI
echo -e "${YELLOW}Found $TOTAL_COUNT lines with MUST marker:${NC}"
echo ""
echo "$MUST_LINES"
echo ""

# Save to file
echo "# LeetCode Problems with MUST marker" > "$OUTPUT_FILE"
echo "# Generated on: $(date)" >> "$OUTPUT_FILE"
echo "# Total count: $TOTAL_COUNT" >> "$OUTPUT_FILE"
echo "" >> "$OUTPUT_FILE"
echo "$MUST_LINES" >> "$OUTPUT_FILE"

# Print success message
echo ""
echo -e "${BLUE}========================================${NC}"
echo -e "${GREEN}Results saved to: $OUTPUT_FILE${NC}"
echo -e "${GREEN}Total problems found: $TOTAL_COUNT${NC}"
echo -e "${BLUE}========================================${NC}"
