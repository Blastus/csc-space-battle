import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/*
 * Created by Stephen "Zero" Chappell on 27 May 2016.
 */
class XInput implements KeyListener {
    private static final String CHEAT_STRING = "cheat";
    private int weaponOffset;
    private String cheatBuffer;
    // Request Flags
    private boolean requestCheat;
    private boolean requestPanic;
    private boolean requestToggleEffects;
    private boolean requestExit;
    // Stateful Values
    private boolean requestSlow;
    private boolean requestLeft;
    private boolean requestRight;
    private boolean requestFire;
    private boolean requestBurn;

    XInput() {
        this.weaponOffset = 0;
        this.cheatBuffer = "";
        this.requestCheat = false;
        this.requestPanic = false;
        this.requestToggleEffects = false;
        this.requestExit = false;
        this.requestSlow = false;
        this.requestLeft = false;
        this.requestRight = false;
        this.requestFire = false;
        this.requestBurn = false;
    }

    public void keyPressed(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_1:
            case KeyEvent.VK_2:
            case KeyEvent.VK_3:
            case KeyEvent.VK_4:
            case KeyEvent.VK_5:
            case KeyEvent.VK_6:
            case KeyEvent.VK_7:
            case KeyEvent.VK_8:
            case KeyEvent.VK_9:
                this.weaponOffset = Math.min(
                        Character.getNumericValue(event.getKeyChar()),
                        XWeaponManager.SUPPORTED_WEAPONS) - 1;
                break;
            // Handle changing request flags.
            case KeyEvent.VK_C:
            case KeyEvent.VK_H:
            case KeyEvent.VK_E:
            case KeyEvent.VK_A:
            case KeyEvent.VK_T:
                this.cheatBuffer += event.getKeyChar();
                int difference = this.cheatBuffer.length() - CHEAT_STRING.length();
                if (difference > 0)
                    this.cheatBuffer = this.cheatBuffer.substring(difference);
                if (this.cheatBuffer.equalsIgnoreCase(CHEAT_STRING))
                    this.requestCheat = true;
                break;
            case KeyEvent.VK_P:
                this.requestPanic = true;
                break;
            case KeyEvent.VK_Z:
                this.requestToggleEffects = true;
                break;
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
            case KeyEvent.VK_SPACE:
                this.requestFire = true;
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
            case KeyEvent.VK_SPACE:
                this.requestFire = false;
                break;
            case KeyEvent.VK_UP:
                this.requestBurn = false;
                break;
        }
    }

    int requestsWeapon() {
        return this.weaponOffset;
    }

    boolean requestsCheat() {
        boolean cheat = this.requestCheat;
        this.requestCheat = false;
        return cheat;
    }

    boolean requestsPanic() {
        boolean panic = this.requestPanic;
        this.requestPanic = false;
        return panic;
    }

    boolean requestsToggleEffects() {
        boolean toggleEffects = this.requestToggleEffects;
        this.requestToggleEffects = false;
        return toggleEffects;
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

    boolean requestsFire() {
        return this.requestFire;
    }

    boolean requestsBurn() {
        return this.requestBurn;
    }
}
