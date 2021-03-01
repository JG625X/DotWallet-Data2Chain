import com.alibaba.fastjson.JSON;
import entity.CoinType;
import entity.Receiver;
import entity.response.*;
import org.bitcoinj.params.AbstractBitcoinNetParams;
import org.bitcoinj.params.MainNetParams;
import util.HttpUtil;

import java.lang.reflect.Field;
import java.util.*;


public class DotWalletClient {
    private static final String DotWalletHost = "https://api.ddpurse.com";
    public static final AbstractBitcoinNetParams BSVNetParams = MainNetParams.get();
    private final String clientID;
    private final String secret;

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private DotWalletClient(String clientID, String secret) throws InterruptedException {
        this.clientID = clientID;
        this.secret = secret;
        //token时效为2h,需要定时重新获取
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    GetAccessTokenResponse resp = getAccessToken();
                    if (!resp.isSuccess()) {
                        throw new RuntimeException("get access token failed:" + resp.getMsg());
                    }
                    token = resp.getData().getAccessToken();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, 0, 60 * 60 * 1000);
        //wait for get token
        while (true) {
            if (this.token == null || this.token.equals("")) {
                Thread.sleep(100);
            } else {
                return;
            }
        }
    }

    public static DotWalletClient newInstance(String clientID, String secret) throws InterruptedException {
        return new DotWalletClient(clientID, secret);
    }

    /**
     * 获取access_token
     */
    public GetAccessTokenResponse getAccessToken() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("grant_type", "client_credentials");
        params.put("client_id", this.clientID);
        params.put("client_secret", this.secret);
        return doRequest(DotWalletHost + "/v1/oauth2/get_access_token", params, GetAccessTokenResponse.class);
    }

    /**
     * 注册xPub
     *
     * @param coinType    币种
     * @param userID      打点用户ID
     * @param walletIndex 钱包索引
     * @param xPub        公钥
     */
    public BaseResponse registerAutopayXPub(CoinType coinType, String userID, Integer walletIndex, String xPub) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userID);
        params.put("wallet_index", walletIndex);
        params.put("coin_type", coinType);
        params.put("xpub", xPub);
        params.put("seed", xPub);
        return doRequest(DotWalletHost + "/v1/user/register_autopay_xpub", params, BaseResponse.class);
    }

    /**
     * 获取UTXO
     *
     * @param coinType    币种
     * @param userID      打点用户ID
     * @param walletIndex 钱包索引
     */
    public GetUserUTXOResponse getAutopayUTXO(CoinType coinType, String userID, Integer walletIndex) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userID);
        params.put("wallet_index", walletIndex);
        params.put("coin_type", coinType);
        return doRequest(DotWalletHost + "/v1/user/get_autopay_utxo", params, GetUserUTXOResponse.class);
    }

    /**
     * 自动支付订单接口，用于代付数据上链矿工费
     */
    public AutopayResponse applyAutopayOrder(CoinType coinType, String outOrderID, String userID, String partialRawtx, String productID, String productName, String productDetail, String subject) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userID);
        params.put("out_order_id", outOrderID);
        params.put("subject", subject);
        params.put("coin_type", coinType);

        Map<String, Object> product = new HashMap<>();
        product.put("id", productID);
        product.put("name", productName);
        product.put("detail", productDetail);
        params.put("product", product);

        List<Receiver> receivers = new ArrayList<>();
        Receiver r = new Receiver();
        r.setAmount(0L);
        r.setContent(partialRawtx);
        r.setReceiverType(Receiver.ReceiverType.PartialRawtx);
        receivers.add(r);
        params.put("to", receivers);

        return doRequest(DotWalletHost + "/v1/transact/order/autopay", params, AutopayResponse.class);
    }

    public GetOrderResponse getOrder(String orderID) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("order_id", orderID);
        return doRequest(DotWalletHost + "/v1/transact/order/get_order", params, GetOrderResponse.class);
    }

    /**
     * 获取默克尔证明
     *
     * @param txid 交易txid
     */
    public GetMerkleProofResponse getMerkleProof(String txid) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("txid", txid);
        return doRequest(DotWalletHost + "/v1/bsvchain/get_merkleproof", params, GetMerkleProofResponse.class);
    }

    private <T extends BaseResponse> T doRequest(String requestURL, Map<String, Object> params, Class<T> tClass) throws Exception {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + this.token);
        String paramsJSON = JSON.toJSONString(params);
        System.out.println("#params: " + paramsJSON);
        String result = HttpUtil.httpPostWithJson(requestURL, paramsJSON, headerMap);
        System.out.println("#resp: " + result);
        T response;
        try {
            if (result.equals("")) {
                throw new RuntimeException("empty http result");
            }
            response = JSON.parseObject(result, tClass);
            return response;
        } catch (Exception e) {
            try {
                response = tClass.newInstance();
                Field code = tClass.getSuperclass().getDeclaredField("code");
                code.setAccessible(true);
                code.set(response, -1);
                Field msg = tClass.getSuperclass().getDeclaredField("msg");
                msg.setAccessible(true);
                msg.set(response, result);
                return response;
            } catch (Exception e1) {
                throw new RuntimeException(e1);
            }
        }
    }
}
