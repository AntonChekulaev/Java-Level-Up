package exceptions;

import java.io.IOException;

public class MyException extends Exception {

    private String detail;

    public MyException(String a) {
        detail = a;
    }

    public String toString() {
        return "MyException! " + detail;
    }

}
