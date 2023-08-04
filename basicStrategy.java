import java.io.*;
import java.util.*;

public class basicStrategy {

    private String fileLocation;

    private Player player;
    private Dealer dealer;
    private Referee ref;
    private Printer printOut;
    private Deck deck;

    Scanner playerInput;

    private int numDecks;

    public static void main(String[] args) throws IOException {
        basicStrategy game = new basicStrategy();
        game.startGame();
    }

    public basicStrategy() {

        fileLocation = "C://Users//poken//Documents//GitHub//Blackjack/";
        // fileLocation = "C://Users//Steph//OneDrive//Desktop//GitHub//Blackjack/";

        player = new Player();
        dealer = new Dealer();
        ref = new Referee(fileLocation);
        printOut = new Printer();
        deck = new Deck(fileLocation);

        playerInput = new Scanner(System.in);
    }

    public void startGame() {
        // Print the deck choices
        printOut.printDeckChoices();

        // Create the new deck, otherwise keep asking
        try {
            deck.createNewDeck(Integer.parseInt(playerInput.nextLine()));
            playRound();
        } catch (IOException e) {
            System.out.println(e);
            System.out.println("Choose a real configuration");
            startGame();
        }
    }

    public void deal() {
        for (int x = 0; x < 2; x++) {
            player.addCardToHand(deck.draw());
            dealer.addCardToHand(deck.draw());
        }

        player.checkIfHandCanSplit();
        dealer.checkIfHandCanSplit();
    }

    String playerChoice = "null";

    Hand tempHand;

    public void dealerTurn(Hand playerHand, DealerHand dealerHand) {
        printHands(true);

        if (dealerHand.getHandHardness()) {
            if (dealerHand.getTotal() < 17) {
                dealerHand.addCardToHand(deck.draw());
                dealerTurn(playerHand, dealerHand);
            }
        } else {
            if (dealerHand.getTotal() <= 17) {
                dealerHand.addCardToHand(deck.draw());
                dealerTurn(playerHand, dealerHand);
            }
        }
    }

    private void printHands(boolean input) {
        printOut.printHands(player.getHand(), (DealerHand) dealer.getHand(), input);
    }

    private Hand currentHand;

    public void playRound() {

        player.resetPlayer();
        dealer.clearHand();

        // allow player to control when next hand is dealt
        // playerInput.nextLine();

        deal();

        player.checkIfHandCanSplit();

        if (ref.checkBlackjacks(player, dealer)) {

            printHands(true);
            printOut.printLine();

            deck.printCount();

            playRound();

        } else {

            try {
                player.completeTurn(printOut, ref, (DealerHand) dealer.getHand(), playerInput, deck);

                while (player.getCompletedHands().size() > 0) {

                    currentHand = player.getNextCompletedHand();

                    if (currentHand.getTotal() <= 21) {
                        dealerTurn(player.getHand(), dealer.getHand());
                    } else {
                        printHands(true);
                    }

                    ref.evaluateRound(currentHand, dealer.getHand(), printOut, deck, numDecks);

                    printOut.printLine();

                }

                System.out.println(deck.getCount());

                this.playRound();

            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

}
