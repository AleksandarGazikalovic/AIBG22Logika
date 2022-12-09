package aibg.logika;

import aibg.logika.Game.Game;
import aibg.logika.Map.Entity.Player;
import aibg.logika.Map.Entity.Spawnpoint;
import aibg.logika.Map.Map;
import aibg.logika.Map.Tile.Tile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        String MAPS_FOLDER = "./maps";
        URL mapsURL = Test.class.getClassLoader().getResource(MAPS_FOLDER+"FirstRoundTopicAIBGaziBre.txt");
        Map map = new Map(29, mapsURL);
        ArrayList<ArrayList<Tile>> tiles = map.getTiles();
        List<String> username = new ArrayList<>();
        username.add("player1");
        username.add("player2");
        username.add("player3");
        username.add("player4");
        Player player1 = new Player(new Spawnpoint(-7, -7), 1, username.get(0), map);
        Player player2 = new Player(new Spawnpoint(14, -7), 2, username.get(1), map);
        Player player3 = new Player(new Spawnpoint(7, 7), 3, username.get(2), map);
        Player player4 = new Player(new Spawnpoint(-14, 7), 4, username.get(3), map);

        Game game = new Game(map, username);
        game.getPlayers().put(player1.getPlayerIdx(), player1);
        game.getPlayers().put(player2.getPlayerIdx(), player2);
        game.getPlayers().put(player3.getPlayerIdx(), player3);
        game.getPlayers().put(player4.getPlayerIdx(), player4);

        String l = "a";
        String s = "s";
        String d = "d";
        String w = "w";

        int potez = 1;
        int igrac = 1;
        while (true) {
            System.out.println("Potez: " + potez);
            System.out.print("Unesi akciju za igraca sa ID-jem " + game.getPlayers().get(igrac).getPlayerIdx() + ": ");

            Scanner scanner = new Scanner(System.in);
            String action = scanner.next();
            game.update(action, game.getPlayers().get(igrac).getPlayerIdx());
            //printGame(game);
            try {

                if (game.getPlayers().get(igrac).getPlayerIdx() == 1) {
                    printPlayer1(game);
                    igrac++;
                } else if (game.getPlayers().get(igrac).getPlayerIdx() == 2) {
                    printPlayer2(game);
                    igrac++;
                } else if (game.getPlayers().get(igrac).getPlayerIdx() == 3) {
                    printPlayer3(game);
                    igrac++;
                } else if (game.getPlayers().get(igrac).getPlayerIdx() == 4) {
                    printPlayer4(game);
                    igrac=1;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            potez++;
            System.out.println("-------------------------------------------------------------------");
        }
    }

    public static void printGame(Game game) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(game));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printPlayers(Game game) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String gameString = mapper.writeValueAsString(game);
        JsonNode gameNode = mapper.readTree(gameString);

        JsonNode p1Node = gameNode.get("player1");
        String p1String = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(p1Node);
        JsonNode p2Node = gameNode.get("player2");
        String p2String = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(p2Node);
        JsonNode p3Node = gameNode.get("player3");
        String p3String = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(p3Node);
        JsonNode p4Node = gameNode.get("player4");
        String p4String = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(p4Node);

        System.out.println(p1String);
        System.out.println(p2String);
    }

    public static void printPlayer1(Game game) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String gameString = mapper.writeValueAsString(game);
        JsonNode gameNode = mapper.readTree(gameString);

        JsonNode p1Node = gameNode.get("player1");
        String p1String = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(p1Node);

        System.out.println(p1String);
    }

    public static void printPlayer2(Game game) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String gameString = mapper.writeValueAsString(game);
        JsonNode gameNode = mapper.readTree(gameString);

        JsonNode p2Node = gameNode.get("player2");
        String p2String = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(p2Node);

        System.out.println(p2String);
    }

    public static void printPlayer3(Game game) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String gameString = mapper.writeValueAsString(game);
        JsonNode gameNode = mapper.readTree(gameString);

        JsonNode p3Node = gameNode.get("player3");
        String p3String = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(p3Node);

        System.out.println(p3String);
    }

    public static void printPlayer4(Game game) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String gameString = mapper.writeValueAsString(game);
        JsonNode gameNode = mapper.readTree(gameString);

        JsonNode p4Node = gameNode.get("player4");
        String p4String = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(p4Node);

        System.out.println(p4String);
    }
}
