package exceptions;

import java.io.IOException;

public class myException extends Exception {

    private String detail;

    public myException(String a) {
        detail = a;
    }

    public String toString() {
        return "MyException! " + detail;
    }

}
