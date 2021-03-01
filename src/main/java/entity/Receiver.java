package entity;


import com.alibaba.fastjson.annotation.JSONField;

public class Receiver {
    @JSONField(serialize = false)
    private ReceiverType receiverType;
    private String content;
    private Long amount;

    public Receiver() {
    }

    public enum ReceiverType {
        Address("address"),
        Script("script"),
        Paymail("paymail"),
        PartialRawtx("partial_rawtx");

        public final String value;

        /**
         * @param value
         */
        private ReceiverType(final String value) {
            this.value = value;
        }
    }

    @JSONField(name = "type")
    public String getType() {
        return receiverType.value;
    }

    public ReceiverType getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(ReceiverType receiverType) {
        this.receiverType = receiverType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
