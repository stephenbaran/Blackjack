import java.io.*;
import java.util.*;

public class basicStrategy {
    private Hand player = new Hand();
    private DealerHand dealer = new DealerHand();

    private ArrayList<Card> deck = new ArrayList<Card>();

    private String ANSI_reset, ANSI_green, ANSI_red, clear;

    private String nonAce;

    private HashMap<String, String> pairSplittingTable;
    private HashMap<String, String> softTotalTable;
    private HashMap<String, String> hardTotalTable;
    private HashMap<String, String> surrenderTable;

    public static void main(String[] args) throws IOException {
        basicStrategy game = new basicStrategy();
        game.shuffle();
        game.playRound();
    }

    public basicStrategy() {
        clear = "\033[H\033[2J";
        ANSI_reset = "\033[0m";
        ANSI_green = "\033[42m";
        ANSI_red = "\033[41m";

        pairSplittingTable = new HashMap<String, String>();
        readInfoIntoMap(pairSplittingTable, "pairSplittingTable");
        softTotalTable = new HashMap<String, String>();
        readInfoIntoMap(softTotalTable, "softTotalTable");
        hardTotalTable = new HashMap<String, String>();
        readInfoIntoMap(hardTotalTable, "hardTotalTable");
        surrenderTable = new HashMap<String, String>();
        readInfoIntoMap(surrenderTable, "surrenderTable");
    }

    public void modeSelection() {
        // gonna leave this until i actually want different modes
        // might want to choose number of decks here
    }

    public void shuffle() throws IOException {
        deck.clear();
        Scanner deckInput = new Scanner(
                new File("C://Users//poken//Documents//GitHub//Blackjack//oneDeck.txt"));
        for (int x = 1; x <= 52; x++) {
            deck.add(new Card(deckInput.nextLine()));
        }
        deckInput.close();
    }

    public void deal() {
        player = new Hand();
        dealer = new DealerHand();

        draw(player);
        draw(dealer);
        draw(player);
        draw(dealer);

        player.checkIfHandCanSplit();
        dealer.checkIfHandCanSplit();
    }

    public void draw(Hand input) {
        int randomDraw = (int) (Math.floor(Math.random() * deck.size()));
        input.addCardToHand(deck.get(randomDraw));
        deck.remove(randomDraw);
    }

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
                if (player.getCanDouble()) {
                    return "double";
                } else {
                    return "hit";
                }
            }

            if (hardTotalTable.get(playerHand.getTotal() + "," + dealerHand.getVisibleCard().value).equals("S")) {
                return "stand";
            }

            if (hardTotalTable.get(playerHand.getTotal() + "," + dealerHand.getVisibleCard().value).equals("H")) {
                return "hit";
            }

            if (hardTotalTable.get(playerHand.getTotal() + "," + dealerHand.getVisibleCard().value).equals("D")) {
                if (player.getCanDouble()) {
                    return "double";
                } else {
                    return "hit";
                }
            }
        }
        return "-1";
    }

    public int surrenderAccurance(boolean didSurrender, Hand playerHand, DealerHand dealerHand) {
        if (surrenderTable.get(playerHand.getTotal() + "," + dealerHand.getVisibleCard().value).equals("Y")) {
            if (didSurrender)
                return 1;
            else
                return 0;
        } else {
            if (didSurrender)
                return 0;
            else
                return 1;
        }
    }

    public void readInfoIntoMap(HashMap<String, String> inputMap, String mapName) {
        String[] inputs;
        try {
            File myObj = new File(
                    "C://Users//poken//Documents//GitHub//Blackjack//" + mapName + ".txt");
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

    String playerChoice = "null";
    Scanner playerInput = new Scanner(System.in);

    public String lengthen(String input) {
        if (input.equals("h"))
            return "hit";
        if (input.equals("s"))
            return "stand";
        if (input.equals("sp"))
            return "split";
        if (input.equals("d"))
            return "double";
        if (input.equals("s"))
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
        return false;
    }

    public void playerTurn() throws IOException {
        printHands(false);

        if (player.getTotal() >= 21) {

        } else {

            System.out.println("Enter Choice (hit, stand, split, double, surrender):");
            playerChoice = lengthen(playerInput.nextLine());

            if (validate(player, playerChoice)) {

                if (playerChoice.equals(choiceAccuracy(player, dealer))) {
                    System.out.println(ANSI_green + "CORRECT" + ANSI_reset);
                } else {
                    System.out.println(
                            ANSI_red + "INCORRECT, should have " + choiceAccuracy(player, dealer) + ANSI_reset);
                }

                if (player.getCanDouble() && playerChoice.equals("double")) {
                    draw(player);
                }

                player.setCanDouble(false);

                if (playerChoice.equals("hit")) {
                    draw(player);
                    playerTurn();
                }

                if (player.getCanSplit() && playerChoice.equals("split")) {
                    // playSplit(player.get(0), dealer);
                }

            } else {
                System.out.println("type something real bro");
                playerTurn();
            }

        }
    }

    public void dealerTurn() {
        printHands(true);
        if (dealer.getHandHardness()) {
            if (dealer.getTotal() < 17) {
                draw(dealer);
                dealerTurn();
            }
        } else {
            if (dealer.getTotal() <= 17) {
                draw(dealer);
                dealerTurn();
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
        if (deck.size() < 0.75 * 52) {
            shuffle();
        }
    }

    public void playRound() {

        this.deal();

        player.checkIfHandCanSplit();

        try {
            player.setCanDouble(true);
            playerTurn();

            if (player.getTotal() <= 21)
                dealerTurn();
            else
                printHands(true);

            evaluateRound(player, dealer);

            System.out.println("---------------------------");

            this.playRound();

        } catch (IOException e) {
            System.out.println(e);
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

}

// public void draw() throws IOException {
// int randomDraw = (int) (Math.floor(Math.random() * deck.size()));
// if (playerTurn == true) {
// player.add(deck.get(randomDraw));
// deck.remove(randomDraw);
// } else {
// dealer.add(deck.get(randomDraw));
// deck.remove(randomDraw);
// }
// ++nDraws;
// // it's really only shuffled after the round is finished so this needs to be
// // moved somewhere else
// if (nDraws > 0.75 * nDecks * 52) {
// // 75% is a guess for when the deck is shuffled
// shuffle();
// }
// }