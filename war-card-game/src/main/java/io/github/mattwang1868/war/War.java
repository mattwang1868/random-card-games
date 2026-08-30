package io.github.mattwang1868.war;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import io.github.mattwang1868.carddeck.*;

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
            //start gameplay loop
            int turn = 1;
            System.out.println("Starting Game:");
            while (turn <= MAXTURNS && !WarEngine2P.isGameOver(players)) {
                System.out.println("\nTurn " + turn + ":");
                System.out.println("Player 1: "+ players.get(0).size() + " cards\nPlayer 2: " + players.get(1).size() + " cards");
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
                //in the case where max turns are reached
                System.out.println(MAXTURNS + " turns reached");
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
            //deal cards
            List<CardStack> players = WarEngine34P.startGame(numPlayers);
            //start gameplay loop
            int turn = 1;
            System.out.println("Starting Game:");
            while (turn <= MAXTURNS && !WarEngine34P.isGameOver(players)) {
                System.out.println("\nTurn " + turn + ":");
                for (int i = 0; i < numPlayers; i++) {
                    System.out.println("Player " + (i+1) + ": " + players.get(i).size() + " cards");
                }
                System.out.println();
                WarEngine34P.passTurn(players);
                turn++;
            }
            //game ending
            System.out.println("\n\nGame Over");
            //print winner for games where 1 player runs out
            if (WarEngine34P.isGameOver(players)) {
                if (players.get(0).size() > 0) {
                    System.out.println("Player 1 Wins!");
                } else if (players.get(1).size() > 0) {
                    System.out.println("Player 2 Wins!");
                } else if (numPlayers == 3 || players.get(3).size() == 0) {
                    System.out.println("Player 3 Wins!");
                } else {
                    System.out.println("Player 4 Wins!");
                }
            } else {
                //in the case where 3000 turns are reached
                System.out.println(MAXTURNS + " turns reached");
                List<Integer> allSizes = new ArrayList<>();
                allSizes.add(0, players.get(0).size());
                allSizes.add(1, players.get(1).size());
                allSizes.add(2, players.get(2).size());
                if (numPlayers == 4) {
                    allSizes.add(3, players.get(3).size());
                }

                List<Integer> winners = new ArrayList<>();
                int max = -1;
                for (int i = 0; i < numPlayers; i++) {
                    System.out.println("Player " + (i+1) + " had " + allSizes.get(i) + " cards.");
                    int current = allSizes.get(i);
                    if (current > max) {
                        winners.clear();
                        winners.add(i);
                        max = current;
                    } else if (current == max) {
                        winners.add(i);
                    }
                }

                if (winners.size() == 1) {
                    System.out.println("Player " + (winners.get(0) + 1) + " wins due to having the most cards.");
                } else {
                    System.out.print("It's a tie between Players");
                    for (int i = 0; i < winners.size(); i++) {
                        System.out.print(" " + (winners.get(i) + 1));
                        if (winners.size() > 2 && i < winners.size() - 1) {
                            System.out.print(",");
                        }
                        if (i == winners.size() - 2) {
                            System.out.print(" and");
                        }
                    }
                    System.out.println(" (cool)!");
                }
            }
        }
        keyboard.close();
    }

}
    
