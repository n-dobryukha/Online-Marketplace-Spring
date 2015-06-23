package com.ndobriukha.onlinemarketplace.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ndobriukha.onlinemarketplace.dao.ItemDao;
import com.ndobriukha.onlinemarketplace.domain.Bid;
import com.ndobriukha.onlinemarketplace.domain.Item;
import com.ndobriukha.onlinemarketplace.domain.User;

@RestController
@RequestMapping("/rest/items")
public class RestItemController {

	@Autowired
	private ItemDao<Item, Long> itemDao;
	
	@RequestMapping(value = "/all", method = RequestMethod.GET)
    public Map<String, List<Map<String, Object>>> getAllItems(Authentication auth) {
		final List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		List<Item> items = itemDao.get();		
		for (Item item: items) {
			if (item.isSold()) { // || (item.getSellerId().equals(user.getId()))) {
				continue;
			}
			Map<String, Object> dataItem = buildItemData(item);
			if ((auth != null) && (auth.isAuthenticated())) {
				User user = (User) auth.getPrincipal();
				if (item.getSeller().equals(user)) {
					dataItem.put("action", "edit");
				}				
			} else {
				dataItem.put("action", "");
			}
			data.add(dataItem);
		}
		Map<String, List<Map<String, Object>>> results = new HashMap<String, List<Map<String, Object>>>();
		results.put("data", data);
        return results;
    }
	
	@RequestMapping(value = "/my", method = RequestMethod.GET)
    public Map<String, List<Map<String, Object>>> getMyItems(Authentication auth) {
		User user = null;
		if ((auth != null) && (auth.isAuthenticated())) {
			user = (User) auth.getPrincipal();
		}
		final List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		List<Item> items = itemDao.get();		
		for (Item item: items) {
			List<Bid> bids = itemDao.getAllBidsByItem(item);
			boolean isInclude = false;
			for (Bid bid: bids) {
				if (bid.getBidder().equals(user)) {
					isInclude = true;
					break;
				}
			}
			if ((!isInclude) && (!item.getSeller().equals(user))) {
				continue;
			}
			Map<String, Object> dataItem = buildItemData(item);
			if (item.isSold()) {
				dataItem.put("action", "sold");
			} else {
				Calendar cal = Calendar.getInstance();
				cal.setTime(item.getStartBidding());
				cal.add(Calendar.HOUR, item.getTimeLeft());
				if (cal.before(Calendar.getInstance())) {
					dataItem.put("action", "time is up");
				} else {
					if (!isInclude) {
						dataItem.put("action", "edit");
					}
				}
			}
			data.add(dataItem);
		}
		Map<String, List<Map<String, Object>>> results = new HashMap<String, List<Map<String, Object>>>();
		results.put("data", data);
        return results;
    }
	
	@SuppressWarnings("serial")
	private Map<String, Object> getEmptyItemData() {
		Map<String, Object> result = new HashMap<String, Object>();		
		result.put("uid", "");
		result.put("title", "");
		result.put("description", "");
		result.put("seller", new HashMap<String, Object>() {{ put("id",""); put("name", ""); }});
		result.put("startPrice", "");
		result.put("bidInc", "");
		result.put("bestOffer", "");
		result.put("bidder", new HashMap<String, Object>() {{ put("id",""); put("name", ""); }});
		result.put("stopDate", new HashMap<String, Object>() {{ put("display",""); put("timestamp", ""); }});
		result.put("action", "");
		return result;
	}
	
	private Map<String, Object> buildItemData(Item item) {
		Map<String, Object> data = getEmptyItemData();
		NumberFormat numberFormatter = new DecimalFormat("#0.00");

		Map<String, Object> sellerData = new HashMap<String, Object>();
		User seller = item.getSeller();
		sellerData.put("id", seller.getId());
		sellerData.put("name", seller.getFullName());

		Bid bid = itemDao.getBestBidByItem(item);
		if (bid != null) {
			User bidder = bid.getBidder();
			if (bidder != null) {
				Map<String, Object> bidderData = new HashMap<String, Object>();
				bidderData.put("id", bidder.getId());
				bidderData.put("name", bidder.getFullName());
				data.put("bidder", bidderData);
			}
		}
		 
		Map<String, Object> dateData = new HashMap<String, Object>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(item.getStartBidding());
		cal.add(Calendar.HOUR, item.getTimeLeft());

		dateData.put("display", (new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss")
				.format(cal.getTime())));
		dateData.put("timestamp", cal.getTime());

		data.put("uid", item.getId());
		data.put("title", item.getTitle());
		data.put("description", item.getDescription());
		data.put("seller", sellerData);
		data.put("startPrice", numberFormatter.format(item.getStartPrice()));
		data.put(
				"bidInc",
				(!item.isBuyItNow()) ? numberFormatter.format(item
						.getBidIncrement()) : "");
		data.put("bestOffer",
				(bid != null) ? numberFormatter.format(bid.getAmount()) : "");
		data.put("stopDate", dateData);
		if (item.isSold() || (cal.before(Calendar.getInstance()))) {
			data.put("action", "");
		} else {
			data.put("action", (item.isBuyItNow()) ? "buy" : "bid");
		}

		return data;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Item getItem(@PathVariable Long id) {
        return itemDao.get(id);
    }
	
	private Map<String, String> buildConstraintViolationsMap(ConstraintViolationException e) {
		Map<String, String> constraintViolations = new HashMap<String,String>();
		for (ConstraintViolation<?> entity: e.getConstraintViolations()) {
			constraintViolations.put(entity.getPropertyPath().toString(), entity.getMessage());
		}
		return constraintViolations;
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> createItem(@RequestBody Item item, Authentication auth, HttpServletResponse res) throws IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		if ((auth != null) && (auth.isAuthenticated())) {
			User user = (User) auth.getPrincipal();
			item.setSeller(user);
			item.setStartBidding(new Timestamp(new Date().getTime()));
			try {
				itemDao.create(item);
			} catch (ConstraintViolationException e) {
				result.put("fieldErrors", buildConstraintViolationsMap(e));
				result.put("status","WRONGPARAM");
				return result;
			}
		} else {
			res.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied.");
			return null;
		}
		result.put("status","SUCCESS");
		return result;		
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> updateItem(@RequestBody Item item, Authentication auth, HttpServletResponse res) throws IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		if ((auth != null) && (auth.isAuthenticated())) {
			User user = (User) auth.getPrincipal();
			if (item.getId() != null) {
				Item persistItem = itemDao.get(item.getId());
				if (persistItem != null) {
					if (persistItem.getSeller().equals(user)) {
						persistItem.setTitle(item.getTitle());
						persistItem.setDescription(item.getDescription());
						persistItem.setStartPrice(item.getStartPrice());
						persistItem.setTimeLeft(item.getTimeLeft());
						persistItem.setBuyItNow(item.isBuyItNow());
						persistItem.setBidIncrement(item.getBidIncrement());
						try {
							itemDao.update(persistItem);
						} catch (ConstraintViolationException e) {
							result.put("fieldErrors", buildConstraintViolationsMap(e));
							result.put("status","WRONGPARAM");
							return result;
						}
					}
				} else {
					result.put("status","EXCEPTION");
					result.put("errorMsg",String.format("Item with ID = %s doesn't exists.", item.getId()));
				}
			}
		} else {
			res.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied.");
			return null;
		}
        result.put("status","SUCCESS");
		return result;
    }
}
