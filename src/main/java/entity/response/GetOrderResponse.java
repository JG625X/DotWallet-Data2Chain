package entity.response;

import com.alibaba.fastjson.annotation.JSONField;
import entity.CoinType;

public class GetOrderResponse extends BaseResponse {
    private GetOrderData data;

    public GetOrderData getData() {
        return data;
    }

    public void setData(GetOrderData data) {
        this.data = data;
    }

    public class GetOrderData {
        @JSONField(name = "order_id")
        private String orderID;
        @JSONField(name = "out_order_id")
        private String outOrderID;
        @JSONField(name = "payer_user_id")
        private String payerUserID;
        @JSONField(name = "coin_type")
        private CoinType coinType;
        @JSONField(name = "txid")
        private String txid;
        @JSONField(name = "fee")
        private Integer fee;
        @JSONField(name = "amount")
        private Long amount;
        @JSONField(name = "subject")
        private String subject;
        @JSONField(name = "product_id")
        private String productID;
        @JSONField(name = "product_name")
        private String productName;
        @JSONField(name = "product_detail")
        private String productDetail;
        /**
         * 订单状态，1表示订单未支付，2表示订单已支付
         */
        @JSONField(name = "status")
        private Integer status;
        @JSONField(name = "created_at")
        private Integer createdAt;
        /**
         * 交易确认数，-1表示还未上链
         */
        @JSONField(name = "confirmation")
        private Integer confirmation;
        @JSONField(name = "transaction")
        private Transaction transaction;

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

        public String getPayerUserID() {
            return payerUserID;
        }

        public void setPayerUserID(String payerUserID) {
            this.payerUserID = payerUserID;
        }

        public CoinType getCoinType() {
            return coinType;
        }

        public void setCoinType(CoinType coinType) {
            this.coinType = coinType;
        }

        public String getTxid() {
            return txid;
        }

        public void setTxid(String txid) {
            this.txid = txid;
        }

        public Integer getFee() {
            return fee;
        }

        public void setFee(Integer fee) {
            this.fee = fee;
        }

        public Long getAmount() {
            return amount;
        }

        public void setAmount(Long amount) {
            this.amount = amount;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getProductID() {
            return productID;
        }

        public void setProductID(String productID) {
            this.productID = productID;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductDetail() {
            return productDetail;
        }

        public void setProductDetail(String productDetail) {
            this.productDetail = productDetail;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Integer getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Integer createdAt) {
            this.createdAt = createdAt;
        }

        public Integer getConfirmation() {
            return confirmation;
        }

        public void setConfirmation(Integer confirmation) {
            this.confirmation = confirmation;
        }

        public Transaction getTransaction() {
            return transaction;
        }

        public void setTransaction(Transaction transaction) {
            this.transaction = transaction;
        }

        public class Transaction {
            @JSONField(name = "blockhash")
            private String blockHash;
            @JSONField(name = "blockheight")
            private Integer blockHeight;
            @JSONField(name = "confirmation")
            private Integer confirmation;
            @JSONField(name = "fee")
            private Integer fee;
            /**
             * 交易被打包上区块时间，unix时间，单位秒
             */
            @JSONField(name = "time")
            private Integer time;
            private AddressVinOrVout[] vins;

            public String getBlockHash() {
                return blockHash;
            }

            public void setBlockHash(String blockHash) {
                this.blockHash = blockHash;
            }

            public Integer getBlockHeight() {
                return blockHeight;
            }

            public void setBlockHeight(Integer blockHeight) {
                this.blockHeight = blockHeight;
            }

            public Integer getConfirmation() {
                return confirmation;
            }

            public void setConfirmation(Integer confirmation) {
                this.confirmation = confirmation;
            }

            public Integer getFee() {
                return fee;
            }

            public void setFee(Integer fee) {
                this.fee = fee;
            }

            public Integer getTime() {
                return time;
            }

            public void setTime(Integer time) {
                this.time = time;
            }

            public AddressVinOrVout[] getVins() {
                return vins;
            }

            public void setVins(AddressVinOrVout[] vins) {
                this.vins = vins;
            }

            public AddressVinOrVout[] getVouts() {
                return vouts;
            }

            public void setVouts(AddressVinOrVout[] vouts) {
                this.vouts = vouts;
            }

            public class AddressVinOrVout {
                private String address;
                private Long amount;
                private Integer index;

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }

                public Long getAmount() {
                    return amount;
                }

                public void setAmount(Long amount) {
                    this.amount = amount;
                }

                public Integer getIndex() {
                    return index;
                }

                public void setIndex(Integer index) {
                    this.index = index;
                }
            }

            private AddressVinOrVout[] vouts;

        }
    }
}
