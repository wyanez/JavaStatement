package javastatement;

/**
 * ***********************************************************************************
 *
 * This Component Class provides one JTextArea capable to capture and display
 * the System output and System err, any contanct and question to
 * acortiz@ucsm.edu.pe
 *
 * Based in Java Developer Connection(sm) (JDC) Tech Tips, October 21, 1999.
 * Wrote by Patrick Chan. Topic called "Capturing Standard Output in a Log File"
 *
 * Class : JOutputArea.java Version	: 1.0 22 Jun 2003 Author : Alan Ortiz Mejia
 * Date	: 22/06/2003
 *
 ************************************************************************************
 */
import java.awt.*;
import java.io.*;

/**
 * This Component Class provides one JTextArea capable to capture and display
 * the System output and System err
 *
 * Based in Java Developer Connection(sm) (JDC) Tech Tips, October 21, 1999.
 * Wrote by Patrick Chan. Topic called "Capturing Standard Output in a Log File"
 *
 * @version	: 1.0 22 Jun 2003
 * @author : Alan Ortiz Mejia
 */
public class OutputArea extends TextArea {

    private OutputAreaStream out;

    /**
     * Defines a JOutputArea that captures the output and err streams with the
     * default show options
     */
    public OutputArea() {
        out = new OutputAreaStream(System.out, this);
        setEditable(false);
    }

    /**
     * Defines a JOutputArea with the specified show options
     * @param sOut
     * @param sErr
     * @param mirror
     */
    public OutputArea(boolean sOut, boolean sErr, boolean mirror) {
        this();
        out.showOut(sOut);
        out.showErr(sErr);
        out.mirror = mirror;
    }

    /**
     * Specifies if this component shows the stream(s) also in console
     * @param b
     */
    public void setMirror(boolean b) {
        out.mirror = b;
    }

    /**
     * Sets if the JOutputArea shows the output stream
     * @param b
     */
    public void showOut(boolean b) {
        out.showOut(b);
    }

    /**
     * Sets if the JOutputArea shows the err stream
     * @param b
     */
    public void showErr(boolean b) {
        out.showErr(b);
    }

    private class OutputAreaStream extends PrintStream {

        PrintStream oldStdout;
        PrintStream oldStderr;
        TextArea outText;
        boolean mirror = false;

        // constructor
        public OutputAreaStream(PrintStream ps, OutputArea outArea) {
            super(ps);
            oldStdout = System.out;
            oldStderr = System.err;
            outText = outArea;
            setup();
        }

        private void setup() {
            System.setOut(this);
            System.setErr(this);
        }

        // show output?
        public void showOut(boolean b) {
            if (b) {
                System.setOut(this);
            } else {
                System.setOut(oldStdout);
            }
        }

        // show err?
        public void showErr(boolean b) {
            if (b) {
                System.setErr(this);
            } else {
                System.setErr(oldStderr);
            }
        }

        //Finalizes the component
        @Override
        public void finalize() {
            System.setOut(oldStdout);
            System.setErr(oldStderr);
        }

        // PrintStream override.
        @Override
        public void write(int b) {
            try {
                outText.append(Integer.toString(b));
            } catch (Exception e) {
                System.err.println(e);
                setError();
            }
            if (mirror) {
                super.write(b);
            }
        }
        // PrintStream override.

        @Override
        public void write(byte buf[], int off, int len) {
            try {
                outText.append(new String(buf, off, len));
            } catch (Exception e) {
                System.err.println(e);    
                setError();
            }
            if (mirror) {
                super.write(buf, off, len);
            }
        }
    }
}
