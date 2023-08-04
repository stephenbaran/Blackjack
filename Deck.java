import java.util.*;
import java.io.*;

public class Deck {

    private ArrayList<Card> deck;

    private int numDecks, randomDraw, count;

    private String fileLocation;

    private Card tempCard;

    public Deck(String input) {
        deck = new ArrayList<Card>();
        fileLocation = input;
        count = 0;
    }

    public void createNewDeck(int input) throws IOException {
        numDecks = input;
        shuffle();
    }

    Scanner deckInput;

    public void shuffle() throws IOException {
        deck.clear();

        for (int y = 0; y < numDecks; y++) {
            deckInput = new Scanner(
                    new File(fileLocation + "deckTypes/oneDeck.txt"));
            for (int x = 0; x < 52; x++) {
                deck.add(new Card(deckInput.nextLine().substring(0, 1)));
            }
        }

        deckInput.close();
    }

    public Card draw() {
        randomDraw = (int) (Math.floor(Math.random() * deck.size()));

        tempCard = deck.get(randomDraw);

        deck.remove(randomDraw);

        if (tempCard.numValue > 9 || tempCard.isAce) {
            count--;
        } else if (tempCard.numValue < 7) {
            count++;
        }

        return tempCard;
    }

    public int getCount() {
        return count;
    }

    public void resetCount() {
        count = 0;
    }

    public void printCount() {
        System.out.println("Count " + count);
    }

    public int size() {
        return deck.size();
    }
}