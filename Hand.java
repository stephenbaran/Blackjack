import java.util.ArrayList;

public class Hand {

    private ArrayList<String> hand;
    private int total;

    private boolean isHandHard, canHandSplit, canHandDouble;

    public Hand() {
        hand = new ArrayList<String>();
        total = 0;
        isHandHard = true;
        canHandSplit = false;
        canHandDouble = true;
    }

    public boolean getCanDouble() {
        return canHandDouble;
    }

    public boolean getCanSplit() {
        return canHandSplit;
    }

    public void setCanDouble(boolean input) {
        canHandDouble = input;
    }

    public void makeHandSoft() {
        isHandHard = false;
    }

    public void makeHandHard() {
        isHandHard = false;
    }

    public boolean getHandHardness() {
        return isHandHard;
    }

    public void checkIfHandCanSplit() {

        if (hand.get(0).equals("A") || hand.get(1).equals("A")) {
            if (hand.get(0).equals(hand.get(1)))
                canHandSplit = true;
        }
        if (!isNumeric(hand.get(0)) && !isNumeric(hand.get(1)))
            canHandSplit = true;
        if (hand.get(0).equals(hand.get(1)))
            canHandSplit = true;

    }

    public void setIfHandCanSplit(boolean input) {
        canHandSplit = input;
    }

    public void setIfHandCanDouble(boolean input) {
        canHandDouble = input;
    }

    public ArrayList<String> getHand() {
        return hand;
    }

    public void addCardToHand(String card) {
        hand.add(card);
    }

    public void addTotal(int sum) {
        total += sum;
    }

    public void subtractTotal(int sum) {
        total -= sum;
    }

    public int getTotal() {
        return total;
    }

    public void clearHand() {
        hand.clear();
    }

    public String get(int pos) {
        return hand.get(pos);
    }

    public int size() {
        return hand.size();
    }

    public boolean isNumeric(String strNum) {
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
}
