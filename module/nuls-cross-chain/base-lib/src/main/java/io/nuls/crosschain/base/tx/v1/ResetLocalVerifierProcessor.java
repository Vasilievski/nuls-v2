package io.nuls.crosschain.base.tx.v1;

import io.nuls.base.data.BlockHeader;
import io.nuls.base.data.Transaction;
import io.nuls.base.protocol.TransactionProcessor;
import io.nuls.core.constant.TxType;
import io.nuls.core.core.annotation.Autowired;
import io.nuls.core.core.annotation.Component;
import io.nuls.crosschain.base.service.ResetLocalVerifierService;

import java.util.List;
import java.util.Map;

/**
 * @Author: zhoulijun
 * @Time: 2020/11/23 12:00
 * @Description: 功能描述
 */
@Component("ResetLocalVerifierProcessorV1")
public class ResetLocalVerifierProcessor implements TransactionProcessor {

    @Autowired
    ResetLocalVerifierService resetLocalVerifierService;

    @Override
    public int getType() {
        return TxType.RESET_LOCAL_VERIFIER_LIST;
    }

    @Override
    public Map<String, Object> validate(int chainId, List<Transaction> txs, Map<Integer, List<Transaction>> txMap, BlockHeader blockHeader) {
        return resetLocalVerifierService.validate(chainId,txs,blockHeader);
    }

    @Override
    public boolean commit(int chainId, List<Transaction> txs, BlockHeader blockHeader) {
        return resetLocalVerifierService.commitTx(chainId,txs,blockHeader);
    }

    @Override
    public boolean rollback(int chainId, List<Transaction> txs, BlockHeader blockHeader) {
        return resetLocalVerifierService.rollbackTx(chainId,txs,blockHeader);
    }

}