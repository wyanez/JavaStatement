package javastatement;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.SystemColor;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Program that lets you run a short segment of Java code.
 * @author Shawn Silverman 
 * William Yanez <wyanez@gmail.com> updates & improvements
 */
public class JavaStatementGui extends Frame implements ActionListener {
    
    private static final Font FONT_CODE = new Font("Monospaced", Font.PLAIN, 12);
    private static final  int TEXT_COLS = 80;
    private TextArea textCode;
    private TextArea textImport;
    private OutputArea textOutput;
    private Button runBtn;
    private Button cancelBtn;
    private Button sopBtn;
    private Button clearBtn;
    private Button copyClipBtn;
    
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
        panel.setLayout(new BorderLayout());
        textCode = new TextArea(10, TEXT_COLS);
        textCode.setFont(FONT_CODE);
        
        textImport = new TextArea(5, TEXT_COLS);
        textImport.setFont(FONT_CODE);
        textImport.setText("import java.io.*;");

        textOutput = new OutputArea();
        textOutput.setRows(10);
        textOutput.setColumns(TEXT_COLS);

        panel.add(textImport,BorderLayout.NORTH);
        panel.add(textCode,BorderLayout.CENTER);
        panel.add(textOutput,BorderLayout.SOUTH);

        this.add(panel, BorderLayout.CENTER);

        runBtn = new Button("Run!");
        runBtn.addActionListener(this);

        cancelBtn = new Button("Cancel");
        cancelBtn.addActionListener(this);

        clearBtn = new Button("Clear Output");
        clearBtn.addActionListener(this);

        sopBtn = new Button("System.out.println()");
        sopBtn.addActionListener(this);
        
        copyClipBtn = new Button("Copy Clipboard");
        copyClipBtn.addActionListener(this);

        Panel panelBtn = new Panel();
        panelBtn.add(runBtn);
        panelBtn.add(cancelBtn);
        panelBtn.add(clearBtn);
        panelBtn.add(sopBtn);
        panelBtn.add(copyClipBtn);
        this.add(panelBtn, BorderLayout.SOUTH);

        this.setBackground(SystemColor.control);        
        textCode.requestFocus();
    }
    
    /**
     * Listens to actions from the run button.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (runBtn == source) {
            String sourceCode = textCode.getText().trim();
            String importCode = textImport.getText().trim();
            if(!sourceCode.isEmpty()){
                // Disable the run button while we're running
                runBtn.setEnabled(false);
                try {
                    javaStmt.doRun(sourceCode,importCode);
                } catch (IOException ex) {
                    System.err.println(ex.toString());
                }
                runBtn.setEnabled(true);
            }
        }

        if (cancelBtn == source) {
            textCode.setText("import java.io.*;");
            textCode.setText("");
            textOutput.setText("");
            textCode.requestFocus();
        }

        if (clearBtn == source) {
            textOutput.setText("");
            textCode.requestFocus();
        }

        if (sopBtn == source) {
            textCode.insert("System.out.println();", textCode.getCaretPosition());
            textCode.setCaretPosition(textCode.getCaretPosition() - 2);
            textCode.requestFocus();
        }
        
        if(copyClipBtn == source){
            String sourceCode = textCode.getText().trim();
            String importCode = textImport.getText().trim();
            if(!sourceCode.isEmpty()) copyCodeToClipboard(sourceCode, importCode);
            
        }
    }
    
    /**
     * Copy snippets code to Clipboard
     * @param sourceCode
     * @param importCode 
     */
    public void copyCodeToClipboard(String sourceCode, String importCode){
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        
        String importCodeCommented = Arrays.stream(importCode.split(System.lineSeparator()))
                                    .map( (line) -> "//" + line)
                                    .collect(Collectors.joining(System.lineSeparator()));
        
        String code =  importCodeCommented + System.lineSeparator() + sourceCode;
        Transferable contents = new StringSelection(code);
        clipboard.setContents(contents, null);
        System.out.println("Código copiado al clipboard Ok!");
    }
    

}
