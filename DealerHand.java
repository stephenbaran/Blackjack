public class DealerHand extends Hand {

    public DealerHand() {
        super();
    }

    public Card getVisibleCard() {
        return super.getHand().get(1);
    }
}
