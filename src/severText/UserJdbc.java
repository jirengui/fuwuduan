package severText;

import java.awt.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.mysql.jdbc.Statement;

public class UserJdbc
{

    public UserJdbc()
    {
        // TODO Auto-generated constructor stub
    }

    public int createUser(String account, String password)
    {
        String sql = "insert into account(account,password)value(?,?)";
        String sql1 = "insert into user_information(userId)value(?)";
        Connection conn = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        int useId = -1;
        try
        {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);// RETURN_GENERATED_KEYS�����Զ����ɵ�keyֵ
            ps.setString(1, account);
            ps.setString(2, password);
            // EBPLogger.debug(ps.toString());
            int sqlNum = ps.executeUpdate(); // �����ֶ�
            rs = ps.getGeneratedKeys();
            while (rs.next())
            {
                useId = rs.getInt(1);
            }
            ps.close();
            rs.close();
            ps1 = conn.prepareStatement(sql1);
            ps1.setInt(1, useId);
            ps1.executeUpdate();
            ps1.close();

        }
        catch (Exception e)
        {
            // TODO: handle exception
            e.printStackTrace();
        }
        finally
        {
            DBUtil.close(conn, ps);

        }
        return useId;
    }

    // �����û��������û���Ϣ
    public AccountSvcBean selectUserPassword(String userName)
    {
        String sql1 = "select count(*) from account where account=?;";
        String sql = "select user_id, account, password from account where account=?;";
        String sql2 = "select userName, userZhiwu from user_information where userId=?;";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        AccountSvcBean accountSvcBean = null;

        try
        {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql1);
            ps.setString(1, userName);
            rs = ps.executeQuery();
            if (rs.next())
            {
                if (rs.getInt(1) == 0)
                {
                    ps.close();
                    rs.close();
                    return accountSvcBean;
                }
            }
            ps.close();
            rs.close();
            int userId = -1;
            ps = conn.prepareStatement(sql);
            ps.setString(1, userName);
            // EBPLogger.debug(ps.toString());
            rs = ps.executeQuery();
            if (rs.next())
            {
                accountSvcBean = new AccountSvcBean(rs.getString(2),
                        rs.getString(3));
                userId = rs.getInt(1);
                accountSvcBean.setUserId(userId);
            }
            ps.close();
            rs.close();
            if (userId != -1)
            {
                ps = conn.prepareStatement(sql2);
                ps.setInt(1, userId);
                // EBPLogger.debug(ps.toString());
                rs = ps.executeQuery();
                if (rs.next())
                {
                    accountSvcBean.setUserName(rs.getString(1));
                    accountSvcBean.setUserZhiwu(rs.getString(2));
                    System.out.println(accountSvcBean.getUserZhiwu());
                }
                ps.close();
                rs.close();
            }
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            DBUtil.close(conn, ps, rs);

        }
        return accountSvcBean;
    }

    public void updateUserInformation(String userId, String path,
            String userName, String userAccount, String userJieShao)
    {
        String sql = "update user_information set userTouXiang=?,userName=?,userZhiwu=?,userJieShao=? where userId=?";
        Connection conn = null;
        PreparedStatement ps = null;
        int useId = -1;
        try
        {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);// RETURN_GENERATED_KEYS�����Զ����ɵ�keyֵ
            ps.setInt(5, Integer.parseInt(userId));
            ps.setString(1, userJieShao);
            ps.setString(2, path);
            ps.setString(3, userAccount);
            ps.setString(4, userName);
            int sqlNum = ps.executeUpdate(); // �����ֶ�
            // EBPLogger.debug(ps.toString());
            ps.close();
        }
        catch (Exception e)
        {
            // TODO: handle exception
            e.printStackTrace();
        }
        finally
        {
            DBUtil.close(conn, ps);

        }
    }

    public String[] teacher()
    {

        String sql = "select teacher from teacher;";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        java.util.List<String> list = new ArrayList<>();

        try
        {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            // EBPLogger.debug(ps.toString());
            rs = ps.executeQuery();
            int i = 0;
            while (rs.next())
            {
                list.add(rs.getString(1));
                i++;
            }
            String[] teacher = new String[list.size()];
            for (int j = 0; j < list.size(); j++)
            {
                teacher[j] = list.get(j);
            }
            ps.close();
            rs.close();
            return teacher;
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            DBUtil.close(conn, ps, rs);

        }
        return new String[]{"��������ʦ"};
    }
    public java.util.List<String> className(String jiaoshiName, String banji)
    {

        String sql = "select course_name from timetable where teacher=? and class_name=?;";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        java.util.List<String> list = new ArrayList<>();
        String weekstr = "����һ";
        Date today = new Date();
        Calendar c=Calendar.getInstance();
        c.setTime(today);
        int weekday=c.get(Calendar.DAY_OF_WEEK);
        switch (weekday)
        {
            case 1:
                weekstr = "������";
                break;
            case 2:
                weekstr = "����һ";
                break;
            case 3:
                weekstr = "���ڶ�";
                break;
            case 4:
                weekstr = "������";
                break;
            case 5:
                weekstr = "������";
                break;
            case 6:
                weekstr = "������";
                break;
            case 7:
                weekstr = "������";
                break;
            default:
                break;
        }

        try
        {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            // EBPLogger.debug(ps.toString());
            ps.setString(1, jiaoshiName);
            ps.setString(2, banji);
            rs = ps.executeQuery();
            int i = 0;
            while (rs.next())
            {
                String name = rs.getString(1);
                String[] strs = name.split(",");
                for(int a = 0; a < strs.length; a++){
                    String[] str1 = strs[a].split(" ");
                    System.out.println("���ڣ�" + weekday + "���ݣ�" + str1[1]);
                    if (str1[1].equals(weekstr))
                    {
                        String[] ss = str1[2].split("]");
                        System.out.println("�༶��" + ss[1]);
                        list.add(ss[1]);
                    }
                }
                i++;
            }
     
            ps.close();
            rs.close();
            return list;
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            DBUtil.close(conn, ps, rs);

        }
        list.add("���������");
       return list;
    }


    public TeacherBean keCheng(String name)
    {
        String sql = "select course_name,class_name,course_zhenname from timetable where teacher=?;";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String weekstr = "����һ";
        Date today = new Date();
        Calendar c=Calendar.getInstance();
        c.setTime(today);
        int weekday=c.get(Calendar.DAY_OF_WEEK);
        switch (weekday)
        {
            case 1:
                weekstr = "������";
                break;
            case 2:
                weekstr = "����һ";
                break;
            case 3:
                weekstr = "���ڶ�";
                break;
            case 4:
                weekstr = "������";
                break;
            case 5:
                weekstr = "������";
                break;
            case 6:
                weekstr = "������";
                break;
            case 7:
                weekstr = "������";
                break;
            default:
                break;
        }
        
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");//�������ڸ�ʽ
        Date now =null;
        Date beginTime = null;
        Date endTime = null;
        String jieke = "��һ��-�ڶ���";
        try {
            now = df.parse(df.format(new Date()));
            beginTime = df.parse("08:00");
            endTime = df.parse("9:40");
            if (belongCalendar(now, beginTime, endTime))
            {
                jieke = "��һ��-�ڶ���";
            }
            beginTime = df.parse("10:10");
            endTime = df.parse("11:50");
            if (belongCalendar(now, beginTime, endTime))
            {
                jieke = "������-���Ľ�";
            }
         
            beginTime = df.parse("13:30");
            endTime = df.parse("15:10");
            if (belongCalendar(now, beginTime, endTime))
            {
                jieke = "�����-������";
            }
         
            beginTime = df.parse("15:30");
            endTime = df.parse("17:10");
            if (belongCalendar(now, beginTime, endTime))
            {
                jieke = "���߽�-�ڰ˽�";
            }
         
         
        } catch (Exception e) {
            e.printStackTrace();
        }


        TeacherBean teacherBean = new TeacherBean();
        String[] teacher = new String[200];
        String[] banji = new String[200];

        try
        {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            // EBPLogger.debug(ps.toString());
            rs = ps.executeQuery();
            int i = 0;
            while (rs.next())
            {
                String name1 = rs.getString(1);
                String[] strs = name1.split(",");
                for(int a = 0; a < strs.length; a++){
                    String[] str1 = strs[a].split(" ");
                    System.out.println("���ڣ�" + weekstr + "���ݣ�" + str1[1] + "�ڿ�1��"+ str1[2] + "�ڿ�2��"+jieke);
                    if (str1[1].equals(weekstr))
                    {
                        if (str1[2].contains(jieke))
                        {
                            teacher[i] = rs.getString(3);
                            banji[i] = rs.getString(2);
                            String[] ss = str1[2].split("]");
                            System.out.println("���ң�" + ss[1]);
                        }
            
                    }
                }
                
                i++;
                System.out.println("teacher[i] :" + teacher[i]);

            }
            ps.close();
            rs.close();
            teacherBean.setBanji(banji);
            teacherBean.setKeCheng(teacher);
            return teacherBean;
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            DBUtil.close(conn, ps, rs);

        }
        return teacherBean;
    }

    public int createBean(String bean)
    {
        // 9��
        String[] users = bean.split(",");// 0ΪuserId,1Ϊteacher_name
                                         // 2Ϊcourse_name 3Ϊ�༶ 4Ϊ��ѧ¥ 5Ϊ��ʦ״̬
                                         // 6Ϊѧ��״̬ 7ΪͼƬ��ַ
        String sql = "insert into record(submitter_id,date,teacher_name,course_name,class,classroom,teacher_status,student_status,image_path)value(?,?,?,?,?,?,?,?,?)";
        Connection conn = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        int useId = -1;
        try
        {
            String tuPian = users[7];
            for (int i = 8; i < users.length; i++)
            {
                tuPian = tuPian + "," + users[i];
            }
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// �������ڸ�ʽ
            String date = df.format(new Date());// new Date()Ϊ��ȡ��ǰϵͳʱ��
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);// RETURN_GENERATED_KEYS�����Զ����ɵ�keyֵ
            ps.setInt(1, Integer.parseInt(users[0]));
            ps.setString(2, date);
            ps.setString(3, users[1]);
            ps.setString(4, users[2]);
            ps.setString(5, users[3]);
            ps.setString(6, users[4]);
            ps.setString(7, users[5]);
            ps.setString(8, users[6]);
            ps.setString(9, tuPian);
            // EBPLogger.debug(ps.toString());
            int sqlNum = ps.executeUpdate(); // �����ֶ�
            rs = ps.getGeneratedKeys();
            while (rs.next())
            {
                useId = rs.getInt(1);
            }
            ps.close();
            rs.close();

        }
        catch (Exception e)
        {
            // TODO: handle exception
            e.printStackTrace();
        }
        finally
        {
            DBUtil.close(conn, ps);

        }
        return useId;
    }

    public String[] getBean(String userId)
    {
        System.out.println("����UserJdbc");
        String sql = "select date,teacher_name,course_name,class,classroom,teacher_status,student_status,image_path from record where submitter_id=? order by date desc;";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        java.util.List<String> list = new ArrayList<>();
      
        try
        {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, userId);
            // EBPLogger.debug(ps.toString());
            rs = ps.executeQuery();
            int i = 0;
            while (rs.next())
            {
                // 1Ϊ����2Ϊteacher_name 3Ϊcourse_name 4Ϊ�༶ 5Ϊ��ѧ¥ 6Ϊ��ʦ״̬ 7Ϊѧ��״̬
                // 8ΪͼƬ��ַ
                list.add(rs.getString(1) + "," + rs.getString(2) + ","
                        + rs.getString(3) + "," + rs.getString(4) + ","
                        + rs.getString(5) + "," + rs.getString(6) + ","
                        + rs.getString(7) + "," + rs.getString(8));
                System.out.println("bean :" + list.get(i));
                i++;
            }
            String[] teacher = new String[list.size()];
            for (int j = 0; j < list.size(); j++)
            {
             teacher[j] = list.get(j);   
            }
            ps.close();
            rs.close();
            return teacher;
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            DBUtil.close(conn, ps, rs);

        }
        return new String[]{"null"};
    }

    public String cishu(String id)
    {
        String sql = "select count(*) from record where submitter_id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int cishua = 0;
        try
        {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            // EBPLogger.debug(ps.toString());
            rs = ps.executeQuery();
            int i = 0;
            while (rs.next())
            {
                cishua = rs.getInt(1);
            }
            ps.close();
            rs.close();
            return String.valueOf(cishua);
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            DBUtil.close(conn, ps, rs);

        }
        return String.valueOf(cishua);
    }

    public String pictureAddress(String teacherName, String pictureTime)
    {
        String sql = "select image_path from record where date=? and teacher_name=?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String ads = null;
        try
        {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(2, teacherName);
            ps.setString(1, pictureTime);
            // EBPLogger.debug(ps.toString());
            rs = ps.executeQuery();
            int i = 0;
            while (rs.next())
            {
                ads = rs.getString(1);
            }
            ps.close();
            rs.close();
            System.out.println("ADS: " + ads);
            return ads;
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            DBUtil.close(conn, ps, rs);

        }
        return ads;
    }

    public void setXiaoxi(String xiaoxi, String senduserId, String acceptuserId)
    {
        String sql = "insert into xiaoxi(senduserId,xiaoxi,acceptuserId)value(?,?,?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try
        {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);// RETURN_GENERATED_KEYS�����Զ����ɵ�keyֵ
            ps.setString(1, senduserId);
            ps.setString(2, xiaoxi);
            ps.setString(3, acceptuserId);
            int sqlNum = ps.executeUpdate(); // �����ֶ�
            // EBPLogger.debug(ps.toString());
            ps.close();

        }
        catch (Exception e)
        {
            // TODO: handle exception
            e.printStackTrace();
        }
        finally
        {
            DBUtil.close(conn, ps);

        }
    }

    public ArrayList<String> getXiaoxi(String userId)
    {
        String sql = "select xiaoxi,senduserId from xiaoxi where acceptuserId=? OR acceptuserId=?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<String> list = new ArrayList<String>();

        try
        {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, userId);
            ps.setString(2, "0");
            // EBPLogger.debug(ps.toString());
            rs = ps.executeQuery();
            String string = null;
            while (rs.next())
            {
                String string2 = getName(rs.getString(2));
                string = rs.getString(1) + "," + string2;
                list.add(string);
            }
            ps.close();
            rs.close();
            if (!list.isEmpty())
            {
                return list;
            }

        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            DBUtil.close(conn, ps, rs);

        }
        return list;
    }

    public ArrayList<String> getLiebiao()
    {
        String sql = "select userName,userId from user_information";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<String> list = new ArrayList<String>();
        try
        {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            // EBPLogger.debug(ps.toString());
            rs = ps.executeQuery();
            String string = null;
            while (rs.next())
            {
                string = rs.getString(2) + "," + rs.getString(1);
                list.add(string);
            }
            ps.close();
            rs.close();
            if (list != null)
            {
                System.out.println("�����б�" + list.get(0));
                return list;
            }

        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            DBUtil.close(conn, ps, rs);

        }
        return list;
    }

    public String getName(String userId)
    {
        String sql = "select userName from user_information where userId=?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            // EBPLogger.debug(ps.toString());
            ps.setString(1, userId);
            rs = ps.executeQuery();
            String string = null;
            while (rs.next())
            {
                string = rs.getString(1);
            }
            ps.close();
            rs.close();
            if (string != null)
            {
                System.out.println("���֣�" + string);
                return string;
            }

        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            DBUtil.close(conn, ps, rs);

        }
        return null;
    }
    public String getxinxi(String date, String section, String course_id)
    {
        String sql = "select teacher,course_name,class,classroom from timetable where id=? and date=? and section=?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            // EBPLogger.debug(ps.toString());
            ps.setString(1, course_id);
            ps.setString(2, date);
            ps.setString(3, section);
            rs = ps.executeQuery();
            String string = null;
            while (rs.next())
            {
                string = rs.getString(1) + "," + rs.getString(2) + "," + rs.getString(3) + "," + rs.getString(4) ;
            }
            ps.close();
            rs.close();
            if (string != null)
            {
                System.out.println("��Ϣ��" + string);
                return string;
            }

        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            DBUtil.close(conn, ps, rs);

        }
        return null;
    }
    public String getjihua(String teacher_id)
    {
        ArrayList<String> list = new ArrayList<>();
        Date date=new Date();//ȡʱ��
        Date date1=new Date();//ȡʱ��
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,1);//��������������һ��.����������,������ǰ�ƶ�
        date=calendar.getTime(); //���ʱ���������������һ��Ľ�� 
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String tomorrow = formatter.format(date);//���������
        String today = formatter.format(date1);//���������
        System.out.println("��������ڣ�" + tomorrow + "��������ڣ�" + today);
        String sql = "select time,place,complete from plan where teacher_id=? and ((time=? or time=? ) or complete=?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try
        {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            // EBPLogger.debug(ps.toString());
            ps.setString(1, teacher_id);
            ps.setString(2, today);
            ps.setString(3, tomorrow);
            ps.setString(4, "false");
            rs = ps.executeQuery();
            String string = null;
            //1Ϊʱ�䣬2Ϊ�ص�, 3Ϊ״̬
            while (rs.next())
            {
                if (string == null)
                {
                    string = rs.getString(1) + "," + rs.getString(2) + "," + rs.getString(3);
                }else {
                    string = string + "/" + rs.getString(1) + "," + rs.getString(2)+ "," + rs.getString(3);
                }
                list.add(rs.getString(3));
            }
            ps.close();
            rs.close();
            for(int i = 0; i < list.size(); i++){
                if (list.get(i).equals("plan"))
                {
                    updatejiua(teacher_id, today, "false");
                    updatejiua(teacher_id, tomorrow, "false");
                }
      
            }
            if (string != null)
            {
                System.out.println("�ƻ���" + string);
                return string;
            }

        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            DBUtil.close(conn, ps, rs);

        }
        return null;
    }
    public void updatejiua(String teacher_Id, String time, String bot)
    {
        String sql = "update plan set complete=? where teacher_Id=? and time=?";
        Connection conn = null;
        PreparedStatement ps = null;
        int useId = -1;
        try
        {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);// RETURN_GENERATED_KEYS�����Զ����ɵ�keyֵ
            ps.setString(1, bot);
            ps.setString(2, teacher_Id);
            ps.setString(3, time);
            System.out.println("���£�" + bot + " " + teacher_Id + " " + time);
            int sqlNum = ps.executeUpdate(); // �����ֶ�
            // EBPLogger.debug(ps.toString());
            ps.close();
        }
        catch (Exception e)
        {
            // TODO: handle exception
            e.printStackTrace();
        }
        finally
        {
            DBUtil.close(conn, ps);

        }
    }

    /**
     * �ж�ʱ���Ƿ���ʱ�����
     * @param nowTime
     * @param beginTime
     * @param endTime
     * @return
     */
    public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }
}
