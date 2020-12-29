package org.tuomola.mainapplication;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {
	
	@Autowired
	private ScheduledBean scheduledBean;

	@RequestMapping("/")
	public String index() {
		return scheduledBean.getCurrentString();
	}

}