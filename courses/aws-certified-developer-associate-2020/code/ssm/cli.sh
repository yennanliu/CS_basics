# GET PARAMETERS
aws ssm get-parameters --names /my-app/dev/db-url /my-app/dev/db-password
# GET PARAMETERS WITH DECRYPTION
aws ssm get-parameters --names /my-app/dev/db-url /my-app/dev/db-password --with-decryption

# GET PARAMETERS BY PATH
aws ssm get-parameters-by-path --path /my-app/dev/
# GET PARAMETERS BY PATH RECURSIVE
aws ssm get-parameters-by-path --path /my-app/ --recursive
# GET PARAMETERS BY PATH WITH DECRYPTION
aws ssm get-parameters-by-path --path /my-app/ --recursive --with-decryption