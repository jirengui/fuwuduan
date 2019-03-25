package severText;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import severText.AccountSvcBean;

public class DBUtil {

	private static String driver = ConfigUtil.getProperValue("mysql.driver");
	private static String url = ConfigUtil.getProperValue("mysql.url");
	private static String username = ConfigUtil.getProperValue("mysql.username");
	private static String password = ConfigUtil.getProperValue("mysql.password");

	public static Connection getConnection() {
		try {
			return DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void close(Connection conn, PreparedStatement ps) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void close(Connection conn, PreparedStatement ps, ResultSet rs) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args)
    {
        String sql = "select user_id, account, password from account where account = ?;";
        Connection conn = null;
        PreparedStatement ps  = null;
        ResultSet rs = null;
        AccountSvcBean accountSvcBean = null;

        try
        {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
//            ps.setString(1, "cyp");
//            EBPLogger.debug(ps.toString());
            rs = ps.executeQuery();
            if (rs.next())
            {
                accountSvcBean = new AccountSvcBean(rs.getString(2),rs.getString(3));
                accountSvcBean.setUserId(rs.getInt(1));
            }
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            DBUtil.close(conn, ps, rs);
            
        }
        System.out.println(accountSvcBean);
        
    }
}
