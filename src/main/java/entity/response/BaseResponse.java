package entity.response;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 响应基类
 */
public class BaseResponse {
    public static final Integer SuccessCode = 0;
    /**
     * 状态码
     */
    @JSONField(name = "code")
    private Integer code;
    /**
     * 信息
     */
    @JSONField(name = "msg")
    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return this.code == SuccessCode;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
