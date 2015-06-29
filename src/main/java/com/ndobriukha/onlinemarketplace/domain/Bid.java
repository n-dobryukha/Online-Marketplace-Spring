package com.ndobriukha.onlinemarketplace.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;


@Entity
@Table(name="BIDS")
public class Bid {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Sequence")
	@SequenceGenerator(name = "id_Sequence", sequenceName = "BIDS_PK_SEQ", allocationSize = 1)
	private Long id;
	
	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name="ITEM_ID")	
	private Item item;
	
	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name="BIDDER_ID")	
	private User bidder;
	
	@Column(name="AMOUNT")
	private double amount;
	
	@Column(name="TS")
	@Type(type = "timestamp")
	private Date timestamp;
	
	public Bid() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public User getBidder() {
		return bidder;
	}

	public void setBidder(User bidder) {
		this.bidder = bidder;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
}
