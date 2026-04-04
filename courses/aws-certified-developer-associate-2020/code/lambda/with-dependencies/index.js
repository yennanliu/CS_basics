// Require the X-Ray SDK (need to install it first)
const AWSXRay = require('aws-xray-sdk-core')

// Require the AWS SDK (comes with every Lambda function)
const AWS = AWSXRay.captureAWS(require('aws-sdk'))

// We'll use the S3 service, so we need a proper IAM role
const s3 = new AWS.S3()

exports.handler = async function(event) {
  return s3.listBuckets().promise()
}