import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Base64;

import javax.xml.bind.DatatypeConverter;

public class Encoding {

    public static String hashSHA256(byte[] inputBytes){
    	String hashValue = "";
    	try {
    		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
    		messageDigest.update(inputBytes);
    		byte[] digestedBytes = messageDigest.digest();
    		hashValue = DatatypeConverter.printHexBinary(digestedBytes).toLowerCase();
    	}catch(Exception e){
    		
    	}
		return hashValue;
    	
    }
    
    public static String Base64encode(String input) {
    	String s = null;
    	try {
			s = Base64.getEncoder().encodeToString(input.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	return s;
    }
    
    public static String Base64decode(String input) {
    	byte[] s = null;
    	s = Base64.getDecoder().decode(input);
    	try {
			input = new String(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		
			e.printStackTrace();
		}
    	return input;
    }
	
}
