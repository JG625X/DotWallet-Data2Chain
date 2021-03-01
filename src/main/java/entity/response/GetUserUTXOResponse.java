package entity.response;

import entity.UTXO;

public class GetUserUTXOResponse extends BaseResponse {
    private UTXO[] data;

    public UTXO[] getData() {
        return data;
    }

    public void setData(UTXO[] data) {
        this.data = data;
    }

}
