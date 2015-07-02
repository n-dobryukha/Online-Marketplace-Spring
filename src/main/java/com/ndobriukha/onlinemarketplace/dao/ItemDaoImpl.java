package com.ndobriukha.onlinemarketplace.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

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
	public List<Item> getByParameters(Map<String, String[]> params) throws NumberFormatException, ParseException {
		boolean isFirst = true;
		List<Object> valuesArrayList = new ArrayList<Object>();
		List<Type> typesArrayList = new ArrayList<Type>();
		StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM ITEMS WHERE ID IN (SELECT ID FROM (\r\n" + 
				"    SELECT ID, TITLE, DESCRIPTION, NVL(B.BID, I.START_PRICE) AS \"PRICE\",\r\n" + 
				"            I.START_BIDDING AS \"START_DATE\",\r\n" + 
				"            (I.START_BIDDING + NUMTODSINTERVAL(I.TIME_LEFT, 'HOUR')) AS \"EXPIRE_DATE\",\r\n" + 
				"            I.BUY_IT_NOW,\r\n" + 
				"            NVL(B.\"BIDDER_COUNT\", 0) \"BIDDER_COUNT\"\r\n" + 
				"        FROM ITEMS I\r\n" + 
				"        LEFT JOIN (SELECT ITEM_ID, MAX(AMOUNT) BID, COUNT(DISTINCT BIDDER_ID) \"BIDDER_COUNT\"\r\n" + 
				"                FROM BIDS\r\n" + 
				"                GROUP BY ITEM_ID) B ON I.ID = B.ITEM_ID)\r\n" + 
				"    WHERE");
		for(Entry<String, String[]> param: params.entrySet()) {
			String key = param.getKey();
			String[] value = param.getValue();
			if ((value.length == 0) || (value[0].equals(""))) {
				continue;
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			switch (key) {
			case "uid":
				sqlBuilder.append( String.format("%s ID = ?", !isFirst ? " AND" : ""));
				valuesArrayList.add(Long.parseLong(value[0]));
				typesArrayList.add(StandardBasicTypes.LONG);
				isFirst = false;
				break;
			case "title":
			case "description":
				sqlBuilder.append( String.format("%s UPPER(%s) like ?", !isFirst ? " AND" : "", key.toUpperCase()));
				valuesArrayList.add(String.format("%%%s%%", value[0].toUpperCase()));
				typesArrayList.add(StandardBasicTypes.STRING);
				isFirst = false;
				break;
			case "minPrice":
				sqlBuilder.append( String.format("%s PRICE >= ?", !isFirst ? " AND" : ""));
				valuesArrayList.add(Double.parseDouble(value[0]));
				typesArrayList.add(StandardBasicTypes.DOUBLE);
				isFirst = false;
				break;
			case "maxPrice":
				sqlBuilder.append( String.format("%s PRICE <= ?", !isFirst ? " AND" : ""));
				valuesArrayList.add(Double.parseDouble(value[0]));
				typesArrayList.add(StandardBasicTypes.DOUBLE);
				isFirst = false;
				break;
			case "bidderCount":
				sqlBuilder.append( String.format("%s BIDDER_COUNT = ?", !isFirst ? " AND" : ""));
				valuesArrayList.add(Integer.parseInt(value[0]));
				typesArrayList.add(StandardBasicTypes.INTEGER);
				isFirst = false;
				break;
			case "isBuyItNow":
				sqlBuilder.append( String.format("%s BUY_IT_NOW = ?", !isFirst ? " AND" : ""));
				valuesArrayList.add(Boolean.valueOf(value[0].toUpperCase()));
				typesArrayList.add(StandardBasicTypes.YES_NO);
				isFirst = false;
				break;
			case "startDate":
				sqlBuilder.append( String.format("%s START_DATE >= ?", !isFirst ? " AND" : ""));
				valuesArrayList.add(dateFormat.parse(value[0]));
				typesArrayList.add(StandardBasicTypes.TIMESTAMP);
				isFirst = false;
				break;
			case "expireDate":
				sqlBuilder.append( String.format("%s EXPIRE_DATE <= ?", !isFirst ? " AND" : ""));
				valuesArrayList.add(dateFormat.parse(value[0]));
				typesArrayList.add(StandardBasicTypes.TIMESTAMP);
				isFirst = false;
				break;
			default:
				break;
			}			
		}
		sqlBuilder.append(")");
		Object[] valuesArray = valuesArrayList.toArray(new Object[valuesArrayList.size()]);
		Type[] typesArray = typesArrayList.toArray(new Type[typesArrayList.size()]);;
		Query query = getSession().createSQLQuery(sqlBuilder.toString())
				.addEntity(Item.class)
				.setParameters(valuesArray, typesArray);
		return query.list();
	}
}
