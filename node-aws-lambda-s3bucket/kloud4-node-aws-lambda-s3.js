const AWS = require('aws-sdk');
const fs = require('fs');

var s3Obj = new AWS.S3({
  endpoint: 's3.ap-south-1.amazonaws.com',
  accessKeyId: 'AKIA5FTZAU6ZVR3DATHY',
  secretAccessKey: '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'
});

exports.handler = async (event) => {
  var pathParam = event.path;
  if(pathParam != null && String(pathParam).includes('createstudentbucket')) {
    var queryParams =   event.queryStringParameters;
    var keyName = queryParams.keyId;
    var params = {
        Body: JSON.stringify(event.body),
        Bucket:'kloud4-bucket',
        Key:keyName,
        ContentType: 'text/pdf'
        };
        try {
          bucketresponse = await s3Obj.putObject(params).promise();
      } catch(err) {
          console.log("S3 bucket error"+err);
          return {
            statusCode: 500,
            body: 'S3 Bucket Error'+err,
          };
      }
      return {
          statusCode: 400,
          body: 'S3 update successs',
        };
  } else if(pathParam != null && String(pathParam).includes('fetchstudentbucket')) {
        console.log("---------Coming inside fetchstudentbucket---");
        var bucketData = "";
        try {
            var queryParams =   event.queryStringParameters;
            var bucketId = queryParams.bucketId;
            var params = {
                Bucket:'kloud4-bucket',
                Key:bucketId
            };
           bucketData = await s3Obj.getObject(params).promise();
        } catch (e) {
            console.log("---------->S3 Bucket erro boss !"+e);
            return {
                statusCode: 500,
                body: 'S3 Bucket Error!'+e,
              };
        }
      const respnse = bucketData.Body
      return {
        statusCode: 400,
        body: '"'+ respnse+'"'
      };
  } else if (pathParam != null && String(pathParam).includes('deletestudentbucket')) {
        try {
            var queryParams = event.queryStringParameters;
            var bucketId = queryParams.bucketId;
            var params = {
                Bucket:'kloud4-bucket',
                Key:bucketId
            };
            var deleteResult = await s3Obj.deleteObject(params).promise();
            return {
              statusCode: 400,
              body: 'Bucket has been deleted successfully',
            };
        } catch(err) {
          return {
            statusCode: 500,
            body: 'delete bucket error',
          };
        }
  } else if (pathParam != null && String(pathParam).includes('liststudentbucket')) {
    try {
        var params = {
            Bucket:'kloud4-bucket'};
        var objectList = await s3Obj.listObjects(params).promise();
        return {
          statusCode: 500,
          body: JSON.stringify(objectList.Contents)
        };
    } catch(err) {
        return {
          statusCode: 500,
          body: 'S3 Bucket Object List Error'+err,
        };
    }
  }
}