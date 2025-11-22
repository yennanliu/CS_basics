#!/bin/bash

###############################################################################
# Script: get_again_problems.sh
# Description: Scan README.md for LeetCode problems marked with "AGAIN"
#              and output to CLI and save to file
# Usage: ./get_again_problems.sh
###############################################################################

# Color codes for better CLI output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Get script directory and project root
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"

# Define paths
README_FILE="$PROJECT_ROOT/README.md"
OUTPUT_FILE="$PROJECT_ROOT/data/again_problems.txt"

# Check if README.md exists
if [ ! -f "$README_FILE" ]; then
    echo -e "${RED}Error: README.md not found at $README_FILE${NC}"
    exit 1
fi

# Print header
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}  LeetCode Problems with AGAIN marker  ${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""

# Extract lines containing "AGAIN" and save to variable
AGAIN_LINES=$(grep -i "AGAIN" "$README_FILE")

# Count total problems found
TOTAL_COUNT=$(echo "$AGAIN_LINES" | wc -l)

# Print to CLI
echo -e "${YELLOW}Found $TOTAL_COUNT lines with AGAIN marker:${NC}"
echo ""
echo "$AGAIN_LINES"
echo ""

# Save to file
echo "# LeetCode Problems with AGAIN marker" > "$OUTPUT_FILE"
echo "# Generated on: $(date)" >> "$OUTPUT_FILE"
echo "# Total count: $TOTAL_COUNT" >> "$OUTPUT_FILE"
echo "" >> "$OUTPUT_FILE"
echo "$AGAIN_LINES" >> "$OUTPUT_FILE"

# Print success message
echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Results saved to: $OUTPUT_FILE${NC}"
echo -e "${GREEN}Total problems found: $TOTAL_COUNT${NC}"
echo -e "${GREEN}========================================${NC}"
