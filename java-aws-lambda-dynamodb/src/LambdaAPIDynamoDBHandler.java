package com.lambda.jav.lambda;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class LambdaAPIDynamoDBHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
	@Override
	public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {

		LambdaLogger logger = context.getLogger();
		Map<String, String> inputParams = input.getPathParameters();
		logger.log("------Input Params ::==========>>>"+inputParams);
        if ((input != null) && inputParams != null) {
        	JsonParser parser = new JsonParser();
    		JsonObject responseJson = new JsonObject();
    		JsonObject event = (JsonObject) parser.parse(input.getBody());
        	
            if (inputParams.get("createcustomer") != null && inputParams.get("createcustomer").contains("createcustomer")) {
            	String pathParam = inputParams.get("createcustomer").toString();
	        	CustomerData customerObj = new CustomerData(event.toString());
	    		com.amazonaws.services.dynamodbv2.document.DynamoDB dynamoDB = new com.amazonaws.services.dynamodbv2.document.DynamoDB(getCustomerTable());
	    		Table table = dynamoDB.getTable("CustomerDetails");
	    		Item item = new Item()
	    			    .withPrimaryKey("CustomerId", customerObj.getCustomerId())
	    			    .withString("CustomerCompanyName", customerObj.getCustomerCompany())
	    			    .withString("CustomerAddress", customerObj.getCustomerAdderss())
	    			    .withString("CustomerName", customerObj.getCustomerName());
	    		PutItemOutcome outcome = table.putItem(item);
	    		return sendResponse("Student record created successfully in AWS Dynamodb using Lambda");
            } else if(inputParams.get("fetchcustomer") != null && inputParams.toString().contains("fetchcustomer")) {
            	logger.log("-------called fetchcustomer-----");
            	return sendResponse(handleGetByParam(input, context));
            } else if(inputParams.get("deletecustomer") != null && inputParams.get("deletecustomer").toString().contains("deletecustomer")) {
            	logger.log("-------called deletecustomer-----");
            	return sendResponse(handleDeleteById(input, context));
            }
        }
        
        return sendResponse("Error");
        
	}
	
	private APIGatewayProxyResponseEvent sendResponse(String resString) {
		APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();
        responseEvent.setBody(resString);
        responseEvent.setStatusCode(200);
        return responseEvent;
	}
	
	private AmazonDynamoDB getCustomerTable() {
		BasicAWSCredentials awacred = new BasicAWSCredentials("AKIA5FTZAU6ZUR3ZFHUW","pU3Xc8hxA7pdnjjelsX0+rFYicQIUPPkAl8VdWMw");
		AWSCredentialsProvider awacredprovider = new AWSStaticCredentialsProvider(awacred);
		AmazonDynamoDB dynamoDBclient = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.AP_SOUTH_1).withCredentials(awacredprovider).build();
	    return dynamoDBclient;
	}
	
	public String handleGetByParam(APIGatewayProxyRequestEvent input, Context context) {
	    JsonParser parser = new JsonParser();
	    JsonObject responseJson = new JsonObject();

	    try {
	        JsonObject event = (JsonObject) parser.parse(input.getBody());
	        JsonObject responseBody = new JsonObject();
	        
	        Map<String, String> inputParams = input.getQueryStringParameters();
            GetItemRequest getitemreq = new GetItemRequest();
        	AttributeValue attributeVal = new AttributeValue();
        	attributeVal.setS(inputParams.get("studentId").toString());
        	Map<String,AttributeValue> attributeMap = new HashMap<String,AttributeValue>();
        	attributeMap.put("CustomerId", attributeVal);
        	getitemreq.withKey(attributeMap).withTableName("CustomerDetails");
        	GetItemResult customerDetailsResult = getCustomerTable().getItem(getitemreq);
        	if(customerDetailsResult == null) {
        		return "No Student records found with this id";
        	}
            return customerDetailsResult.toString();

	    } catch (Exception pex) {
	        sendResponse(pex.toString());
	    }
	    
	    return "No Record found";
	}
	
	public String handleDeleteById(APIGatewayProxyRequestEvent input, Context context) {
	    try {
	        if (input.getQueryStringParameters() != null) {
	        	Map<String, String> inputParams = input.getQueryStringParameters();
	            if (inputParams.get("studentId") != null) {
	            	String id = inputParams.get("studentId").toString();
	                DeleteItemRequest deleteItemReq = new DeleteItemRequest();
		        	AttributeValue attributeVal = new AttributeValue();
		        	attributeVal.setS(id);
		        	Map<String,AttributeValue> attributeMap = new HashMap<String,AttributeValue>();
		        	attributeMap.put("CustomerId", attributeVal);
		        	deleteItemReq.withKey(attributeMap).withTableName("CustomerDetails");
		        	DeleteItemResult customerDetailsResult = getCustomerTable().deleteItem(deleteItemReq);
		        	return "Student record has been deleted successfully";
	            }
	        } 
	    } catch (Exception pex) {
	        return "Student record deletion error";
	    }
	    return "Not able to delete student record";
	}
}
