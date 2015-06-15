package com.ndobriukha.onlinemarketplace;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ndobriukha.onlinemarketplace.dao.UserDao;
import com.ndobriukha.onlinemarketplace.domain.User;

public class UserTest {
	
	UserDao userDao = new UserDao();
	private QueryRunner runner;
	
	private final String INSERT_SQL = "INSERT INTO USERS (FULL_NAME, BILLING_ADDRESS, LOGIN, PASSWORD, EMAIL) VALUES(?, ?, ?, ?, ?)";
	
	@Before
	public void setUp() {
		runner = new QueryRunner();
	}
	
	@After
	public void tearDown() throws SQLException {
		userDao.rollback();
	}

	/*@Test
	public void testTest() {
		
		CommonDao<User> userDao = new CommonDao<User>();
		
		User user = new User();
		user.setFullName("TEST");
		user.setBillingAddress("ADDRESS");
		user.setLogin("TESTLOGIN");
		user.setPassword("PASSWORD");
		user.setEmail("EMAIL@MAIL.COM");
		
		userDao.save(user);
		
	}*/
	
	/**
	 * 1. Получение всех пользователей.
	 * 
	 * Step to reproduce: С помощью sql-скриптов создать в базе несколько
	 * пользователей. Вызвать метод для получения всех существующих
	 * пользователей.
	 * 
	 * Expected result: Тест считается успешным, если пользователи, добавленные
	 * в базу с помощью sql-скриптов, совпадают с пользователями, которые вернет
	 * вызванный метод.
	 * 
	 * @throws PersistException
	 * @throws NamingException
	 */
	/*@Test
	public void testBatchInsert() throws SQLException, NamingException {
		String[][] params = { { "A", "A", "A", "A", "A" },
				{ "B", "B", "B", "B", "B" }, { "C", "C", "C", "C", "C" } };
		userDao.getSession().doWork(connection -> runner.batch(connection, INSERT_SQL, params));
		List<User> users = userDao.getAll();
		Assert.assertEquals(params.length, users.size());
		for (int i = 0; i < users.size(); i++) {
			User user = users.get(i);
			Assert.assertArrayEquals(params[i], user.getFieldsValues());
		}
	}*/

	/**
	 * 2. Создание нового пользователя.
	 * 
	 * Step to reproduce: Вызвать метод для создания пользователя с определённым
	 * логином и паролем. Вызвать метод для получения всех существующих
	 * пользователей.
	 * 
	 * Expected result: Тест считается успешным, если в списке присутствует
	 * новый пользователь.
	 * 
	 * @throws PersistException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 */
	/*@Test
	public void testCreate() throws NoSuchAlgorithmException, InvalidKeySpecException {
		User user = new User();
		user.setFullName("Full Name");
		user.setBillingAddress("Address");
		user.setLogin("test_login");
		user.setHashedPassword("password");
		user.setEmail("email@mail.com");
		userDao.save(user);
		Assert.assertNotNull("After persist object ID is null", user.getId());
		List<User> users = userDao.getAll();
		Assert.assertEquals("More than one created User.", 1, users.size());
		Assert.assertEquals(user, users.get(0));
	}*/
	
	/**
	 * 2.1 Создание нового пользователя.
	 * 
	 * Step to reproduce: С помощью sql-скрипта создать в базе пользователя с
	 * логином «login». Вызвать метод для создания пользователя с логином
	 * «login».
	 * 
	 * Expected result: Тест считается успешным, если возникает соответствующее
	 * исключение.
	 * 
	 * @throws SQLException
	 * @throws PersistException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 */
	@Test(expected = ConstraintViolationException.class)
	public void testDuplicateexception() throws NoSuchAlgorithmException, InvalidKeySpecException {
		String login = "test_login";
		userDao.getSession().doWork(connection -> runner.update(connection, INSERT_SQL, "Full Name", "Address", login, "password", "email"));
		User user = new User();
		user.setFullName("Full Name");
		user.setBillingAddress("Address");
		user.setLogin("test_login");
		user.setHashedPassword("password");
		user.setEmail("email@mail.com");
		userDao.save(user);
		Assert.assertNotNull("Persist object is null", user);
		Assert.assertNotNull("After persist object ID is null", user.getId());
	}
}
