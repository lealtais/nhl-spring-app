package com.nhl.nhl_spring_app.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhl.nhl_spring_app.model.NhlPlayer;
import com.nhl.nhl_spring_app.model.NhlTeam;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class NhlPlayersService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;

    public NhlPlayersService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<NhlTeam> getTeams() {
        return Arrays.asList(
            new NhlTeam("ANA", "Anaheim Ducks"),
            new NhlTeam("BOS", "Boston Bruins"),
            new NhlTeam("BUF", "Buffalo Sabres"),
            new NhlTeam("CAR", "Carolina Hurricanes"),
            new NhlTeam("CBJ", "Columbus Blue Jackets"),
            new NhlTeam("CGY", "Calgary Flames"),
            new NhlTeam("CHI", "Chicago Blackhawks"),
            new NhlTeam("COL", "Colorado Avalanche"),
            new NhlTeam("DAL", "Dallas Stars"),
            new NhlTeam("DET", "Detroit Red Wings"),
            new NhlTeam("EDM", "Edmonton Oilers"),
            new NhlTeam("FLA", "Florida Panthers"),
            new NhlTeam("LAK", "Los Angeles Kings"),
            new NhlTeam("MIN", "Minnesota Wild"),
            new NhlTeam("MTL", "Montreal Canadiens"),
            new NhlTeam("NJD", "New Jersey Devils"),
            new NhlTeam("NSH", "Nashville Predators"),
            new NhlTeam("NYI", "New York Islanders"),
            new NhlTeam("NYR", "New York Rangers"),
            new NhlTeam("OTT", "Ottawa Senators"),
            new NhlTeam("PHI", "Philadelphia Flyers"),
            new NhlTeam("PIT", "Pittsburgh Penguins"),
            new NhlTeam("SEA", "Seattle Kraken"),
            new NhlTeam("SJS", "San Jose Sharks"),
            new NhlTeam("STL", "St. Louis Blues"),
            new NhlTeam("TBL", "Tampa Bay Lightning"),
            new NhlTeam("TOR", "Toronto Maple Leafs"),
            new NhlTeam("UTA", "Utah Hockey Club"),
            new NhlTeam("VAN", "Vancouver Canucks"),
            new NhlTeam("VGK", "Vegas Golden Knights"),
            new NhlTeam("WPG", "Winnipeg Jets"),
            new NhlTeam("WSH", "Washington Capitals")
        );
    }

    public List<NhlPlayer> getPlayersByTeam(String teamAbbrev) {
        String url = "https://api-web.nhle.com/v1/roster/" + teamAbbrev + "/current";
        List<NhlPlayer> players = new ArrayList<>();

        try {
            String json = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(json);

            // A API separa por posições, então iteramos sobre as 3 listas
            addPlayersFromJsonArray(root.get("forwards"), teamAbbrev, players);
            addPlayersFromJsonArray(root.get("defensemen"), teamAbbrev, players);
            addPlayersFromJsonArray(root.get("goalies"), teamAbbrev, players);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return players;
    }

    private void addPlayersFromJsonArray(JsonNode arrayNode, String teamAbbrev, List<NhlPlayer> list) {
        if (arrayNode == null || !arrayNode.isArray()) return;

        for (JsonNode node : arrayNode) {
            NhlPlayer p = new NhlPlayer();
            p.setId(node.get("id").asLong());
            
            String firstName = node.has("firstName") && node.get("firstName").has("default") 
                    ? node.get("firstName").get("default").asText() : "";
            String lastName = node.has("lastName") && node.get("lastName").has("default") 
                    ? node.get("lastName").get("default").asText() : "";
                    
            p.setName(firstName + " " + lastName);
            p.setTeam(teamAbbrev);
            p.setNumber(node.has("sweaterNumber") ? node.get("sweaterNumber").asInt() : 0);
            p.setPosition(node.has("positionCode") ? node.get("positionCode").asText() : "N/A");
            p.setNationality(node.has("birthCountry") ? node.get("birthCountry").asText() : "N/A");
            p.setImage(node.has("headshot") ? node.get("headshot").asText() : "");
            
            if (node.has("heightInCentimeters")) {
                p.setHeight(node.get("heightInCentimeters").asDouble());
            }
            if (node.has("weightInKilograms")) {
                p.setWeight(node.get("weightInKilograms").asDouble());
            }
            if (node.has("birthDate")) {
                p.setBirthDate(node.get("birthDate").asText());
            }

            list.add(p);
        }
    }
}
