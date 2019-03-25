package severText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;

public class ServerThread extends Thread
{
    // 和本线程相关的Socket
    Socket skt = null;

    public static List<ChatBean> out_1 = new ArrayList<ChatBean>();

    public static List<Socket> clientSocketList = new ArrayList<Socket>();
    // 服务器端口

    public ServerThread(Socket socket)
    {
        this.skt = socket;
    }

    @Override
    public void run()
    {
        FileOutputStream fos = null;
        boolean flag = false;
        int userId = -1;
        // TODO Auto-generated method stub
        System.out.println("接收到Socket请求");
        // 接受客户端信息
        BufferedReader in;
        try
        {
            while (true)
            {
                in = new BufferedReader(
                        new InputStreamReader(skt.getInputStream(), "UTF-8"));
                String str = in.readLine();
                // 读取客户端的信息
                // ，1为客户端接收图片，2为客户端上传图片，3为注册账号,4为登陆,5为更新个人资料
                System.out.println("str: " + str);
                if (str.equals("0"))
                {
                    PrintWriter out = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(skt.getOutputStream())),
                            true);
                    String pictureTime = in.readLine();
                    String teacherName = in.readLine();
                    UserJdbc aJdbc = new UserJdbc();
                    String address = aJdbc.pictureAddress(teacherName,
                            pictureTime);
                    String[] ads = address.split(",");
                    int length = -1;
                    if (ads[0].equals("") || ads == null)
                    {

                    }
                    else
                    {
                        length = ads.length;
                        address = ads[0];
                    }
                    if (address == null || address.equals("null"))
                    {
                        out.write(String.valueOf(0));
                        out.close();
                        break;
                    }
                    out.write(String.valueOf(length));
                    out.close();
                    in.close();
                    skt.close();
                    break;
                }
                else if (str.equals("1"))
                {
                    // 发送图片
                    DataOutputStream dos = new DataOutputStream(
                            skt.getOutputStream());
                    String pictureTime = in.readLine();
                    String teacherName = in.readLine();
                    UserJdbc aJdbc = new UserJdbc();
                    String address = aJdbc.pictureAddress(teacherName,
                            pictureTime);
                    String[] ads = address.split(",");
                    int length = -1;
                    if (ads[0].equals("") || ads == null)
                    {

                    }
                    else
                    {
                        length = ads.length;
                        address = ads[0];
                    }
                    if (address == null || address.equals("null"))
                    {
                        break;
                    }
                    dos.writeInt(length);
                    dos.flush();
                    for (int i = 0; i < length; i++)
                    {
                        FileInputStream fis = new FileInputStream(ads[i]);
                        int size = fis.available();
                        byte[] data = new byte[size];
                        fis.read(data);
                        dos.writeInt(size);
                        dos.write(data);
                        dos.flush();
                        System.out.println("size: " + size);
                        System.out.println("i: " + i);
                    }
                    dos.close();
                    in.close();
                    skt.close();
                    break;
                    // 。。。。
                }
                else if (str.equals("2"))
                {
                    // 接收客户端文件
                    DataInputStream dis;
                    dis = new DataInputStream(skt.getInputStream());
                    PrintWriter writer = new PrintWriter(skt.getOutputStream());
                    String trueName = dis.readUTF();
                    fos = new FileOutputStream("/root/Public/" + trueName);
                    byte[] inputByte = new byte[1024 * 8];
                    int length;
                    while ((length = dis.read(inputByte, 0,
                            inputByte.length)) > 0)
                    {
                        System.out.println("正在接收数据..." + length);
                        flag = true;
                        fos.write(inputByte, 0, length);
                        fos.flush();
                    }
                    System.out.println("图片接收完成");
                    fos.close();
                    dis.close();
                    // 服务器发送消息
                    writer.println(flag);// 返回是否接收到图片
                    writer.flush();
                    writer.close();
                    in.close();
                    skt.close();
                    break;
                }
                else if (str.equals("3"))
                {
                    String uName = in.readLine();
                    System.out.println(uName);
                    String[] users = uName.split(",");
                    UserJdbc user = new UserJdbc();
                    userId = user.createUser(users[0], users[1]);
                    PrintWriter out = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(skt.getOutputStream())),
                            true);
                    out.println(String.valueOf(userId));
                    out.flush();
                    out.close();
                    in.close();
                    skt.close();
                    break;
                }
                else if (str.equals("4"))
                {
                    String uName = in.readLine();
                    System.out.println(uName);
                    String[] users = uName.split(",");
                    AccountSvcBean userBean = new AccountSvcBean();
                    UserJdbc user = new UserJdbc();
                    System.out.println("users:" + users.length);
                    if (users.length == 0 || users.length == 1)
                    {
                        continue;
                    }
                    userBean = user.selectUserPassword(users[0]);
                    if (userBean == null)
                    {
                        PrintWriter out = new PrintWriter(
                                new BufferedWriter(new OutputStreamWriter(
                                        skt.getOutputStream(), "UTF-8")),
                                true);
                        out.println("-2");
                        out.flush();
                        out.close();
                        in.close();
                        skt.close();
                        break;
                    }
                    System.out.println("password = " + users[1]);
                    if (users[1].equals(userBean.getPassword()))
                    {
                        PrintWriter out = new PrintWriter(
                                new BufferedWriter(new OutputStreamWriter(
                                        skt.getOutputStream(), "UTF-8")),
                                true);
                        out.println(userBean.getUserId());
                        out.flush();
                        out.println(userBean.getUserName());
                        out.flush();
                        out.println(userBean.getUserZhiwu());
                        out.flush();
                        System.out.println("用户名称：" + userBean.getUserName());
                        out.close();
                        in.close();
                        skt.close();
                        break;
                    }
                    else
                    {
                        PrintWriter out = new PrintWriter(
                                new BufferedWriter(new OutputStreamWriter(
                                        skt.getOutputStream(), "UTF-8")),
                                true);
                        out.println("-1");
                        out.flush();
                        out.close();
                        in.close();
                        skt.close();
                        break;

                    }

                }
                else if (str.equals("5"))
                {
                    String userId1 = "-1";
                    UserJdbc user = new UserJdbc();
                    //
                    DataInputStream dis;
                    dis = new DataInputStream(skt.getInputStream());
                    PrintWriter writer = new PrintWriter(skt.getOutputStream());
                    String string = dis.readUTF();
                    String trueName = null;
                    String tu_name = null;
                    try
                    {
                        dis = new DataInputStream(skt.getInputStream());
                        trueName = dis.readUTF();
                    }
                    catch (Exception e)
                    {
                        // TODO: handle exception
                    }

                    tu_name = "/root/Public/" + trueName;
                    // tu_name = "D:/fuWuQi/" + trueName;
                    fos = new FileOutputStream("/root/Public/" + trueName);
                    // fos = new FileOutputStream("D:/fuWuQi/" + trueName);
                    byte[] inputByte = new byte[8192];

                    long length;
                    length = dis.readLong();
                    dis = new DataInputStream(skt.getInputStream());
                    while (true)
                    {
                        if (length >= 8192)
                        {
                            dis.readFully(inputByte);
                            fos.write(inputByte, 0, 8192);
                        }
                        else
                        {
                            byte[] smallBuf = new byte[(int) length];
                            dis.readFully(smallBuf);
                            fos.write(smallBuf);
                            break;
                        }
                        length -= 8192;
                        System.out.println("正在接收数据..." + length);
                        flag = true;
                        // fos.write(inputByte, 0, length);
                        fos.flush();
                    }
                    System.out.println("图片接收完成");
                    fos.close();
                    string = string + "," + tu_name;
                    String users[] = string.split(",");
                    System.out.println("String: " + string);
                    user.updateUserInformation(users[0], users[1], users[2],
                            users[3], users[4]);
                    fos.close();
                    dis.close();
                    in.close();
                    skt.close();
                    break;
                }
                else if (str.equals("6"))
                {
                    String[] teacher = new String[200];
                    UserJdbc user = new UserJdbc();
                    teacher = user.teacher();
                    PrintWriter out = new PrintWriter(
                            new BufferedWriter(new OutputStreamWriter(
                                    skt.getOutputStream(), "UTF-8")),
                            true);
                    String str1;
                    str1 = teacher[0] + ",";
                    for (int i = 1; i < teacher.length; i++)
                    {
                        str1 = str1 + teacher[i] + ",";
                    }
                    System.out.println("str :" + str1);
                    out.println(str1);
                    out.flush();
                    out.close();
                    in.close();
                    skt.close();
                    break;

                }
                else if (str.equals("7"))
                {
                    String[] keCheng = new String[200];
                    String[] class_name = new String[200];
                    UserJdbc user = new UserJdbc();
                    String name = in.readLine();
                    keCheng = user.keCheng(name).getKeCheng();
                    class_name = user.keCheng(name).getBanji();
                    PrintWriter out = new PrintWriter(
                            new BufferedWriter(new OutputStreamWriter(
                                    skt.getOutputStream(), "UTF-8")),
                            true);
                    String str1;
                    System.out.println("name :" + name);
                    str1 = keCheng[0] + ",";
                    for (int i = 1; i < keCheng.length; i++)
                    {
                        str1 = str1 + keCheng[i] + ",";
                    }
                    String str2;
                    str2 = class_name[0] + ",";
                    for (int i = 1; i < class_name.length; i++)
                    {
                        str2 = str2 + class_name[i] + ",";
                    }
                    System.out.println("str2 :" + str2);
                    out.println(str1);
                    out.flush();
                    out.println(str2);
                    out.flush();
                    out.close();
                    in.close();
                    skt.close();
                    break;
                }
                else if (str.equals("8"))
                {
                    // 接收客户端文件
                    DataInputStream dis;
                    dis = new DataInputStream(skt.getInputStream());
                    PrintWriter writer = new PrintWriter(skt.getOutputStream());
                    String string = dis.readUTF();
                    String trueName = null;
                    String tu_name = null;
                    int i = dis.readInt();
                    for (int a = 0; a < i; a++)
                    {
                        try
                        {
                            dis = new DataInputStream(skt.getInputStream());
                            trueName = dis.readUTF();
                        }
                        catch (Exception e)
                        {
                            // TODO: handle exception
                        }

                        if (a == 0)
                        {
                            tu_name = "/root/Public/" + trueName;
                            // tu_name = "D:/fuWuQi/" + trueName;
                        }
                        else
                        {
                            tu_name = tu_name + "," + "/root/Public/"
                                    + trueName;
                        }
                        fos = new FileOutputStream("/root/Public/" + trueName);
                        // fos = new FileOutputStream("D:/fuWuQi/" + trueName);
                        byte[] inputByte = new byte[8192];

                        long length;
                        length = dis.readLong();
                        dis = new DataInputStream(skt.getInputStream());
                        while (true)
                        {
                            if (length >= 8192)
                            {
                                dis.readFully(inputByte);
                                fos.write(inputByte, 0, 8192);
                            }
                            else
                            {
                                byte[] smallBuf = new byte[(int) length];
                                dis.readFully(smallBuf);
                                fos.write(smallBuf);
                                break;
                            }
                            length -= 8192;
                            System.out.println("正在接收数据..." + length);
                            flag = true;
                            // fos.write(inputByte, 0, length);
                            fos.flush();
                        }
                        fos.close();
                        System.out.println("a: " + a);
                    }
                    System.out.println("图片接收完成");
                    fos.close();
                    dis.close(); // 服务器发送消息
                    writer.println(flag);// 返回是否接收到图片
                    writer.flush();
                    writer.close();
                    string = string + "," + tu_name;
                    System.out.println("String: " + string);
                    UserJdbc jdbc = new UserJdbc();
                    jdbc.createBean(string);
                    in.close();
                    skt.close();
                    break;
                }
                else if (str.equals("9"))
                {
                    System.out.println("进入9");
                    String[] jilu;
                    UserJdbc user1 = new UserJdbc();
                    String name = in.readLine();
                    System.out.println("naem: " + name);
                    jilu = user1.getBean(name);
                    System.out.println("进入bean");
                    PrintWriter out = new PrintWriter(
                            new BufferedWriter(new OutputStreamWriter(
                                    skt.getOutputStream(), "UTF-8")),
                            true);
                    out.println(Integer.toString(jilu.length));
                    out.flush();
                    for (int i = 0; i < jilu.length; i++)
                    {
                        out.println(jilu[i]);
                        System.out.println("记录：" + jilu[i] + "个数：" + i);
                        out.flush();
                    }
                    out.close();
                    in.close();
                    skt.close();
                    System.out.println("出来了");
                    break;
                }
                else if (str.equals("10"))
                {
                    String string;
                    string = in.readLine();
                    string = string + "," + null;
                    UserJdbc jdbc = new UserJdbc();
                    jdbc.createBean(string);
                    in.close();
                    skt.close();
                    break;
                }
                else if (str.equals("11"))
                {
                    String string;
                    string = in.readLine();
                    UserJdbc jdbc = new UserJdbc();
                    String aString = jdbc.cishu(string);
                    PrintWriter out = new PrintWriter(
                            new BufferedWriter(new OutputStreamWriter(
                                    skt.getOutputStream(), "UTF-8")),
                            true);
                    out.println(aString);
                    out.flush();
                    out.close();
                    in.close();
                    skt.close();
                    break;
                }
                else if (str.equals("12"))
                {

                    String userId_xiaoxi = in.readLine();
                    String Xiaoxi = null;
                    String sendUserId = in.readLine();
                    List<String> xx = new ArrayList<String>();

                    // 聊天室发送消息
                    while ((Xiaoxi = in.readLine()) != null)
                    {
                        System.out.println("客户机说：" + Xiaoxi);
                        xx.add(Xiaoxi);
                        UserJdbc userJdbc = new UserJdbc();
                        userJdbc.setXiaoxi(Xiaoxi, userId_xiaoxi, sendUserId);// userId_xiaoxi发给sendUserId
                    }
                    UserJdbc userJdbc = new UserJdbc();
                    String sas = userJdbc.getName(userId_xiaoxi);
                    for (int j = 0; j < out_1.size(); j++)
                    {
                        if (sendUserId.equals("0"))
                        {
                            for (int i = 0; i < xx.size(); i++)
                            {
                                if (!out_1.get(j).getAccept_name()
                                        .equals(userId_xiaoxi))
                                {
                                    out_1.get(j).getOut()
                                            .println(xx.get(i) + "," + sas);
                                    out_1.get(j).getOut().flush();
                                }

                            }
                        }
                        if (out_1.get(j).getAccept_name().equals(sendUserId))
                        {
                            for (int i = 0; i < xx.size(); i++)
                            {
                                out_1.get(j).getOut()
                                        .println(xx.get(i) + "," + sas);
                                out_1.get(j).getOut().flush();
                            }
                        }
                    }
                    in.close();
                    skt.close();
                    break;
                }
                else if (str.equals("13"))
                {
                    // 聊天室接收消息
                    String userId_xiaoxi = in.readLine();
                    UserJdbc userJdbc = new UserJdbc();
                    ArrayList<String> xiaoxi = userJdbc
                            .getXiaoxi(userId_xiaoxi);
                    if (xiaoxi != null)
                    {
                        PrintWriter out = new PrintWriter(
                                new BufferedWriter(new OutputStreamWriter(
                                        skt.getOutputStream(), "UTF-8")),
                                true);
                        for (int j = 0; j < xiaoxi.size(); j++)
                        {
                            out.println(xiaoxi.get(j));
                            out.flush();
                            System.out.println("服务器发送消息 ：" + xiaoxi.get(j));
                        }
                        ChatBean chatBean = new ChatBean();
                        chatBean.setAccept_name(userId_xiaoxi);
                        chatBean.setOut(out);
                        out_1.add(chatBean);
                    }
                    break;

                }
                else if (str.equals("14"))
                {
                    // 聊天室列表
                    ArrayList<String> list = new ArrayList<String>();
                    list = null;
                    UserJdbc userJdbc = new UserJdbc();
                    list = userJdbc.getLiebiao();
                    if (list != null)
                    {
                        PrintWriter out = new PrintWriter(
                                new BufferedWriter(new OutputStreamWriter(
                                        skt.getOutputStream(), "UTF-8")),
                                true);
                        for (int j = 0; j < list.size(); j++)
                        {
                            out.println(list.get(j));
                            out.flush();
                        }
                    }
                    skt.close();
                    break;

                }
                else if (str.equals("15"))
                {
                    String userId_1 = in.readLine();
                    for (int i = 0; i < out_1.size(); i++)
                    {
                        if (out_1.get(i).getAccept_name().equals(userId_1))
                        {
                            out_1.get(i).getOut().close();
                            out_1.remove(i);
                            i--;
                        }
                    }
                }
                else if (str.equals("16"))
                {
                    Tim tim = new Tim();
                    String section = tim.isBelong();
                    String date;
                    Date dd = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                    date = sdf.format(dd);
                    System.out.println("日期：" + date);
                    System.out.println("节数：" + section);
                    String course_id = in.readLine();
                    String xinxi = null;
                    UserJdbc userJdbc = new UserJdbc();
                    xinxi = userJdbc.getxinxi(date, section, course_id);
                    System.out.println("信息：" + xinxi);
                    if (xinxi != null)
                    {
                        PrintWriter out = new PrintWriter(
                                new BufferedWriter(new OutputStreamWriter(
                                        skt.getOutputStream(), "UTF-8")),
                                true);
                        out.println(xinxi);
                        out.flush();
                        out.close();
                    }

                }
                else if (str.equals("17"))
                {
                    String teacher_id = in.readLine();
                    String fanhui = null;
                    UserJdbc userJdbc = new UserJdbc();
                    fanhui = userJdbc.getjihua(teacher_id);
                    if (fanhui != null)
                    {
                        PrintWriter out = new PrintWriter(
                                new BufferedWriter(new OutputStreamWriter(
                                        skt.getOutputStream(), "UTF-8")),
                                true);
                        out.println(fanhui);
                        out.flush();
                        out.close();
                    }
                }
                else if (str.equals("18"))
                {
                    String teacher_Id = in.readLine();
                    String time = in.readLine();
                    String bot = in.readLine();
                    UserJdbc userJdbc = new UserJdbc();
                    userJdbc.updatejiua(teacher_Id, time, bot);
                }
                else if (str.equals("19"))
                {
                    System.out.println("已经联通");
                }
                else if (str.equals("20"))
                {
                    String teacherName = in.readLine();
                    String classname = in.readLine();
                    List<String> className = new ArrayList<String>();
                    UserJdbc user = new UserJdbc();
                    className = user.className(teacherName, classname);
                    PrintWriter out = new PrintWriter(
                            new BufferedWriter(new OutputStreamWriter(
                                    skt.getOutputStream(), "UTF-8")),
                            true);
                    if (className != null && !className.isEmpty()
                            && className.size() > 0)
                    {

                        String str1 = className.get(0);
                        for (int i = 0; i < className.size(); i++)
                        {
                            if (i == 0)
                            {
                                str1 = className.get(0);
                            }
                            else
                            {
                                str1 = str1 + "," + className.get(i);
                            }
                        }
                        System.out.println("20号str :" + str1);
                        out.println(str1);
                    }
                    out.flush();
                    out.close();
                    in.close();
                    skt.close();
                    break;

                }
                in.close();
                skt.close();
                break;
            }

        }
        catch (UnsupportedEncodingException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        catch (IOException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}
