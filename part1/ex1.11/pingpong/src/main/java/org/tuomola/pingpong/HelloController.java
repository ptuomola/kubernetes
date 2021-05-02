package org.tuomola.pingpong;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestMapping;
import java.io.FileWriter;
import java.io.IOException;

@RestController
public class HelloController {

        private static int counter = 0;
        private static String FILE_PATH = "/usr/src/app/files/pingpong.txt";

	@RequestMapping("/")
	public String index() throws IOException {
                String responseStr = "pong " + counter; 
                counter++;

                FileWriter outFile = new FileWriter(FILE_PATH);
                System.out.println("writing to file " + FILE_PATH + " value " + counter);
                outFile.write("Ping / Pongs: " + counter);
                outFile.close();
        
                return responseStr;
        }
}