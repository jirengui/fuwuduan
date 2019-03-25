package severText;

public class AccountSvcBean {
    private int userId;
    private String account;
    private String password;
    private String userName;
    private String userZhiwu;

    public AccountSvcBean() {
	}
    
    public AccountSvcBean(String account, String password)
    {
        this.account = account;
        this.password = password;
    }
    public AccountSvcBean(int userId, String account, String password, String userName)
    {
        this.userId = userId;
        this.account = account;
        this.password = password;
        this.userName = userName;
    }
    public int getUserId()
    {
        return userId;
    }
    public void setUserId(int userId)
    {
        this.userId = userId;
    }
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    public String getAccount()
    {
        return account;
    }
    public String getPassword()
    {
        return password;
    }
    public String getUserName()
    {
        return userName;
    }

    public String getUserZhiwu()
    {
        return userZhiwu;
    }

    public void setUserZhiwu(String userZhiwu)
    {
        this.userZhiwu = userZhiwu;
    }
}
