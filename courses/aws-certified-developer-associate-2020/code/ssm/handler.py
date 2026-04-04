import json
import boto3
import os

ssm = boto3.client('ssm', region_name="eu-west-3")
dev_or_prod = os.environ['DEV_OR_PROD']

def lambda_handler(event, context):
    db_url = ssm.get_parameters(Names=["/my-app/" + dev_or_prod + "/db-url"])
    print(db_url)   
    db_password = ssm.get_parameters(Names=["/my-app/" + dev_or_prod + "/db-password"], WithDecryption=True)
    print(db_password)
    return "worked!"
