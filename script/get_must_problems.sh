#!/bin/bash
# Save README's MUST-marked problem rows to data/must_problems.txt.
#
# A wrapper kept for the commands the skills document; the rows are chosen by
# script/readme_markers.py, which reads the main problem tables only (it used to
# be `grep -i MUST README.md`, which also caught prose and imported rows).
#
# Usage: bash script/get_must_problems.sh [--stdout]
exec python3 "$(dirname "${BASH_SOURCE[0]}")/readme_markers.py" must "$@"
