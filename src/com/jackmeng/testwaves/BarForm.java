package com.jackmeng.testwaves;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class BarForm extends JPanel {
  private int[] bars;
  private int barsWidth, barsGap, arcH, arcW;

  public BarForm(int height, int width, int barsGap, int barsWidth, int arcH, int arcW) {
    setPreferredSize(new Dimension(width, height));
    this.barsGap = barsGap;
    this.barsWidth = barsWidth;
    this.arcH = arcH;
    this.arcW = arcW;
    bars = new int[width - (width / (barsGap + barsWidth))];
    bars = Utils.fillArr(bars, () -> Utils.rng(0, 300));
    System.out.println(Arrays.toString(bars));
    System.out.println(bars.length);
    /*addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        bars = new int[getWidth() - (getWidth() / (barsGap + barsWidth))];
        bars = Utils.fillArr(bars, () -> Utils.rng(0, getHeight()));
        refresh(bars);
      }
    });*/
  }

  public void refresh(int[] bars) {
    this.bars = bars;
    SwingUtilities.invokeLater(this::repaint);
  }

  public int[] getCurrentDrawable() {
    return bars;
  }

  @Override
  public void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setColor(Color.BLACK);
    for (int i = 10, j = 0; i < this.getWidth() && j < bars.length; j++, i += barsGap) {
      g2.fillRoundRect(i, getHeight() / 2, barsWidth, Math.abs(bars[j] - getHeight() / 2), arcW, arcH);
    }
    g2.dispose();
  }

  public static void main(String[] args) {
    JFrame f = new JFrame();
    f.setPreferredSize(new Dimension(300, 100));
    BarForm bf = new BarForm(300, 100, 4, 3, 5, 5);
    f.getContentPane().add(bf);
    f.pack();
    f.setVisible(true);
  }
}
