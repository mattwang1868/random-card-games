package io.github.mattwang1868.war;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import io.github.mattwang1868.carddeck.*;

/**
 * Class that simulates mechanics in the card game War with 3 or 4 players.
 */
public class WarEngine34P {

    final private static Scanner INPUT = new Scanner(System.in);
    /**
     * Draws each side's cards, determines who wins, then gives the prize pool
     * to the winner
     * @param state
     *              the current players left and their stack of cards
    */
    public static void passTurn(List<CardStack> state) {
        waitForInput();
        //track which players are left at the beginning of the turn
        List<Integer> remaining = new ArrayList<>();
        for (int i = 0; i < state.size(); i++) {
            if (state.get(i).size() > 0) {
                remaining.add(i);
            }
        }
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
        //check if any players were eliminated this turn
        for (int i = 0; i < state.size(); i++) {
            if (state.get(i).size() == 0 && remaining.contains(i)) {
                System.out.println("Player " + (i+1) + " is out of cards.");
            }
        }
    }

    /**
     * Prints the cards last played formatted for each player.
     * An eliminated player or player who didn't play a card is represented
     * by null and will print whitespace. |played| must be the number of players total
     * @param played
     *                  the cards played that round to print
     */
    public static void printNext(List<Card> played) {
        for (int i = 0; i < played.size(); i++) {
            if (played.get(i) != null) {
                System.out.print(played.get(i).getCardValue().toString());
            }
            System.out.print("\t\t");
        }
    }

    /**
     * Waits for the user to hit the return key.
     */
    public static void waitForInput() {
        INPUT.nextLine();
        //System.out.println();
    }
    /**
     * Draws one card per player and returns a list of cards ordered by
     * player number
     * @param state the current players left and their stack of cards
     * @return      the top card in each of the players' stack of cards
     */
    public static List<Card> drawCards(List<CardStack> state) {
        List<Card> played = new ArrayList<>();
        for (int i = 0; i < state.size(); i++) {
            if (state.get(i).size() > 0) {
                played.add(i, state.get(i).removeTop());
            } else {
                //add in null to show the player is eliminated
                played.add(i, null);
            }
        }
        return played;
    }

    /**
     * Draws one card per player in the List playersDrawing. To be used in a War in which not everybody is participating.
     * Requires that every player in playersDrawing has at least 1 card remaining.
     * @param state             The current cards held by all players
     * @param playersDrawing    The players drawing a card
     * @return  A list of cards containing the top card in each of the players' stack of cards for each player
     *          in playersDrawing
     */
    public static List<Card> drawCards(List<CardStack> state, List<Integer> playersDrawing) {
        List<Card> played = new ArrayList<>();
        for (int i = 0; i < state.size(); i++) {
            if (playersDrawing.contains(i)) {
                played.add(i, state.get(i).removeTop());
            } else {
                //add in null to show eliminated or irrelevant player
                played.add(i, null);
            }
        }
        return played;
    }

    /**
     * Given the cards played this turn, determines which player wins.
     * 
     * @param state     the current card stacks of both players
     * @param played    the cards played this round (must be length 2)
     * @return          0 if player 1 wins, 1 if player 2 wins, etc., depending on the cards in played in state
     *                  following the rules of War
     */
    public static int determineWinner(List<CardStack> state, List<Card> played) {
        int max = 0;
        List<Integer> maxLocations = new ArrayList<>();
        //find the player(s) with the largest card
        for (int i = 0; i < state.size(); i++) {
            if (played.get(i) != null) {
                //add 13 to get the right ace value
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

        }
        //in the event of a war, check if players are able to do war
        if (maxLocations.size() > 1) {
            for (int i = 0; i < state.size(); i++) {
                //players with no cards left lose war by default
                if (maxLocations.contains(i) && state.get(i).size() == 0) {
                    maxLocations.remove(maxLocations.indexOf(i));
                    System.out.println("Player " + (i+1) + " doesn't have enough cards left for war.");
                }
            }
        }
        if (maxLocations.size() == 1) {
            return maxLocations.get(0);
        } else if (maxLocations.size() > 1) {
            return goToWar(state, played, maxLocations);
        } else {
            //in the rare case that all players can't do a war, the winner would be the largest of the non-warring cards
            System.out.println("All tied players don't have enough cards for war.");
            List <Card> remaining = new ArrayList<>();
            for (int i = 0; i < state.size(); i++) {
                int value = played.get(i).getCardValue().baseValue();
                if (value == 1) {
                    value += 13;
                }
                if (value == max) {
                    remaining.add(i, null);
                } else {
                    remaining.add(i, played.get(i));
                }
            }
            for (Card x : remaining) {
                System.out.println(x);
            }
            return determineWinner(state, remaining);
        }
    }

    /**
     * draws max(3, |remaining cards| - 1) cards per player then draws 1 card per player in players.
     * The final card drawn for the players are then compared and the winner is returned.
     * @param state  current card stacks of both players
     * @param played all cards played so far in the round
     * @param players list containing all players participating
     * @return 0 if player 1 wins the war, 1 if player 2 wins, etc..
     */
    public static int goToWar(List<CardStack> state, List<Card> played, List<Integer> players) {
        System.out.println("War!");
        List<CardStack> relevantPlayers = new ArrayList<>();
        for (int i : players) {
            relevantPlayers.add(state.get(i));
        }
        for (int i = 0; i < 3; i++) {
            waitForInput();
            for (int j = 0; j < state.size(); j++) {
                if (players.contains(j) && state.get(j).size() > 1) {
                    Card temp = state.get(j).removeTop();
                    //System.out.print(temp.getCardValue().toString() + "\t\t");
                    System.out.print("X\t\t");
                    played.add(temp);
                } else {
                    System.out.print("\t\t");
                }
            }
        }
        waitForInput();
        System.out.println();
        //draw the next round of cards
        List<Card> nextRound = drawCards(state, players);
        printNext(nextRound);
        waitForInput();
        //determine the winner
        int winner = determineWinner(state, nextRound);
        //add the cards to the total prize pool
        played.addAll(nextRound);
        return winner;
    }

    /**
     * Gives all cards in prize to state[winner]. Ignores null entries in prize.
     * @param state     The current stack of cards of all players.
     * @param prize     the cards to allocate. May contain null, which will not be allocated.
     * @param winner    the player to give cards to. 0 for player 1, 1 for player 2, etc.
     */
    public static void allocateCards(List<CardStack> state, List<Card> prize, int winner) {
        for (int i = 0; i < prize.size(); i++) {
            //ignore null values in prize
            if (prize.get(i) == null) {
                prize.remove(i);
                i--;
            }
        }
        //print out the winnings
        //reveal face down war cards if applicable
        System.out.print("Player " + (winner+1) + " gets: ");
        for (int i = 0; i < prize.size(); i++) {
            if (prize.get(i) != null) {
                state.get(winner).addCard(prize.get(i));
                System.out.print(prize.get(i).getCardValue().toString());
                if (i != prize.size() - 1) {
                    System.out.print(", ");
                }
            }
        }
        System.out.println();
        waitForInput();    
    }
    
    /**
     * returns if all but one side is out of cards.
     * @param state     the card stacks of all players
     * @return      true if only one entry in state has length >0, false otherwise
     */
    public static boolean isGameOver(List<CardStack> state) {
        int playersRemaining = 0;
        for (int i = 0; i < state.size(); i++) {
            if (state.get(i).size() > 0) {
                playersRemaining++;
                if (playersRemaining > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Deals 1/numPlayers of a deck each to numPlayers players. Returns the stack the players hold in a list.
     * If 52 % numPlayers != 0, extra cards will be dealt in player order.
     * @return A list of numPlayers card stacks each representing a player
     */
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
    
