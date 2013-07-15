package food.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import test.MongoUtil;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

import food.data.Hotel;
import food.data.MenuItem;

@Path("/service")
public class HotelService {

	@Path("hotel")	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getHotels(@QueryParam("hotelId") String hotelId, 
			@QueryParam("hotelName") String hotelName,
			@QueryParam("itemID") String itemID,
			@QueryParam("langitude") String langitude,
			@QueryParam("latitude") String latitude,
			@QueryParam("eastLat") String eastLat,
			@QueryParam("eastLong") String eastLong,
			@QueryParam("westLat") String westLat,
			@QueryParam("westLong") String westLong,
			@QueryParam("callback") String callback) {

		BasicDBObject dbquery = new BasicDBObject();
		if(hotelId != null){
			dbquery.put("hotelId", hotelId);
		}
		if(itemID != null){
			dbquery.put("itemID", itemID);
		}
		if(hotelName != null){
			Pattern john = Pattern.compile(".*"+hotelName+".*", Pattern.CASE_INSENSITIVE);
			dbquery.put("hotelName", john);
		}

		if(latitude!=null && langitude != null && eastLat!=null && eastLong!=null && westLat!=null && westLong!=null){
			dbquery.put("location.lat",new BasicDBObject("$gt", eastLat).append("$lt", westLat));
			dbquery.put("location.lng",new BasicDBObject("$gt", eastLong).append("$lt", westLong));
		}

		DBCursor cur = MongoUtil.hotelCollection.find(dbquery);
		StringBuffer result = new StringBuffer().append("");
		int i=0;
		while(cur.hasNext()){
			result.append(cur.next().toString());
			if(cur.hasNext()) result.append(", ");
		}
		//return callback+"({ \"result\" : ["+ result.toString() +"]})";
		return "{ \"result\" : ["+ result.toString() +"]}";
	} 

	public String getItemName(String itemID) {
		BasicDBObject dbquery = new BasicDBObject("itemId",itemID);
		DBCursor cur = MongoUtil.itemCollection.find(dbquery);
		if(cur.hasNext()){
			return cur.next().get("itemName").toString();
		}
		return "";
	}

	@Path("topItems")	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getTopItems( 
			@QueryParam("langitude") String langitude,
			@QueryParam("latitude") String latitude,
			@QueryParam("eastLat") String eastLat,
			@QueryParam("eastLong") String eastLong,
			@QueryParam("westLat") String westLat,
			@QueryParam("westLong") String westLong,
			@QueryParam("callback") String callback) {

		BasicDBObject dbquery = new BasicDBObject();

		if(latitude!=null && langitude != null && eastLat!=null && eastLong!=null && westLat!=null && westLong!=null){
			dbquery.put("location.lat",new BasicDBObject("$gt", eastLat).append("$lt", westLat));
			dbquery.put("location.lng",new BasicDBObject("$gt", eastLong).append("$lt", westLong));
		}

		HashMap<String, Double> ratingMap = new HashMap<String, Double>();
		TreeMap<String,Double> sorted_map = new TreeMap<String,Double>(new ValueComparator(ratingMap));
		DBCursor cur = MongoUtil.hotelCollection.find(dbquery);
		
		StringBuffer result = new StringBuffer().append("");
		JsonParser jsonParser = new JsonParser();
		Gson gson = new Gson();
		while(cur.hasNext()){
			ArrayList<MenuItem> menuItems = gson.fromJson(jsonParser.parse(cur.next().toString()),Hotel.class).getMenuItems();
			
			for(MenuItem menuItem : menuItems){
				//System.out.println(menuItem.getItemID());
				double score=menuItem.getRating();
				if(ratingMap.get(menuItem.getItemID())!=null){
					score += ratingMap.get(menuItem.getItemID());
				}
				ratingMap.put(menuItem.getItemID(), score);
			}
		
		}
		//System.out.println(ratingMap);
		sorted_map.putAll(ratingMap);
		//System.out.println(sorted_map);
		int count=0;
		for(Entry<String, Double> e : sorted_map.entrySet()){
			//System.out.println(e.getKey());
			BasicDBObject idQuery = new BasicDBObject("itemId",e.getKey());
			result.append(MongoUtil.itemCollection.findOne(idQuery));
			count++;

			if(count<=9){
				result.append(", ");
			}
			if(count >=10)
				break;

		}
		//return callback+"({ \"result\" : ["+ result.toString() +"]})";
		return "{ \"result\" : ["+ result.toString() +"]}";
	}
	
	@Path("topHotels")	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getTopHotels(
			@QueryParam("itemID") String itemID,
			@QueryParam("langitude") String langitude,
			@QueryParam("latitude") String latitude,
			@QueryParam("eastLat") String eastLat,
			@QueryParam("eastLong") String eastLong,
			@QueryParam("westLat") String westLat,
			@QueryParam("westLong") String westLong,
			@QueryParam("callback") String callback) {

		BasicDBObject dbquery = new BasicDBObject();
		
		if(itemID != null){
			dbquery.put("menuItems.itemID", itemID);
		}

		if(latitude!=null && langitude != null && eastLat!=null && eastLong!=null && westLat!=null && westLong!=null){
			dbquery.put("location.lat",new BasicDBObject("$gt", eastLat).append("$lt", westLat));
			dbquery.put("location.lng",new BasicDBObject("$gt", eastLong).append("$lt", westLong));
		}

		DBCursor cur = MongoUtil.hotelCollection.find(dbquery);
		
		StringBuffer result = new StringBuffer().append("");
		while(cur.hasNext()){
			result.append(cur.next().toString());
			if(cur.hasNext()) result.append(", ");
		}
		//System.out.println(ratingMap);
				//return callback+"({ \"result\" : ["+ result.toString() +"]})";
		return "{ \"result\" : ["+ result.toString() +"]}";
	}
	
	
	class ValueComparator implements Comparator<String> {

	    Map<String, Double> base;
	    public ValueComparator(Map<String, Double> base) {
	        this.base = base;
	    }

	    // Note: this comparator imposes orderings that are inconsistent with equals.    
	    public int compare(String a, String b) {
	        if (base.get(a) >= base.get(b)) {
	            //System.out.println("a : "+a + base.get(a));
	        	return -1;
	        } else {
	            return 1;
	        } // returning 0 would merge keys
	    }
	}
}
