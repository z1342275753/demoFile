package servlet.entity;

public class Question {

    private Integer age;
    private String title;
    private String answer;

    public Question(){};
    public Question(Integer age, String answer) {
        this.age=age;
//        this.title = title;
        this.answer = answer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "Question{" +
                "age=" + age +
                ", title='" + title + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
