package io.github.mattwang1868.war;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import io.github.mattwang1868.carddeck.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
/**
 * Class that runs a game of War in the terminal with 2-4 players.
 */
public class War {
    //prevent infinite games
    private static final int MAXTURNS = 3000;
    public static void main(String[] args) {
        //find the number of players
        Scanner keyboard = new Scanner(System.in);
        int numPlayers;
        do {
        System.out.print("Enter number of players (2-4): ");
        numPlayers = keyboard.nextInt();
        } while (numPlayers < 2 || numPlayers > 4);
        
        //start the game
        //2 player option
        if (numPlayers == 2) {
            //deal cards
            List<CardStack> players = WarEngine2P.startGame();
            //print players' deck to a test file for testing
            try {
                BufferedWriter file = new BufferedWriter(new FileWriter("test.txt"));
                for (CardStack k : players) {
                    file.append("\nNEW PLAYER\n");
                    for (Card s : k.getCards()) {
                        file.append("\n" +s.getCardValue().toString());
                    }
                }
                file.close();
            } catch (IOException e) {
                System.err.println("Unable to write to file");
            }
            //start gameplay loop
            int turn = 1;
            System.out.println("Starting Game:");
            while (turn <= 3000 && !WarEngine2P.isGameOver(players)) {
                System.out.print("\nTurn " + turn + ":");
                WarEngine2P.passTurn(players);
                turn++;
            }
            //game ending
            System.out.println("\n\nGame Over");
            //print winner for games where 1 player runs out
            if (WarEngine2P.isGameOver(players)) {
                if (players.get(0).size() == 0) {
                    System.out.println("Player 2 Wins!");
                } else {
                    System.out.println("Player 1 Wins!");
                }
            } else {
                //in the case where 3000 turns are reached
                int winner;
                System.out.println("3000 turns reached");
                int p1Size = players.get(0).size();
                int p2Size = players.get(1).size();
                if (p1Size > p2Size) {
                    System.out.println("Player 1 wins due to having more cards (" + p1Size + ").");
                } else if (p1Size < p2Size) {
                    System.out.println("Player 2 wins due to having more cards (" + p2Size + ").");
                } else {
                    System.out.println("Both players have " + p1Size + " cards, so it's a Tie (cool)!");
                }
            }
        } else {
            //3 or 4 player option
            List<CardStack> players = WarEngine34P.startGame(numPlayers);
            for (CardStack k : players) {
                System.out.println("\nNEW PLAYER\n");
                for (Card s : k.getCards()) {
                    System.out.println(s);
                }
            }
        }
    }

}
    
