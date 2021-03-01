package entity.response;


import com.alibaba.fastjson.annotation.JSONField;

public class AutopayResponse extends BaseResponse {
    private AutopayData data;

    public AutopayData getData() {
        return data;
    }

    public void setData(AutopayData data) {
        this.data = data;
    }

    public class AutopayData {
        @JSONField(name = "order_id")
        private String orderID;
        @JSONField(name = "out_order_id")
        private String outOrderID;
        @JSONField(name = "amount")
        private Long amount;
        @JSONField(name = "user_id")
        private String userID;
        @JSONField(name = "fee")
        private Integer fee;
        @JSONField(name = "txid")
        private String txid;

        public String getOrderID() {
            return orderID;
        }

        public void setOrderID(String orderID) {
            this.orderID = orderID;
        }

        public String getOutOrderID() {
            return outOrderID;
        }

        public void setOutOrderID(String outOrderID) {
            this.outOrderID = outOrderID;
        }

        public Long getAmount() {
            return amount;
        }

        public void setAmount(Long amount) {
            this.amount = amount;
        }

        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }

        public Integer getFee() {
            return fee;
        }

        public void setFee(Integer fee) {
            this.fee = fee;
        }

        public String getTxid() {
            return txid;
        }

        public void setTxid(String txid) {
            this.txid = txid;
        }
    }
}
