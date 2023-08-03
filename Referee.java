import java.io.*;
import java.util.*;

public class Referee {

    private String fileLocation;

    private HashMap<String, String> pairSplittingTable;
    private HashMap<String, String> softTotalTable;
    private HashMap<String, String> hardTotalTable;
    private HashMap<String, String> surrenderTable;

    public Referee(String input) {
        pairSplittingTable = new HashMap<String, String>();
        readInfoIntoMap(pairSplittingTable, "pairSplittingTable");
        softTotalTable = new HashMap<String, String>();
        readInfoIntoMap(softTotalTable, "softTotalTable");
        hardTotalTable = new HashMap<String, String>();
        readInfoIntoMap(hardTotalTable, "hardTotalTable");
        surrenderTable = new HashMap<String, String>();
        readInfoIntoMap(surrenderTable, "surrenderTable");

        fileLocation = input;
    }

    public void readInfoIntoMap(HashMap<String, String> inputMap, String mapName) {
        String[] inputs;
        try {
            File myObj = new File(
                    fileLocation + "tables/" + mapName + ".txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                inputs = data.split(":");
                inputMap.put(inputs[0], inputs[1]);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private String nonAce;

    public String choiceAccuracy(Hand playerHand, DealerHand dealerHand) {
        if (playerHand.getCanSplit()) {
            if (playerHand.get(0).equals("A") || playerHand.get(0).equals("8")) {
                return "split";
            }

            if (!playerHand.get(0).equals("5") && !playerHand.getCard(0).isTen) {
                if (pairSplittingTable.get(playerHand.get(0) + "," + dealerHand.getVisibleCard().value).equals("D")) {
                    return "split";
                }

                if (pairSplittingTable.get(playerHand.get(0) + "," + dealerHand.getVisibleCard().value).equals("Y")) {
                    return "split";
                }
            }
        }

        if (!playerHand.getHandHardness()) {

            nonAce = Integer.toString(playerHand.getTotal() - 11);

            if (nonAce.equals("1"))
                return "hit";

            if (nonAce.equals("9") || nonAce.equals("10")) {
                return "stand";
            }

            if (playerHand.getCanDouble()
                    && (softTotalTable.get(nonAce + "," + dealerHand.getVisibleCard().value).equals("DS")
                            || softTotalTable.get(nonAce + "," + dealerHand.getVisibleCard().value).equals("D")))
                return "double";

            if (softTotalTable.get(nonAce + "," + dealerHand.getVisibleCard().value).equals("H")) {
                return "hit";
            }

            if (softTotalTable.get(nonAce + "," + dealerHand.getVisibleCard().value).equals("S")) {
                return "stand";
            }

            if (softTotalTable.get(nonAce + "," + dealerHand.getVisibleCard().value).equals("D"))
                return "hit";
            else if (softTotalTable.get(nonAce + "," + dealerHand.getVisibleCard().value).equals("DS"))
                return "stand";

        } else {
            if (playerHand.getTotal() >= 17) {
                return "stand";
            } else if (playerHand.getTotal() <= 8) {
                return "hit";
            }

            if (playerHand.getTotal() == 11) {
                if (player.getCanDouble()) {
                    return "double";
                } else {
                    return "hit";
                }
            }

            if (hardTotalTable.get(playerHand.getTotal() + "," + dealerHand.getVisibleCard().value).equals("S")) {
                return "stand";
            }

            if (playerHand.getCanSurrender() && playerHand.getTotal() <= 17 && playerHand.getTotal() >= 14) {
                if (surrenderTable.get(playerHand.getTotal() + "," + dealerHand.getVisibleCard().value).equals("Y")) {
                    return "surrender";
                }
            }

            if (hardTotalTable.get(playerHand.getTotal() + "," + dealerHand.getVisibleCard().value).equals("H")) {
                return "hit";
            }

            if (hardTotalTable.get(playerHand.getTotal() + "," + dealerHand.getVisibleCard().value).equals("D")) {
                if (player.getCanDouble()) {
                    return "double";
                } else {
                    return "hit";
                }
            }
        }
        return "-1";
    }

}