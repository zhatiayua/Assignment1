package RoboRace;

import java.util.*;
import java.io.*;

public class GameMaster implements Runnable {

    int playerNumber;
    Robot[] robots;
    private Board board;
    private CardFactory cardFactory;
    private Player[] players;

    public GameMaster(int playerNumber, String[] names, Player[] players) {
        this.players = players;
        this.playerNumber = playerNumber;
        robots = new Robot[playerNumber];
        for (int i = 0; i < playerNumber; i++) {
            robots[i] = new Robot(names[i], 2 * i + 1);
        }
        Factory factory = Factory.load("factory.xml");
        board = new Board(factory, playerNumber, robots);
        cardFactory = new CardFactory();
    }

    public void run() {
        for (Player player : players) {
            player.receiveBoard(board);
        }
        CardList[] cardLists = new CardList[playerNumber];
        EventCounter eventCounter = new EventCounter();
        EventList eventList = new EventList();
    }

}
