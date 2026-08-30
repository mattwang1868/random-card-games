package io.github.mattwang1868.war;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import io.github.mattwang1868.carddeck.*;

/**
 * Class that simulates mechanics in the card game War with 2 players.
 */
public class WarEngine2P {

    final private static int NUMPLAYERS = 2;
    final private static Scanner INPUT = new Scanner(System.in);
    /**
     * Draws each side's cards, determines who wins, then gives the prize pool
     * to the winner.
     * @param state
     *              the current players left and their stack of cards
    */
    public static void passTurn(List<CardStack> state) {
        waitForInput();
        //draw 1 card per side and print it
        List<Card> played = drawCards(state);
        printNext(played);
        waitForInput();
        //find the winner of the round
        int winner = determineWinner(state, played);
        //print winner
        System.out.println("Player " + (winner + 1) + " wins the round");
        //give winner all cards
        allocateCards(state, played, winner);
    }

    /**
     * Prints the 2 cards in played. Played must have exactly 2 cards.
     * @param played
     *                  the cards played that round
     */
    public static void printNext(List<Card> played) {
        System.out.println(played.get(0).getCardValue().toString() + "\t\t" + played.get(1).getCardValue().toString());
    }

    /**
     * Waits for the user to hit the return key.
     */
    public static void waitForInput() {
        INPUT.nextLine();
        //System.out.println();
    }

    /**
     * Draws one card per player and returns a list of two cards ordered by
     * player number.
     * @param state the current players and their stack of cards
     * @return      the top card in each of the players' stack of cards
     */
    public static List<Card> drawCards(List<CardStack> state) {
        List<Card> played = new ArrayList<>();
        for (int i = 0; i < NUMPLAYERS; i++) {
            played.add(i, state.get(i).removeTop());
        }
        return played;
    }

    /**
     * Given the cards played this turn, determines which side wins.
     * 
     * @param state  the current card stacks of both players
     * @param played    the cards played this round (must be length 2)
     * @return      0 if player 1 wins, 1 if player 2 wins
     */
    public static int determineWinner(List<CardStack> state, List<Card> played) {
        //account for aces not being worth 1
        int p1 = played.get(0).getCardValue().baseValue();
        if (p1 == 1) {
            p1 += 13;
        }

        int p2 = played.get(1).getCardValue().baseValue();
        if (p2 == 1) {
            p2 += 13;
        }
        //determine which player has a higher card value
        if (p1 > p2) {
            return 0;
        } else if (p2 > p1) {
            return 1;
        }
        else {
            //if either player plays the last card, war is impossible so the other wins by default
            if (state.get(0).size() < 1) {
                System.out.println("Player 1 doesn't have enough cards for war");
                return 1;
            } else if (state.get(1).size() < 1) {
                System.out.println("Player 2 doesn't have enough cards for war");
                return 0;
            } else {
                //go to war
                return goToWar(state, played);
            }
        }
    }

    /**
     * draws max(3, |remaining cards| - 1) cards per player then draws 1 card per player.
     * The final card drawn for both players are then compared and the winner is returned.
     * @param state  current card stacks of both players
     * @param played all cards played so far in the round
     * @return 0 if player 1 wins the war, 1 if player 2 wins
     */
    public static int goToWar(List<CardStack> state, List<Card> played) {
        System.out.println("War!");
        CardStack p1 = state.get(0);
        CardStack p2 = state.get(1);
        Card p1Card, p2Card;
        //draw 3 cards or until only 1 card is left for each side
        for (int i = 0; i < 3; i++) {
            waitForInput();
            if (p1.size() > 1) {
                p1Card = p1.removeTop();
                played.add(p1Card);
            } else {
                p1Card = null;
            }
            if (p2.size() > 1) {
                p2Card = p2.removeTop();
                played.add(p2Card);            
            } else {
                p2Card = null;
            }
            if (p1Card != null) {
                //System.out.print(p1Card.getCardValue().toString() + "\t");
                System.out.print("X\t");
            } else {
                System.out.print("\t");
            }
            System.out.print("\t");
            if (p2Card != null) {
                //System.out.println(p2Card.getCardValue().toString() + "\t");
                System.out.print("X\t");
            }
        }
        waitForInput();
        System.out.println();
        //draw the next round of cards
        List<Card> nextRound = drawCards(state);
        printNext(nextRound);
        waitForInput();
        //determine the winner
        int winner = determineWinner(state, nextRound);
        //add the cards to the total prize pool
        played.addAll(nextRound);
        return winner;
    }

    /**
     * Appends all cards in prize to state[winner].
     * @param state     the card stacks of both players
     * @param prize     the cards to be added to the winning side
     * @param winner    the player that won (0 for player 1, 1 for player 2)
     */
    public static void allocateCards(List<CardStack> state, List<Card> prize, int winner) {
        //print out the winnings
        //reveal face down war cards if applicable
        System.out.print("Player " + (winner+1) + " gets: ");
        for (int i = 0; i < prize.size(); i++) {
            state.get(winner).addCard(prize.get(i));
            System.out.print(prize.get(i).getCardValue().toString());
            if (i != prize.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();
        waitForInput();
    }
    
    /**
     * returns if one side is out of cards.
     * @param state     the card stacks of the two players
     * @return      true if |state[0]| = 0 or |state[1]| = 0, false otherwise
     */
    public static boolean isGameOver(List<CardStack> state) {
        //check if either side is out of cards
        if (state.get(0).size() == 0 || state.get(1).size() == 0) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Deals half of a deck each to 2 players. Returns the stack the two players hold in a list.
     * @return A list of 2 card stacks each representing a player
     */
    public static List<CardStack> startGame() {
        //create player card stacks
        List<CardStack> players = new ArrayList<>();
        for (int i = 0; i < NUMPLAYERS; i++) {
            players.add(new CardStack());
        }
        //create a deck and shuffle
        Deck starting = new Deck();
        starting.shuffle();
        //deal cards one at a time alternating between players
        int currentPlayer = 0;
        while (starting.size() > 0) {
            currentPlayer %= NUMPLAYERS;
            players.get(currentPlayer).addCard(starting.draw());
            currentPlayer++;
        }
        return players;
    }
}
    
