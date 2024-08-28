package com.bajajFinservHealth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


public class DestinationHashGenerator {

	
	 public static void main(String[] args) {
	        if (args.length != 2) {
	            System.out.println("Usage: java -jar DestinationHashGenerator.jar <PRN> <JSON File Path>");
	            return;
	        }

	        String prn = args[0].toLowerCase().trim();
	        String jsonFilePath = args[1];

	        try {
	            String destinationValue = findDestinationValue(jsonFilePath);
	            String randomString = generateRandomString(8);
	            String hash = generateMD5(prn + destinationValue + randomString);
	            System.out.println(hash + ";" + randomString);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    private static String findDestinationValue(String jsonFilePath) throws IOException {
	        ObjectMapper mapper = new ObjectMapper();
	        JsonNode root = mapper.readTree(new File(jsonFilePath));
	        return findDestinationValueRecursively(root);
	    }

	    private static String findDestinationValueRecursively(JsonNode node) {
	        if (node.isObject()) {
	            for (JsonNode child : node) {
	                String result = findDestinationValueRecursively(child);
	                if (result != null) {
	                    return result;
	                }
	            }
	        } else if (node.isArray()) {
	            for (JsonNode child : node) {
	                String result = findDestinationValueRecursively(child);
	                if (result != null) {
	                    return result;
	                }
	            }
	        } else if (node.isTextual()) {
	            return node.asText();
	        }
	        return null;
	    }

}
}
