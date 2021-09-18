
import glob

def list_json_files(json_path):
    result = []
    json_files = glob.glob(json_path)
    for json_file in json_files:
        result.append(json_file)
    return result

def load_data(json_file):
    counter = 0
    with open(json_file, 'r') as f:
        for line in f.readlines():
            if counter > 3:
                break
            print (str(counter))
            print (line)
            counter += 1

def load_all_data(json_path):
    json_files = list_json_files(json_path)
    for json_file in json_files:
        print ("*** json_file = " + str(json_file))
        load_data(json_file)


if __name__ == '__main__':
    json_path = "data/log_data/2018/11/*"
    load_all_data(json_path)