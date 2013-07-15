package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import food.data.Deal;
import food.data.Hotel;
import food.data.Item;
import food.data.Location;
import food.data.MenuItem;
import food.data.Review;
import food.service.HotelService;

public class InsertTest {
	static int itemId =1;
	
	public static void main(String[] args) {
		System.out.println("Hello World");
		
		//Item item1 = new Item(1, "bir");
		double lat = 17.3667;
		double lng = 78.4667;
		//String result = makeRestCall("https://api.foursquare.com/v2/venues/explore?venuePhotos=1&ll="
		//+lat+","+lng+"&section=food,drinks,coffee&client_secret=RZG14LIXYDCWNBZXMRNI0O5J11CDVFJTALZP2LBGXO5RNB5G&client_id=Y4KGBABM13G0KEYBZZUIDE1PYMUCEZJHRFAFJ0BEHBH20KCL&v=20130713");
		//System.out.println(result);
		//HashMap<String, ArrayList<Item>> map = new HashMap<String, ArrayList<Item>>();
		//map.put("bakery", createListFromFile("/Users/GreatGod/Desktop/bakery.list"));
		//map.put("rest", createListFromFile("/Users/GreatGod/Desktop/rest.list"));
		//map.put("ice", createListFromFile("/Users/GreatGod/Desktop/ice.list"));
		//map.put("cafe", createListFromFile("/Users/GreatGod/Desktop/cafe.list"));
	//	insertFrom4Square(result,map);
		//inseetFromLocu(makeRestCall("http://api.locu.com/v1_0/menu_item/search/?api_key=7ee2879545b136914aa24ed53ec5dae62d07199e&category=restaurant&country=usa"));
		HotelService h = new HotelService();
		System.out.println(h.getItemName("7d021b0c9f4ec79a3d5c76fae8ba8a91b7457979bc9731b2b4c948c92b292a60"));
	}
	
	private static void inseetFromLocu(String result) {
		JsonParser jsonParser = new JsonParser();
		JsonArray objects = jsonParser.parse(result).getAsJsonObject().get("objects").getAsJsonArray();
		for(int i=0;i<objects.size();i++){
			String desc = objects.get(i).getAsJsonObject().get("description").getAsString();
			String id = objects.get(i).getAsJsonObject().get("id").getAsString();
			String name = objects.get(i).getAsJsonObject().get("name").getAsString();
			JsonObject venue = objects.get(i).getAsJsonObject().get("venue").getAsJsonObject();
			String country = venue.get("country").getAsString();
			String hotelId = venue.get("id").getAsString();
			Double lat = venue.get("lat").getAsDouble();
			Double lng = venue.get("long").getAsDouble();
			String hotelName = venue.get("name").getAsString();
			String region = venue.get("region").getAsString();
			String address = venue.get("street_address").getAsString()  
					  +" "+ venue.get("country").getAsString() + " " +venue.get("postal_code").getAsInt();
			
			
			//BasicDBObject dbquery = new BasicDBObject("itemName", name);
			Item item = new Item(id, name, desc, getLargeImgUrl(name, lat, lng, "q"));
			//DBObject obj = MongoUtil.itemCollection.findOne(dbquery);
			Gson gson = new Gson();
			BasicDBObject searchQuery = new BasicDBObject("itemName", name);
			DBObject searchObj = MongoUtil.itemCollection.findOne(searchQuery);
			DBObject obj;
			System.out.println("!!!!!!!!!!"+name);

			if(searchObj == null){
				obj = (DBObject)JSON.parse(gson.toJson(item));
				MongoUtil.itemCollection.save(obj);
			}else{
//				System.out.println("!!!!!!!!!!"+name);
				obj=searchObj;
			}
			Item lastItem = gson.fromJson(gson.toJson(obj), Item.class);
			Location location = new Location(lat, lng, address, venue.get("locality").getAsString() , venue.get("region").getAsString(), country);
			ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
			MenuItem menuItem = new MenuItem(lastItem.getItemId(), getRandomNumber(0, 5));
			menuItems.add(menuItem);
			
			BasicDBObject dbquery = new BasicDBObject("hotelId", hotelId);
			DBObject hotelResult = MongoUtil.hotelCollection.findOne(dbquery);
			if(hotelResult==null){
				Hotel hotel = new Hotel(hotelId, hotelName, menuItems, new ArrayList<Review>(), location, 0, 0, 0, getRandomNumber(0, 5), "9991122121", getLargeImgUrl(hotelName, lat, lng, "q"));
				DBObject hotelObj = (DBObject)JSON.parse(gson.toJson(hotel));
				MongoUtil.hotelCollection.save(hotelObj);
			}else{
				Hotel hotel = gson.fromJson(jsonParser.parse(hotelResult.toString()),Hotel.class);
				ArrayList<MenuItem> temp = hotel.getMenuItems();
				temp.add(menuItem);
				hotel.setMenuItems(temp);
				MongoUtil.hotelCollection.remove(dbquery);
				//DBObject updateQuery = new BasicDBObject("$set",new BasicDBObject("menuItems",temp));
				//MongoUtil.hotelCollection.update(dbquery, updateQuery);
				DBObject hotelObj = (DBObject)JSON.parse(gson.toJson(hotel));
				MongoUtil.hotelCollection.save(hotelObj);
			}
		}
	}

	private static ArrayList<Item> createListFromFile(String fileName) {
		BufferedReader in = null;
		ArrayList<Item> list = new ArrayList<Item>();
		int itemId=0;
		try {
			in = new BufferedReader(new FileReader(fileName));
			String line = null;
			while ((line = in.readLine()) != null) {
				String[] splits = line.trim().split("\\|");
				Item item = new Item(itemId+++"", splits[0].trim(),null,getLargeImgUrl(splits[0].trim(), 0, 0, "q"));
				if(splits.length > 1 && splits[1]!=null){
					item.setItemDesc(splits[1]);
				}
				list.add(item);
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
		return list;
	}

	private static void insertFrom4Square(String result, HashMap<String, ArrayList<Item>> map){
		JsonParser jsonParser = new JsonParser();
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
			ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
			
			for(int k=0;k<phrases.size();k++){
				//menuItems.add(new MenuItem(new Item(itemId++, 
					//								phrases.get(k).getAsJsonObject().get("phrase").getAsString(), 
					//								phrases.get(k).getAsJsonObject().get("sample").getAsJsonObject().get("text").getAsString()), 
						//	getRandomNumber(1, 5),"default.jpg"));
			}
			String categories = venue.get("categories").getAsJsonArray().get(0).getAsJsonObject().get("name").getAsString();
			menuItems.addAll(getMenuItemFromCatagory(map,categories));
			
			Hotel hotel = new Hotel(id,name, menuItems, reviews,locationObj, checkinsCount,usersCount, tipCount,rating,contact,getLargeImgUrl(name+" "+locationObj.getCity(),locationLat,locationLng,"m"));
			Gson gson = new Gson();
			DBObject obj = (DBObject)JSON.parse(gson.toJson(hotel));
			MongoUtil.hotelCollection.save(obj);
			System.out.println(name);
		}
	}
	
	static int getRandomNumber(int min, int max)
	{
	    Random rand = new Random();
	    return rand.nextInt(max - min + 1) + min;
	}
	
	private static ArrayList<MenuItem> getMenuItemFromCatagory(HashMap<String, ArrayList<Item>> map, String cat) {
		String lowerCaseCat = cat.toLowerCase();
		if(lowerCaseCat.contains("ice") || lowerCaseCat.contains("cream")){
			System.out.println("ice");
			return getMenuList(map.get("ice"));
		}else if(lowerCaseCat.contains("cafe") || lowerCaseCat.contains("tea") || lowerCaseCat.contains("café")){
			System.out.println("cafe");
			return getMenuList(map.get("cafe"));
		}else if(lowerCaseCat.contains("bakery") || lowerCaseCat.contains("biscuit")){
			System.out.println("bakery");
			return getMenuList(map.get("bakery"));
		}else{
			System.out.println("rest");
			return getMenuList(map.get("rest"));
		}
	}

	static void insertDeals(){
		DBCursor cur = MongoUtil.hotelCollection.find();
		int i=0;
		Gson gson = new Gson();
		while(cur.hasNext()){
			DBObject obj = cur.next();
			String hotelId = (String)obj.get("hotelId");
			obj.get("menuItems.itemID");
			 
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			Date tomorrow = calendar.getTime();
			//MongoUtil.dealCollection.save((DBObject)JSON.parse(gson.toJson(new Deal(new Date(), calendar.getTime(), , hotelId, get))));
			
		}
	}
	
	private static ArrayList<MenuItem> getMenuList(ArrayList<Item> items) {
		ArrayList<MenuItem> result = new ArrayList<MenuItem>();
		Gson gson = new Gson();
		if(items == null)
			return result;
		for(Item item : items){
			BasicDBObject dbquery = new BasicDBObject("itemName", item.getItemName());
			DBObject obj = MongoUtil.itemCollection.findOne(dbquery);
			if(obj == null){
				obj = (DBObject)JSON.parse(gson.toJson(item));
				MongoUtil.itemCollection.save(obj);
			}
			result.add(new MenuItem(obj.get("_id").toString(), getRandomNumber(1, 5))); 
			
		}
		return result;
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

	static String getLargeImgUrl(String text, double lat, double lng, String type){
		String restURL = "http://api.flickr.com/services/rest/?method=flickr.photos.search&" +
				"api_key=bed36549031f5209c0221db9e92ce4cc&accuracy=11&format=json&nojsoncallback=1";
		if(lat!=0)
			restURL = restURL+"&lat="+lat;
		if(lng!=0)
			restURL = restURL + "&lon"+lng;
		
		if(text != null)
			restURL=restURL+"&text="+text;	
		String result = makeRestCall(restURL);
		JsonParser jsonParser = new JsonParser();
		JsonArray photos = jsonParser.parse(result).getAsJsonObject().get("photos").getAsJsonObject().get("photo").getAsJsonArray();
		if(photos.size() == 0)
			return "default.jpg";
				
		JsonObject	photo = photos.get(0).getAsJsonObject();
		
		String imgurl = "http://farm"+photo.get("farm").getAsString()+".staticflickr.com/"+photo.get("server").getAsString()+"/"+
				photo.get("id").getAsString()+"_"+ photo.get("secret").getAsString() +"_"+type+".jpg";
		System.out.println(imgurl);
		return imgurl;
	}
	
	static Map<Integer,ArrayList<Integer>> genrateDealsForHotels(ArrayList<Integer> itemLists, double rating)
	{
	    int[] NoDays = {1,2,3};
	    int[] percentages = {25,40,10,15,30};

	    int Deals = 0;

	    if (rating > 8) {
	        Deals = 1;
	    } else if (rating > 7) {
	        Deals = 2;
	    } else if (rating > 5) {
	        Deals = 3;
	    } else if (rating > 4) {
	        Deals = 4;
	    }

	    int length = itemLists.size();

	    ArrayList<Integer> list = getRandomNumberInPool(0, length-1, Deals);

	    Map<Integer,ArrayList<Integer>> discounts = new HashMap<Integer,ArrayList<Integer>>();

	    for (int i = 0; i < list.size(); i++) {
	        int discountPer = percentages[getRandomNumber(0, 4)];
	        int noDays = NoDays[getRandomNumber(0, 2)];
	        int itemId = list.get(i);
	        ArrayList<Integer> s = new ArrayList<Integer>();
	        s.add(discountPer);
	        s.add(noDays);
	        discounts.put(itemLists.get(itemId),s);
	    }

	    return discounts;
	}

	static ArrayList<Integer> getRandomNumberInPool(int min, int max, int pickNo)
	{
	    Random rand = new Random();

	    ArrayList<Integer> ranNum = new ArrayList<Integer>();

	    for (int i = 0; i <pickNo; i++) {
	        int rndnum = getRandomNumber(min, max);

	        while (ranNum.contains(rndnum)) {
	            rndnum = getRandomNumber(min, max);
	        }

	        ranNum.add(rndnum);
	    }

	    return ranNum;
	}
}
