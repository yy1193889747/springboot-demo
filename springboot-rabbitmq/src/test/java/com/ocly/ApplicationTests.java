package com.ocly;

import com.ocly.modle.User;
import com.ocly.rabbit.ObjSender;
import com.ocly.rabbit.Sender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	private Sender sender;
	@Autowired
	private ObjSender objSender;

	@Test
	public void hello() throws Exception {
		for (int i = 0; i < 10 ; i++) {
			sender.send();
		}
	}

	@Test
	public void user() throws Exception {
		User user = new User();
		user.setAge(18);
		user.setName("ocly");
		objSender.send(user);
	}

	@Test
	public void topic() throws Exception {
		sender.topicSendone();
	}
}
