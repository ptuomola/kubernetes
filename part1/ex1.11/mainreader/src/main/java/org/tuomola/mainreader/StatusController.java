package org.tuomola.mainreader;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

	private static String FILE_PATH = "/usr/src/app/files/pingpong.txt";

	@RequestMapping("/")
	public String index() throws FileNotFoundException, NoSuchAlgorithmException {
		  Scanner myReader = new Scanner(new File(FILE_PATH));
		  String pingpongs = myReader.nextLine();
			String timestamp = ZonedDateTime.now( ZoneOffset.UTC ).format( DateTimeFormatter.ISO_INSTANT );
		  String hash = getHash(pingpongs.getBytes());
		  myReader.close();
		  return timestamp + " hash: " + hash + "\n" + pingpongs;
	}
	
	private String getHash(byte[] val) throws NoSuchAlgorithmException
	{
	    MessageDigest md = MessageDigest.getInstance("MD5");
	    byte[] digest = md.digest(val);
	    BigInteger bigInt = new BigInteger(1,digest);
	    return bigInt.toString(16);
	}
}