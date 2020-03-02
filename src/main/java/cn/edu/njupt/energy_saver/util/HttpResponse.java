package cn.edu.njupt.energy_saver.util;

public class HttpResponse<T> {
    private boolean success;
    private String errMsg;
    private Integer errCode;
    private T data;

    private HttpResponse(boolean success, String errMsg, Integer errCode, T data) {
        this.success = success;
        this.errMsg = errMsg;
        this.errCode = errCode;
        this.data = data;
    }

    public HttpResponse() {
    }

    public static <T> HttpResponse success(T data) {
        return new HttpResponse<>(true, null, null, data);
    }

    public static HttpResponse<Void> failure(String errMsg, Integer errCode) {
        return new HttpResponse<>(false, errMsg, errCode, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
