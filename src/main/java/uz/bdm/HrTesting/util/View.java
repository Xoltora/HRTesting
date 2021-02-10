package uz.bdm.HrTesting.util;

public final class View {

    public interface ApiResponse {

    }
    public interface Question extends ApiResponse{
    }

    public interface QuestionWithAnswer extends Question, Answer {
    }

    public interface QuestionWithRightAnswer extends Question, AnswerWithRight{

    }

    public interface Answer {
    }

    public interface AnswerWithRight extends Answer {
    }

    public interface Exam extends ApiResponse{

    }

    public interface ExamWithResult extends Exam{

    }

    public interface ExamWithResultUnchecked extends Exam, ResultWithUnchecked{

    }

    public interface Result{

    }

    public interface ResultWithUnchecked extends Result{

    }
}
