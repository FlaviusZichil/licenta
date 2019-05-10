package app.utils.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class WeatherApi {

	public static String getCurrentAddressIP() throws UnknownHostException {
        String ipAddress = ""; 
        try
        { 
            URL urlToRetrieveIPAddress = new URL("http://bot.whatismyipaddress.com"); 
            BufferedReader sc =  new BufferedReader(new InputStreamReader(urlToRetrieveIPAddress.openStream())); 
            ipAddress = sc.readLine().trim(); 
        } 
        catch (Exception e) 
        { 
        	ipAddress = "Cannot Execute Properly"; 
        } 
        return ipAddress;
	}
	
	
	public static Map<String, String> getLocationByIpAddress() throws UnknownHostException {
		final String API_KEY = "df96e8e0e38606dd7d6533b50b2fe929f864079c2b66a5d9ecccfa93accda0f7";
		final String URL_STRING = "https://api.ipinfodb.com/v3/ip-city/?key=";
		final String IP = getCurrentAddressIP();
		
		Map<String, String> location = new HashMap<>();
		
		try {
			StringBuilder result = new StringBuilder();
			URL url = new URL(URL_STRING + API_KEY + "&ip=" + IP);
			URLConnection connection = url.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			
			while((line = reader.readLine()) != null) {
				result.append(line);
			}
			reader.close();
			
			List<String> elements = Arrays.asList(result.toString().split(";"));
			location.put(elements.get(8), elements.get(9));
		}
		catch(IOException e) {
			System.out.println(e.getMessage());
		}	
		return location;
	}
	
	public static Map<String, Object> jsonToMap(String str){
		Map<String, Object> map = new Gson().fromJson(str, new TypeToken<HashMap<String, Object>>() {}.getType());
		return map;
	}
	
	private static Map<String, String> parseJson(String json){
		Map<String, String> map = new HashMap<>();
		
		json = json.substring(1, json.length() - 1);
		List<String> JSONelements = Arrays.asList(json.toString().split(","));
		
		for(String element : JSONelements) {
			element.trim();
			if(element.charAt(element.length() - 1) == ',') {
				element = element.substring(0, element.length() - 1);
			}
			String key = element.substring(0, element.indexOf("=")).trim();
			String value = element.substring(element.indexOf("=") + 1).trim();
			map.put(key, value);
		}
		return map;
	}
	
	private static Double kelvinToCelsius(Double kelvinTemperature) {
		Double celsiusTemperature = kelvinTemperature - 273.15;
		return celsiusTemperature; 
	}
	
	public static void getWeatherData() throws UnknownHostException {
		final String API_KEY = "91c82cc78c59bb61cac61da945ef9337";
		final String URL_STRING = "https://api.openweathermap.org/data/2.5/weather?lat=";
		String LATITUDE = "";
		String LONGITUDE = "";
		
		Map<String, String> location = getLocationByIpAddress();
	
		for (Map.Entry<String, String> entry : location.entrySet()) {
			LATITUDE = entry.getKey();
			LONGITUDE = entry.getValue();
		}

		try {
			StringBuilder result = new StringBuilder();
			URL url = new URL(URL_STRING + LATITUDE + "&lon=" + LONGITUDE + "&&appid=" + API_KEY);
			URLConnection connection = url.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			
			while((line = reader.readLine()) != null) {
				result.append(line);
			}
			reader.close();
			
			Map<String, Object> responseMap = jsonToMap(result.toString());

			String weather = responseMap.get("weather").toString();
			weather = weather.substring(1, weather.length() - 1);
			
			Map<String, String> main = parseJson(responseMap.get("main").toString());
			Map<String, String> wind = parseJson(responseMap.get("wind").toString());
			Map<String, String> rain = parseJson(responseMap.get("rain").toString());
			Map<String, String> clouds = parseJson(responseMap.get("clouds").toString());
			Map<String, String> weatherMap = parseJson(weather);
			
			System.out.println(main.toString());
			System.out.println("===================");
			System.out.println(wind.toString());
			System.out.println("===================");
			System.out.println(rain.toString());
			System.out.println("===================");
			System.out.println(clouds.toString());
			System.out.println("===================");
			System.out.println(weatherMap.toString());
						
			System.out.println("TEMPERATURE: " + kelvinToCelsius(Double.parseDouble(main.get("temp"))));
			System.out.println("HUMIDITY: " + main.get("humidity"));
			System.out.println("PRESSURE: " + main.get("pressure"));			
			System.out.println("WIND SPEED: " + wind.get("speed"));		
			System.out.println("WEATHER: " + weatherMap.get("main"));		
			System.out.println("CLOUDS: " + clouds.get("all"));
		}
		catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
