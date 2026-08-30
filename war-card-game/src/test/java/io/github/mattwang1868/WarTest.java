package io.github.mattwang1868;

import org.junit.jupiter.api.Test;

import io.github.mattwang1868.carddeck.*;
import io.github.mattwang1868.war.WarEngine34P;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Test class for various test cases that might come up.
 */
class WarGameTest {

    @Test
    void testWarBackup3P() {
        // Test the case in which two players are at war but are unable to continue
        List<CardStack> state = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            state.add(new CardStack());
        }
        List<Card> played = new ArrayList<>();
        played.add(new Card(Suit.CLUBS, CardValue.EIGHT));
        played.add(new Card(Suit.SPADES, CardValue.EIGHT));
        played.add(new Card(Suit.DIAMONDS, CardValue.TWO));

        List<Card> expectedPlayed = new ArrayList<>();
        expectedPlayed.add(new Card(Suit.CLUBS, CardValue.EIGHT));
        expectedPlayed.add(new Card(Suit.SPADES, CardValue.EIGHT));
        expectedPlayed.add(new Card(Suit.DIAMONDS, CardValue.TWO));
        Deck remaining = new Deck();
        for (int i = 0; i < 52; i++) {
            Card next = remaining.draw();
            if (!played.contains(next)) {
                state.get(2).addCard(next);
            }
        }

        int winner = WarEngine34P.determineWinner(state, played);
        assertEquals(2, winner);
        assertEquals(expectedPlayed, played);
        assertEquals(0, state.get(0).size());
        assertEquals(0, state.get(1).size());
        assertEquals(49, state.get(2).size());


    }

    @Test
    void testWarBackup4P1() {
        // Test the case in which two players are at war but are unable to continue
        List<CardStack> state = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            state.add(new CardStack());
        }
        List<Card> played = new ArrayList<>();
        played.add(new Card(Suit.CLUBS, CardValue.EIGHT));
        played.add(new Card(Suit.SPADES, CardValue.EIGHT));
        played.add(new Card(Suit.DIAMONDS, CardValue.TWO));
        played.add(new Card(Suit.HEARTS, CardValue.FOUR));
        List<Card> expectedPlayed = new ArrayList<>();
        expectedPlayed.add(new Card(Suit.CLUBS, CardValue.EIGHT));
        expectedPlayed.add(new Card(Suit.SPADES, CardValue.EIGHT));
        expectedPlayed.add(new Card(Suit.DIAMONDS, CardValue.TWO));
        expectedPlayed.add(new Card(Suit.HEARTS, CardValue.FOUR));

        Deck remaining = new Deck();
        for (int i = 0; i < 52; i++) {
            Card next = remaining.draw();
            if (!played.contains(next)) {
                state.get(i % 2 + 2).addCard(next);
            }
        }

        int winner = WarEngine34P.determineWinner(state, played);
        assertEquals(3, winner);
        assertEquals(expectedPlayed, played);
        assertEquals(0, state.get(0).size());
        assertEquals(0, state.get(1).size());
    }

    @Test
    void testWarBackup4P2() {
        // Test the case in which two players are at war but are unable to continue
        //but the other two players are also tied
        List<CardStack> state = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            state.add(new CardStack());
        }
        List<Card> played = new ArrayList<>();
        played.add(new Card(Suit.CLUBS, CardValue.EIGHT));
        played.add(new Card(Suit.SPADES, CardValue.EIGHT));
        played.add(new Card(Suit.DIAMONDS, CardValue.TWO));
        played.add(new Card(Suit.HEARTS, CardValue.TWO));
        List<Card> expectedPlayed = new ArrayList<>();
        expectedPlayed.add(new Card(Suit.CLUBS, CardValue.EIGHT));
        expectedPlayed.add(new Card(Suit.SPADES, CardValue.EIGHT));
        expectedPlayed.add(new Card(Suit.DIAMONDS, CardValue.TWO));
        expectedPlayed.add(new Card(Suit.HEARTS, CardValue.TWO));


        Deck random = new Deck();
        random.shuffle();
        for (int i = 0; i < 6; i++) {
            Card next = random.draw();
            if (!played.contains(next)) {
                state.get(i % 2 + 2).addCard(next);
            }
        }
        state.get(2).addCard(new Card(Suit.CLUBS, CardValue.ACE));
        state.get(3).addCard(new Card(Suit.DIAMONDS, CardValue.KING));

        Deck remaining = new Deck();
        for (int i = 0; i < 52; i++) {
            Card next = remaining.draw();
            if (!played.contains(next) && !state.get(2).contains(next) && !state.get(3).contains(next)) {
                state.get(i % 2 + 2).addCard(next);
            }
        }
        int winner = WarEngine34P.determineWinner(state, played);
        assertEquals(2, winner);
        assertEquals(expectedPlayed, played);
        assertEquals(0, state.get(0).size());
        assertEquals(0, state.get(1).size());
    }

}

