package shop.mookmall.com.core.exception;

import org.springframework.http.HttpStatus;
import shop.mookmall.com.dto.ResponseDTO;

public class Exception500 extends RuntimeException {

    public Exception500(String message) {
        super(message);
    }

    public ResponseDTO<?> body(){
        return new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR, "serverError", getMessage());
    }

    public HttpStatus status(){
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
