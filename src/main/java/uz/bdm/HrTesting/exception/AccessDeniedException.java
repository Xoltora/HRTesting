package uz.bdm.HrTesting.exception;

/**
 * User: Ismoil
 * Date: 10/1/2020
 * Time: 4:29 AM
 */
public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException(String message) {
        super(message);
    }
}
