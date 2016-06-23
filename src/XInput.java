import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/*
 * Created by Stephen "Zero" Chappell on 27 May 2016.
 */
class XInput implements KeyListener {
    // Request Flags
    private boolean requestExit;
    // Stateful Values
    private boolean requestSlow;
    private boolean requestLeft;
    private boolean requestRight;
    private boolean requestBurn;

    XInput() {
        this.requestExit = false;
        this.requestSlow = false;
        this.requestLeft = false;
        this.requestRight = false;
        this.requestBurn = false;
    }

    public void keyPressed(KeyEvent event) {
        switch (event.getKeyCode()) {
            // Handle changing request flags.
            case KeyEvent.VK_ESCAPE:
                this.requestExit = true;
                break;
            // Handle changing stateful values.
            case KeyEvent.VK_DOWN:
                this.requestSlow = true;
                break;
            case KeyEvent.VK_LEFT:
                this.requestLeft = true;
                break;
            case KeyEvent.VK_RIGHT:
                this.requestRight = true;
                break;
            case KeyEvent.VK_UP:
                this.requestBurn = true;
                break;
        }
    }

    public void keyTyped(KeyEvent event) {
    }

    public void keyReleased(KeyEvent event) {
        // Only handle changing stateful values.
        switch (event.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                this.requestSlow = false;
                break;
            case KeyEvent.VK_LEFT:
                this.requestLeft = false;
                break;
            case KeyEvent.VK_RIGHT:
                this.requestRight = false;
                break;
            case KeyEvent.VK_UP:
                this.requestBurn = false;
                break;
        }
    }

    boolean requestsExit() {
        boolean exit = this.requestExit;
        this.requestExit = false;
        return exit;
    }

    boolean requestsSlow() {
        return this.requestSlow;
    }

    boolean requestsLeft() {
        return this.requestLeft;
    }

    boolean requestsRight() {
        return this.requestRight;
    }

    boolean requestsBurn() {
        return this.requestBurn;
    }
}
