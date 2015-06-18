package com.ndobriukha.onlinemarketplace.domain;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ndobriukha.onlinemarketplace.util.PasswordHash;

@Entity
@Table(name="USERS")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Sequence")
	@SequenceGenerator(name = "id_Sequence", sequenceName = "USERS_PK_SEQ", allocationSize = 1)
	private Long id;
	
	@Column(name="FULL_NAME")
	@NotNull
	private String fullName;
	
	@Column(name="BILLING_ADDRESS")
	private String billingAddress;
	
	@Column(name="LOGIN")
	@NotNull
	@Size(min=6,max=30)
	private String login;
	
	@Size(min=6)
	@Column(name="PASSWORD")
	private String password;
	
	@Column(name="EMAIL")
	@Email
	private String email;
	
	public User() {		
	}

	public User(String fullName, String billingAddress, String login,
			String password, String email) throws NoSuchAlgorithmException, InvalidKeySpecException {
		super();
		this.fullName = fullName;
		this.billingAddress = billingAddress;
		this.login = login;
		this.password = PasswordHash.createHash(password);
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@JsonIgnore
	public String getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}

	@JsonIgnore
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setHashedPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		this.password = PasswordHash.createHash(password);
	}
	
}