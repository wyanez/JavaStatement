package javastatement;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.SystemColor;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Program that lets you run a short segment of Java code.
 * @author Shawn Silverman 
 * William Yanez <wyanez@gmail.com> updates & improvements
 */
public class JavaStatementGui extends Frame implements ActionListener {
    
    private TextArea text;
    private OutputArea output;
    private Button runBtn;
    private Button cancelBtn;
    private Button sopBtn;
    private Button clearBtn;
    
    private final JavaStatement javaStmt;
    /**
     * Create a new JavaStatement window.
     */
    public JavaStatementGui() {
        super("Java Statement");
        setupGui();
        javaStmt = new JavaStatement();
    }

    private void setupGui() {
        // Create the components
        Panel panel = new Panel();
        panel.setLayout(new GridLayout(2, 1, 5, 5));
        text = new TextArea(10, 45);
        text.setFont(new Font("Monospaced", Font.PLAIN, 12));

        output = new OutputArea();
        output.setRows(10);
        output.setColumns(45);

        panel.add(text);
        panel.add(output);

        this.add(panel, BorderLayout.CENTER);

        runBtn = new Button("Run!");
        runBtn.addActionListener(this);

        cancelBtn = new Button("Cancel");
        cancelBtn.addActionListener(this);

        clearBtn = new Button("Clear Output");
        clearBtn.addActionListener(this);

        sopBtn = new Button("System.out.println()");
        sopBtn.addActionListener(this);

        Panel p = new Panel();
        p.add(runBtn);
        p.add(cancelBtn);
        p.add(clearBtn);
        p.add(sopBtn);
        this.add(p, BorderLayout.SOUTH);

        this.setBackground(SystemColor.control);
    }

    

    /**
     * Listens to actions from the run button.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (runBtn == source) {
            String sourceCode = text.getText().trim();
            if(!sourceCode.isEmpty()){
                // Disable the run button while we're running
                runBtn.setEnabled(false);
                try {
                    javaStmt.doRun(sourceCode);
                } catch (IOException ex) {
                    System.err.println(ex.toString());
                }
                runBtn.setEnabled(true);
            }
        }

        if (cancelBtn == source) {
            text.setText("");
            output.setText("");
            text.requestFocus();
        }

        if (clearBtn == source) {
            output.setText("");
            text.requestFocus();
        }

        if (sopBtn == source) {
            text.insert("System.out.println();", text.getCaretPosition());
            text.setCaretPosition(text.getCaretPosition() - 2);
            text.requestFocus();
        }
    }
}
