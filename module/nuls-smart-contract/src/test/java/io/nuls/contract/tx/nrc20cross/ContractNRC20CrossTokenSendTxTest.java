/**
 * MIT License
 * <p>
 * Copyright (c) 2017-2019 nuls.io
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.nuls.contract.tx.nrc20cross;


import io.nuls.contract.mock.basetest.ContractTest;
import io.nuls.contract.tx.base.BaseQuery;
import io.nuls.contract.util.Log;
import io.nuls.core.model.StringUtils;
import io.nuls.core.parse.JSONUtils;
import io.nuls.core.rpc.info.Constants;
import io.nuls.core.rpc.model.ModuleE;
import io.nuls.core.rpc.model.message.Response;
import io.nuls.core.rpc.netty.processor.ResponseMessageProcessor;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static io.nuls.contract.constant.ContractCmdConstant.*;

/**
 * @author: PierreLuo
 * @date: 2019-03-15
 */
public class ContractNRC20CrossTokenSendTxTest extends BaseQuery {

    /**
     * 创建合约
     */
    @Test
    public void createContract() throws Exception {
        //sender = toAddress32;
        InputStream in = new FileInputStream(ContractTest.class.getResource("/nuls-cross-chain-nrc20-test.jar").getFile());
        byte[] contractCode = IOUtils.toByteArray(in);
        String remark = "create cross token";
        String name = "CCT";
        String symbol = "CrossChainToken";
        String amount = BigDecimal.TEN.pow(10).toPlainString();
        String decimals = "8";
        Map params = this.makeCreateParams(sender, contractCode, "cct", remark, name, symbol, amount, decimals);
        Response cmdResp2 = ResponseMessageProcessor.requestAndResponse(ModuleE.SC.abbr, CREATE, params);
        Map result = (HashMap) (((HashMap) cmdResp2.getResponseData()).get(CREATE));
        assertTrue(cmdResp2, result);
        String hash = (String) result.get("txHash");
        String contractAddress = (String) result.get("contractAddress");
        Map map = waitGetContractTx(hash);
        Assert.assertTrue(JSONUtils.obj2PrettyJson(map), (Boolean) ((Map)(map.get("contractResult"))).get("success"));
        Log.info("contractResult:{}", JSONUtils.obj2PrettyJson(map));
    }

    /**
     * 调用合约
     */
    @Test
    public void callContract() throws Exception {
        BigInteger value = BigInteger.ZERO;
        if(StringUtils.isBlank(methodName)) {
            methodName = "transfer";
        }
        if(StringUtils.isBlank(tokenReceiver)) {
            tokenReceiver = toAddress1;
        }
        String methodDesc = "";
        String remark = "call contract test - token转账";
        String token = BigInteger.valueOf(800L).toString();
        Map params = this.makeCallParams(sender, value, contractAddress_nrc20, methodName, methodDesc, remark, tokenReceiver, token);
        Response cmdResp2 = ResponseMessageProcessor.requestAndResponse(ModuleE.SC.abbr, CALL, params);
        Map result = (HashMap) (((HashMap) cmdResp2.getResponseData()).get(CALL));
        assertTrue(cmdResp2, result);
        String hash = (String) result.get("txHash");
        Log.info("contractResult:{}", JSONUtils.obj2PrettyJson(waitGetContractTx(hash)));
    }

    /**
     * token转账
     */
    @Test
    public void tokenTransfer() throws Exception {
        BigInteger value = BigInteger.TEN.pow(10);
        String remark = "token transfer to " + contractAddress;
        Map params = this.makeTokenTransferParams(sender, contractAddress, contractAddress_nrc20, value, remark);
        Response cmdResp2 = ResponseMessageProcessor.requestAndResponse(ModuleE.SC.abbr, TOKEN_TRANSFER, params);
        Map result = (HashMap) (((HashMap) cmdResp2.getResponseData()).get(TOKEN_TRANSFER));
        assertTrue(cmdResp2, result);
        String hash = (String) result.get("txHash");
        Log.info("contractResult:{}", JSONUtils.obj2PrettyJson(waitGetContractTx(hash)));
    }

}
