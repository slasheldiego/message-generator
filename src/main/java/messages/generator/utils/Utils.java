package messages.generator.utils;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import messages.generator.entities.Event;

public class Utils{

    public Utils(){

    }

    public static List<Event> generateEvents(int lenght) {
        List<Event> events = new ArrayList<Event>();

        List<String> countries = new ArrayList<String>(Arrays.asList("PE", "CO", "CL", "AR"));
        List<String> descriptions = new ArrayList<String>(Arrays.asList("Tech", "Food", "House", "Clothes"));

        for (int i = 0; i < lenght; i++) {
            Random rand = new Random();
            Long id = UUID.randomUUID().getMostSignificantBits();
            int quantity = ThreadLocalRandom.current().nextInt(1, 20 + 1);
            double unitprice = ThreadLocalRandom.current().nextDouble(1, 100 + 1);
            Long customerId = ThreadLocalRandom.current().nextLong(1000, 11000 + 1);

            events.add(new Event(id, new Date(System.currentTimeMillis()), quantity, unitprice, customerId,
                    countries.get(rand.nextInt(4)), descriptions.get(rand.nextInt(4))));
        }

        return events;
    }

    public List<String> list2JsonList( List<Event> list ){
        List<String> listString = new ArrayList<String>();
        for (Event event : list) {

            // Creating the ObjectMapper object
            ObjectMapper mapper = new ObjectMapper();
            // Converting the Object to JSONString
            String jsonString = "";
            try {
                jsonString = mapper.writeValueAsString(event);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            listString.add(jsonString);
        }
        return listString;
    }

}