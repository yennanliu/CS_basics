
import glob, json

def list_json_files(json_path):
    result = []
    json_files = glob.glob(json_path)
    for json_file in json_files:
        result.append(json_file)
    return result

def load_data(json_file):
    result = []
    with open(json_file, "r") as f:
        for line in f.readlines():
            data = json.loads(line)
            result.append(data)
    return result

def load_all_data(json_path):
    result = []
    json_files = list_json_files(json_path)
    for json_file in json_files:
        print ("*** json_file = " + str(json_file))
        tmp = load_data(json_file)
        result.append(tmp)
    resultx = []
    # TODO : flatten array via better way
    for array in result:
        for item in array:
            resultx.append(item)
    return resultx

def save_data(input_data, dest_file):

    with open(dest_file, "w", newline='\n') as f:
        for idx in range(100):
            f.write(json.dumps(input_data[idx]) + '\n')

if __name__ == '__main__':
    json_path = "data/log_data/2018/11/*"
    dest_file = "output/output.json"
    json_file = "data/log_data/2018/11/2018-11-09-events.json"
    merged_data = load_all_data(json_path)
    save_data(merged_data, dest_file)