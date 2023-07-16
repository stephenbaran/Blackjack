import java.util.ArrayList;

public class Hand {

    private ArrayList<Card> hand;
    private int total, numTimesSplit;

    private boolean isHandHard, canHandSplit, canHandSurrender;
    private boolean wasHandSplit;

    public Hand() {
        hand = new ArrayList<Card>();
        total = 0;
        isHandHard = true;
        canHandSplit = false;
        wasHandSplit = false;
        numTimesSplit = 0;
        canHandSurrender = true;
    }

    public Hand(int timesSplit) {
        hand = new ArrayList<Card>();
        total = 0;
        isHandHard = true;
        canHandSplit = false;
        wasHandSplit = true;
        numTimesSplit = timesSplit;
    }

    public void splitHand() {
        wasHandSplit = true;

        if (this.getCard(0).isAce) {
            hand.get(1).upgradeAce();
        }
    }

    public boolean getIfHandWasSplit() {
        return wasHandSplit;
    }

    public int getNumTimesSplit() {
        return numTimesSplit;
    }

    public boolean getCanDouble() {
        return hand.size() <= 2;
    }

    public boolean getCanSplit() {
        return canHandSplit;
    }

    public boolean getHandHardness() {
        return isHandHard;
    }

    public boolean getCanSurrender() {
        return canHandSurrender;
    }

    public void checkIfHandCanSplit() {
        if (hand.get(0).isAce && hand.get(1).isAce)
            canHandSplit = true;
        if (hand.get(0).isTen && hand.get(1).isTen)
            canHandSplit = true;
        if (hand.get(0).numValue == hand.get(1).numValue)
            canHandSplit = true;
        if (numTimesSplit >= 2)
            canHandSplit = false;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void addCardToHand(Card card) {

        if (hand.size() == 2) {
            canHandSurrender = false;
        }

        if (card.isAce && total + 11 > 21)
            card.downgradeAce();
        else if (card.isAce)
            isHandHard = false;

        hand.add(card);

        total += card.numValue;

        if (total > 21 && !isHandHard) {
            for (int x = 0; x < hand.size(); x++) {
                if (hand.get(x).isAce && hand.get(x).numValue == 11) {
                    hand.get(x).downgradeAce();
                    recalculateTotal();
                    isHandHard = true;
                    break;
                }
            }
        }
    }

    public void recalculateTotal() {
        total = 0;
        for (int x = 0; x < hand.size(); x++) {
            total += hand.get(x).numValue;
        }
    }

    public int getTotal() {
        return total;
    }

    public void clearHand() {
        hand.clear();
    }

    public String get(int pos) {
        return hand.get(pos).value;
    }

    public Card getCard(int pos) {
        return hand.get(pos);
    }

    public int size() {
        return hand.size();
    }
}
