package aibg.logika.Map.Entity;

import aibg.logika.Game.Game;
import aibg.logika.Game.GameParameters;
import aibg.logika.Game.GameTraining;
import aibg.logika.Map.Map;
import aibg.logika.Map.Tile.Tile;
import aibg.logika.dto.DoActionRequestDTO;
import aibg.logika.dto.DoActionResponseDTO;
import aibg.logika.service.GameService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Console;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Semaphore;

import static aibg.logika.Game.GameParameters.ASTEROID_HEALTH;
import static java.lang.Integer.max;
import static java.lang.Math.ceil;

@Getter
@Setter
public class TrainingBot extends Player{
    @JsonIgnore
    private Logger LOG = LoggerFactory.getLogger(GameService.class);

    String type = "BOT";

    //@JsonIgnore
    //private GameService gameService;

    //@JsonIgnore
    //private String id;


    public TrainingBot(Spawnpoint spawnpoint, int playerIdx, Map map, GameService gameService) {
        super(spawnpoint, playerIdx,"Bot" + playerIdx, map);
        //this.gameService = gameService;
        this.playerIdx = playerIdx;

    }

    public void runBot(GameTraining game) { // Odradi se jednom po potezu, zove se od spolja, azurira gameState kada zavrsi

        String someAction = "";
        // formiranje someAction kao akcije koju bot zeli da uradi - ovde AI bota treba da odradi posao


        do{
            Player enemy = game.getPlayer1();
            for(Player pl:game.getPlayers().values()){ //trazi najblizeg
                if (pl.getPlayerIdx() == this.playerIdx)
                    continue;
                if(game.hexDistance(this.q,this.r,enemy.getQ(),enemy.getR())>game.hexDistance(this.q,this.r,pl.getQ(),pl.getR())){
                    enemy = pl;
                }
            }
            if(game.hexDistance(this.q,this.r,enemy.getQ(),enemy.getR())<=3){
                someAction = "attack,"+enemy.getQ()+","+enemy.getR();
            }else someAction = imAStar(map.getTile(enemy.getQ(),enemy.getR()),new HashSet<Entity>(),game);

        }while(game.update(someAction,this.playerIdx)!=null);

        LOG.info(String.valueOf("Training bot " + playerIdx + " did his thing: " + someAction));
        //return dto.getGameState(); ovo treba da vraca, nakon sto se srede bagovi

    }


    private String randomMove(){
        int newQ = this.q + (int)(Math.random() * 3) - 1;
        int newR = this.r + (int)(Math.random() * 3) - 1;
        return "move," + newQ+"," + newR;
    }


    /** A* implementation
     * @param target Element which is searched for
     * @param secondary If any element contained in Set is found, search is ended early and function returns a move towards that element
     * @return Move action towards target or secondary
     */
    private String imAStar(Tile target, Set<Entity> secondary, GameTraining game){
        PriorityQueue<Node> queue = new PriorityQueue<>(11, new NodeComparator());
        Set<Tile> visited = new HashSet<>();
        Node start = new Node(map.getTile(this.q,this.r),0,game.hexDistance(this.q,this.r,target.getQ(),target.getR()),null); // mora null  da se stavi zbog fullQueue
        fillQueue(start,target,visited, queue,secondary, game); /** napuni prioritetni red susedima polja na kome se bot trenutno nalazi */

        while(!queue.isEmpty()){
            Node min= queue.poll();
            while(visited.contains(min)){
                min = queue.poll();
            }
            if(min.heuristics == 1 || min.tile == target || secondary.contains(min.tile) ) //izmeniti mozda, tako da bude
                return min.move;
            fillQueue(min,target,visited, queue,secondary, game);
            //TODO ako nije pronadjeno, ubacim ovde opt sve one ifove za dodavanje suseda u red, ALI kad dodajem u red, q i r menjam u odnosu na q i r iz min, distance postavljam na min.distance+1, a move !samo prenosim identicno!! jer to pocetno move treba da mi se vrati
        }

        return randomMove(); //ako alg ne vrati nista vrati rendom move
    }


    private void fillQueue(Node node,Tile target, Set<Tile> visited, PriorityQueue<Node> queue, Set<Entity> secondary, GameTraining game) { //TODO dodati u uslove da full polja ne mogu da se dodaju u red, pa u elsu proveriti,ako je to full polje target ili neko od sekundarnih return ih
        int q = node.tile.getQ();
        int r = node.tile.getR();
        int dist = node.distance + 1;
        Entity entity = null;
        Tile tile = null;
        if (q+1 + r-1 >= -map.getSize() / 2 && q+1 + r-1 <= map.getSize() / 2 && Math.abs(q+1) <= map.getSize() / 2 && Math.abs(r-1) <= map.getSize() / 2 ) {
            tile = game.getMap().getTile(q+1, r-1);
            if(node.move == null) // ovo je prvo dodavanje
                visitNode(tile,dist,target,queue,visited,game, "move," + Integer.toString(tile.getQ()) + "," + Integer.toString(tile.getR()));
            else visitNode(tile, dist, target, queue, visited, game, node.move); // pvp je propagacija; treba prvo dodavanje da se salje dalje uvek
        }
        if (q-1 + r+1 >= -map.getSize() / 2 && q-1 + r+1 <= map.getSize() / 2 && Math.abs(q-1) <= map.getSize() / 2 && Math.abs(r+1) <= map.getSize() / 2) {
            tile = game.getMap().getTile(q - 1, r + 1);
            if(node.move == null)
                visitNode(tile,dist,target,queue,visited,game, "move," + Integer.toString(tile.getQ()) + "," + Integer.toString(tile.getR()));
            else visitNode(tile, dist, target, queue, visited, game, node.move);
        }
        if (q+1 + r >= -map.getSize() / 2 && q+1 + r <= map.getSize() / 2 && Math.abs(q+1) <= map.getSize() / 2 && Math.abs(r) <= map.getSize() / 2){
            tile = game.getMap().getTile(q+1, r);
            if(node.move == null)
                visitNode(tile,dist,target,queue,visited,game, "move," + Integer.toString(tile.getQ()) + "," + Integer.toString(tile.getR()));
            else visitNode(tile, dist, target, queue, visited, game, node.move);
        }

        if (q-1 + r >= -map.getSize() / 2 && q-1 + r <= map.getSize() / 2 && Math.abs(q-1) <= map.getSize() / 2 && Math.abs(r) <= map.getSize() / 2){
            tile = game.getMap().getTile(q-1, r);
            if(node.move == null)
                visitNode(tile,dist,target,queue,visited,game, "move," + Integer.toString(tile.getQ()) + "," + Integer.toString(tile.getR()));
            else visitNode(tile, dist, target, queue, visited, game, node.move);
        }

        if (q + r+1 >= -map.getSize() / 2 && q + r+1 <= map.getSize() / 2 && Math.abs(q) <= map.getSize() / 2 && Math.abs(r+1) <= map.getSize() / 2){
            tile = game.getMap().getTile(q, r+1);
            if(node.move == null)
                visitNode(tile,dist,target,queue,visited,game, "move," + Integer.toString(tile.getQ()) + "," + Integer.toString(tile.getR()));
            else visitNode(tile, dist, target, queue, visited, game, node.move);
        }

        if (q + r-1 >= -map.getSize() / 2 && q + r-1 <= map.getSize() / 2 && Math.abs(q) <= map.getSize() / 2 && Math.abs(r-1) <= map.getSize() / 2){
            tile = game.getMap().getTile(q, r-1);
            if(node.move == null)
                visitNode(tile,dist,target,queue,visited,game, "move," + Integer.toString(tile.getQ()) + "," + Integer.toString(tile.getR()));
            else visitNode(tile, dist, target, queue, visited, game, node.move);
        }

        //return queue;
    }

    private void visitNode(Tile tile, int dist, Tile target, PriorityQueue<Node> queue, Set<Tile> visited, GameTraining game, String move){
        Entity entity = tile.getEntity();
        Node node = null;

        if (!visited.contains(tile)) {
            if ((entity = tile.getEntity()) instanceof Asteroid) /** Luka pliz proveri jel bi ovo bilo ok */ /** -Math.floorDiv(-((Asteroid)entity).getHealth(), 100) --> heuristika se povecava za gornji ceo deo helta asteroida podeljenih sa 100 (helti asteroida su 265 -> heuristika += 3)  */
                node = new Node(tile, dist + (int) Math.ceil((double) (((Asteroid) entity).getHealth()) / this.getPower()), game.hexDistance(tile.getQ(), tile.getR(), target.getQ(), target.getR()), move);
            else if ( entity instanceof Blackhole) /** ovo mi je kao za full polja da ne radi nista */
                node = new Node(tile, dist + 2, game.hexDistance(tile.getQ(), tile.getR(), target.getQ(), target.getR()), move);
            else if (entity instanceof Wormhole) {
                // TODO
            } else if( entity instanceof Boss ){
                //node = new Node(tile, dist, game.hexDistance(tile.getQ(), tile.getR(), target.getQ(), target.getR()), move); // ovo ako ocu da ide u queue, pa da se search zavrsi kad ga popujem iz queue
                return; // NE MOZE DA NAGAZI
            }else {
                for(Player pl : game.getPlayers().values()){
                    if (pl.getPlayerIdx() == this.playerIdx)
                        continue;
                    if (pl.getQ() == tile.getQ() && pl.getR() == tile.getR())
                        return; // PLAYERA NE MOZE DA NAGAZI
                }
                if (node == null)
                    node = new Node(tile, dist, game.hexDistance(tile.getQ(), tile.getR(), target.getQ(), target.getR()), move);
            }
            if (node != null){
                queue.add(node);
                visited.add(tile);
            }
        }
    }


    class Node{
        Tile tile;
        int distance;
        int heuristics;
        String move;
        Node(Tile tile, int distance, int heuristics, String move){
            this.tile=tile;
            this.distance=distance;
            this.heuristics=heuristics;
            this.move=move;
        }

    }
    class NodeComparator implements Comparator<Node> {

        /**
         * Compares its two arguments for order.  Returns a negative integer,
         * zero, or a positive integer as the first argument is less than, equal
         * to, or greater than the second.<p>
         */
        @Override
        public int compare(Node o1, Node o2) {
            int o1sum = o1.distance + o1.heuristics;
            int o2sum = o2.distance + o2.heuristics;
            return o1sum - o2sum;
        }
    }



}


