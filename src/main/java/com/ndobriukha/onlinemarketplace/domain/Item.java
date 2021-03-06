package com.ndobriukha.onlinemarketplace.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

@Entity
@Table(name="ITEMS")
public class Item {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Sequence")
	@SequenceGenerator(name = "id_Sequence", sequenceName = "ITEMS_PK_SEQ", allocationSize = 1)
	private Long id;
	
	@ManyToOne(/*cascade = CascadeType.REMOVE*/)
	@JoinColumn(name="SELLER_ID")
	@NotNull
	private User seller;
	
	@Column(name="TITLE")
	@NotNull
	private String title;
	
	@Column(name="DESCRIPTION")
	@NotNull
	private String description;
	
	@Column(name="START_PRICE")
	@NotNull
	@DecimalMin("0.01")
	private double startPrice;
	
	@Column(name="TIME_LEFT")
	@NotNull
	@Min(value=1)
	@Max(value=1000)
	private int timeLeft;
	
	@Column(name="START_BIDDING")
	@Type(type = "timestamp")
	private Date startBidding;
	
	@Column(name="BUY_IT_NOW")
	@Type(type="yes_no")
	private boolean buyItNow;
	
	@Column(name="BID_INCREMENT")
	private double bidIncrement;
	
	@Column(name="SOLD")
	@Type(type="yes_no")
	private boolean sold;
	
	public Item() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getSeller() {
		return seller;
	}

	public void setSeller(User seller) {
		this.seller = seller;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getStartPrice() {
		return startPrice;
	}

	public void setStartPrice(double startPrice) {
		this.startPrice = startPrice;
	}

	public int getTimeLeft() {
		return timeLeft;
	}

	public void setTimeLeft(int timeLeft) {
		this.timeLeft = timeLeft;
	}

	public Date getStartBidding() {
		return startBidding;
	}

	public void setStartBidding(Date startBidding) {
		this.startBidding = startBidding;
	}

	public boolean isBuyItNow() {
		return buyItNow;
	}

	public void setBuyItNow(boolean buyItNow) {
		this.buyItNow = buyItNow;
	}

	public double getBidIncrement() {
		return bidIncrement;
	}

	public void setBidIncrement(double bidIncrement) {
		this.bidIncrement = bidIncrement;
	}

	public boolean isSold() {
		return sold;
	}

	public void setSold(boolean sold) {
		this.sold = sold;
	}

	
}
