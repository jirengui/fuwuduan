package severText;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Secer extends Thread
{
    private static final int HOST_PORT = 12345;

    public static void main(String[] args)
    {

        try
        {
            ServerSocket server = new ServerSocket(HOST_PORT);
            Socket skt = null;
            // 记录客户端数量
            int count = 0;
            while (true)
            {
                System.out.println("服务器已启动,等待客户机连接.....");
                skt = server.accept();
                ServerThread serverThread = new ServerThread(skt);
                serverThread.start();
                System.out.println("线程总数：" +Thread.activeCount());
                count++;
                System.out.println("已链接客户机总数：" + count);
            }
        }
        catch  (IOException e)
        {
            e.printStackTrace();
        }
    }
}