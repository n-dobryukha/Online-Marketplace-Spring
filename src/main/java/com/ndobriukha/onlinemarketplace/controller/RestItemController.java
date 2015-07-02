package com.ndobriukha.onlinemarketplace.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ndobriukha.onlinemarketplace.dao.BidDao;
import com.ndobriukha.onlinemarketplace.dao.ItemDao;
import com.ndobriukha.onlinemarketplace.domain.Bid;
import com.ndobriukha.onlinemarketplace.domain.Item;
import com.ndobriukha.onlinemarketplace.domain.User;

@RestController
@RequestMapping("/rest/item")
public class RestItemController {

	@Autowired
	private ItemDao<Item, Long> itemDao;
	
	@Autowired
	private BidDao<Bid, Long> bidDao;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
    public Map<String, List<Map<String, Object>>> getItems(Authentication auth, @RequestParam String scope, @RequestParam(defaultValue = "false") boolean search, HttpServletRequest req ) throws NumberFormatException, ParseException {
		User user = null;
		if ((auth != null) && (auth.isAuthenticated())) {
			user = (User) auth.getPrincipal();
		}
		final List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		List<Item> items = null;
		if (search) {
			items = itemDao.getByParameters(req.getParameterMap());			
		} else {
			items = itemDao.get();
		}
		for (Item item: items) {
			Map<String, Object> dataItem = buildItemData(item);
			switch (scope) {
			case "all":
				if (item.isSold()) {
					continue;
				}
				if (user != null) {
					if (user.equals(item.getSeller())) {
						dataItem.put("action", "edit");
					}
				} else {
					dataItem.put("action", "");
				}
				break;
			case "my":
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
				break;	
			default:
				continue;
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
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");

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
		dateData.put("display", dateFormat.format(cal.getTime()));
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
	
	private Map<String, String> buildConstraintViolationsMap(ConstraintViolationException e) {
		Map<String, String> constraintViolations = new HashMap<String,String>();
		for (ConstraintViolation<?> entity: e.getConstraintViolations()) {
			constraintViolations.put(entity.getPropertyPath().toString(), entity.getMessage());
		}
		return constraintViolations;
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> updateItem(@RequestBody Item item, @PathVariable Long id, Authentication auth, HttpServletResponse res) throws IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		if ((auth != null) && (auth.isAuthenticated())) {
			User user = (User) auth.getPrincipal();
			if (id != null) {
				Item persistItem = itemDao.get(id);
				if (persistItem != null) {
					if (user.equals(persistItem.getSeller())) {
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
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteItem(@PathVariable Long id, Authentication auth, HttpServletResponse res) throws IOException {
		if ((auth != null) && auth.isAuthenticated()) {
			User user = (User) auth.getPrincipal();
			if (user != null) {
				Item persistItem = itemDao.get(id);
				if (persistItem != null) {
					if (user.equals(persistItem.getSeller())) {
						itemDao.delete(persistItem);
						return;
					} else {
						res.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied.");
						return;
					}
				}
			}
		} else {
			res.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied.");
			return;
		}
	}
	
	@RequestMapping(value = "/{id}/bid/", method = RequestMethod.GET)
	public Map<String, List<Map<String, Object>>> getBids(@PathVariable Long id, Authentication auth, HttpServletResponse res) throws IOException {
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		NumberFormat numberFormatter = new DecimalFormat("#0.00");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		Item persistItem = itemDao.get(id);
		if (persistItem != null) {
			List<Bid> bids = itemDao.getAllBidsByItem(persistItem);
			if ((bids != null) && (bids.size() > 0)) {
				int count = 0;
				for (Bid bid: bids) {
					Map<String, Object> dataItem = new HashMap<String, Object>();
					dataItem.put("count", ++count);
					
					Map<String, Object> bidderData = new HashMap<String, Object>();
					User bidder = bid.getBidder();				
					if (bidder != null) {
						bidderData.put("id", bidder.getId());
						bidderData.put("name", bidder.getFullName());
					}
					dataItem.put("bidder", bidderData);
					dataItem.put("amount", numberFormatter.format(bid.getAmount()));
					
					Map<String, Object> dateData = new HashMap<String, Object>();
					dateData.put("display", dateFormat.format(bid.getTimestamp()));
					dateData.put("timestamp", Long.toString(bid.getTimestamp().getTime()));				
					dataItem.put("ts", dateData);
					
					data.add(dataItem);
				}
			}
		}
		Map<String, List<Map<String, Object>>> results = new HashMap<String, List<Map<String, Object>>>();
		results.put("data", data);
		return results;
	}
	
	@RequestMapping(value = "/{id}/bid/", method = RequestMethod.POST)
	public Map<String, Object> createBids(@PathVariable Long id, @RequestParam double amount,
			Authentication auth,
			HttpServletRequest req,
			HttpServletResponse res) throws IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		if ((auth != null) && auth.isAuthenticated()) {
			Item item = itemDao.get(id);
			if (item != null) {
				Bid bid = new Bid();
				bid.setBidder((User) auth.getPrincipal());
				bid.setItem(item);
				bid.setAmount(amount);
				bid.setTimestamp(new Timestamp(new Date().getTime()));
				
				Long bidId = bidDao.create(bid);
				res.setStatus(HttpServletResponse.SC_CREATED);
				res.addHeader("Location", String.format("%s/rest/item/%s/bid/%s", req.getContextPath(), id, bidId));				
				result = buildItemData(itemDao.get(id));
			}
		} else {
			res.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied.");
			return null;
		}
		return result;
	}
}
