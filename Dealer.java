
public class Dealer extends Competitor {

    private DealerHand tempHand;

    public Dealer() {
        super(new DealerHand());
    }

    public Card getVisibleCard() {
        tempHand = (DealerHand) hand;
        return tempHand.getVisibleCard();
    }

    public String getVisibleCardValue() {
        tempHand = (DealerHand) hand;
        return tempHand.getVisibleCard().value;
    }

    public void clearHand() {
        hand = new DealerHand();
    }

    public DealerHand getHand() {
        return (DealerHand) hand;
    }

}