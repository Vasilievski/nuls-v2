package io.nuls.test.cases;

import io.nuls.tools.core.annotation.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author: zhoulijun
 * @Time: 2019-03-21 10:50
 * @Description: 功能描述
 */
@Component
public class Sleep10SecCase implements TestCaseIntf<Object,Object> {

    static final int SEC = 10;

    @Override
    public String title() {
        return "等待"+SEC+"秒";
    }

    @Override
    public Object doTest(Object param, int depth) throws TestFailException {
        try {
            for (int j = 1; j <= SEC; j++) {
                System.out.print(j + " ");
                TimeUnit.SECONDS.sleep(1L);
            }
            System.out.println();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return param;
    }
}