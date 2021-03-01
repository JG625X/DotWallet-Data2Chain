package entity.response;

import com.alibaba.fastjson.annotation.JSONField;

public class GetAccessTokenResponse extends BaseResponse {
    private GetAccessTokenData data;

    public GetAccessTokenData getData() {
        return data;
    }

    public void setData(GetAccessTokenData data) {
        this.data = data;
    }

    public class GetAccessTokenData {
        @JSONField(name = "access_token")
        private String accessToken;
        @JSONField(name = "expires_in")
        private Integer expiresIn;
        @JSONField(name = "token_type")
        private String tokenType;

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public Integer getExpiresIn() {
            return expiresIn;
        }

        public void setExpiresIn(Integer expiresIn) {
            this.expiresIn = expiresIn;
        }

        public String getTokenType() {
            return tokenType;
        }

        public void setTokenType(String tokenType) {
            this.tokenType = tokenType;
        }
    }

}
