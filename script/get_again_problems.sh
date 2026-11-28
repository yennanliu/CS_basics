#!/bin/bash
# Save README's AGAIN-marked problem rows to data/again_problems.txt.
#
# A wrapper kept for the commands the skills document; the rows are chosen by
# script/readme_markers.py, which reads the main problem tables only (it used to
# be `grep -i AGAIN README.md`, which also caught prose and imported rows).
#
# Usage: bash script/get_again_problems.sh [--stdout]
exec python3 "$(dirname "${BASH_SOURCE[0]}")/readme_markers.py" again "$@"
