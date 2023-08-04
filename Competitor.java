public class Competitor {

    protected Hand hand;

    public Competitor(Hand input) {
        hand = input;
    }

    public void checkIfHandCanSplit() {
        hand.checkIfHandCanSplit();
    }

    public void addCardToHand(Card card) {
        hand.addCardToHand(card);
    }

    public Hand getHand() {
        return hand;
    }

    public int getTotal() {
        return hand.getTotal();
    }

    public void clearHand() {
        hand = new Hand();
    }

}