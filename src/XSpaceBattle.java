import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * Created by Stephen "Zero" Chappell on 27 May 2016.
 */
class XSpaceBattle extends JFrame implements ActionListener {
    private static final String WINDOW_TITLE = "Space Battle";
    private static final int CANVAS_WIDTH = 800;
    private static final int CANVAS_HEIGHT = 600;
    private static final int FRAMES_PER_SECOND = 40;
    private static final Color BACKGROUND_COLOR = Color.BLACK;
    private final XInput input;
    private final Canvas context;
    private final XPlayer player;
    private final Timer updater;

    private XSpaceBattle() {
        super(WINDOW_TITLE);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.input = new XInput();
        this.context = new Canvas();
        // Add key listener to all components.
        this.addKeyListener(this.input);
        this.context.addKeyListener(this.input);
        this.context.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        this.add(this.context);
        this.pack();
        this.setResizable(false);
        this.setVisible(true);
        this.player = new XPlayer(
                this.context.getSize(),
                this.input,
                new XVector(context.getWidth() >> 1, context.getHeight() >> 1));
        this.updater = new Timer(1000 / FRAMES_PER_SECOND, this);
        this.updater.start();
    }

    public static void main(String[] argv) {
        new XSpaceBattle();
    }

    public void dispose() {
        this.updater.stop();
        super.dispose();
    }

    public void actionPerformed(ActionEvent event) {
        if (this.input.requestsExit())
            SwingUtilities.invokeLater(this::dispose);
        this.player.move();
        this.updateGraphics();
    }

    private void updateGraphics() {
        int width = this.context.getWidth();
        int height = this.context.getHeight();
        Image buffer = this.context.createImage(width, height);
        Graphics surface = buffer.getGraphics();
        // Fill in the background.
        surface.setColor(BACKGROUND_COLOR);
        surface.fillRect(0, 0, width, height);
        this.player.draw(surface);
        this.context.getGraphics().drawImage(buffer, 0, 0, null);
    }
}
