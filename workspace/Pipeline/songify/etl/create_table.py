import psycopg2
from etl.sql_queries import create_table_queries, drop_table_queries

def create_db():
    """
    drop songfify DB if exists, create songfify DB again,
    return cur, conn
    """

    host = '127.0.0.1'
    dbname = 'test_db'
    user = 'postgres'
    password = 'postgres'
    creds = "host={host} dbname={dbname} user={user} password={password}"

    creds = creds.format(host=host, dbname=dbname, user=user, password=password)
    print (creds)
    conn = psycopg2.connect(creds)
    conn.set_session(autocommit=True)
    cur = conn.cursor()

    # create songfify database with UTF8 encoding
    cur.execute("DROP DATABASE IF EXISTS songfify")
    cur.execute("CREATE DATABASE songfify WITH ENCODING 'utf8' TEMPLATE template0")

    conn.close()

    # connect to songfify DB
    creds2 = creds.format(host, "songfify", user, password)
    conn = psycopg2.connect(creds2)
    cur = conn.cursor()

    return cur, conn


def drop_tables(cur, conn):

    for query in drop_table_queries:
        cur.execute(query)
        conn.commit()

def create_table(cur, conn):
    for query in create_table_queries:
        cur.execute(query)
        conn.commit()

def main():

    cur, conn = create_db()

    drop_tables(cur, conn)
    print ("drop table OK!")

    create_table(cur, conn)
    print ("create table OK!")

    conn.close()

# if __name__ == '__main__':
#     main()