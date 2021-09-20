import os
import glob
import psycopg2

from etl.sql_queries import *
from etl.helper import *


def process_song_file(cur, file_path):

    #song_data = []

    # with open(json_file, "r") as f:
    #     for line in f.readlines():
    #         data = json.loads(line)
    #         song_data.append(data)

    song_data = load_data(file_path)

    for data in song_data:

        num_songs = data['num_songs']
        artist_id = data['artist_id']
        artist_latitude = data['artist_latitude']
        artist_longitude  = data['artist_longitude']
        artist_location = data['artist_location']
        artist_name = data['artist_name']
        song_id = data['song_id']
        title = data['title']
        duration = data['duration']
        year = data['year']

        #-------------------------
        # insert song data
        #-------------------------
        artist_data = (artist_id, artist_name, artist_location, artist_latitude, artist_longitude)
        cur.execute(song_table_insert, song_data)

    print ("song data inserted OK!")


def process_log_file(cur, file_path):

    log_data = load_data(file_path)

    column_labels = ["timestamp", "hour", "day", "weelofyear", "month", "year", "weekday"]

    for data in load_data:

        # TODO : implement this logic
        if data['page'] == 'NextSong':
            print ("data['page'] == 'NextSong'")

    #-------------------------
    # insert time data
    #-------------------------
    time_data = []
    t = [ x['ts'] for x in data ]
    for data in t:
        time_data.append([data ,data.hour, data.day, data.weekofyear, data.month, data.year, data.day_name()])

    for line in time_data:
        cur.execute(time_table_insert, list(line))

    #-------------------------
    # insert user data
    #-------------------------
    for line in log_data:
        cur.execute(
            user_table_insert, 
            (line['userId'], line['firstName'], line['lastName'], line['gender'], line['level'])
            )

    #-------------------------
    # insert songplay data
    #-------------------------
    for line in log_data:

        # get artist, song, length...
        cur.execute(song_select, (line.song, line.artist, line.length))
        results = cur.fetchone()

        if results:
            songid, artistid = results
        else :
            songid, artistid = None, None

        songplay_data = ( line.ts, line.userId, line.level, songid, artistid, line.sessionId, line.location, line.userAgent)

        cur.execute(
            song_select,
            songplay_data
            )


def process_data(cur, conn, filepath, func):

    all_files = []

    # get all files matching extension from directory
    for root, dirs, files in os.walk(filepath):
        files = glob.glob(os.path.join(root, '*.json'))

        for f in files:
            all_files.append(os.abspath(f))

    # get total number of files
    num_files = len(all_files)
    print("{num_files} files found in {filepath}".format(num_files=num_files, filepath=filepath))

    # implement func on the files via iteration
    for i, datafile in enumerate(all_files, 1): # idx starts from 1
        func(cur, datafile)
        conn.commit()
        print ("{}/{} files processed".format(i, num_files))


def main():

    host = '127.0.0.1'
    dbname = 'test_db'
    user = 'postgres'
    password = 'postgres'
    creds = "host={host} dbname={dbname} user={user} password={password}"

    creds = creds.format(host=host, dbname=dbname, user=user, password=password)
    print (creds)

    conn = psycopg2.connect(creds)
    cur = conn.cursor()

    # process song_data, log_data
    process_data(cur, conn, filepath='data/song_data', func=process_song_file)
    process_data(cur, conn, filepath='data/log_data', func=process_log_file)

    conn.close()

if __name__ == '__main__':
    main()
    print ("main process finished!")