package org.tuomola.pingpong;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {

        private static int counter = 0;

	@RequestMapping("/")
	public String index() {
                String responseStr = "pong " + counter; 
                counter++;
                return responseStr;
        }
}
