package javastatement;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * encapsulates the process of create a file .java from of source code gived, 
 * compile and run the java class dinamically
 *
 * @author William Yanez <wyanez@gmail.com>
 */
public class JavaStatement {
    
    /**
     * Compiles and runs the short code segment.
     *
     * @param sourceCode source code for compile
     * @return a boolean that indicates if was sucessfully or not
     * @throws IOException if there was an error creating the source file.
     */
    public boolean doRun(String sourceCode) throws IOException {
        // Create a temp. file
        File sourceFile = createSourceFile(sourceCode);
        String filename = sourceFile.getName();
        String classname = filename.substring(0, filename.length() - 5);
        // Compile
        String[] args = new String[]{
            "-d", System.getProperty("user.dir"),
            filename
        };
        
        int status = com.sun.tools.javac.Main.compile(args);
        boolean result = status == 0;
        // Run
        switch (status) {
            case 0:  // OK
                // Make the class file temporary as well

                File f = new File(sourceFile.getParent(), classname + ".class");
                f.deleteOnExit();

                try {
                    // Try to access the class and run its main method                    
                    Class clazz = Class.forName(classname);
                    Method main = clazz.getMethod("main", new Class[]{String[].class});
                    main.invoke(null, new Object[]{new String[0]});
                } catch (InvocationTargetException ex) {
                    // Exception in the main method that we just tried to run
                    showMsg("Exception in main: " + ex.toString());
                    result = false;
                } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | 
                        NoSuchMethodException | SecurityException ex) {
                    showMsg(ex.toString());
                    result = false;
                }
                break;
            case 1:
                showMsg("Compile status: ERROR");
                break;
            case 2:
                showMsg("Compile status: CMDERR");
                break;
            case 3:
                showMsg("Compile status: SYSERR");
                break;
            case 4:
                showMsg("Compile status: ABNORMAL");
                break;
            default:
                showMsg("Compile status: Unknown exit status");
        }
        return result;
    }
    
    private File createSourceFile(String sourceCode) throws IOException{
        File file = File.createTempFile("jav", ".java",
         new File(System.getProperty("user.dir")));

        // Set the file to be deleted on exit
        file.deleteOnExit();

        // Get the file name and extract a class name from it
        String filename = file.getName();
        //System.out.println("Creando Archivo "+file.getAbsolutePath());
        String classname = filename.substring(0, filename.length() - 5);

        try ( // Output the source
            PrintWriter out = new PrintWriter(new FileOutputStream(file))) {
            out.println("/**");
            out.println(" * Source created on " + new Date());
            out.println(" */");
            out.println("public class " + classname + " {");
            out.println("    public static void main(String[] args) throws Exception {");
            
            // Your short code segment
            out.print("        ");
            out.println(sourceCode);
            
            out.println("    }");
            out.println("}");
            
            // Flush and close the stream
            out.flush();
        }
        return file;
    }
    
    /**
     * Shows a message to the user.
     *
     * @param msg the message
     */
    private void showMsg(String msg) {
        System.err.println(msg);
    }

}
