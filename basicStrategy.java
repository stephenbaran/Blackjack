import java.io.*;
import java.util.*;

public class basicStrategy {
    private Hand player = new Hand();
    private DealerHand dealer = new DealerHand();

    private Stack<Hand> alternatePlayerHands;

    private ArrayList<Card> deck = new ArrayList<Card>();

    private String ANSI_reset, ANSI_green, ANSI_red, clear;

    private String nonAce;

    private int count;

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

        alternatePlayerHands = new Stack<Hand>();

        count = 0;
    }

    public void shuffle() throws IOException {
        deck.clear();
        Scanner deckInput = new Scanner(
                new File("C://Users//Steph//OneDrive//Desktop//GitHub//Blackjack//oneDeck.txt"));
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

        int numValue = deck.get(randomDraw).numValue;

        if (numValue > 9 || numValue == 1) {
            count--;
        }
        else if (numValue < 7) {
            count++;
        }
        
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

            if (playerHand.getCanSurrender() && playerHand.getTotal() <= 17 && playerHand.getTotal() >= 14) {
                if (surrenderTable.get(playerHand.getTotal() + "," + dealerHand.getVisibleCard().value).equals("Y")) {
                    return "surrender";
                }
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

    public void readInfoIntoMap(HashMap<String, String> inputMap, String mapName) {
        String[] inputs;
        try {
            File myObj = new File(
                    "C://Users//Steph//OneDrive//Desktop//GitHub//Blackjack//" + mapName + ".txt");
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

                        clearSplits();
                    }
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
        if (deck.size() < 0.3 * 52) {
            shuffle();
            count = 0;
            System.out.println("deck shuffled");
            System.out.println("count is reset to " + count);
        }
    }

    public void playRound() {
        //allow player to control when next hand is dealt
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

            // NEED TO CHECK FOR BLACKJACKS HERE

            try {
                playerTurn(tempHand, dealer);

                if (!tempHand.getIfHandWasSplit()) {

                    if (tempHand.getTotal() <= 21)
                        dealerTurn(tempHand, dealer);
                    else
                        printHands(tempHand, dealer, true);

                    evaluateRound(tempHand, dealer);

                    System.out.println("---------------------------");
                }

            } catch (IOException e) {
                System.out.println(e);
            }
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

