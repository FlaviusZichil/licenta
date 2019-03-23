package app.beans;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import app.entities.City;
import app.repositories.CityRepository;

@Component
public class LoadCitiesBean {

	@Autowired
	private CityRepository cityRepository;
	
	@EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) throws IOException {
		this.getClosestTrips();
    }
	
	private void getClosestTrips() throws IOException {		
		Iterable<City> allCities = cityRepository.findAll();		
		
		if(convertFromIterableToList(allCities).isEmpty()) {
			String citiesFromJSON = new String(Files.readAllBytes(Paths.get(".\\src\\main\\resources\\static\\json\\judete.json")));
			
			List<String> cities = getValuesForGivenKey(citiesFromJSON, "city");
			List<String> latitudes = getValuesForGivenKey(citiesFromJSON, "lat");
			List<String> longitudes = getValuesForGivenKey(citiesFromJSON, "lng");
			
			addCitiesToDatabase(cities, latitudes, longitudes);
		}			
	}
	
	private List<String> getValuesForGivenKey(String jsonArrayStr, String key) {
	    List<String> values = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(jsonArrayStr);
        for (int idx = 0; idx < jsonArray.length(); idx++) {
            JSONObject jsonObj = jsonArray.getJSONObject(idx);
            values.add(jsonObj.optString(key));
        }
        return values;
	}
	
	private void addCitiesToDatabase(List<String> cities, List<String> latitudes, List<String> longitudes) {
		for(int index = 0; index < cities.size(); index++) {
			cityRepository.save(new City(cities.get(index), 
										 Double.parseDouble(latitudes.get(index)), 
										 Double.parseDouble(longitudes.get(index))));
		}
	}
	
	private List<City> convertFromIterableToList(Iterable<City> cities){
		List<City> citiesList = new ArrayList<>();
		for(City city : cities) {
			citiesList.add(city);
		}
		return citiesList;
	}
}
