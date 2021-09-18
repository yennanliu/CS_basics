import os
import glob
import psycopg2

from sql_queries import *
from helper import *

def process_song_data(cur, file_path):

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


def process_log_data(cur, file_path):

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

    pass

def main():

    pass