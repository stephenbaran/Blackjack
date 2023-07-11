import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.io.IOException;
import java.io.*;
import java.util.*;

public class basicStrategy {
    private ArrayList<String> player = new ArrayList<String>();
    private ArrayList<String> dealer = new ArrayList<String>();
    private ArrayList<String> deck = new ArrayList<String>();
    private String currentCard;
    private String clear;
    private String ANSI_reset, ANSI_green, ANSI_red;
    private int playerTotal, dealerTotal;
    private boolean playerHandHard, dealerHandHard, playerCanDouble;
    private int nDecks, nDraws;
    private int nChoices, nCorrect;
    private boolean playerTurn;
    private String nonAce;

    private HashMap<String, String> pairSplittingTable;
    private HashMap<String, String> softTotalTable;
    private HashMap<String, String> hardTotalTable;
    private HashMap<String, String> surrenderTable;

    private ArrayList<String> tempArr;

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
        nDecks = 1;
        nDraws = 0;
        nChoices = 0;
        nCorrect = 0;
        playerTurn = true;
        playerHandHard = true;
        dealerHandHard = true;

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
                new File("C://Users//Steph//OneDrive//Desktop//VS Projects//Blackjack//src//oneDeck.txt"));
        for (int x = 1; x <= nDecks * 52; x++) {
            deck.add(deckInput.nextLine());
        }
        deckInput.close();
        nDraws = 0;
    }

    public void play() {

    }

    public void deal() {
        player.clear();
        dealer.clear();

        playerHandHard = true;
        dealerHandHard = true;
        playerTotal = 0;
        dealerTotal = 0;
        draw(player);
        draw(dealer);
        draw(player);
        draw(dealer);
    }

    public void draw(ArrayList<String> input) {
        int randomDraw = (int) (Math.floor(Math.random() * deck.size()));
        input.add(deck.get(randomDraw));

        if (isNumeric(deck.get(randomDraw))) {
            if (input == player) {
                playerTotal += Double.parseDouble(deck.get(randomDraw));
            } else {
                dealerTotal += Double.parseDouble(deck.get(randomDraw));
                ;
            }
        } else {
            if (input == player) {
                if (!deck.get(randomDraw).equals("A")) {
                    playerTotal += 10;
                } else if (playerTotal + 11 > 21) {
                    playerTotal += 1;
                } else {
                    playerTotal += 11;
                    playerHandHard = false;
                }
            } else {
                if (!deck.get(randomDraw).equals("A")) {
                    dealerTotal += 10;
                } else if (dealerTotal + 11 > 21) {
                    dealerTotal += 1;
                } else {
                    dealerTotal += 11;
                    dealerHandHard = false;
                }
            }
        }

        if (!playerHandHard && playerTotal > 21) {
            playerTotal -= 10;
            playerHandHard = true;
        }
        if (!dealerHandHard && dealerTotal > 21) {
            dealerTotal -= 10;
            dealerHandHard = true;
        }

        deck.remove(randomDraw);
        ++nDraws;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public void draw() throws IOException {
        int randomDraw = (int) (Math.floor(Math.random() * deck.size()));
        if (playerTurn == true) {
            player.add(deck.get(randomDraw));
            deck.remove(randomDraw);
        } else {
            dealer.add(deck.get(randomDraw));
            deck.remove(randomDraw);
        }
        ++nDraws;
        // it's really only shuffled after the round is finished so this needs to be
        // moved somewhere else
        if (nDraws > 0.75 * nDecks * 52) {
            // 75% is a guess for when the deck is shuffled
            shuffle();
        }
    }

    

    public int choiceAccuracy(String choice) {

        // for(int x = 0; x < player.size(); x++) {
        //     tempArr.add(player.get(x));
        //     if (!isNumeric(player.get(x)) && !player.get(x).equals("A")) player.set(x, "T");
        // }

        if (player.get(0).equals(player.get(1)) && player.size() == 2) {
            if (player.get(0).equals("A") || player.get(0).equals("8")) {
                if (choice.equals("split")) {
                    return 1;
                } else {
                    return 0;
                }
            }
            if(!player.get(0).equals("5") && isNumeric(player.get(0))) {
                if (pairSplittingTable.get(player.get(0) + "," + dealer.get(1)).equals("D")) {
                    if (choice.equals("split"))
                        return 1;
                    else
                        return 0;
                }

                if (pairSplittingTable.get(player.get(0) + "," + dealer.get(1)).equals("Y")) {
                    if(choice.equals("split")) return 1;
                    else return 0;
                }
            }
        }

        if (!playerHandHard) {
            nonAce = Integer.toString(playerTotal - 11);

            if (nonAce.equals("9") || nonAce.equals("10")) {
                if (choice.equals("stand"))
                    return 1;
                else
                    return 0;
            }

            if (choice.equals("double") && (softTotalTable.get(nonAce + "," + dealer.get(1)).equals("DS")
                    || softTotalTable.get(nonAce + "," + dealer.get(1)).equals("D")))
                return 1;

            if (softTotalTable.get(nonAce + "," + dealer.get(1)).equals("H")) {
                if (choice.equals("hit"))
                    return 1;
                else
                    return 0;
            }

            if (softTotalTable.get(nonAce + "," + dealer.get(1)).equals("S")) {
                if (choice.equals("stand"))
                    return 1;
                else
                    return 0;
            }

            if (playerCanDouble)
                return 0;
            else if (choice.equals("hit") && softTotalTable.get(nonAce + "," + dealer.get(1)).equals("D"))
                return 1;
            else if (choice.equals("stand") && softTotalTable.get(nonAce + "," + dealer.get(1)).equals("DS"))
                return 1;
            else
                return 0;

        } else {
            if (playerTotal >= 17) {
                if (choice.equals("stand"))
                    return 1;
                else
                    return 0;
            } else if (playerTotal <= 8) {
                if (choice.equals("hit"))
                    return 1;
                else
                    return 0;
            }

            if (playerTotal == 11) {
                if (playerCanDouble) {
                    if (choice.equals("double"))
                        return 1;
                    else
                        return 0;
                } else {
                    if (choice.equals("hit"))
                        return 1;
                    else
                        return 0;
                }
            }

            if (hardTotalTable.get(playerTotal + "," + dealer.get(1)).equals("S")) {
                if (choice.equals("stand"))
                    return 1;
                else
                    return 0;
            }

            if (hardTotalTable.get(playerTotal + "," + dealer.get(1)).equals("H")) {
                if (choice.equals("hit"))
                    return 1;
                else
                    return 0;
            }

            if (hardTotalTable.get(playerTotal + "," + dealer.get(1)).equals("D")) {
                if (playerCanDouble) {
                    if (choice.equals("double"))
                        return 1;
                    else
                        return 0;
                } else {
                    if (choice.equals("hit"))
                        return 1;
                    else
                        return 0;
                }
            }
        }
        return -1;
    }

    public int surrenderAccurance(boolean didSurrender) {
        if (surrenderTable.get(playerTotal + "," + dealer.get(1)).equals("Y")) {
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
                    "C://Users//Steph//OneDrive//Desktop//VS Projects//Blackjack//src//" + mapName + ".txt");
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

    public void playerTurn() throws IOException {
        System.out.println("Enter Choice (hit, stand, split, double, surrender):");
        playerChoice = playerInput.nextLine();
        if (playerChoice.equalsIgnoreCase("hit") || playerChoice.equalsIgnoreCase("h")) {
            if (correctAction() == "hit")
                System.out.println(ANSI_green + "CORRECT" + ANSI_reset);
            else {
                System.out.println(ANSI_red + "INCORRECT, should have " + correctAction() + ANSI_reset);
            }
        } else if (playerChoice.equalsIgnoreCase("stand") || playerChoice.equalsIgnoreCase("s")) {
            if (correctAction() == "stand")
                System.out.println(ANSI_green + "CORRECT" + ANSI_reset);
            else {
                System.out.println(ANSI_red + "INCORRECT, should have " + correctAction() + ANSI_reset);
            }
        } else if (playerChoice.equalsIgnoreCase("split") || playerChoice.equalsIgnoreCase("sp")) {
            if (correctAction() == "split")
                System.out.println(ANSI_green + "CORRECT" + ANSI_reset);
            else {
                System.out.println(ANSI_red + "INCORRECT, should have " + correctAction() + ANSI_reset);
            }
        } else if (playerChoice.equalsIgnoreCase("double") || playerChoice.equalsIgnoreCase("d")) {
            if (correctAction() == "double")
                System.out.println(ANSI_green + "CORRECT" + ANSI_reset);
            else {
                System.out.println(ANSI_red + "INCORRECT, should have " + correctAction() + ANSI_reset);
            }
        } else if (playerChoice.equalsIgnoreCase("surrender") || playerChoice.equalsIgnoreCase("sur")) {

        } else {
            System.out.println("type something real bro");
        }

    }

    public String correctAction() {
        if (choiceAccuracy("split") == 1) {
            if (pairSplittingTable.get(player.get(0) + "," + dealer.get(1)).equals("D")) {
                return "split ONLY if double after split";
            } else {
                return "split";
            }
        } else if (choiceAccuracy("double") == 1) {
            return "double";
        } else if (choiceAccuracy("hit") == 1)
            return "hit";
        else if (choiceAccuracy("stand") == 1)
            return "stand";
        else
            return "IDK";
    }

    public void dealerTurn() {
        if (dealerHandHard == true) {
            if (dealerTotal < 17) {
                // dealer hits
            } else {
                // dealer stands
            }
        } else {
            if (dealerTotal <= 17) {
                // dealer hits
            } else {
                // dealer stands
            }
        }
    }

    private boolean isRoundOver;

    public void playRound() {
        isRoundOver = false;
        this.deal();

        System.out.println(ANSI_green + "DEALER: " + dealer.get(1) + ANSI_reset);
        System.out.println(ANSI_green + "YOU: " + player.get(0) + " " + player.get(1) + ANSI_reset);

        playerCanDouble = true;

        while (!isRoundOver) {
            try {
                playerTurn();
            } catch (IOException e) {
                System.out.println(e);
            }
            isRoundOver = true;
        }

        this.playRound();

    }

}
