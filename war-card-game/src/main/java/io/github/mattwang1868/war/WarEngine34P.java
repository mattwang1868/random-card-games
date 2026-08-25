package io.github.mattwang1868.war;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import io.github.mattwang1868.carddeck.*;
public class WarEngine34P {

    /**
     * Draws each side's cards, determines who wins, then gives the prize pool
     * to the winner
     * @param state
     *              the current players left and their stack of cards
    */
    public static void passTurn(List<CardStack> state) {
        List<Card> played = drawCards(state);
        int winner = determineWinner(state, played);
        allocateCards(state, played, winner);
    }

    /**
     * Draws one card per player and returns a list of cards ordered by
     * player number
     * @param state the current players left and their stack of cards
     * @return      the top card in each of the players' stack of cards
     */
    public static List<Card> drawCards(List<CardStack> state) {
        int numPlayers = state.size();
        List<Card> played = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            played.add(i, state.get(i).removeTop());
        }
        return played;
    }

    /**
     * 
     * @param state
     * @param playersDrawing
     * @return
     */
    public static List<Card> drawCards(List<CardStack> state, List<Integer> playersDrawing) {
        int numPlayers = state.size();
        List<Card> played = new ArrayList<>();
        for (int i : playersDrawing) {
            played.add(i, state.get(i).removeTop());
        }
        return played;
    }

    /**
     * 
     * @param state
     * @param played
     * @return
     */
    public static int determineWinner(List<CardStack> state, List<Card> played) {
        int numPlayers = played.size();
        int max = played.get(0).getCardValue().baseValue();
        if (max == 1) {
            max += 13;
        }
        List<Integer> maxLocations = new ArrayList<>();
        maxLocations.add(0);
        for (int i = 1; i < numPlayers; i++) {
            int value = played.get(i).getCardValue().baseValue();
            if (value == 1) {
                value += 13;
            }
            if (value == max) {
                maxLocations.add(i);
            } else if (value > max) {
                max = value;
                maxLocations.clear();
                maxLocations.add(i);
            }

        }
        if (maxLocations.size() == 1) {
            return maxLocations.get(0);
        } else if (maxLocations.size() > 1) {
            //TODO: if a side just played their last card they should lose in a war
            return goToWar(state, played, maxLocations);
        } else {
            throw new IllegalStateException("Winner not found");
        }
    }

    public static int goToWar(List<CardStack> state, List<Card> played, List<Integer> players) {
        for (int p : players) {
            CardStack currentPlayer = state.get(p);
            int counter = 0;
            while (counter < 3 && currentPlayer.size() > 1) {
                played.add(currentPlayer.removeTop());
                counter++;
            }
        
        }
        List<Card> nextRound = drawCards(state, players);
        int winner = determineWinner(state, nextRound);
        played.addAll(nextRound);
        return winner;
    }

    public static void allocateCards(List<CardStack> state, List<Card> prize, int winner) {
        //TODO: implement so guys get their prizes
    }
    
    public static boolean isGameOver(List<CardStack> state) {
        //TODO: implement game ending and also make it so players lose
        return false;
    }
    public static List<CardStack> startGame(int numPlayers) {
        List<CardStack> players = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            players.add(new CardStack());
        }
        Deck starting = new Deck();
        starting.shuffle();
        int currentPlayer = 0;
        while (starting.size() > 0) {
            currentPlayer %= numPlayers;
            players.get(currentPlayer).addCard(starting.draw());
            currentPlayer++;
        }
        return players;
    }
}
    
