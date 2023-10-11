package cs208;

public class RegisteredStudent {
    private int studentId;
    private int classId;

    public RegisteredStudent(int studentId, int classId)
    {
        this.studentId = studentId;
        this.classId = classId;
    }

    public int getStudentId()
    {
        return studentId;
    }

    public void setStudentId(int studentId)
    {
        this.studentId = studentId;
    }

    public int getClassId()
    {
        return classId;
    }

    public void setClassId(int classId)
    {
        this.classId = classId;
    }


    @Override
    public String toString()
    {
        return "RegisteredStudent{" +
                "studentId='" + studentId + '\'' +
                ", classId='" + classId + '\'' +
                '}';
    }

}
