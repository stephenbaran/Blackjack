import java.io.IOException;
import java.util.*;

public class Player extends Competitor {

    String playerChoice;

    private Stack<Hand> alternateHands, completedHands;

    public Player() {
        super(new Hand());
        alternateHands = new Stack<Hand>();
        completedHands = new Stack<Hand>();
    }

    public void resetPlayer() {
        alternateHands = new Stack<Hand>();
        completedHands = new Stack<Hand>();
        hand = new Hand();
    }

    public void completeTurn(Printer printOut, Referee ref, DealerHand dealerHand, Scanner scan, Deck deck)
            throws IOException {

        hand.checkIfHandCanSplit();

        printOut.printHands(super.getHand(), dealerHand, false);

        if (hand.getTotal() >= 21) {
            completedHands.add(hand);
        }

        if (hand.getTotal() < 21) {

            System.out.println("Enter Choice (hit, stand, split, double, surrender):");
            playerChoice = lengthen(scan.nextLine());

            if (ref.validate(hand, playerChoice)) {

                if (playerChoice.equals(ref.choiceAccuracy(hand, dealerHand))) {
                    printOut.printGreen("CORRECT");
                } else {
                    printOut.printRed("INCORRECT, should have " + ref.choiceAccuracy(hand, dealerHand));
                }

                if (hand.getCanDouble() && playerChoice.equals("double")) {
                    hand.addCardToHand(deck.draw());
                    completedHands.add(hand);

                }

                if (playerChoice.equals("hit")) {
                    hand.addCardToHand(deck.draw());
                    hand.checkIfHandCanSplit();
                    completeTurn(printOut, ref, dealerHand, scan, deck);
                }

                if (hand.getCanSplit() && playerChoice.equals("split")) {
                    hand.addCardToHand(deck.draw());
                    hand.addCardToHand(deck.draw());

                    Card inputs[] = new Card[2];
                    // The next line is supposed to take position 1, not 0. This avoids taking a
                    // downgraded Ace. Don't change!!
                    inputs[0] = hand.getCard(1);
                    inputs[1] = hand.getCard(2);
                    alternateHands.add(new Hand(inputs));

                    // The next line is supposed to take position 1, not 0. This avoids taking a
                    // downgraded Ace. Don't change!!
                    inputs[0] = hand.getCard(1);
                    inputs[1] = hand.getCard(3);
                    alternateHands.add(new Hand(inputs));

                    hand = alternateHands.pop();

                    completeTurn(printOut, ref, dealerHand, scan, deck);
                }

                if (playerChoice.equals("stand")) {
                    completedHands.add(hand);
                }

            } else {
                System.out.println("type something real bro");
                completeTurn(printOut, ref, dealerHand, scan, deck);
            }

        }

        if (alternateHands.size() > 0) {
            hand = alternateHands.pop();
            completeTurn(printOut, ref, dealerHand, scan, deck);
        }
    }

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

    public Stack<Hand> getCompletedHands() {
        return completedHands;
    }

    public Hand getNextCompletedHand() {
        return completedHands.pop();
    }

}