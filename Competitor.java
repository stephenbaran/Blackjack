public class Competitor {

    private Hand hand;

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
}