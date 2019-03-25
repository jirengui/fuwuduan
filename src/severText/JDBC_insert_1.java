package severText;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;
import severText.AccountSvcBean;

public class JDBC_insert_1
{

    public static void main(String[] args)
    {
        String sql = "insert into tb_account(account,password)value(?,?)";
        Connection conn = null;
        PreparedStatement ps  = null;
        ResultSet rs = null;
        AccountSvcBean accountSvcBean = null;

        try
        {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);//RETURN_GENERATED_KEYS返回自动生成的key�?
            ps.setString(1, "user3");
            ps.setString(2, "user2");
//            EBPLogger.debug(ps.toString());
            int sqlNum = ps.executeUpdate(); //更新字段
            rs = ps.getGeneratedKeys();
            while (rs.next())
            {
//                accountSvcBean = new AccountSvcBean(rs.getString(2),rs.getString(3));
//                accountSvcBean.setUserId(rs.getInt(1));
//                System.out.println(accountSvcBean);
                int useId = rs.getInt(1);
            }
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            DBUtil.close(conn, ps);
            
        }
    }

}
