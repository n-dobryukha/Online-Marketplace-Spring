package com.ndobriukha.onlinemarketplace.dao;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.ndobriukha.onlinemarketplace.domain.Bid;
import com.ndobriukha.onlinemarketplace.domain.Item;

public class ItemDaoImpl extends GenericDaoHibernateImpl<Item, Long> implements ItemDao<Item, Long> {
	
	public ItemDaoImpl() {
	}
	
	public ItemDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public Bid getBestBidByItem(Item item) {
		return (Bid) getSession().createCriteria(Bid.class)
				.add( Restrictions.eq("item", item) )
				.addOrder( Property.forName("amount").desc() )
				.addOrder( Property.forName("id").desc() )
				.setMaxResults(1)
				.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Bid> getAllBidsByItem(Item item) {
		return (List<Bid>) getSession().createCriteria(Bid.class)
				.add( Restrictions.eq("item", item) )
				.addOrder( Property.forName("amount").desc() )
				.addOrder( Property.forName("id").desc() )
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getByParameters(Map<String, String[]> params) {
		boolean isFirst = true;
		StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM (\r\n" + 
				"    SELECT ID, TITLE, DESCRIPTION, (I.START_PRICE + NVL(B.BID, 0)) AS \"PRICE\",\r\n" + 
				"            TO_CHAR(I.START_BIDDING, 'yyyy-mm-dd hh24:mi') AS \"START\",\r\n" + 
				"            TO_CHAR((I.START_BIDDING + NUMTODSINTERVAL(I.TIME_LEFT, 'HOUR')), 'yyyy-mm-dd hh24:mi') AS \"EXPIRE\",\r\n" + 
				"            I.BUY_IT_NOW,\r\n" + 
				"            NVL(B.\"COUNT\", 0) \"COUNT\"\r\n" + 
				"        FROM ITEMS I\r\n" + 
				"        LEFT JOIN (SELECT ITEM_ID, MAX(AMOUNT) BID, COUNT(DISTINCT BIDDER_ID) \"COUNT\"\r\n" + 
				"                FROM BIDS\r\n" + 
				"                GROUP BY ITEM_ID) B ON I.ID = B.ITEM_ID)\r\n" + 
				"    WHERE");
		for(Entry<String, String[]> param: params.entrySet()) {
			String key = param.getKey();
			String[] value = param.getValue();
			System.out.println(String.format("%s = %s", key, value[0]));
			if ((value.length == 0) || (value[0].equals(""))) {
				continue;
			}
			switch (key) {
			case "uid":
				sqlBuilder.append( String.format("%s ID = ?", !isFirst ? " AND" : ""));
				isFirst = false;
				break;
			case "title":
			case "description":
				sqlBuilder.append( String.format("%s UPPER(%s) like '%%?%%'", !isFirst ? " AND" : "", key.toUpperCase()));
				isFirst = false;
				break;
			case "minPrice":
				sqlBuilder.append( String.format("%s PRICE >= ?", !isFirst ? " AND" : ""));
				isFirst = false;
				break;
			case "maxPrice":
				sqlBuilder.append( String.format("%s PRICE <= ?", !isFirst ? " AND" : ""));
				isFirst = false;
				break;
			default:
				break;
			}
			Query query = getSession().createQuery("");
		}
		return sqlBuilder.toString();
	}
}
