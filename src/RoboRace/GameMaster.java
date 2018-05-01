package RoboRace;

import java.util.*;
import java.io.*;

public class GameMaster {

    private int numberPlayers;
    String[] playerNames;
    private Player[] players;
    private Robot[] robots;
    Factory factory;
    private Board board;

    public GameMaster(int numbers, String[] playerNames, Player[] players) {
        this.numberPlayers = numbers;
        this.players = players;
        this.playerNames = playerNames;
        for (int i = 0; i < numberPlayers; i++) {
            players[i] = new Player();
        }
        this.robots = new Robot[numberPlayers];
        for (int i = 0; i < numberPlayers; i++) {
            robots[i] = new Robot(playerNames[i], 2 * i + 1);
        }
        factory = Factory.load("factory.xml");
        board = new Board(factory, numberPlayers, robots);
    }

    public void run() {
        CardList[] cardList = new CardList[numberPlayers];
        EventCounter eventCounter = new EventCounter();
        EventList eventList = new EventList();
        CardList phaseList = new CardList();

        for (int i = 0; i < numberPlayers; i++) {
            players[i].receiveBoard(board);
        }
        boolean hasWinner = false;
        while (!hasWinner) {
            board.revitalize();
            CardFactory cardFactory = new CardFactory();
            for (int i = 0; i < numberPlayers; i++) {
                cardList[i] = new CardList();
                for (int j = 0; j < 7; j++) {
                    cardList[i].add(cardFactory.createCard());
                }
                cardList[i] = players[i].selectCards(cardList[i]);
            }
            int phase = 0;
            while (phase < 5 && !hasWinner) {
                phase++;
                if (eventList.containsVictory()) {
                    break;
                }
                phaseList.clear();
                for (int i = 0; i < numberPlayers; i++) {
                    phaseList.add(cardList[i].get(phase - 1));
                }
                Collections.sort(phaseList);
                for (int i = 0; i < phaseList.size(); i++) {
                    Card card = phaseList.get(i);
                    if (robots[i].isAlive()) {
                        card.execute(eventCounter, eventList, robots[i], board);
                        Location loca = board.getLocation(robots[i].getLocation());
                        loca.effect(eventCounter, eventList, phase, robots[i], board);
                    }
                }
                if (eventList.containsVictory()) {
                    break;
                }
            }
            hasWinner = eventList.containsVictory();
        }
        GameDialogs.showMessageDialog("Winner:", eventList.getWinner());
    }
}
