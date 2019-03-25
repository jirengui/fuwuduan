package severText;

import java.io.PrintWriter;

public class ChatBean
{
    private String send_name;
    private String accept_name;
    private PrintWriter out;

    public ChatBean()
    {
        // TODO Auto-generated constructor stub
    }
    public ChatBean(String send, String accept, PrintWriter out)
    {
        // TODO Auto-generated constructor stub
        this.out = out;
        this.send_name = send;
        this.accept_name = accept;
    }

    public String getSend_name()
    {
        return send_name;
    }

    public void setSend_name(String send_name)
    {
        this.send_name = send_name;
    }

    public String getAccept_name()
    {
        return accept_name;
    }

    public void setAccept_name(String accept_name)
    {
        this.accept_name = accept_name;
    }

    public PrintWriter getOut()
    {
        return out;
    }

    public void setOut(PrintWriter out)
    {
        this.out = out;
    }

}
