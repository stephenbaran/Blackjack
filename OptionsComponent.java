import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class OptionsComponent extends JComponent implements ActionListener{

  private JButton btnPlay = new JButton("PLAY"); 
  private JButton btnExit = new JButton("EXIT");
  private static BufferedImage backgroundImage;

  public OptionsComponent() { 
    btnPlay.addActionListener(this);
    btnExit.addActionListener(this);
  }

  public void paintComponent(Graphics g) {//we will decorate the component with this method.
    // wtf is this shit
    Graphics2D g2 = (Graphics2D) g; //we first cast Graphics g to Graphics2D g2 in order to use a more powerful brush.

    // need to get my own image for the blackjack table background

    try {
      backgroundImage = ImageIO.read(new File("images/background.png"));
    }
    catch(IOException e) {}

    g2.drawImage(backgroundImage, 0, 0, null);

    g2.setFont(new Font("Comic Sans MS", Font.BOLD, 100));
    g2.setColor(Color.WHITE);
    g2.drawString("Welcome", 380, 100);
    g2.drawString("to", 530, 180);
    g2.drawString("BLACKJACK!", 290, 260);

    g2.setFont(new Font("Arial", Font.BOLD, 30));

    btnPlay.setBounds(500, 300, 150, 80); 
    btnExit.setBounds(500, 400, 150, 80);
    btnHelp.setBounds(80, 75, 150, 80);
    btnInfo.setBounds(900, 75, 150, 80);

    //this is stephen commenting, there is no fucking wau we will use comic sans

    btnPlay.setFont(new Font("Comic Sans MS", Font.BOLD, 40)); 
    btnExit.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
    btnHelp.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
    btnInfo.setFont(new Font("Comic Sans MS", Font.BOLD, 40));

    super.add(btnPlay); 
    super.add(btnExit);
  }

  public void actionPerformed(ActionEvent e) {

    JButton selectedButton = (JButton)e.getSource();

    if(selectedButton == btnExit) { 
      System.exit(0); 
    }

    else if(selectedButton == btnPlay) {//if the selected button is the play button (btnPlay), we start the game.
      Tester.currentState = Tester.STATE.GAME; //we equalize the current state to STATE.GAME where STATE was the enum declared in the Tester class. Because we will no longer be in the menu, we will jump to the game.
      Tester.menuFrame.dispose(); //we first get rid of the menu frame and the components it has.
      Tester.gameRefreshThread.start(); //then, simultaneously, we start our two threads and these run at the same time.
      Tester.gameCheckThread.start();
    }
    
  }

  
}