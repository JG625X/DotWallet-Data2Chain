package entity;

public enum CoinType {
    BSV("BSV"),
    BTC("BTC"),
    ETH("ETH");
    public final String value;

    private CoinType(String coinType) {
        this.value = coinType;
    }
}
