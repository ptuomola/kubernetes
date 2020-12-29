package org.tuomola.mainreader;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

	private static String FILE_PATH = "/tmp/timestamp";

	@RequestMapping("/")
	public String index() throws FileNotFoundException, NoSuchAlgorithmException {
		  Scanner myReader = new Scanner(new File(FILE_PATH));
		  String timestamp = myReader.nextLine();
		  String hash = getHash(timestamp.getBytes());
		  myReader.close();
		  return timestamp + " hash: " + hash;
	}
	
	private String getHash(byte[] val) throws NoSuchAlgorithmException
	{
	    MessageDigest md = MessageDigest.getInstance("MD5");
	    byte[] digest = md.digest(val);
	    BigInteger bigInt = new BigInteger(1,digest);
	    return bigInt.toString(16);
	}

}