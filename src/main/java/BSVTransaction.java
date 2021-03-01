import com.google.common.primitives.Bytes;
import entity.UTXO;
import com.subgraph.orchid.encoders.Hex;
import org.bitcoinj.core.*;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BSVTransaction extends Transaction {
    public static final byte ForkID = 0x40;

    public BSVTransaction(NetworkParameters params) {
        super(params);
        this.setVersion(2);
    }

    //sig all:[version,hash_twice(all_input_txid),hash_twice(all_input_sequence_number),input_txid,len(p2pkh_script),p2pkh_script,value,input_sequence_number,hash_twice(vouts_script),locktime,sign_hash]
    private Sha256Hash hashForSignature(int inputIndex, byte sigHashType) {
        List<Byte> dataToHash = new ArrayList<>();
        byte sigHashMask = 0x1f;
        //version
        dataToHash.addAll(Bytes.asList(Util.intToBytesLittle((int) getVersion())));
        //input txid
        if ((sigHashType & SigHash.ANYONECANPAY.value) == 0) {
            List<Byte> inputTxidList = new ArrayList<>();
            for (TransactionInput input : getInputs()) {
                inputTxidList.addAll(Bytes.asList(input.getOutpoint().bitcoinSerialize()));
            }
            dataToHash.addAll(Bytes.asList(Sha256Hash.twiceOf(Bytes.toArray(inputTxidList)).getBytes()));
        } else {
            //anyone can pay
            dataToHash.addAll(Bytes.asList(Sha256Hash.ZERO_HASH.getBytes()));
        }
        //SequenceNumber
        if ((sigHashType & SigHash.ANYONECANPAY.value) == 0 &&
                (sigHashType & sigHashMask) != SigHash.SINGLE.value &&
                (sigHashType & sigHashMask) != SigHash.NONE.value) {
            List<Byte> inputSequenceList = new ArrayList<>();
            for (TransactionInput input : getInputs()) {
                inputSequenceList.addAll(Bytes.asList(Util.intToBytesLittle((int) input.getSequenceNumber())));
            }
            dataToHash.addAll(Bytes.asList(Sha256Hash.twiceOf(Bytes.toArray(inputSequenceList)).getBytes()));
        } else {
            //anyone can pay
            dataToHash.addAll(Bytes.asList(Sha256Hash.ZERO_HASH.getBytes()));
        }
        dataToHash.addAll(Bytes.asList(getInput(inputIndex).getOutpoint().bitcoinSerialize()));
        long len = this.getInput(inputIndex).getScriptBytes().length;
        if (len < 0xfd) {
            dataToHash.addAll(Bytes.asList(Util.byteToBytesLittle((byte) len)));
        } else if (len <= Short.MAX_VALUE) {
            dataToHash.addAll(Bytes.asList(Util.byteToBytesLittle((byte) 0xfd)));
            dataToHash.addAll(Bytes.asList(Util.shortToBytesLittle((short) len)));
        } else {
            dataToHash.addAll(Bytes.asList(Util.byteToBytesLittle((byte) 0xfe)));
            dataToHash.addAll(Bytes.asList(Util.intToBytesLittle((int) len)));
        }
        dataToHash.addAll(Bytes.asList(this.getInput(inputIndex).getScriptBytes()));
        dataToHash.addAll(Bytes.asList(Util.longToBytesLittle(Objects.requireNonNull(this.getInput(inputIndex).getValue()).value)));
        dataToHash.addAll(Bytes.asList(Util.intToBytesLittle((int) getInput(inputIndex).getSequenceNumber())));
        if ((sigHashType & SigHash.SINGLE.value) != SigHash.SINGLE.value && (sigHashType & SigHash.NONE.value) != SigHash.NONE.value) {
            List<Byte> outputScriptList = new ArrayList<>();
            for (TransactionOutput output : getOutputs()) {
                outputScriptList.addAll(Bytes.asList(output.bitcoinSerialize()));
            }
            dataToHash.addAll(Bytes.asList(Sha256Hash.twiceOf(Bytes.toArray(outputScriptList)).getBytes()));
        } else if ((sigHashType & sigHashMask) == SigHash.SINGLE.value && inputIndex < getOutputs().size()) {
            //sig single
            dataToHash.addAll(Bytes.asList(Sha256Hash.twiceOf(getOutput(inputIndex).bitcoinSerialize()).getBytes()));
        } else {
            dataToHash.addAll(Bytes.asList(Sha256Hash.ZERO_HASH.getBytes()));
        }
        dataToHash.addAll(Bytes.asList(Util.intToBytesLittle((int) getLockTime())));
        dataToHash.addAll(Bytes.asList(Util.intToBytesLittle((sigHashType | ForkID) & 0xff)));
        return Sha256Hash.twiceOf(Bytes.toArray(dataToHash));
    }

    private Script createVinScript(int inputIndex, ECKey key, SigHash hashType) {
        byte trueSigHashByte = (byte) (hashType.byteValue() | ForkID);
        Sha256Hash hash = this.hashForSignature(inputIndex, trueSigHashByte);
        System.out.println("hash: " + hash.toString());
        ECKey.ECDSASignature ecdsaSignature = key.sign(hash);
        byte[] ecdsaSignatureByte = ecdsaSignature.encodeToDER();
        byte[] trueSignResult = new byte[ecdsaSignatureByte.length + 1];
        trueSignResult[trueSignResult.length - 1] = trueSigHashByte;
        System.arraycopy(ecdsaSignatureByte, 0, trueSignResult, 0, ecdsaSignatureByte.length);
        return new ScriptBuilder().data(trueSignResult).data(key.getPubKey()).build();
    }

    /**
     * 构建数据上链交易
     *
     * @param key     主私钥
     * @param utxo    utxo
     * @param hexData 需要上链的数据，格式为十六进制
     */
    public String buildDataToChainTransaction(DeterministicKey key, UTXO utxo, String hexData) {
        //衍生出真正签名的私钥
        DeterministicKey changeStep = HDKeyDerivation.deriveChildKey(key, new ChildNumber(0, false));
        DeterministicKey privateKey = HDKeyDerivation.deriveChildKey(changeStep, new ChildNumber(0, false));
        TransactionOutPoint outPoint = new TransactionOutPoint(this.params, utxo.getIndex(), new Sha256Hash(utxo.getTxid()));
        //添加vin
        this.addInput(new TransactionInput(this.params, null, utxo.getSigScript(), outPoint, Coin.valueOf(utxo.getValue())));
        //添加数据上链vout，一定要先放数据上链的vout再放找零vout
        this.addOutput(Coin.valueOf(0), new Script(Hex.decode(hexData)));
        Script inputScript = this.createVinScript(0, privateKey, SigHash.ANYONECANPAY_SINGLE);
        //添加找零vout
        this.addOutput(Coin.valueOf(utxo.getValue()), Address.fromBase58(this.params, utxo.getAddr()));
        this.getInput(0).setScriptSig(inputScript);
        return new String(Hex.encode(this.bitcoinSerialize()));
    }
}
