package cn.edu.njupt.energy_saver.exception;

public enum CustomError {


    UNAUTHORIZED(0, "Unauthorized Request"),
    NOT_FOUND(1, "User Not Found"),
    WROING_PASSWORD(2, "Wrong Password"),
    ;

    private int code;

    private String errMsg;

    CustomError(int code, String errMsg) {
        this.code = code;
        this.errMsg = errMsg;
    }

    public int getCode() {
        return this.code;
    }

    public String getErrMsg() {
        return errMsg;
    }
}
