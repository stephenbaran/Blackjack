import javax.swing.JComponent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GameComponent extends JComponent implements MouseListener {//this class will implement the MouseListener because we will check if the user clicked a certain coordinate on the component.
  public BufferedImage backgroundImage; //we name three images: one for the background image, one for the blackjack logo that will be located in the center, and for the chip.
  public BufferedImage logo;
  private ArrayList<Card> dealerHand; //as usual, we have to card arraylists which will serve as hands: one for the dealer and for the player.
  private ArrayList<Card> playerHand;
  private int dealerScore; //we have two integers: one for dealer's score and the other for player's score.
  private int playerScore;
  public boolean faceDown = true; //this boolean value will tell the program if we have the card facedown or faceup.

  public GameComponent(ArrayList<Card> dH, ArrayList<Card> pH) { //this will be the constructor for this class which will accept two hands as a parameter.
    dealerHand = dH; //we initalize the instant fields.
    playerHand = pH;
    dealerScore = 0; //the scores start as 0.
    playerScore = 0;
    addMouseListener(this);//we add MouseListener to the component.
  }

  public void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g; //we first cast Graphics g to Graphics2D g2 in order to use a more powerful brush.

    try {
      backgroundImage = ImageIO.read(new File("images/background.png")); //we read a file which is the png image of a poker table for our background image.
      logo = ImageIO.read(new File("images/blackjackLogo.png")); //we read a file which is the png image of a blackjack logo for the logo on the poker table.
    }
    catch(IOException e) {}

    g2.drawImage(backgroundImage, 0, 0, null); //we draw these images to the component.
    g2.drawImage(logo, 510, 200, null);
    g2.setColor(Color.WHITE); //we set the colors.
    g2.setFont(new Font("Comic Sans MS", Font.BOLD, 30)); //we change their fonts.
    g2.drawString("DEALER", 515, 50); //we draw these strings which visualize the game.
    g2.drawString("PLAYER", 515, 380);
    g2.drawString("DEALER WON: ", 50, 100);
    g2.drawString(Integer.toString(dealerScore), 300, 100); //we draw the dealer's score accordingly.
    g2.drawString("PLAYER WON: ", 50, 150);
    g2.drawString(Integer.toString(playerScore), 300, 150); //we draw the player's score accordingly.

  }

  public void refresh(int cB, int uS, int dS, boolean fD) { //this refresh method will refresh the GameComponent when it is called.
    currentBalance = cB;
    playerScore = uS;
    dealerScore = dS;
    faceDown = fD;
    this.repaint();
  }

  public void mousePressed(MouseEvent e) {
    int mouseX = e.getX(); 
    int mouseY = e.getY();

    // add stuff here maybe

      Tester.newGame.startGame(); //then we start the game.
    }

  }
  // wtf does that mean
  public void mouseExited(MouseEvent e) {//these methods will not be used in this project but it is necessary to write them.

  }
  public void mouseEntered(MouseEvent e) {

  }
  public void mouseReleased(MouseEvent e) {

  }
  public void mouseClicked(MouseEvent e) {

  }

}