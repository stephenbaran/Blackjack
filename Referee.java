import java.io.*;
import java.util.*;

public class Referee {

    private String fileLocation;

    private HashMap<String, String> pairSplittingTable;
    private HashMap<String, String> softTotalTable;
    private HashMap<String, String> hardTotalTable;
    private HashMap<String, String> surrenderTable;

    public Referee(String input) {

        fileLocation = input;

        pairSplittingTable = new HashMap<String, String>();
        readInfoIntoMap(pairSplittingTable, "pairSplittingTable");
        softTotalTable = new HashMap<String, String>();
        readInfoIntoMap(softTotalTable, "softTotalTable");
        hardTotalTable = new HashMap<String, String>();
        readInfoIntoMap(hardTotalTable, "hardTotalTable");
        surrenderTable = new HashMap<String, String>();
        readInfoIntoMap(surrenderTable, "surrenderTable");
    }

    public void readInfoIntoMap(HashMap<String, String> inputMap, String mapName) {
        String[] inputs;
        try {
            File myObj = new File(
                    fileLocation + "tables/" + mapName + ".txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                inputs = data.split(":");
                inputMap.put(inputs[0], inputs[1]);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private String nonAce;

    public String choiceAccuracy(Hand playerHand, DealerHand dealerHand) {
        if (playerHand.getCanSplit()) {
            if (playerHand.get(0).equals("A") || playerHand.get(0).equals("8")) {
                return "split";
            }

            if (!playerHand.get(0).equals("5") && !playerHand.getCard(0).isTen) {
                if (pairSplittingTable.get(playerHand.get(0) + "," + dealerHand.getVisibleCard().value).equals("D")) {
                    return "split";
                }

                if (pairSplittingTable.get(playerHand.get(0) + "," + dealerHand.getVisibleCard().value).equals("Y")) {
                    return "split";
                }
            }
        }

        if (!playerHand.getHandHardness()) {

            nonAce = Integer.toString(playerHand.getTotal() - 11);

            if (nonAce.equals("0"))
                return "split";

            if (nonAce.equals("1"))
                return "hit";

            if (nonAce.equals("9") || nonAce.equals("10")) {
                return "stand";
            }

            if (playerHand.getCanDouble()
                    && (softTotalTable.get(nonAce + "," + dealerHand.getVisibleCard().value).equals("DS")
                            || softTotalTable.get(nonAce + "," + dealerHand.getVisibleCard().value).equals("D")))
                return "double";

            if (softTotalTable.get(nonAce + "," + dealerHand.getVisibleCard().value).equals("H")) {
                return "hit";
            }

            if (softTotalTable.get(nonAce + "," + dealerHand.getVisibleCard().value).equals("S")) {
                return "stand";
            }

            if (softTotalTable.get(nonAce + "," + dealerHand.getVisibleCard().value).equals("D"))
                return "hit";
            else if (softTotalTable.get(nonAce + "," + dealerHand.getVisibleCard().value).equals("DS"))
                return "stand";

        } else {
            if (playerHand.getTotal() >= 17) {
                return "stand";
            } else if (playerHand.getTotal() <= 8) {
                return "hit";
            }

            if (playerHand.getTotal() == 11) {
                if (playerHand.getCanDouble()) {
                    return "double";
                } else {
                    return "hit";
                }
            }

            if (hardTotalTable.get(playerHand.getTotal() + "," + dealerHand.getVisibleCard().value).equals("S")) {
                return "stand";
            }

            if (playerHand.getCanSurrender() && playerHand.getTotal() <= 17 && playerHand.getTotal() >= 14) {
                if (surrenderTable.get(playerHand.getTotal() + "," + dealerHand.getVisibleCard().value).equals("Y")) {
                    return "surrender";
                }
            }

            if (hardTotalTable.get(playerHand.getTotal() + "," + dealerHand.getVisibleCard().value).equals("H")) {
                return "hit";
            }

            if (hardTotalTable.get(playerHand.getTotal() + "," + dealerHand.getVisibleCard().value).equals("D")) {
                if (playerHand.getCanDouble()) {
                    return "double";
                } else {
                    return "hit";
                }
            }
        }
        return "-1";
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

    public boolean checkBlackjacks(Player player, Dealer dealer) {
        if (player.getTotal() == 21 && dealer.getTotal() == 21) {
            System.out.println("DOUBLE BLACKJACK, TIE");
            return true;
        } else if (player.getTotal() == 21) {
            System.out.println("BLACKJACK!, YOU WIN");
            return true;
        } else if (dealer.getTotal() == 21) {
            System.out.println("DEALER BLACKJACK, YOU LOSE");
            return true;
        }
        return false;
    }

    public void evaluateRound(Hand playerHand, DealerHand dealerHand, Printer printOut, Deck deck, int numDecks)
            throws IOException {

        System.out.println("ROUND OVER");
        System.out.println("Your total " + playerHand.getTotal());
        System.out.println("House total " + dealerHand.getTotal());

        if (playerHand.getTotal() > 21)
            printOut.printLoss();
        else if (dealerHand.getTotal() > 21)
            printOut.printWin();
        else {
            if (playerHand.getTotal() > dealerHand.getTotal()) {
                printOut.printWin();
            } else if (playerHand.getTotal() == dealerHand.getTotal()) {
                printOut.printDraw();
            } else {
                printOut.printLoss();
            }
        }

        if (numDecks == 2 && deck.size() < 0.4 * 52 * 2) {
            deck.shuffle();
            deck.resetCount();
            printOut.deckWasReset();
        }

        else if (numDecks == 6 && deck.size() < 0.3 * 52 * 6) {
            deck.shuffle();
            deck.resetCount();
            printOut.deckWasReset();
        }

        else if (numDecks == 1 && deck.size() < 0.5 * 52 * 1) {
            deck.shuffle();
            deck.resetCount();
            printOut.deckWasReset();
        }
    }

}