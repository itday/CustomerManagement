package application;

import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Main {
    private JFrame jFrame;

    private static final int HEIGHT = 700;
    private static final int WEIDTH = 480;

    /**
     * Flag: Save button in details view/tab enabled or not.
     * <br>
     * <code>true</code> (default): Save button in details tab is always enabled
     * (checks are executed after clicking save).
     * <br>
     * <code>false</code>: Save button is only enabled when checks are passed.
     */
    public static boolean saveButtonEnabled = true;

    /**
     * Flag about name checking in details tab.
     * <br>
     * <code>true</code> forbidden: "von Helen" allowed: "Von Helen"
     * <br>
     * <code>false</code> (default): forbidden: "von helen" allowed: "von Helen"
     */
    public static boolean hardNameChecking = false;

    /**
     * Flag enables some defects (if set to <code>true</code>).
     * <li>Empty fields allowed.</li>
     * <li>Multiply spaces/hyphen between words (name and street checks) will be
     * ignored.
     * Also allowed: "Ada -Helen"</li>
     * <li>Double entries in database allowed.</li>
     */
    public static boolean enableDefects = false;

    public Main(String[] args) {
        setParameters();

        jFrame = new JFrame("Customer Management");
        jFrame.setSize(HEIGHT, WEIDTH);
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIcon(jFrame);
        Controller controller = new Controller();
        controller.initialize(jFrame);
    }

    private void setParameters() {
        saveButtonEnabled = Boolean.parseBoolean(System.getProperty("saveButtonEnabled", "true"));
        hardNameChecking = Boolean.parseBoolean(System.getProperty("hardNameChecking", "false"));
        enableDefects = Boolean.parseBoolean(System.getProperty("enableDefects", "false"));
    }

    public static void setIcon(JFrame frame) {
        try {
            frame.setIconImage(ImageIO.read(Main.class.getResourceAsStream("/application/img/CustomerManagerLogo.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Main(args);
    }
}