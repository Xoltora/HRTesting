package uz.bdm.HrTesting.dto;

import com.fasterxml.jackson.annotation.JsonView;
import uz.bdm.HrTesting.util.View;

import java.util.List;

public class ResponseData<T> {
    @JsonView(value = View.ApiResponse.class)
    private boolean accept;
    @JsonView(value = View.ApiResponse.class)
    private T data;
    @JsonView(value = View.ApiResponse.class)
    private String message;
    @JsonView(value = View.ApiResponse.class)
    private List<CustomError> errors;

    public ResponseData() {
    }

    public ResponseData(boolean accept, T data, String message, List<CustomError> errors) {
        this.accept = accept;
        this.data = data;
        this.message = message;
        this.errors = errors;
    }

    public ResponseData(boolean accept, String message) {
        this.accept = accept;
        this.message = message;
    }

    public boolean isAccept() {
        return accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CustomError> getErrors() {
        return errors;
    }

    public void setErrors(List<CustomError> errors) {
        this.errors = errors;
    }
}
