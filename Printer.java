public class Printer {
    
    private String ANSI_reset, ANSI_green, ANSI_red, clear;

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
}