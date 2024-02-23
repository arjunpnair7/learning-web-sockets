package com.example.demo;

import com.example.demo.Model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class ChatController {

    boolean initialFiltering = true;


    private String demoData = "{\n" +
            "  \"businesses\": [\n" +
            "    {\n" +
            "      \"id\": \"zi7BwwdL6bPzIy6LeUNKbg\",\n" +
            "      \"alias\": \"gumii-naperville\",\n" +
            "      \"name\": \"Gumii\",\n" +
            "      \"image_url\": \"https://s3-media1.fl.yelpcdn.com/bphoto/nZ8_fZJeUmBTpIGxsM0vig/o.jpg\",\n" +
            "      \"is_closed\": false,\n" +
            "      \"url\": \"https://www.yelp.com/biz/gumii-naperville?adjust_creative=y5_45BgO8_k3C60DB3gTkg&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=y5_45BgO8_k3C60DB3gTkg\",\n" +
            "      \"review_count\": 236,\n" +
            "      \"categories\": [\n" +
            "        {\n" +
            "          \"alias\": \"korean\",\n" +
            "          \"title\": \"Korean\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"rating\": 4.8,\n" +
            "      \"coordinates\": {\n" +
            "        \"latitude\": 41.706892690299135,\n" +
            "        \"longitude\": -88.20405446442004\n" +
            "      },\n" +
            "      \"transactions\": [\n" +
            "        \"delivery\"\n" +
            "      ],\n" +
            "      \"price\": \"$$\",\n" +
            "      \"location\": {\n" +
            "        \"address1\": \"3124 S Route 59\",\n" +
            "        \"address2\": \"Ste 140\",\n" +
            "        \"address3\": \"\",\n" +
            "        \"city\": \"Naperville\",\n" +
            "        \"zip_code\": \"60564\",\n" +
            "        \"country\": \"US\",\n" +
            "        \"state\": \"IL\",\n" +
            "        \"display_address\": [\n" +
            "          \"3124 S Route 59\",\n" +
            "          \"Ste 140\",\n" +
            "          \"Naperville, IL 60564\"\n" +
            "        ]\n" +
            "      },\n" +
            "      \"phone\": \"+13312298785\",\n" +
            "      \"display_phone\": \"(331) 229-8785\",\n" +
            "      \"distance\": 3629.128002738791\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"fwV7GlH_BZWEd5Ed8DY7kw\",\n" +
            "      \"alias\": \"backroads-burger-and-bar-plainfield\",\n" +
            "      \"name\": \"Backroads Burger & Bar\",\n" +
            "      \"image_url\": \"https://s3-media1.fl.yelpcdn.com/bphoto/8oeq5XwHijJw7CK1Uq0ONg/o.jpg\",\n" +
            "      \"is_closed\": false,\n" +
            "      \"url\": \"https://www.yelp.com/biz/backroads-burger-and-bar-plainfield?adjust_creative=y5_45BgO8_k3C60DB3gTkg&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=y5_45BgO8_k3C60DB3gTkg\",\n" +
            "      \"review_count\": 279,\n" +
            "      \"categories\": [\n" +
            "        {\n" +
            "          \"alias\": \"burgers\",\n" +
            "          \"title\": \"Burgers\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"alias\": \"tradamerican\",\n" +
            "          \"title\": \"American\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"alias\": \"sportsbars\",\n" +
            "          \"title\": \"Sports Bars\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"rating\": 4.5,\n" +
            "      \"coordinates\": {\n" +
            "        \"latitude\": 41.63279511587637,\n" +
            "        \"longitude\": -88.22162325982413\n" +
            "      },\n" +
            "      \"transactions\": [\n" +
            "        \"delivery\"\n" +
            "      ],\n" +
            "      \"price\": \"$$\",\n" +
            "      \"location\": {\n" +
            "        \"address1\": \"13717 Us-30\",\n" +
            "        \"address2\": \"\",\n" +
            "        \"address3\": \"\",\n" +
            "        \"city\": \"Plainfield\",\n" +
            "        \"zip_code\": \"60544\",\n" +
            "        \"country\": \"US\",\n" +
            "        \"state\": \"IL\",\n" +
            "        \"display_address\": [\n" +
            "          \"13717 Us-30\",\n" +
            "          \"Plainfield, IL 60544\"\n" +
            "        ]\n" +
            "      },\n" +
            "      \"phone\": \"+18157336054\",\n" +
            "      \"display_phone\": \"(815) 733-6054\",\n" +
            "      \"distance\": 5098.110487603798\n" +
            "    }\n" +
            "  ],\n" +
            "  \"total\": 554,\n" +
            "  \"region\": {\n" +
            "    \"center\": {\n" +
            "      \"longitude\": -88.2257080078125,\n" +
            "      \"latitude\": 41.67854188629129\n" +
            "    }\n" +
            "  }\n" +
            "}";

    List<String> joinedUsers = new ArrayList<>();
    List<String> filteredPokemonList = new ArrayList<>();
    Set<String> votedUsers = new HashSet<>();
    private PokemonProxy pokemonProxy;

    public ChatController(PokemonProxy pokemonProxy) {
        this.pokemonProxy = pokemonProxy;
    }

    @MessageMapping("/room/{ident}/add")
    @SendTo("/room/{ident}")
    public Message addUser(@DestinationVariable String ident, @Payload String username) throws JsonProcessingException {
        System.out.println("LINE 26");
        System.out.println("PAYLOAD: " + username);
        joinedUsers.add(username);
        System.out.println(joinedUsers);

        ObjectMapper objectMapper1 = new ObjectMapper();
        RestaurantList restaurantList = objectMapper1.readValue(demoData, RestaurantList.class);
        List<Restaurant> restaurantList1 = restaurantList.getBusinesses();
        for (Restaurant curr: restaurantList1) {
            System.out.println(curr);
        }

        return new Message(MessageType.ADD_USER, username, joinedUsers);
    }

    @MessageMapping("/room/{ident}/donevoting")
    @SendTo("/room/{ident}")
    public Message doneVoting(@Payload String username) {
        votedUsers.add(username);
        System.out.println("VOTED USERS SIZE: " + votedUsers.size());
        System.out.println(("TOTAL USERS SIZE: " + joinedUsers.size()));
        if (votedUsers.size() == joinedUsers.size()) {
            List<String> filteredListNoDuplicates = new ArrayList<>();
            for (String curr: filteredPokemonList) {
                if (!filteredListNoDuplicates.contains(curr)) {
                    filteredListNoDuplicates.add(curr);
                }
            }
            initialFiltering = false;
            return new Message(MessageType.DONE_VOTING, "Nothing", filteredListNoDuplicates);
        }
        return new Message(MessageType.VOTE, "Nothing", joinedUsers);
    }

    @MessageMapping("/room/{ident}/vote")
    @SendTo("/room/{ident}")
    public Message addVote(@DestinationVariable String ident, @Payload String usernameAndPokemon) {
//        filteredPokemonList.add(pokemonName);
        System.out.println("FILTERED LIST: " + usernameAndPokemon);

        String[] parts = usernameAndPokemon.split(":");
        votedUsers.add(parts[0]);
//        if (!filteredPokemonList.contains(parts[1])) {
            filteredPokemonList.add(parts[1]);
//        }


        return new Message(MessageType.VOTE, "Nothing", joinedUsers);
    }

    @MessageMapping("/room/{ident}/start")
    @SendTo("/room/{ident}")
    public Message startVoting(@Payload String tmp) throws JsonProcessingException {
        System.out.println("LINE 35");

        if (!initialFiltering) {
            List<String> tempCopy = new ArrayList<>();
            for (String curr: filteredPokemonList) {
                tempCopy.add(curr);
            }
            filteredPokemonList.clear();
            votedUsers.clear();
            return new Message(MessageType.DATA, "abc", tempCopy);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        PokemonListResponse pokemonListResponse = objectMapper.readValue(pokemonProxy.getPokemonList(), PokemonListResponse.class);

        // Get the list of Pokemon objects
        List<Pokemon> pokemonList = pokemonListResponse.getResults();
        List<String> pokemonNames = new ArrayList<>();
        // Log the list of Pokemon objects to the console
        for (Pokemon pokemon : pokemonList) {
            System.out.println(pokemon.getName());
            pokemonNames.add(pokemon.getName());
        }

//        System.out.println(tmp);
//        String pokemonList = pokemonProxy.getPokemonList();
//        System.out.println("LINE 37: " + pokemonList);
        return new Message(MessageType.DATA, "abc", pokemonNames);
    }

}
