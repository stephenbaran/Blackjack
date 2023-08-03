import java.io.*;
import java.util.*;

public class basicStrategy {

    private String fileLocation = "C://Users//Steph//OneDrive//Desktop//GitHub//Blackjack/";
    //private String fileLocation = "C://Users//poken//Documents//GitHub//Blackjack/";

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
		} catch (NumberFormatException e) {
            System.out.println("Choose a real configuration");
            startGame();
		} catch (IOException e) {
			System.out.println("Choose a real configuration");
            startGame();
		}
    }

    public void deal() {
        for(int x = 0; x < 2; x++) {
            player.addCardToHand(deck.draw());
            dealer.addCardToHand(deck.draw());
        }

        player.checkIfHandCanSplit();
        dealer.checkIfHandCanSplit();
    }

    

    

    String playerChoice = "null";

    public String lengthen(String input) {
        if (input.equals("h"))
            return "hit";
        if (input.equals("s"))
            return "stand";
        if (input.equals("sp"))
            return "split";
        if (input.equals("d"))
            return "double";
        if (input.equals("sur"))
            return "surrender";
        return input;
    }

    public boolean validate(Hand inputHand, String input) {
        if (input.equals("hit") || input.equals("stand"))
            return true;
        if (inputHand.getCanDouble() && input.equals("double"))
            return true;
        if (inputHand.getCanSplit() && input.equals("split"))
            return true;
        if (inputHand.getCanSurrender() && input.equals("surrender"))
            return true;
        return false;
    }

    Hand tempHand;

    public void playerTurn(Hand playerHand, DealerHand dealerHand) throws IOException {
        printHands(playerHand, dealerHand, false);

        if (playerHand.getTotal() >= 21) {

        } else {

            System.out.println("Enter Choice (hit, stand, split, double, surrender):");
            playerChoice = lengthen(playerInput.nextLine());

            if (validate(playerHand, playerChoice)) {

                if (playerChoice.equals(choiceAccuracy(playerHand, dealerHand))) {
                    System.out.println(ANSI_green + "CORRECT" + ANSI_reset);
                } else {
                    System.out.println(
                            ANSI_red + "INCORRECT, should have " + choiceAccuracy(playerHand, dealerHand) + ANSI_reset);
                }

                if (playerHand.getCanDouble() && playerChoice.equals("double")) {
                    draw(playerHand);
                }

                if (playerChoice.equals("hit")) {
                    draw(playerHand);
                    playerHand.checkIfHandCanSplit();
                    playerTurn(playerHand, dealerHand);
                }

                if (playerHand.getCanSplit() && playerChoice.equals("split")) {

                    playerHand.splitHand();

                    for (int x = 0; x < 2; x++) {
                        tempHand = new Hand(playerHand.getNumTimesSplit() + 1);
                        tempHand.addCardToHand(playerHand.getCard(x));
                        draw(tempHand);

                        tempHand.checkIfHandCanSplit();

                        alternatePlayerHands.add(tempHand);
                    }
                    clearSplits();
                }

            } else {
                System.out.println("type something real bro");
                playerTurn(playerHand, dealerHand);
            }

        }
    }

    public void dealerTurn(Hand playerHand, DealerHand dealerHand) {
        printHands(playerHand, dealerHand, true);

        if (dealerHand.getHandHardness()) {
            if (dealerHand.getTotal() < 17) {
                draw(dealerHand);
                dealerTurn(playerHand, dealerHand);
            }
        } else {
            if (dealerHand.getTotal() <= 17) {
                draw(dealerHand);
                dealerTurn(playerHand, dealerHand);
            }
        }
    }

    private void evaluateRound(Hand playerHand, DealerHand dealerHand) throws IOException {

        System.out.println("ROUND OVER");
        System.out.println("Your total " + playerHand.getTotal());
        System.out.println("House total " + dealerHand.getTotal());

        if (playerHand.getTotal() > 21)
            System.out.println(ANSI_red + "YOU LOSE" + ANSI_reset);
        else if (dealerHand.getTotal() > 21)
            System.out.println(ANSI_green + "YOU WIN" + ANSI_reset);
        else {
            if (playerHand.getTotal() > dealerHand.getTotal()) {
                System.out.println(ANSI_green + "YOU WIN" + ANSI_reset);
            } else if (playerHand.getTotal() == dealerHand.getTotal()) {
                System.out.println(ANSI_red + "DRAW" + ANSI_reset);
            } else {
                System.out.println(ANSI_red + "YOU LOSE" + ANSI_reset);
            }
        }
        System.out.println("the count is " + count);

        if (numDecks == 2 && deck.size() < 0.4 * 52 * 2) {
            shuffle();
            count = 0;
            System.out.println("deck shuffled (double deck)");
            System.out.println("count is reset to " + count);
        }

        else if (numDecks == 6 && deck.size() < 0.3 * 52 * 6) {
            shuffle();
            count = 0;
            System.out.println("deck shuffled (six deck)");
            System.out.println("count is reset to " + count);
        }

        else if (numDecks == 1 && deck.size() < 0.5 * 52 * 1) {
            shuffle();
            count = 0;
            System.out.println("deck shuffled (single deck)");
            System.out.println("count is reset to " + count);
        }
    }

    public void playRound() {
        // allow player to control when next hand is dealt
        // playerInput.nextLine();

        this.deal();

        player.checkIfHandCanSplit();

        if (player.getTotal() == 21 || dealer.getTotal() == 21) {

            if (player.getTotal() == 21 && dealer.getTotal() == 21) {
                System.out.println("DOUBLE BLACKJACK, TIE");
            } else if (player.getTotal() == 21) {
                System.out.println("BLACKJACK!, YOU WIN");
            } else {
                System.out.println("DEALER BLACKJACK, YOU LOSE");
            }

            printHands(player, dealer, true);

            System.out.println("---------------------------");

            System.out.println(count);

            System.out.println();

            this.playRound();
        } else {

            try {

                playerTurn(player, dealer);

                if (!player.getIfHandWasSplit()) {

                    if (player.getTotal() <= 21)
                        dealerTurn(player, dealer);
                    else
                        printHands(true);

                    evaluateRound(player, dealer);

                    System.out.println("---------------------------");

                }

                this.playRound();

            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    private int splitHands;

    public void clearSplits() {

        System.out.println("---------------------------");

        splitHands = 1;

        while (alternatePlayerHands.size() > 0) {
            System.out.println(ANSI_red + "SPLIT HAND " + splitHands + ANSI_reset);
            splitHands++;

            tempHand = alternatePlayerHands.pop();

            try {
                playerTurn(tempHand, dealer);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if (!tempHand.getIfHandWasSplit()) {

            if (tempHand.getTotal() <= 21)
                dealerTurn(tempHand, dealer);
            else
                printHands(tempHand, dealer, true);

            evaluateRound(tempHand, dealer);

            System.out.println("---------------------------");
        }

    }

    String temp;

    private void printHands(boolean canSeeDealer) {
        if (canSeeDealer) {
            temp = ANSI_green + "DEALER: ";
            for (int x = 0; x < dealer.size(); x++)
                temp += dealer.get(x) + " ";
            temp += ANSI_reset;
            System.out.println(temp);
        } else {
            System.out.println(ANSI_green + "DEALER: " + dealer.get(1) + ANSI_reset);
        }

        System.out.println(dealer.getTotal());

        temp = ANSI_green + "YOU: ";
        for (int x = 0; x < player.size(); x++)
            temp += player.get(x) + " ";
        temp += ANSI_reset;
        System.out.println(temp);

        System.out.println(player.getTotal());
    }

    private void printHands(Hand playerHand, DealerHand dealerHand, boolean canSeeDealer) {
        if (canSeeDealer) {
            temp = ANSI_green + "DEALER: ";
            for (int x = 0; x < dealerHand.size(); x++)
                temp += dealerHand.get(x) + " ";
            temp += ANSI_reset;
            System.out.println(temp);
        } else {
            System.out.println(ANSI_green + "DEALER: " + dealer.get(1) + ANSI_reset);
        }

        System.out.println(dealerHand.getTotal());

        temp = ANSI_green + "YOU: ";
        for (int x = 0; x < playerHand.size(); x++)
            temp += playerHand.get(x) + " ";
        temp += ANSI_reset;
        System.out.println(temp);

        System.out.println(playerHand.getTotal());
    }

}
