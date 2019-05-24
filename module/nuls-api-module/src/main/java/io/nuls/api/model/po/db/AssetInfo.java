package io.nuls.api.model.po.db;

import io.nuls.api.constant.ApiConstant;

import java.math.BigInteger;

public class AssetInfo extends TxDataInfo {

    private String key;

    private int chainId;

    private int assetId;

    private String symbol;

    private BigInteger initCoins;

    private int status;

    public AssetInfo() {
    }

    public AssetInfo(int chainId, int assetId, String symbol, BigInteger initCoins) {
        this.key = chainId + "-" + assetId;
        this.chainId = chainId;
        this.assetId = assetId;
        this.symbol = symbol;
        this.initCoins = initCoins;
        this.status = ApiConstant.ENABLE;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getChainId() {
        return chainId;
    }

    public void setChainId(int chainId) {
        this.chainId = chainId;
    }

    public int getAssetId() {
        return assetId;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigInteger getInitCoins() {
        return initCoins;
    }

    public void setInitCoins(BigInteger initCoins) {
        this.initCoins = initCoins;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
