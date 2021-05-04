package org.tuomola.mainreader;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

	@RequestMapping("/")
	public String index() throws NoSuchAlgorithmException, URISyntaxException, IOException, InterruptedException {
		HttpRequest request = HttpRequest.newBuilder()
			.uri(new URI("http://pingpong-svc:2347/pongs"))
			.GET()
			.build();

		HttpResponse<String> response = HttpClient
			.newBuilder()
			.build()
			.send(request, HttpResponse.BodyHandlers.ofString());

		String pingpongs = response.body();	
		String timestamp = ZonedDateTime.now( ZoneOffset.UTC ).format( DateTimeFormatter.ISO_INSTANT );
		String hash = getHash(pingpongs.getBytes());

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