public class Card {

    public String value;
    public int numValue;
    public boolean isAce;
    public boolean isTen;

    public Card(String input) {

        isAce = false;
        isTen = false;

        value = input;
        if (value.equals("K") || value.equals("T") || value.equals("J") || value.equals("Q")) {
            isTen = true;
            numValue = 10;
        } else if (value.equals("A")) {
            isAce = true;
            numValue = 11;
        } else
            numValue = Integer.parseInt(input);
    }

    public void downgradeAce() {
        numValue = 1;
    }
}