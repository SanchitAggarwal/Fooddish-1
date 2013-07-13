package test;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class MongoUtil {
	private static MongoClient mongoClient;
	private static DB db;
	public static DBCollection hotelCollection;
	public static DBCollection itemCollection;
	public static DBCollection topItems;
	
	static{
		try {
			mongoClient = new MongoClient();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		db = mongoClient.getDB("food");
		hotelCollection = db.getCollection("hotelCollection");
		itemCollection = db.getCollection("itemCollection");
	}

	@Override
	protected void finalize() throws Throwable {
		mongoClient.close();
		super.finalize();
	}
}