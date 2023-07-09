import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    private boolean playerHandHard, dealerHandHard;
    private int nDecks, nDraws;
    private int nChoices, nCorrect;
    private boolean playerTurn;
    

    public static void main(String[] args) throws IOException {
        basicStrategy game = new basicStrategy();
        game.shuffle();
        game.deal();
        game.print();
        
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
    }

    public void modeSelection() {
        // gonna leave this until i actually want different modes
        // might want to choose number of decks here
    }

    public void shuffle() throws IOException {
        deck.clear();
        Scanner deckInput = new Scanner(new File("C://Users//Steph//OneDrive//Desktop//VS Projects//Blackjack//src//oneDeck.txt"));
        for (int x = 1; x <= nDecks * 52; x++) {
            deck.add(deckInput.nextLine());
        }
        deckInput.close();
        nDraws = 0;
    }

    public void play() {

    }

    public void deal() {
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
        int randomDraw = (int)(Math.floor(Math.random() * deck.size()));
        input.add(deck.get(randomDraw));

        if(isNumeric(deck.get(randomDraw))) {
            if(input == player) {
                playerTotal += Double.parseDouble(deck.get(randomDraw));
            } else {
                dealerTotal += Double.parseDouble(deck.get(randomDraw));;
            }
        } 
        else {
            if(input == player) {
                if(!deck.get(randomDraw).equals("A")) {
                    playerTotal += 10;
                } 
                else if(playerTotal + 11 > 21) {
                    playerTotal += 1; 
                } 
                else {
                    playerTotal += 11;
                    playerHandHard = false;
                }
            } 
            else {
                if(!deck.get(randomDraw).equals("A")) {
                    dealerTotal += 10;
                } 
                else if(dealerTotal + 11 > 21) {
                    dealerTotal += 1; 
                } 
                else {
                    dealerTotal += 11;
                    dealerHandHard = false;
                }
            }
        }

        if(!playerHandHard && playerTotal > 21) {
            playerTotal -= 10;
            playerHandHard = true;
        }
        if(!dealerHandHard && dealerTotal > 21) {
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
        int randomDraw = (int)(Math.floor(Math.random() * deck.size()));
        if (playerTurn == true) {
            player.add(deck.get(randomDraw));
            deck.remove(randomDraw);
        }
        else {
            dealer.add(deck.get(randomDraw));
            deck.remove(randomDraw);
        }
        ++nDraws;
        // it's really only shuffled after the round is finished so this needs to be moved somewhere else 
        if (nDraws > 0.75 * nDecks * 52) {
            // 75% is a guess for when the deck is shuffled
            shuffle();
        }
    }

    public boolean choiceAccuracy() {
        return true;
    }

    public void print() {
        // what if it's the dealers turn, what about game start
        // if (choiceAccuracy() == true) {
        //     System.out.println(ANSI_green + "Correct" + ANSI_reset);
        // }
        // else {
        //     System.out.println(ANSI_red + "Incorrect" + ANSI_reset);
        // }
        System.out.println(dealer);
        System.out.println(player);
        System.out.println(dealerTotal);
        System.out.println(playerTotal);
        System.out.println(dealerHandHard);
        System.out.println(playerHandHard);
    }

    public void playerTurn() throws IOException {
        String playerChoice = "null";
        Scanner playerInput = new Scanner(System.in);
        do {
            playerChoice = playerInput.nextLine();
            if (playerChoice.equalsIgnoreCase("hit") || playerChoice.equalsIgnoreCase("h")) {
                draw();
            }
            else if (playerChoice.equalsIgnoreCase("stand") || playerChoice.equalsIgnoreCase("s")) {
                
            }
            else if (playerChoice.equalsIgnoreCase("split") || playerChoice.equalsIgnoreCase("sp")) {
                //split();
            }
            else if (playerChoice.equalsIgnoreCase("double") || playerChoice.equalsIgnoreCase("d")) {
                draw();
            }
            else if (playerChoice.equalsIgnoreCase("surrender") || playerChoice.equalsIgnoreCase("sur")) {

            }
            else {
                System.out.println("type something real bro");
            }
        } while (!playerChoice.equalsIgnoreCase("stand"));
        playerInput.close();
    }

    public void dealerTurn() {
        if (dealerHandHard == true) {
            if (dealerTotal < 17) {
                // dealer hits
            }
            else {
                // dealer stands
            }
        }
        else {
            if (dealerTotal <= 17) {
                // dealer hits
            }
            else {
                // dealer stands
            }
        }
    }

    public void updateHandTotal() {
        
    }

}
