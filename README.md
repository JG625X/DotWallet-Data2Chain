# 打点数据上链Demo-v3.0

- [<span id="interface_intro"> 接口说明 </span>](#span-idinterface_intro-%E6%8E%A5%E5%8F%A3%E8%AF%B4%E6%98%8E-span)
  - [HOST](#host)
  - [Request](#request)
  - [Response](#response)
- [<span id="flow_intro"> 准备工作与注意事项 </span>](#span-idflow_intro-%E5%87%86%E5%A4%87%E5%B7%A5%E4%BD%9C%E4%B8%8E%E6%B3%A8%E6%84%8F%E4%BA%8B%E9%A1%B9-span)
  - [打点端](#%E6%89%93%E7%82%B9%E7%AB%AF)
  - [开发者端](#%E5%BC%80%E5%8F%91%E8%80%85%E7%AB%AF)
  - [注意事项](#%E6%B3%A8%E6%84%8F%E4%BA%8B%E9%A1%B9)
  - [身份授权（access token）](#%E8%BA%AB%E4%BB%BD%E6%8E%88%E6%9D%83access-token)
- [API](#api)
    - [注册xPub](#%E6%B3%A8%E5%86%8Cxpub)
    - [获取UTXO](#%E8%8E%B7%E5%8F%96utxo)
    - [提交自动支付订单](#%E6%8F%90%E4%BA%A4%E8%87%AA%E5%8A%A8%E6%94%AF%E4%BB%98%E8%AE%A2%E5%8D%95)
    - [查询订单](#%E6%9F%A5%E8%AF%A2%E8%AE%A2%E5%8D%95)
    - [查询交易默克尔证明](#%E6%9F%A5%E8%AF%A2%E4%BA%A4%E6%98%93%E9%BB%98%E5%85%8B%E5%B0%94%E8%AF%81%E6%98%8E)



### <span id="interface_intro"> 接口说明 </span>

#### HOST

    https://api.ddpurse.com

#### Request

- body参数均为json格式
- 请求头中需添加`Content-Type:application/json`

#### Response

- API接口均返回如下json对象，错误码`code=0`时成功，其他表示失败；`msg`描述错误原因； `data`为json对象或数组。

``` json
{
  "code": 0,
  "msg": "",
  "data": {}
}
```

### <span id="flow_intro"> 准备工作与注意事项 </span>

#### 打点端

打点向开发者提供数据上链付款用户ID。
User-ID:

#### 开发者端

1. 本地生成HD私钥
2. 将xpub注册至付款用户下以监控UTXO
3. 将`wallet_index`告知打点对接者
4. 交易均使用路径为`path(xpub)/0/0`的私钥进行签名，签名类型Hash-Type为`anyonecanpay|single`

#### 注意事项

	1、开发者构建交易rawtx时，数据上链的vout需放在首位，即vouts[0]
	2、找零地址填写UTXO的地址与金额

#### 身份授权（access token）

开发者进入[打点开放平台](https://developers.dotwallet.com)注册成为打点开发者，并创建应用，随后在邮件中收取`client_id`和`client_secret`。将`client_id`告知打点对接者。

**HTTP Request**

POST https://api.ddpurse.com/v1/oauth2/get_access_token

**Request Body**：

| 参数名        | 类型   | 必须 | 备注                       |
| ------------- | ------ | ---- | -------------------------- |
| client_id     | string | 是   | 开发者应用 ID              |
| client_secret | string | 是   | 开发者应用私钥             |
| grant_type    | string | 是   | 固定值: client_credentials |

**Response Body**

| 参数名       | 类型   | 说明                             |
| :----------- | :----- | :------------------------------- |
| access_token | string | 用于访问 API 接口的 access_token |
| expires_in   | int    | access_token 的过期时间(秒)      |
| token_type   | string | token 类型，返回固定值：Bearer   |


## API

### 注册xPub

**HTTP Request**

POST https://api.ddpurse.com/v1/user/register_autopay_xpub

**Request Header**

| 参数名        | 类型   | 是否必须 | 说明                                   |
| :------------ | :----- | :------- | -------------------------------------- |
| Authorization | string | 是       | Authorization: Bearer APP-ACCESS-TOKEN |

**Request Body**

| 参数名       | 类型   | 必须 | 备注                       |
| ------------ | ------ | ---- | -------------------------- |
| coin_type    | string | 是   | 固定值`BSV`                |
| user_id      | string | 是   | 用户 ID                    |
| wallet_index | int    | 是   | 钱包索引                   |
| xpub         | string | 是   | 用户公钥                   |
| seed         | string | 是   | 任意值，推荐此处填写xpub值 |

**Response Body**

成功：

``` json
{
  "code": 0,
  "msg": "",
  "data":{}
}
```

* 例子（curl）

```shell
# With shell, you can just pass the correct header with each request
curl --location --request POST 'https://api.ddpurse.com/v1/user/register_autopay_xpub' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIwZDE1OGJjMzYwNWZmZmUzMGY3MDQ2ZjBjOWM3ZTRiZiIsImV4cCI6MTYxNDIzNDIxN30.CfHsjHIGrj-OnqO7EFe5PQQaAlL5p4Dgn5u46hp8gQto-SJB-70Ojdua0Y_cZBOgEkppoJbXN6tyuAvjJ-8JXg' \
--header 'Content-Type: application/json' \
--data-raw '{
    "user_id":"6a1b2161094e32bf2c352910218048da",
    "wallet_index":10086,
    "coin_type":"BSV",
    "xpub":"xpub6ChjhQpXMfvfiy7FYrdXowaivVPRbvwSRUJSW919LCx1wK6AtgUyhz6nqLUAwaDd5bPjquHKZxpNqkMp5std1HYqcEn84TNfTUJ7Ni7w671",
    "seed":"xpub6ChjhQpXMfvfiy7FYrdXowaivVPRbvwSRUJSW919LCx1wK6AtgUyhz6nqLUAwaDd5bPjquHKZxpNqkMp5std1HYqcEn84TNfTUJ7Ni7w671"
}'
```

### 获取UTXO

**HTTP Request**

POST https://api.ddpurse.com/v1/user/get_autopay_utxo

**Request Header**

| 参数名        | 类型   | 是否必须 | 说明                                   |
| :------------ | :----- | :------- | -------------------------------------- |
| Authorization | string | 是       | Authorization: Bearer APP-ACCESS-TOKEN |

**Request Body**

| 参数名       | 类型   | 必须 | 备注        |
| ------------ | ------ | ---- | ----------- |
| coin_type    | string | 是   | 固定值`BSV` |
| user_id      | string | 是   | 用户 ID     |
| wallet_index | int    | 是   | 钱包索引    |

**Response Body**

| 参数名    | 类型   | 备注                                                   |
| --------- | ------ | ------------------------------------------------------ |
| height    | int    | 交易所在区块高度，-1表示交易还在内存池中，还未打包上块 |
| blockhash | string | 交易所在区块hash                                       |
| txid      | string | 交易ID                                                 |
| addr      | string | 地址                                                   |
| index     | int    | 该UTXO在交易中的位置                                   |
| value     | int    | 该UTXO金额                                             |
| SigScript | byte[] | 该UTXO锁定脚本                                         |

成功：

``` json
{
    "code": 0,
    "msg": "",
    "data": [
        {
            "height": -1,
            "timestamp": 1614323264,
            "blockhash": "",
            "txid": "40058932b637cb7c171f4ce4856c9fc35108974c0073bcc5b70909ae8bdca1cf",
            "addr": "mvQALp2ZNqedVEqHHBFeuz4VusrZapGCs1",
            "index": 2,
            "value": 998270,
            "SigScript": "dqkUo0EIJJRWvb/M3nYcNN9wtm4BvuqIrA==",
        }
    ]
}
```

* 例子（curl）

```shell
# With shell, you can just pass the correct header with each request
curl --location --request POST 'https://api.ddpurse.com/v1/user/get_autopay_utxo' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIwZDE1OGJjMzYwNWZmZmUzMGY3MDQ2ZjBjOWM3ZTRiZiIsImV4cCI6MTYxNDIzNDIxN30.CfHsjHIGrj-OnqO7EFe5PQQaAlL5p4Dgn5u46hp8gQto-SJB-70Ojdua0Y_cZBOgEkppoJbXN6tyuAvjJ-8JXg' \
--header 'Content-Type: application/json' \
--data-raw '{   
    "user_id":"6a1b2161094e32bf2c352910218048da",
   "coin_type":"BSV",
    "wallet_index":10086
}'
```

### 提交自动支付订单

**HTTP Request**

`POST https://api.ddpurse.com/v1/transact/order/autopay`

**Request Header**

| 参数名        | 类型   | 是否必须 | 说明                                   |
| :------------ | :----- | :------- | -------------------------------------- |
| Authorization | string | 是       | Authorization: Bearer APP-ACCESS-TOKEN |

**Request Body**

| 参数名       | 类型     | 是否必须 | 说明                                                  |
| :----------- | :------- | :------- | :---------------------------------------------------- |
| out_order_id | string   | 是       | 商家订单号，随机字符串，建议使用 UUID，订单号不可重复 |
| coin_type    | string   | 是       | 付款的币种，固定值`BSV`                               |
| user_id      | string   | 是       | 用户 ID                                               |
| +to          | object[] | 是       | 接收方列表                                            |
| └ type       | string   | 是       | 接收方类型，固定值`partial_rawtx`                     |
| └ content    | string   | 是       | rawtx                                                 |
| └ amount     | int      | 是       | 固定值`0`                                             |
| +product     | object   | 是       | 商品信息                                              |
| └ id         | string   | 是       | 商品的编号                                            |
| └ name       | string   | 是       | 商品名称                                              |
| └ detail     | string   | 否       | 商品描述                                              |
| subject      | string   | 否       | 订单标题                                              |

**Response Body**

| 参数名       | 类型   | 说明                            |
| :----------- | :----- | :------------------------------ |
| order_id     | string | 订单号                          |
| out_order_id | string | 商户订单号                      |
| user_id      | string | 付款用户 ID                     |
| amount       | int    | 交易金额，单位：聪 satoshi(bsv) |
| fee          | int    | 交易矿工费，单位：聪 satoshi    |
| txid         | string | 交易的 txid                     |

成功：

``` json
{
    "code": 0,
    "msg": "",
    "data": {
        "amount": 0,
        "badge_code": "",
        "fee": 198,
        "order_id": "1366230202601971712",
        "out_order_id": "l111231",
        "txid": "1f90d0fc584615acce96ad53ed12a3248f01ff10e0b08a5bf0c9db5980f0fb31",
        "user_id": "6a1b2161094e32bf2c352910218048da"
    }
}
```

* 例子（curl）

```shell
# With shell, you can just pass the correct header with each request
curl --location --request POST 'https://api.ddpurse.com/v1/transact/order/autopay' \
--header 'Authorization: Bearer <APP-ACCESS-TOKEN>' \
--header 'Content-Type: application/json' \
--data-raw '{
    "coin_type": "BSV",
    "out_order_id": "l111231",
    "product": {
        "detail": "This is a red apple.",
        "id": "001",
        "name": "apple"
    },
    "subject": "Red Apple",
    "to": [
        {
            "amount": 0,
            "content": "0200000001cfa1dc8bae0909b7c5bc73004c970851c39f6c85e44c1f177ccb37b632890540010000006b483045022100bea11e312fe4900fe76955224b13c9c735a8a0ac2be712f6fd06cf56f21a13a502206265887cfcaf42fb5847b3dc4ebf974e8feb66919ecf11ffba65436eaadf5ccbc32103d9feab50c01410c3061cfc76c069095f8aae3c3d5a91f6e28dbe87eafa414154ffffffff02000000000000000007006a0000aabbcc10270000000000001976a914875205b508694236f8b7f874f2faaa040e17ffc088ac00000000",
            "type": "partial_rawtx"
        }
    ],
    "user_id": "6a1b2161094e32bf2c352910218048da"
}'
```

### 查询订单

**HTTP Request**

POST https://api.ddpurse.com/v1/transact/order/get_order

**Request Header**

| 参数名        | 类型   | 是否必须 | 说明                                                         |
| :------------ | :----- | :------- | ------------------------------------------------------------ |
| Authorization | string | 是       | Authorization: Bearer APP-ACCESS-TOKEN，授权方式: [应用授权](#auth_client_credentials) |

**Request Body**

| 参数名   | 类型   | 是否必须 | 说明   |
| :------- | :----- | :------- | :----- |
| order_id | string | 是       | 订单号 |

**Response Body**

| 参数名         | 类型     | 说明                                              |
| :------------- | :------- | :------------------------------------------------ |
| order_id       | string   | 订单号                                            |
| out_order_id   | string   | 商户订单号                                        |
| payer_user_id  | string   | 付款用户 ID                                       |
| coin_type      | string   | 支付的币种                                        |
| amount         | int      | 交易金额，单位：聪 satoshi(bsv)或者 gwei （eth)   |
| fee            | int      | 交易矿工费，单位：聪 satoshi(bsv)或者 gwei （eth) |
| txid           | string   | 交易的 txid                                       |
| subject        | string   | 订单标题                                          |
| product_id     | string   | 商品的编号                                        |
| product_name   | string   | 商品名称                                          |
| product_detail | string   | 商品描述                                          |
| confirmation   | int      | 交易确认数，`-1`表示还未上链                      |
| status         | int      | 订单状态，`1`表示订单未支付，`2`表示订单已支付    |
| created_at     | int      | 订单创建时间，值为 unix 时间，单位秒              |
| +transaction   | object   | 交易详情，仅在订单已支付状态下有值                |
| └ blockhash    | string   | 交易所在区块hash                                  |
| └ blockheight  | int      | 交易所在区块高度，`-1`表示交易还未上块            |
| └ time         | int      | 交易被打包上区块时间，unix时间，单位秒            |
| └ confirmation | int      | 交易确认数，`-1`表示交易还未上块                  |
| └ fee          | int      | 交易手续费                                        |
| └ txid         | string   | 交易ID                                            |
| └ +vins        | object[] | 交易输入                                          |
| └ └ address    | string   | 地址                                              |
| └ └ amount     | string   | 金额                                              |
| └ └ index      | string   | 位置下标                                          |
| └ +vouts       | object[] | 交易输出                                          |
| └ └ address    | string   | 地址                                              |
| └ └ amount     | string   | 金额                                              |
| └ └ index      | string   | 位置下标                                          |

成功：


```json
{
  "code": 0,
  "msg": "",
  "data": {
    "order_id": "1306515412057333760",
    "out_order_id": "0001",
    "payer_user_id": "6a1b2161094e32bf2c352910218048da",
    "coin_type": "BSV",
    "txid": "9bbe61bede9868a194a3568b88f4d504cb9de7aca407adf7d1ad9bf867373914",
    "fee": 233,
    "amount": 10000,
    "subject": "Red Apple",
    "product_id": "001",
    "product_name": "apple",
    "product_detail": "This is a red apple.",
    "status": 2,
    "created_at": 1602829881,
    "confirmation": -1,
    "transaction": {
            "blockhash": "",
            "blockheight": -1,
            "confirmation": -1,
            "fee": 116,
            "time": 1605254849,
            "vins": [
                {
                    "address": "mqcsXSYXd1fimNKWZS7tyPFyunA1ewzVhV",
                    "amount": 9881906,
                    "index": 0
                }
            ],
            "vouts": [
                {
                    "address": "mjLxBnPyFPP1qTDzu7Lhv597gJGR5kpPvd",
                    "amount": 200000,
                    "index": 0
                },
                {
                    "address": "n2V8gRt1ytG8QGaA9VqH7JnSLvNcU2pGW6",
                    "amount": 9681790,
                    "index": 1
                }
            ]
        }
  }
}
```

* 例子（curl）

```shell
# 每次请求，提交正确的header（shell例子）：
curl --location --request GET 'https://api.ddpurse.com/v1/transact/order/get_order' \
--header 'Authorization: Bearer <APP-ACCESS-TOKEN>' \
--header 'Content-Type: application/json' \
--data-raw '{
    "order_id":"1306515412057333760"
}'
```

### 查询交易默克尔证明

详细请看：[默克尔证明](https://tsc.bitcoinassociation.net/standards/merkle-proof-standardised-format/)

**HTTP Request**

POST https://api.ddpurse.com/v1/bsvchain/get_merkleproof

**Request Header**

| 参数名        | 类型   | 是否必须 | 说明                                                         |
| :------------ | :----- | :------- | ------------------------------------------------------------ |
| Authorization | string | 是       | Authorization: Bearer APP-ACCESS-TOKEN，授权方式: [应用授权](#auth_client_credentials) |

**Request Body**

| 参数名 | 类型   | 必须 | 备注       |
| ------ | ------ | ---- | ---------- |
| txid   | string | 是   | 交易的txid |

**Response Body**

成功：

``` json
{
    "code": 0,
    "msg": "",
    "data": {
        "flags": 2,
        "index": 717,
        "txid": "",
        "target": {
            "hash": "160afcfcf6f81d514d46d9979a3cd8b52b38b7839328b77abcdfc4455395b68f",
            "confirmations": 10,
            "height": 17270,
            "version": 536870912,
            "versionHex": "20000000",
            "merkleroot": "de74950863a74ffca8528b847ffeca938fc99f9d9e6d68a4477412d5af8befcb",
            "num_tx": 719,
            "time": 1614322970,
            "mediantime": 1613984482,
            "nonce": 0,
            "bits": "207fffff",
            "difficulty": 4.656542373906925e-10,
            "chainwork": "00000000000000000000000000000000000000000000000000000000000086ee",
            "previousblockhash": "6a0fb3486d57d900d6fe6b1cd1030395d266f2799caa022691ba8fd5ca79de83",
            "nextblockhash": "3b53c9c2d92bd42f51d4af170053d0d7137bc31af2c6cab0fc7cdc425d4ed237"
        },
        "nodes": [
            "a731a9ee5770d60dae00707de85d5a8ec821e6ebbcff67ab6a87f1a83c898213",
            "37d80d9c0b329ecd3b9a61f4c6d37308cd593b9aa19c32a159f99f0759a74218",
            "c2ad258c1ad4fa1b2cf998f4be6dcf2e61dfc6c7270de15e3ff7a6cdf566088e",
            "0995a3da5b92f9a91964b93039120ae57fcd8165aa9345ce0d80d06300fa3270",
            "bf660fdbdbba51b50a56b00e32b2910b2211878f6ba3e82e219d5c53403be06e",
            "7d136999801572d291e61d7e9a188a586f1a88892ef8a776159a1547239ebafd",
            "71a1bcc370cd24e1ff62a7221030f9c3bd0910fdf988289db594d44f825d4b4f",
            "9ed156df172c6ea167c5c33d1a9aaba79f69e58e6de9336f04c3750b76a3e50d",
            "a84636bff02bd24c25989d21b7952cffdc9967ffe83c0fb5a90fe744c35b5b02",
            "efb9412a9aa4818f421139c8a353bf723271375cba1140c254286937437dffd1"
        ]
    }
}
```

* 例子（curl）

```shell
# With shell, you can just pass the correct header with each request
curl --location --request POST 'https://api.ddpurse.com/v1/bsvchain/get_merkleproof' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIwZDE1OGJjMzYwNWZmZmUzMGY3MDQ2ZjBjOWM3ZTRiZiIsImV4cCI6MTYxNDIzNDIxN30.CfHsjHIGrj-OnqO7EFe5PQQaAlL5p4Dgn5u46hp8gQto-SJB-70Ojdua0Y_cZBOgEkppoJbXN6tyuAvjJ-8JXg' \
--header 'Content-Type: application/json' \
--data-raw '{"txid":"5d34fe70067f1621a586cc5c4ea2828f81c5a760fcb0906dddf0a1bd05a966b4"}'
```