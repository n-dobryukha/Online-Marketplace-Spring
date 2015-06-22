package com.ndobriukha.onlinemarketplace.controller;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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
    public Map<String, List> getAllItems() {
		final List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		List<Item> items = itemDao.get();
		
		for (Item item: items) {
			if (item.isSold()) { // || (item.getSellerId().equals(user.getId()))) {
				continue;
			}
			Map<String, Object> dataItem = buildItemData(item);
			/*if (UserManagementService.GUEST_ROLE.equals(req.getSession().getAttribute("Role"))) {
				dataItem.put("action", "");
			} else if (item.getSellerId().equals(user.getId())) {
				dataItem.put("action", "edit");
			}*/
			data.add(dataItem);
		}
		Map<String, List> results = new HashMap<String, List>();
		results.put("data", data);
        return results;
    }
	
	@RequestMapping(value = "/my", method = RequestMethod.GET)
    public List<Item> getMyItems() {
        return itemDao.get();
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

		Bid bid = null;
		/*
		 * Bid bid = oraBidDao.getBestBidByItemId(item.getId()); if (bid !=
		 * null) { User bidder = oraUserDao.get(bid.getBidderId()); if (bidder
		 * != null) { Map<String, Object> bidderData = new HashMap<String,
		 * Object>(); bidderData.put("id", bidder.getId());
		 * bidderData.put("name", bidder.getFullName()); data.put("bidder",
		 * bidderData); } }
		 */
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
}
