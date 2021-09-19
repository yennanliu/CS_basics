
from etl.create_table import main as create_table_main
from etl.etl import main as etl_main

if __name__ == "__main__":
    create_table_main.main()
    etl_main.main()