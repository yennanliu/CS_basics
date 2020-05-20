### Quick start
```bash

$ git clone https://github.com/yennanliu/CS_basics.git

```

### Python 2 -> Python 3 
```bash
# via 2to3 tool
# https://docs.python.org/2/library/2to3.html

# exmple : transform single python script 
# -w : write the change to file directly 
# -n : not output .py.bak file
$ 2to3 -w -n algorithm/python/bfs.py

# example : transform all script under file
$ 2to3 -w -n algorithm/python/*.py
```