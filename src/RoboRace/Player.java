package RoboRace;

import COSC3P40.midi.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;

public class Player extends JFrame {

    private String name;
    private Board board;
    private BoardCanvas boardCanvas;
    private CardPane cardPane;
    

    public void receiveBoard(Board board) {
        this.board = board;
        boardCanvas = new BoardCanvas(board);
        cardPane = new CardPane();
        getContentPane().add("North", boardCanvas);
        getContentPane().add("South", cardPane);
        pack();
        setResizable(false);
        setVisible(true);
        boardCanvas.start();      
    }
    
    public void receiveEvents(EventList events){};
    
    public CardList selectCards(CardList cards){
        return cardPane.selectCards(cards);
    }
    
    
    
}
