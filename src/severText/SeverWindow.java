package severText;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.InetAddress;
//没写完
public class SeverWindow extends Frame
{

    private Secer s = new Secer();  
    private Label label;  
  
    public SeverWindow(String title) {  
        super(title);  
        label = new Label();  
        add(label, BorderLayout.PAGE_START);  
        label.setText("服务器已经启动");  
        this.addWindowListener(new WindowListener() {  
            public void windowOpened(WindowEvent e) {  
                new Thread(new Runnable() {  
                    public void run() {  
                        try {  
                            s.start();  
                            label.setText("服务器开始");  

                        } catch (Exception e) {  
                            // e.printStackTrace();  
                        }  
                    }  
                }).start();  
            }  
  
            public void windowIconified(WindowEvent e) {  
            }  
  
            public void windowDeiconified(WindowEvent e) {  
            }  
  
            public void windowDeactivated(WindowEvent e) {  
            }  
  
            public void windowClosing(WindowEvent e) {  
                System.exit(0);  
            }  
  
            public void windowClosed(WindowEvent e) {  
            }  
  
            public void windowActivated(WindowEvent e) {  
            }  
        });  
    }  
  
    /** 
     * @param args 
     */  
//    public static void main(String[] args) throws IOException {  
//        InetAddress address = InetAddress.getLocalHost();  
//        SeverWindow window = new SeverWindow("文件上传服务端：" + address.getHostAddress());  
//        window.setSize(400, 300);  
//        window.setVisible(true);  
//  
//    }  
  
}  