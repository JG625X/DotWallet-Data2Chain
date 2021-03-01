package entity;

import com.alibaba.fastjson.annotation.JSONField;

public class UTXO {
    private String txid;
    private String addr;
    private Integer index;
    private Long value;
    @JSONField(name = "sigscript")
    private byte[] sigScript;
    private String badgeCode;

    public byte[] getSigScript() {
        return sigScript;
    }

    public void setSigScript(byte[] sigScript) {
        this.sigScript = sigScript;
    }

    @JSONField(name = "ownerid")
    private Long ownerID;
    @JSONField(name = "user_index")
    private Long userIndex;


    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
    
    public String getBadgeCode() {
        return badgeCode;
    }

    public void setBadgeCode(String badgeCode) {
        this.badgeCode = badgeCode;
    }

    public Long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Long ownerID) {
        this.ownerID = ownerID;
    }

    public Long getUserIndex() {
        return userIndex;
    }

    public void setUserIndex(Long userIndex) {
        this.userIndex = userIndex;
    }
}
