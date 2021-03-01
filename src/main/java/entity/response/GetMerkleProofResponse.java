package entity.response;

import com.alibaba.fastjson.annotation.JSONField;

public class GetMerkleProofResponse extends BaseResponse {
    private MerkleProofData data;

    public MerkleProofData getData() {
        return data;
    }

    public void setData(MerkleProofData data) {
        this.data = data;
    }

    public class MerkleProofData {
        @JSONField(name = "flags")
        private Integer flags;
        @JSONField(name = "index")
        private Integer index;
        @JSONField(name = "txOrId")
        private String TxOrId;
        @JSONField(name = "target")
        private MerkleProofTarget Target;
        @JSONField(name = "nodes")
        private String[] nodes;

        public Integer getFlags() {
            return flags;
        }

        public void setFlags(Integer flags) {
            this.flags = flags;
        }

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        public String getTxOrId() {
            return TxOrId;
        }

        public void setTxOrId(String txOrId) {
            TxOrId = txOrId;
        }

        public MerkleProofTarget getTarget() {
            return Target;
        }

        public void setTarget(MerkleProofTarget target) {
            Target = target;
        }

        public String[] getNodes() {
            return nodes;
        }

        public void setNodes(String[] nodes) {
            this.nodes = nodes;
        }

        public class MerkleProofTarget {
            @JSONField(name = "hash")
            private String hash;
            @JSONField(name = "confirmations")
            private Integer confirmations;
            @JSONField(name = "height")
            private Integer height;
            @JSONField(name = "version")
            private Integer version;
            @JSONField(name = "versionHex")
            private String versionHex;
            @JSONField(name = "merkleroot")
            private String merkleRoot;
            @JSONField(name = "num_tx")
            private Integer numTx;
            @JSONField(name = "time")
            private Integer time;
            @JSONField(name = "mediantime")
            private Integer mediantime;
            @JSONField(name = "nonce")
            private Integer nonce;
            @JSONField(name = "bits")
            private String bits;
            @JSONField(name = "difficulty")
            private Float difficulty;
            @JSONField(name = "chainwork")
            private String chainWork;
            @JSONField(name = "previousblockhash")
            private String PreviousBlockHash;
            @JSONField(name = "nextblockhash")
            private String nextBlockHash;

            public String getHash() {
                return hash;
            }

            public void setHash(String hash) {
                this.hash = hash;
            }

            public Integer getConfirmations() {
                return confirmations;
            }

            public void setConfirmations(Integer confirmations) {
                this.confirmations = confirmations;
            }

            public Integer getHeight() {
                return height;
            }

            public void setHeight(Integer height) {
                this.height = height;
            }

            public Integer getVersion() {
                return version;
            }

            public void setVersion(Integer version) {
                this.version = version;
            }

            public String getVersionHex() {
                return versionHex;
            }

            public void setVersionHex(String versionHex) {
                this.versionHex = versionHex;
            }

            public String getMerkleRoot() {
                return merkleRoot;
            }

            public void setMerkleRoot(String merkleRoot) {
                this.merkleRoot = merkleRoot;
            }

            public Integer getNumTx() {
                return numTx;
            }

            public void setNumTx(Integer numTx) {
                this.numTx = numTx;
            }

            public Integer getTime() {
                return time;
            }

            public void setTime(Integer time) {
                this.time = time;
            }

            public Integer getMediantime() {
                return mediantime;
            }

            public void setMediantime(Integer mediantime) {
                this.mediantime = mediantime;
            }

            public Integer getNonce() {
                return nonce;
            }

            public void setNonce(Integer nonce) {
                this.nonce = nonce;
            }

            public String getBits() {
                return bits;
            }

            public void setBits(String bits) {
                this.bits = bits;
            }

            public Float getDifficulty() {
                return difficulty;
            }

            public void setDifficulty(Float difficulty) {
                this.difficulty = difficulty;
            }

            public String getChainWork() {
                return chainWork;
            }

            public void setChainWork(String chainWork) {
                this.chainWork = chainWork;
            }

            public String getPreviousBlockHash() {
                return PreviousBlockHash;
            }

            public void setPreviousBlockHash(String previousBlockHash) {
                PreviousBlockHash = previousBlockHash;
            }

            public String getNextBlockHash() {
                return nextBlockHash;
            }

            public void setNextBlockHash(String nextBlockHash) {
                this.nextBlockHash = nextBlockHash;
            }
        }
    }
}
