// Mark.java
class Mark {
  private int coursework;
  private int finalExam;

  public Mark(int coursework, int finalExam) {
    this.coursework = coursework;
    this.finalExam = finalExam;
  }

  public void setMark(int coursework, int finalExam) {
    this.coursework = coursework;
    this.finalExam = finalExam;
  }

  public int totalMark() {
    return this.coursework + this.finalExam;
  }

  public String grade() {
    int total = totalMark();
    if (total >= 85) return "A";
    if (total >= 80) return "A-";
    if (total >= 75) return "B+";
    if (total >= 70) return "B";
    if (total >= 65) return "B-";
    if (total >= 60) return "C+";
    if (total >= 55) return "C";
    return "F";
  }

  public double points() {
    int total = totalMark();
    if (total >= 85) return 4.00;
    if (total >= 80) return 3.67;
    if (total >= 75) return 3.33;
    if (total >= 70) return 3.00;
    if (total >= 65) return 2.67;
    if (total >= 60) return 2.33;
    if (total >= 55) return 2.00;
    return 0.00;
  }
}