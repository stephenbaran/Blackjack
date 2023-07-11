public class DealerHand extends Hand {

    private String visibleCard;

    public DealerHand() {
        super();
    }

    public String getVisibleCard() {
        return super.getHand().get(1);
    }
}
