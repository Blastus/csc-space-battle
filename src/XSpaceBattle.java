import javax.swing.*;
import java.awt.*;

/*
 * Created by Stephen "Zero" Chappell on 27 May 2016.
 */
class XSpaceBattle extends JFrame {
    static final XRandom CHAOS = new XRandom();
    private static final String WINDOW_TITLE = "Space Battle";
    private static final int CANVAS_WIDTH = 800;
    private static final int CANVAS_HEIGHT = 600;
    private static final int FRAMES_PER_SECOND = 40;
    private final Timer updater;

    private XSpaceBattle() {
        super(WINDOW_TITLE);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        XInput input = new XInput();
        Canvas context = new Canvas();
        // Add key listener to all components.
        this.addKeyListener(input);
        context.addKeyListener(input);
        context.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        this.add(context);
        this.pack();
        this.setResizable(false);
        this.setVisible(true);
        // Must invoke dispose later so that it does not happen in the middle of a XEngine update.
        XEngine engine = new XEngine(input, context, () -> SwingUtilities.invokeLater(this::dispose));
        this.updater = new Timer(1000 / FRAMES_PER_SECOND, engine);
        this.updater.setActionCommand("update");
        this.updater.start();
    }

    public static void main(String[] argv) {
        new XSpaceBattle();
    }

    public void dispose() {
        this.updater.stop();
        super.dispose();
    }
}
