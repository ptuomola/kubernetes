package org.tuomola.pingpong;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.IOException;


@RestController
public class HelloController {
        @Autowired
        CounterService cs;

	@RequestMapping("/pingpong")
	public String index() throws IOException {
                int counter = cs.getCounter();
                String responseStr = "pong " + counter; 
                counter++;
                cs.setCounter(counter);
                return responseStr;
        }

        @RequestMapping("/pongs")
        public String pongs() {
                return "Ping / Pongs: " + cs.getCounter();
        }
}
