package com.vtube.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vtube.dal.UsersRepository;
import com.vtube.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class UsersRepositoryTests {
	@Autowired
	private UsersRepository usersRepository;
	
	@Test
	public void findByIdTest() {
		User user = new User();
		
		user.setAge(18);
		user.setEmail("testmail@test.tests");
		user.setFirstName("Tester");
		user.setLastName("Testerov");
		user.setNickName("Testcho");
		user.setPassword("testPassword");
		
		user = this.usersRepository.save(user);
		
		User user2 = this.usersRepository.findById(user.getId()).get();
		
		assertEquals(user.getEmail(), user2.getEmail());
	}
}
