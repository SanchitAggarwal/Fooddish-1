package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import food.data.Hotel;
import food.data.Item;
import food.data.Location;
import food.data.Review;

public class InsertTest {
	public static void main(String[] args) {
		System.out.println("Hello World");
		
		//Item item1 = new Item(1, "bir");
		double lat = 17.3667;
		double lng = 78.4667;
		String result = makeRestCall("https://api.foursquare.com/v2/venues/explore?venuePhotos=1&ll="
		+lat+","+lng+"&section=food,drinks,coffee&client_secret=RZG14LIXYDCWNBZXMRNI0O5J11CDVFJTALZP2LBGXO5RNB5G&client_id=Y4KGBABM13G0KEYBZZUIDE1PYMUCEZJHRFAFJ0BEHBH20KCL&v=20130713");
		//System.out.println(result);
		
		insertFrom4Square(result,createMapFromFile("/Users/GreatGod/Desktop/bakery.list"));
	}
	
	private static Map<String, Item> createMapFromFile(String fileName) {
		BufferedReader in = null;
		Map<String, String> map = new Map<String, Item>();
		try {
			in = new BufferedReader(new FileReader(fileName));
			String line = null;
			while ((line = in.readLine()) != null) {
				String[] splits = line.trim().split(" \\| ");
				map.put(fileName, )
			}
		} catch (FileNotFoundException e) {
			System.err.println("File " + fileName + " does not exist");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException ioex) {
				System.err.println("Error while closing File : " + fileName);
			}
		}
		
	}
	
	private static ArrayList<Item>

	private static void insertFrom4Square(String result, Map<String, String> map){
		JsonParser jsonParser = new JsonParser();
		int itemId =1;
		JsonArray items = jsonParser.parse(result).getAsJsonObject().get("response")
		.getAsJsonObject().get("groups")
		.getAsJsonArray().get(0)
		.getAsJsonObject().get("items").getAsJsonArray();
		
		for(int i=0;i<items.size();i++){
			JsonObject ithItem = items.get(i).getAsJsonObject();
			JsonObject venue = ithItem.get("venue").getAsJsonObject();
			String id = venue.get("id").getAsString();
			String name = venue.get("name").getAsString();
			String contact = ""; 
			if(	venue.get("contact") != null && venue.get("contact").getAsJsonObject().get("phone") != null){
					contact = venue.get("contact").getAsJsonObject().get("phone").getAsString();
			}
			
			JsonObject location = venue.get("location").getAsJsonObject();
			String add = "";
			if(location.get("address") != null)
				add=location.get("address").getAsString();
			double locationLat = location.get("lat").getAsDouble();
			double locationLng = location.get("lng").getAsDouble();
			String city = "";
			if(location.get("city") !=null) city = location.get("city").getAsString();
			String state="";
			if(location.get("state")!=null)state = location.get("state").getAsString();
			String country = location.get("country").getAsString();
			Location locationObj = new Location(locationLat,locationLng,add,city,state,country);
			
			JsonObject stat = venue.get("stats").getAsJsonObject();
			int checkinsCount = stat.get("checkinsCount").getAsInt();
			int usersCount = stat.get("usersCount").getAsInt();
			int tipCount = stat.get("tipCount").getAsInt();
			
			double rating = 1;
			if(venue.get("rating") != null) rating = venue.get("rating").getAsDouble();
			
			JsonArray tips = ithItem.get("tips").getAsJsonArray();
			ArrayList<Review> reviews = new ArrayList<Review>();
			for(int j=0;j<tips.size();j++){
				JsonObject tip = tips.get(j).getAsJsonObject();
				String reviewId = tip.get("id").getAsString();
				String desc = tip.get("text").getAsString();
				JsonObject user = tip.get("user").getAsJsonObject();
				String authorName = "";
				if(user != null){
					if(user.get("firstName") != null)  authorName += user.get("firstName").getAsString(); 
					if(user.get("lastName") != null) authorName += " " + user.get("lastName").getAsString();
				}
				reviews.add(new Review(reviewId, authorName, desc, 2, new Date()));
			}
			JsonArray phrases = new JsonArray();
			if(ithItem.get("phrases") != null) phrases = ithItem.get("phrases").getAsJsonArray();
			ArrayList<Item> items2 = new ArrayList<Item>();
			for(int k=0;k<phrases.size();k++){
				items2.add(new Item(itemId++, phrases.get(k).getAsJsonObject().get("phrase").getAsString(),null, 1));
			}
			Hotel hotel = new Hotel(id,name, items2, reviews,locationObj, checkinsCount,usersCount, tipCount,rating,contact);
			Gson gson = new Gson();
			DBObject obj = (DBObject)JSON.parse(gson.toJson(hotel));
			MongoUtil.hotelCollection.save(obj);
			System.out.println(name);
		}
	}
	
	JsonObject checkNotNull(JsonObject obj, String item){
		if(obj.get(item) != null){
			return obj.get(item).getAsJsonObject();
		}
		
		return null;
	}
	
	public static String makeRestCall(String urlString){
		URL url;
		HttpURLConnection conn=null;
		StringBuffer result = new StringBuffer();
		try {
			url = new URL(urlString);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String line;

			while ((line = br.readLine()) != null) {
				result.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		conn.disconnect();
		return result.toString();
	}

}
