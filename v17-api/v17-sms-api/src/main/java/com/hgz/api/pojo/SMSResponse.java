package com.hgz.api.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author huangguizhao
 */
public class SMSResponse {

    //{"Message":"OK","RequestId":"4356F613-4A2F-4E92-A196-70862470C3CD","BizId":"123506073525482939^0","Code":"OK"}
    //@JsonProperty("Message")
    private String Message;
    //@JsonProperty("RequestId")
    private String RequestId;
    private String BizId;
    private String Code;

    @Override
    public String toString() {
        return "SMSResponse{" +
                "Message='" + Message + '\'' +
                ", RequestId='" + RequestId + '\'' +
                ", BizId='" + BizId + '\'' +
                ", Code='" + Code + '\'' +
                '}';
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        message = message;
    }

    public String getRequestId() {
        return RequestId;
    }

    public void setRequestId(String requestId) {
        RequestId = requestId;
    }

    public String getBizId() {
        return BizId;
    }

    public void setBizId(String bizId) {
        BizId = bizId;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }
}
