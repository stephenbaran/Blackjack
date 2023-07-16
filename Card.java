public class Card {

    public String value;
    public int numValue;
    public boolean isAce;
    public boolean isTen;
    public int count;

    public Card() {
        count = 0;
    }

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
        if (numValue > 9) {
            count--;
        }
        else if (numValue < 7) {
            count++;
        }
    }

    public void downgradeAce() {
        numValue = 1;
    }

    public void upgradeAce() {
        numValue = 11;
    }
}