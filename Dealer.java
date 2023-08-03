

public class Dealer extends Competitor {

    private DealerHand tempHand;


    public Dealer() {
        super(new DealerHand());
    }

    public Card getVisibleCard() {
        tempHand = (DealerHand) super.getHand();
        return tempHand.getVisibleCard();
    }

    public String getVisibleCardValue() {
        tempHand = (DealerHand) super.getHand();
        return tempHand.getVisibleCard().value;
    }

}