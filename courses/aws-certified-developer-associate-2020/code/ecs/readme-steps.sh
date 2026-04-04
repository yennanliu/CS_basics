# build a docker image
docker build -t demo .

# login to ECR (change for your region)
$(aws ecr get-login --no-include-email --region eu-west-1)

# tag image (change aws account number)
docker tag demo:latest 1234567890.dkr.ecr.eu-west-1.amazonaws.com/demo:latest

# push image
docker push 1234567890.dkr.ecr.eu-west-1.amazonaws.com/demo:latest

# pull image
docker pull 1234567890.dkr.ecr.eu-west-1.amazonaws.com/demo:latest