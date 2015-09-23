package com.mhzdev.apptemplate.Services.API;


public class BaseResponse<T> {
    public String status;
    public String error_code;
    public String error_message;
    public String lang;

    public T response;
}
