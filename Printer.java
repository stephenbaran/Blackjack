public class Printer {

    private String ANSI_reset, ANSI_green, ANSI_red, clear, temp;

    public Printer() {
        clear = "\033[H\033[2J";
        ANSI_reset = "\033[0m";
        ANSI_green = "\033[42m";
        ANSI_red = "\033[41m";
    }

    public void printDeckChoices() {
        System.out.println("1 deck can be used in casinos but usually with unfair rules");
        System.out.println("2 deck is fairly common in casinos but sometimes has unfair rules");
        System.out.println("6 deck is very common in casinos and usually has good rules");
        System.out.println("Enter deck size (1,2, or 6):");
    }

    public void printHands(Hand playerHand, DealerHand dealerHand, boolean canSeeDealer) {
        if (canSeeDealer) {
            temp = ANSI_green + "DEALER: ";
            for (int x = 0; x < dealerHand.size(); x++)
                temp += dealerHand.get(x) + " ";
            temp += ANSI_reset;
            System.out.println(temp);
        } else {
            System.out.println(ANSI_green + "DEALER: " + dealerHand.getVisibleCard().value + ANSI_reset);
        }

        System.out.println(dealerHand.getTotal());

        temp = ANSI_green + "YOU: ";
        for (int x = 0; x < playerHand.size(); x++)
            temp += playerHand.get(x) + " ";
        temp += ANSI_reset;
        System.out.println(temp);

        System.out.println(playerHand.getTotal());
    }

    public void printLine() {
        System.out.println("---------------------------");
    }

    public void printGreen(String input) {
        System.out.println(ANSI_green + input + ANSI_reset);
    }

    public void printRed(String input) {
        System.out.println(ANSI_red + input + ANSI_reset);
    }

    public void printWin() {
        printGreen("YOU WIN");
    }

    public void printLoss() {
        printRed("YOU LOSE");
    }

    public void printDraw() {
        printRed("YOU DRAW");
    }

    public void deckWasReset() {
        System.out.println("deck shuffled");
        System.out.println("count is reset");
    }
}