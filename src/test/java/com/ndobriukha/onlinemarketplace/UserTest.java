package com.ndobriukha.onlinemarketplace;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;

import org.apache.commons.dbutils.QueryRunner;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.ndobriukha.onlinemarketplace.dao.UserDao;
import com.ndobriukha.onlinemarketplace.domain.User;
import com.ndobriukha.onlinemarketplace.util.HibernateUtil;

@Transactional
public class UserTest {
	
	private UserDao userDao = new UserDao();
	private static QueryRunner runner;
	private static Transaction transaction;
	
	private final String INSERT_SQL = "INSERT INTO USERS (FULL_NAME, BILLING_ADDRESS, LOGIN, PASSWORD, EMAIL) VALUES(?, ?, ?, ?, ?)";
	
	@BeforeClass
	public static void setUp() {
		runner = new QueryRunner();		
		HibernateUtil.openSession();
		transaction = HibernateUtil.getSession().beginTransaction();
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		transaction.rollback();
		HibernateUtil.closeSession();
	}

	
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
	@Test
	public void testBatchInsert() throws SQLException, NamingException {
		String[][] params = { { "A", "A", "A", "A", "A" },
				{ "B", "B", "B", "B", "B" }, { "C", "C", "C", "C", "C" } };
		HibernateUtil.getSession().doWork(connection -> runner.batch(connection, INSERT_SQL, params));
		List<User> users = userDao.get();
		Assert.assertEquals(params.length, users.size());
		for (int i = 0; i < users.size(); i++) {
			User user = users.get(i);
			Assert.assertArrayEquals(params[i], user.getFieldsValues());
		}
		transaction.rollback();
	}

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
	@Test
	public void testCreate() throws NoSuchAlgorithmException, InvalidKeySpecException {
		User user = new User();
		user.setFullName("Full Name");
		user.setBillingAddress("Address");
		user.setLogin("test_login");
		user.setHashedPassword("password");
		user.setEmail("email@mail.com");
		Long id = userDao.create(user);
		Assert.assertNotNull("After persist object ID is null", id);
		Assert.assertEquals(id, user.getId());
		HibernateUtil.getSession().flush();
		List<User> users = userDao.get();
		Assert.assertTrue("Recieved users list doesn't contain created user", users.contains(user));
		transaction.rollback();
	}
	
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
		HibernateUtil.getSession().doWork(connection -> runner.update(connection, INSERT_SQL, "Full Name", "Address", login, "password", "email"));
		User user = new User();
		user.setFullName("Full Name");
		user.setBillingAddress("Address");
		user.setLogin("test_login");
		user.setHashedPassword("password");
		user.setEmail("email@mail.com");
		Long id = userDao.create(user);
		Assert.assertNotNull("After persist object ID is null", id);
		HibernateUtil.getSession().flush();
		transaction.rollback();
	}
}
