package severText;

public class TeacherBean {
    private String[] teacher_name  = new String[200];
    private String[] keCheng = new String[200];
    private String[] banji = new String[200];
    public TeacherBean()
    {}

    public String[] getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String[] teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String[] getKeCheng() {
        return keCheng;
    }

    public void setKeCheng(String[] keCheng) {
        this.keCheng = keCheng;
    }

    public String[] getBanji() {
        return banji;
    }

    public void setBanji(String[] banji) {
        this.banji = banji;
    }
}
