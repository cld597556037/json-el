package com.dong.json.el;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class RuleEngineTest {

    @Test
    public void test_compute() {
        String test = "{'op':'&', left:{'op': '~', 'left':'age', right: '1,4'}, 'right':{'op': '~', 'left':'age', right: '2,3'}}";
        JSONObject jsonObject = JSON.parseObject(test);
        Map<String, Object> context = new HashMap();
        context.put("age", 2);
        RuleEngine engine = new RuleEngine();
        try {
            ExpressionResult result = engine.compute(jsonObject, context);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
