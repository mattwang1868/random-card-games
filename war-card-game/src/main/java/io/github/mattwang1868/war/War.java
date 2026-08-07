package io.github.mattwang1868.war;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import io.github.mattwang1868.carddeck.*;
public class War {
    public static void main(String[] args) {
        //find the number of players
        Scanner keyboard = new Scanner(System.in);
        int numPlayers;
        do {
        System.out.print("Enter number of players (2-4): ");
        numPlayers = keyboard.nextInt();
        } while (numPlayers < 2 || numPlayers > 4);

        //start the game
        List<CardStack> players = WarEngine.startGame(numPlayers);
        for (CardStack k : players) {
            System.out.println("\nNEW PLAYER\n");
            for (Card s : k.getCards()) {
                System.out.println(s);
            }
        }
    }

}
    
