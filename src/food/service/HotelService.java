package food.service;

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

@Path("/service")
public class HotelService {
	
	@Path("hotel")	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getHotels(@QueryParam("hotelId") String hotelId, 
			@QueryParam("hotelName") String hotelName, 
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
		if(hotelName != null){
			Pattern john = Pattern.compile(".*"+hotelName+".*", Pattern.CASE_INSENSITIVE);
			dbquery.put("hotelName", john);
		}
		
		if(latitude!=null && langitude != null && eastLat!=null && eastLong!=null && westLat!=null && westLong!=null){
			dbquery.put("location,lat",new BasicDBObject("$gt", eastLat).append("$lt", westLat));
			dbquery.put("location,lng",new BasicDBObject("$gt", eastLong).append("$lt", westLong));
		}
		
		DBCursor cur = MongoUtil.hotelCollection.find(dbquery);
		if(cur.count()>0){
			return callback+"("+ cur.next().toString() +")";
		}else{
			return callback+"({})";
		} 
	}
}
