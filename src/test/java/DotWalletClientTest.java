import entity.CoinType;
import entity.response.*;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.junit.Before;
import org.junit.Test;


public class DotWalletClientTest {

    private DotWalletClient client;
    private DeterministicKey masterKey;
    private Integer walletIndex;
    private String userID;
    private final CoinType coinType = CoinType.BSV;

    @Before
    public void init() {
        //初始化DotWalletClient
        try {
            String clientID = "";
            String secret = "";
            client = DotWalletClient.newInstance(clientID, secret);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //初始化HD私钥
        masterKey = HDKeyDerivation.createMasterPrivateKey("this-is-a-test-wallet-a".getBytes());
        //初始化新钱包索引
        walletIndex = 6666;
        //打点提供的userID
        userID = "";
    }

    @Test
    public void getToken() throws Exception {
        System.out.println(client.getToken());
    }

    @Test
    public void registerXpub() throws Exception {
        //生成自己的HD钱包
        String xpub = masterKey.serializePrivB58(DotWalletClient.BSVNetParams);
        BaseResponse resp = client.registerAutopayXPub(coinType, userID, walletIndex, xpub);
    }

    @Test
    public void getAddress() throws Exception {
        DeterministicKey changeStep = HDKeyDerivation.deriveChildKey(masterKey, new ChildNumber(0, false));
        DeterministicKey privateKey = HDKeyDerivation.deriveChildKey(changeStep, new ChildNumber(0, false));
        System.out.println(privateKey.toAddress(DotWalletClient.BSVNetParams));
    }

    @Test
    public void getUserUTXO() throws Exception {
        GetUserUTXOResponse resp = client.getAutopayUTXO(coinType, userID, walletIndex);
    }

    @Test
    public void buildTransaction() throws Exception {
        //初始化交易
        BSVTransaction tx = new BSVTransaction(DotWalletClient.BSVNetParams);
        //获取utxo
        GetUserUTXOResponse resp = client.getAutopayUTXO(coinType, userID, walletIndex);
        if (!resp.isSuccess()) {
            throw new RuntimeException(resp.getMsg());
        }
        //要上链的数据
        String data = "006a0000aabbcc";
        //构建交易,签名交易
        String signedRawtx = tx.buildDataToChainTransaction(masterKey, resp.getData()[0], data);

        //下自动支付订单，请求分配的userID支付矿工费
        //商家内部订单号，任意字符串
        String outOrderID = System.currentTimeMillis() + "";

        AutopayResponse autopayResponse = client.applyAutopayOrder(coinType, outOrderID, userID, signedRawtx, "xxx", "data to chain", "productDetail", "this is a data-to-chain order.");
        if (!resp.isSuccess()) {
            throw new RuntimeException(resp.getMsg());
        }
        System.out.println("数据上链成功，txid: " + autopayResponse.getData().getTxid() + " orderID: " + autopayResponse.getData().getOrderID());
    }

    @Test
    public void getOrder() throws Exception {
        String orderID = "1365193115148754944";
        GetOrderResponse resp = client.getOrder(orderID);
        if (!resp.isSuccess()) {
            throw new RuntimeException(resp.getMsg());
        }
    }

    @Test
    public void getMerkleProof() throws Exception {
        String txid = "5d34fe70067f1621a586cc5c4ea2828f81c5a760fcb0906dddf0a1bd05a966b4";
        GetMerkleProofResponse resp = client.getMerkleProof(txid);
        if (!resp.isSuccess()) {
            throw new RuntimeException(resp.getMsg());
        }
    }

}




















