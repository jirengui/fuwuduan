package severText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Tim
{
    String bTime;
    String eTime;
    public Tim()
    {
        // TODO Auto-generated constructor stub
    
    }
    public String isBelong(){

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");//�������ڸ�ʽ
        Date now =null;
        Date beginTime1 = null, beginTime2 = null, beginTime3 = null, beginTime4 = null;
        Date endTime1 = null, endTime2 = null, endTime3 = null, endTime4 = null;
        try {
            now = df.parse(df.format(new Date()));
            beginTime1 = df.parse("08:00");
            endTime1 = df.parse("09:40");
            beginTime2 = df.parse("10:10");
            endTime2 = df.parse("11:50");
            beginTime3 = df.parse("13:30");
            endTime3 = df.parse("15:10");
            beginTime4 = df.parse("15:30");
            endTime4 = df.parse("17:10");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Boolean flag1 = belongCalendar(now, beginTime1, endTime1);
        Boolean flag2 = belongCalendar(now, beginTime2, endTime2);
        Boolean flag3 = belongCalendar(now, beginTime3, endTime3);
        Boolean flag4 = belongCalendar(now, beginTime4, endTime4);
        if (flag1)
        {
            return "һ";
        }
        else if (flag2)
        {
            return "��";
        }
        else if (flag3)
        {
            return "��";
        }
        else if(flag4)
        {
            return "��";
        }else {
            return "��";
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
