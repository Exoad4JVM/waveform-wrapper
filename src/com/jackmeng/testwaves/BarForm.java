package com.jackmeng.testwaves;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BarForm extends JPanel {
  private JPanel internalPane;
  private int[] bars;
  private int barsWidth, barsGap, arcH, arcW, xOffset, yPadding;
  private Color fg, bg;

  public static record BoxWaveConf(int barsXOffset, int barsYPad, int barsArcH, int barsArcW) {
    public boolean assertNonNegative() {
      return barsYPad >= 0 && barsArcH >= 0 && barsArcW >= 0;
    }
  }

  public static record ColorConf(Color fg, Color bg) {
  }

  public static final int START_CENTER = -1;

  public BarForm(int width, int height, int barsGap, int barsWidth, BoxWaveConf bwc, ColorConf colors) {
    super();
    assert width > 0 && height > 0 && barsGap > 0 && barsWidth > 0
        && (bwc != null ? bwc.assertNonNegative() : bwc == null) && colors != null;
    this.barsGap = barsGap;
    this.barsWidth = barsWidth;
    this.arcH = bwc.barsArcH;
    this.arcW = bwc.barsArcW;
    this.xOffset = bwc.barsXOffset > width ? bwc.barsXOffset - width : bwc.barsXOffset;
    this.yPadding = bwc.barsYPad > height ? bwc.barsYPad - height : bwc.barsYPad;
    this.bg = colors.bg == null ? Color.WHITE : colors.bg;
    this.fg = colors.fg == null ? Color.BLACK : colors.fg;

    setIgnoreRepaint(true);
    setPreferredSize(new Dimension(width, height));
    setLayout(new BorderLayout());

    internalPane = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g2.setRenderingHint(
            RenderingHints.KEY_STROKE_CONTROL,
            RenderingHints.VALUE_STROKE_PURE);
        g2.setColor(fg);
        for (int i = xOffset, j = 0; i < internalPane.getWidth() - xOffset && j < bars.length; j++, i += barsGap) {
          if (bars[j] <= 0) {
            bars[j] = yPadding;
          }
          g2.fillRoundRect(i, internalPane.getHeight() / 2 - bars[j] / 2, barsWidth, internalPane.getHeight() - bars[j], arcW, arcH);
        }
      }
    };
    internalPane.setPreferredSize(getPreferredSize());
    internalPane.setBackground(bg);
    internalPane.setIgnoreRepaint(true);

    add(internalPane, BorderLayout.CENTER);

    bars = new int[width - (width / (barsGap + barsWidth))];
    bars = Utils.fillArr(bars, () -> Utils.rng(0, height - yPadding));
    make(bars, 10L);
    addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        internalPane.setPreferredSize(getPreferredSize());
        make(bars, 50L);
      }
    });
    /*
     * addComponentListener(new ComponentAdapter() {
     *
     * @Override
     * public void componentResized(ComponentEvent e) {
     * bars = new int[internalPane.getWidth() - (internalPane.getWidth() / (barsGap
     * + barsWidth))];
     * bars = Utils.fillArr(bars, () -> Utils.rng(0, internalPane.getHeight()));
     * refresh(bars);
     * }
     * });
     */
  }

  public void make(int[] bars, long schedule) {
    this.bars = bars;
    SwingUtilities.invokeLater(() -> internalPane.repaint(schedule));
  }

  public int[] getCurrentDrawable() {
    return bars;
  }

  public static void main(String[] args) throws Exception {
    JFrame f = new JFrame();
    f.setPreferredSize(new Dimension(300, 100));
    BarForm bf = new BarForm(300, 100, 4, 3, new BoxWaveConf(10, 5, 5, 5),
        new ColorConf(Color.ORANGE, Color.BLACK));
    f.getContentPane().add(bf);
    f.pack();
    f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    f.setVisible(true);
  }
}
