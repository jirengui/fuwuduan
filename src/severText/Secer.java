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
            // ��¼�ͻ�������
            int count = 0;
            while (true)
            {
                System.out.println("������������,�ȴ��ͻ�������.....");
                skt = server.accept();
                ServerThread serverThread = new ServerThread(skt);
                serverThread.start();
                System.out.println("�߳�������" +Thread.activeCount());
                count++;
                System.out.println("�����ӿͻ���������" + count);
            }
        }
        catch  (IOException e)
        {
            e.printStackTrace();
        }
    }
}