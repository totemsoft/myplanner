/*
 * JDialog.java
 *
 * Created on 5 September 2002, 15:31
 */

package au.com.totemsoft.swing;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDialog;


public class SplashWindow2 extends JDialog implements Runnable {

    private JDialog hiddenModalDialog;

    private String source;

    private long waitTime;

    private Thread progressRunner;

    /** Creates new form JDialog */
    public SplashWindow2() {
        this(null, SwingUtil.getSharedOwnerFrame(), 0);
    }

    public SplashWindow2(String source) {
        this(source, SwingUtil.getSharedOwnerFrame(), 0);
    }

    public SplashWindow2(java.awt.Frame parent) {
        this(null, parent, 0);
    }

    public SplashWindow2(String source, java.awt.Frame parent) {
        this(source, parent, 0);
    }

    public SplashWindow2(String source, java.awt.Frame parent, long waitTime) {
        this(source, parent, waitTime, true);
    }

    public SplashWindow2(String source, java.awt.Frame parent, long waitTime,
            final boolean autoProgress) {
        this(source, parent, waitTime, autoProgress, true);
    }

    public SplashWindow2(String source, java.awt.Frame parent, long waitTime,
            final boolean autoProgress, boolean modal) {

        super(parent == null ? SwingUtil.getSharedOwnerFrame() : parent);

        this.source = source;
        this.waitTime = waitTime;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        initComponents();
        initComponents2();
        // if ( source == null )
        jProgressBar1.setPreferredSize(new java.awt.Dimension(
                screenSize.width / 4, 15));
        pack();

        Dimension size = getSize();
        setLocation(screenSize.width / 2 - (size.width / 2), screenSize.height
                / 2 - (size.height / 2));

        if (modal) {
            // hiddenModalDialog = new JDialog(
            // SwingUtils.getSharedOwnerFrame(), true );
            // hiddenModalDialog.setDefaultCloseOperation(
            // hiddenModalDialog.DO_NOTHING_ON_CLOSE );
            // hiddenModalDialog.setLocation( (int) screenSize.getWidth(), (int)
            // screenSize.getHeight() );
            // hiddenModalDialog.pack();
        }

        progressRunner = new Thread("SplashWindow2::progressRunner") {
            public void run() {
                if (SplashWindow2.this.waitTime > 0) {
                    new Thread("SplashWindow2::waitRunner") {
                        public void run() {
                            try {
                                sleep(SplashWindow2.this.waitTime);
                            } catch (Exception e) {
                            }
                            close();
                            System.out.println("THREAD " + getName()
                                    + " is finished.");
                        }
                    }.start();
                }

                while (!isInterrupted()) {
                    synchronized (this) {
                        try {
                            wait(500);

                            if (autoProgress)
                                updateProgressBar(1);

                            // SplashWindow2.this.toFront();

                        } catch (InterruptedException e) {
                            System.err.println(e.getMessage());
                            break;
                        }
                    }
                }

                System.out.println("THREAD " + getName() + " is finished.");

            }

        };

        super.setVisible(true);
        setModal(modal);

    }

    /*
     * public void setVisible( boolean value ) { // do nothing } public void
     * show() { // do nothing }
     */

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        jLabel = new javax.swing.JLabel();
        jProgressBar = new javax.swing.JProgressBar();

        if (source != null)
            jLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                    source)));
        jLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel
                .setBorder(new javax.swing.border.LineBorder(
                        java.awt.Color.black));
        getContentPane().add(jLabel, java.awt.BorderLayout.CENTER);

        getContentPane().add(jProgressBar, java.awt.BorderLayout.SOUTH);

    }// GEN-END:initComponents

    private void initComponents2() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();

        jProgressBar1.setForeground(java.awt.Color.blue);
        jProgressBar1.setBackground(this.getBackground());

        jProgressBar1.setFont(new java.awt.Font("Arial", 0, 10));
        jProgressBar1.setBorderPainted(true);
        jProgressBar1.setBorder(new javax.swing.border.LineBorder(
                java.awt.Color.lightGray));

        // jProgressBar1.setBorder(new
        // javax.swing.border.EtchedBorder(java.awt.Color.lightGray, null));
        // jProgressBar1.setBorder(new
        // javax.swing.border.EtchedBorder(java.awt.Color.lightGray, null));

        jButton1 = new javax.swing.JButton();
        setTitle("Processing ...");
        setResizable(false);

        getContentPane().removeAll();
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog();
            }
        });

        jLabel1.setText("        ");
        jLabel1.setFont(new java.awt.Font("Arial", 0, 11));

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jPanel1.add(jLabel1);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.add(jProgressBar1);

        jButton1.setText("Close");
        jButton1.setFont(new java.awt.Font("Arial", 0, 11));
        jButton1.setPreferredSize(new java.awt.Dimension(67, 20));
        jButton1.setMaximumSize(new java.awt.Dimension(67, 20));
        jButton1.setMinimumSize(new java.awt.Dimension(67, 20));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel2.add(jButton1);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        pack();

    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        // Add your handling code here:
        closeDialog();
    }

    private void closeDialog() {
        setVisible(false);
        dispose();
    }

    /** Closes the dialog */
    /**
     * @param args
     *            the command line arguments
     */
    static void main(String args[]) {
        final SplashWindow2 sw = new SplashWindow2(null
        // "/client/Image/splash.gif"
                , null, 10000);
        sw.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                sw.close();
                System.exit(0);
            }
        });
        sw.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                sw.close();
                System.exit(0);
            }
        });
        sw.setStringPainted("Generating Report ... Please wait ...");
        new Thread(sw, "SplashWindowThread").start();

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel;

    private javax.swing.JProgressBar jProgressBar;

    // End of variables declaration//GEN-END:variables
    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JProgressBar jProgressBar1;

    private javax.swing.JButton jButton1;

    // "Generating Report... Please Wait..."
    public void setStringPainted(String s) {
        if (s == null || s.trim().length() == 0) {
            jProgressBar1.setString(null);
            jProgressBar1.setStringPainted(false);
        } else {
            jProgressBar1.setValue(99);
            jLabel1.setText(s);
            jProgressBar1.setValue(10);
            // jProgressBar1.setString(s);
            jProgressBar1.setStringPainted(true);
        }
    }

    public void updateProgressBar(int n) {
        jProgressBar1.setValue(jProgressBar1.getValue() + n);

        if (jProgressBar1.getValue() == jProgressBar1.getMaximum())
            jProgressBar1.setValue(0);
    }

    public void run() {
        jProgressBar1.setValue(0);
        progressRunner.start();

        if (hiddenModalDialog != null)
            hiddenModalDialog.setVisible(true);

    }

    public void close() {

        if (hiddenModalDialog != null)
            hiddenModalDialog.setVisible(false);

        if (progressRunner == null)
            return; // already closed

        setVisible(false);
        progressRunner.interrupt();
        progressRunner = null;
        dispose();

    }

}
