package net.sourceforge.squirrel_sql.client.gui.desktopcontainer.docktabdesktop;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import javax.swing.*;

public class VerticalToggleButton extends JToggleButton
{
   private static final int WIDHT_MARGIN = 20;
   private static final int HEIGHT_MARGIN = 4;

   public VerticalToggleButton(String caption)
   {
      Icon icon = new VerticalLabelIcon(caption, this,
            new Insets(HEIGHT_MARGIN, WIDHT_MARGIN, HEIGHT_MARGIN, WIDHT_MARGIN));
      super.setIcon(icon);
      super.setActionCommand(caption);
   }

   public static void main(String[] args)
   {
      SwingUtilities.invokeLater(new Runnable()
      {
         public void run()
         {
            JFrame frame = new JFrame("Vertical Button Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new FlowLayout());
            frame.add(new VerticalToggleButton("Vertical Up"));
            frame.add(new VerticalToggleButton("Vertical Down"));
            frame.pack();
            frame.setVisible(true);
         }
      });
   }
}


class VerticalLabelIcon implements Icon, Serializable
{
   /* REVISIT: Make accessible. */

   private String caption;
   private Insets insets;
   private FontMetrics fm;
   private int iconWidth;
   private int iconHeight;

   public VerticalLabelIcon(String caption, Component c, Insets insets)
   {
      this.caption = caption;
      this.insets = (Insets) insets.clone();
      initMetrics(c);
   }

   private void initMetrics(Component c)
   {
      this.fm = c.getFontMetrics(c.getFont());
      this.iconWidth = fm.getHeight() + insets.top + insets.bottom;
      this.iconHeight = fm.stringWidth(caption) + insets.left + insets.right;
   }

   @Override
   public int getIconWidth()
   {
      return iconWidth;
   }

   @Override
   public int getIconHeight()
   {
      return iconHeight;
   }

   @Override
   public synchronized void paintIcon(Component c, Graphics g, int x, int y)
   {
      initMetrics(c);
      if (g instanceof Graphics2D)
      {
         Graphics2D g2 = (Graphics2D) g.create(x, y, iconWidth, iconHeight);
         paintLabel(c, g2);
         g2.dispose();
      }
      else
      {
         // Note, drawing to translucent offscreen canvas may (would) not
         // use subpixel anti-aliasing.  This path is meant for compatibility
         // with DebugGraphics and headless runtime.  It is not dpi-aware, also.
         BufferedImage image = new BufferedImage(iconWidth, iconHeight,
                                                 BufferedImage.TYPE_INT_ARGB);
         Graphics2D g2 = image.createGraphics();
         paintLabel(c, g2);
         g2.dispose();
         g.drawImage(image, x, y, null);
      }
   }

   private void paintLabel(Component c, Graphics2D g)
   {
      g.setColor(c.getForeground());
      g.setFont(fm.getFont());
      FontDesktopHints.set(g);

      g.rotate(-Math.PI / 2);
      g.translate(-iconHeight, iconWidth);
      g.drawString(caption, insets.left, -insets.bottom - fm.getDescent());
   }
}
