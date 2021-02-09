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
}
