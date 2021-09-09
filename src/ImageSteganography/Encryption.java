package ImageSteganography;

//EmbedMessage.java
import Coding.SendEmail;
 import java.awt.image.*;
 import javax.swing.*;
 import java.awt.*;
 import java.awt.event.*;
 import javax.imageio.*;

/**
 *
 * @author Hadi
 */ 
 public class Encryption extends JFrame implements ActionListener
 {
 JButton open = new JButton("Open"), embed = new JButton("Embed"), send = new JButton("Send"),
    save = new JButton("Save"), reset = new JButton("Reset");
 
 JTextArea message = new JTextArea(10,3);
 BufferedImage sourceImage = null, embeddedImage = null;
 JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
 JScrollPane originalPane = new JScrollPane(),
    embeddedPane = new JScrollPane();
 
 public Encryption() {
    super("Embed stegonographic message in image");
    assembleInterface();
    open.setBackground(Color.black);
    open.setForeground(Color.WHITE);
    open.setFont(new Font("Monaco", Font.BOLD, 20));
    open.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    
    embed.setBackground(Color.black);
    embed.setForeground(Color.WHITE);
    embed.setFont(new Font("Monaco", Font.BOLD, 20));
    embed.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    
    send.setBackground(Color.black);
    send.setForeground(Color.white);
    send.setFont(new Font("Monaco", Font.BOLD, 20));
    send.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    
    save.setBackground(Color.black);
    save.setForeground(Color.WHITE);
    save.setFont(new Font("Monaco", Font.BOLD, 20));
    save.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    
    reset.setBackground(Color.black);
    reset.setForeground(Color.WHITE);
    reset.setFont(new Font("Monaco", Font.BOLD, 20));
    reset.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    
//    this.setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().
//       getMaximumWindowBounds());
    this.setSize(1000, 700);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);   
    this.setVisible(true);
    sp.setDividerLocation(0.5);
    this.validate();
    }
 
 private void assembleInterface() {
    JPanel p = new JPanel(new FlowLayout());
    p.add(open);
    p.add(embed);
    p.add(send);
    p.add(save);   
    p.add(reset);
    
    this.getContentPane().add(p, BorderLayout.SOUTH);
    open.addActionListener(this);
    embed.addActionListener(this);
    send.addActionListener(this);
   save.addActionListener(this);   
    reset.addActionListener(this);
    open.setMnemonic('O');
    embed.setMnemonic('E');
    send.setMnemonic('G');
    save.setMnemonic('S');
    reset.setMnemonic('R');
    
    p = new JPanel(new GridLayout(1,1));
    p.add(new JScrollPane(message));
    message.setFont(new Font("Arial",Font.BOLD,20));
    p.setBorder(BorderFactory.createTitledBorder("Message to be embedded"));
    this.getContentPane().add(p, BorderLayout.NORTH);
    
    sp.setLeftComponent(originalPane);
    sp.setRightComponent(embeddedPane);
    originalPane.setBorder(BorderFactory.createTitledBorder("Original Image"));
    embeddedPane.setBorder(BorderFactory.createTitledBorder("Steganographed Image"));
    this.getContentPane().add(sp, BorderLayout.CENTER);
    }
 
 @Override
 public void actionPerformed(ActionEvent ae) {
    Object o = ae.getSource();
    if(o == open)
       openImage();
    else if(o == embed)
       embedMessage();
    else if(o == send)
    {
        SendEmail Email = new SendEmail();
        Email.setVisible(true);
        Email.setLocationRelativeTo(null);
        Email.pack();
    }
    else if(o == save) 
       saveImage();
    else if(o == reset) 
       resetInterface();
    }
 
 private java.io.File showFileDialog(final boolean open) {
    JFileChooser fc = new JFileChooser("Open an image");
    javax.swing.filechooser.FileFilter ff = new javax.swing.filechooser.FileFilter() {
       @Override
       public boolean accept(java.io.File f) {
          String name = f.getName().toLowerCase();
          if(open)
             return f.isDirectory() || name.endsWith(".jpg") || name.endsWith(".jpeg") ||
                name.endsWith(".png") || name.endsWith(".gif") || name.endsWith(".tiff") ||
                name.endsWith(".bmp") || name.endsWith(".dib");
          return f.isDirectory() || name.endsWith(".png") ||    name.endsWith(".bmp");
          }
       @Override
       public String getDescription() {
          if(open)
             return "Image (*.jpg, *.jpeg, *.png, *.gif, *.tiff, *.bmp, *.dib)";
          return "Image (*.png, *.bmp)";
          }
       };
    fc.setAcceptAllFileFilterUsed(false);
    fc.addChoosableFileFilter(ff);
 
    java.io.File f = null;
   if(open && fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
       f = fc.getSelectedFile();
    else if(!open && fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
       f = fc.getSelectedFile();
    return f;
    }
 
 private void openImage() {
    java.io.File f = showFileDialog(true);
    if(f==null)
        return;
    try {   
       sourceImage = ImageIO.read(f);
       JLabel l = new JLabel(new ImageIcon(sourceImage));
       originalPane.getViewport().add(l);
       this.validate();
       } catch(Exception ex) {}
    }
 
 private void embedMessage() {
    String mess = message.getText();
    
    if(mess.equals("") || sourceImage.getHeight() == 0){
        JOptionPane.showMessageDialog(this, "Please Select Image and Enter the Text First!");
        return;
    }
    embeddedImage = sourceImage.getSubimage(0,0,
       sourceImage.getWidth(),sourceImage.getHeight());
    embedMessage(embeddedImage, mess);
    JLabel l = new JLabel(new ImageIcon(embeddedImage));
    embeddedPane.getViewport().add(l);
    this.validate();
    }
 
 private void embedMessage(BufferedImage img, String mess) {
    int messageLength = mess.length();
    
    int imageWidth = img.getWidth(), imageHeight = img.getHeight(),
       imageSize = imageWidth * imageHeight;
    if(messageLength * 8 + 32 > imageSize) {
       JOptionPane.showMessageDialog(this, "Message is too long for the chosen image",
          "Message too long!", JOptionPane.ERROR_MESSAGE);
       return;
       }
    embedInteger(img, messageLength, 0, 0);
 
    byte b[] = mess.getBytes();
    for(int i=0; i<b.length; i++)
       embedByte(img, b[i], i*8+32, 0);
    }
 
 private void embedInteger (BufferedImage img, int n, int start, int storageBit) {
    int maxX = img.getWidth(), maxY = img.getHeight(), 
       startX = start/maxY, startY = start - startX*maxY, count=0;
    for(int i=startX; i<maxX && count<32; i++) {
       for(int j=startY; j<maxY && count<32; j++) {
          int rgb = img.getRGB(i, j), bit = getBitValue(n, count);
          rgb = setBitValue(rgb, storageBit, bit);
          img.setRGB(i, j, rgb);
          count++;
          }
       }
    }
 
 private void embedByte (BufferedImage img, byte b, int start, int storageBit) {
    int maxX = img.getWidth(), maxY = img.getHeight(), 
       startX = start/maxY, startY = start - startX*maxY, count=0;
    for(int i=startX; i<maxX && count<8; i++) {
       for(int j=startY; j<maxY && count<8; j++) {
          int rgb = img.getRGB(i, j), bit = getBitValue(b, count);
          rgb = setBitValue(rgb, storageBit, bit);
          img.setRGB(i, j, rgb);
          count++;
          }
       }
    }
 
 private void saveImage() {
    if(embeddedImage == null) {
       JOptionPane.showMessageDialog(this, "No message has been embedded!", 
         "Nothing to save", JOptionPane.ERROR_MESSAGE);
       return;
      }
   java.io.File f = showFileDialog(false);
   if(f==null)
   return;    
  
    String name = f.getName();
    String ext = name.substring(name.lastIndexOf(".")+1).toLowerCase();
    if(!ext.equals("png") && !ext.equals("bmp") &&   !ext.equals("dib")) {
          ext = "png";
          f = new java.io.File(f.getAbsolutePath()+".png");
          }
    try {
       if(f.exists()) f.delete();
       ImageIO.write(embeddedImage, ext.toUpperCase(), f);
       } catch(Exception ex) {}
    }

 private void resetInterface() {
    message.setText("");
    originalPane.getViewport().removeAll();
    embeddedPane.getViewport().removeAll();
    sourceImage = null;
    embeddedImage = null;
    sp.setDividerLocation(0.5);
    this.validate();
    }
 
 private int getBitValue(int n, int location) {
    int v = n & (int) Math.round(Math.pow(2, location));
    return v==0?0:1;
    }
 
 private int setBitValue(int n, int location, int bit) {
    int toggle = (int) Math.pow(2, location), bv = getBitValue(n, location);
    if(bv == bit)
       return n;
    if(bv == 0 && bit == 1)
       n |= toggle;
    else if(bv == 1 && bit == 0)
       n ^= toggle;
    return n;
    }
 
// public static void main(String arg[]) {
////     Encryption embedMessage = new Encryption();
//    }
}