package javastatement;

import java.awt.Frame;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main entry point JavaStatement App.
 * @author  William Yanez <wyanez@gmail.com>
 */
public class Main {

    /**     
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Create the frame and set it to close on exit

        Frame f = new JavaStatementGui();
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Window w = e.getWindow();
                w.setVisible(false);
                w.dispose();
                System.exit(0);
            }
        });

        f.pack();
        f.setVisible(true);
        System.out.println("Working Dir: " + System.getProperty("user.dir"));
        try {
            addPath(System.getProperty("user.dir"));
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        showClasspath();
    }
    
    public static void showClasspath () {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        URL[] urls = ((URLClassLoader)cl).getURLs();
        System.out.println("CLASSPATH:");
        for(URL url: urls){
        	System.out.println(url.getFile());
        }         
   }
    
    //need to do add path to Classpath with reflection since the URLClassLoader.addURL(URL url) method is protected:
    //source: https://stackoverflow.com/questions/7884393/can-a-directory-be-added-to-the-class-path-at-runtime
    public static void addPath(String s) throws Exception {
        File f = new File(s);
        URI u = f.toURI();
        URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class<URLClassLoader> urlClass = URLClassLoader.class;
        Method method = urlClass.getDeclaredMethod("addURL", new Class[]{URL.class});
        method.setAccessible(true);
        method.invoke(urlClassLoader, new Object[]{u.toURL()});
    }
}
