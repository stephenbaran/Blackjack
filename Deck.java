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

    public void shuffle() throws IOException {
        deck.clear();
        Scanner deckInput = new Scanner(
                new File(fileLocation + "deckTypes/oneDeck.txt"));

        for(int y = 0; y < numDecks; y++) {
            for (int x = 1; x <= 52 ; x++) {
                deck.add(new Card(deckInput.nextLine()));
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

}