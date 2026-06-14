// CourseReg.java
class CourseReg {
  private Course course;
  private String session;
  private int semester;
  private Mark mark;

  public CourseReg(Course course, String session, int semester, Mark mark) {
    this.course = course;
    this.session = session;
    this.semester = semester;
    this.mark = mark;
  }

  public Course getCourse() { return this.course; }
  public Mark getMark() { return this.mark; }
  
  public String toString() {
    return course.getCode() + "\t" + mark.totalMark() + "\t" + mark.grade();
  }
}