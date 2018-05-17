package com.dong.json.el;

import com.alibaba.fastjson.JSONObject;
import com.sun.deploy.util.ArrayUtil;
import org.apache.commons.lang3.ArrayUtils;
import sun.reflect.generics.tree.ArrayTypeSignature;

public class RuleEngine {

    public ExpressionResult compute(JSONObject jsonObject) throws Exception {
        if (jsonObject == null) {
            return null;
        }
        ExpressionResult result = new ExpressionResult();
        String opStr = jsonObject.getString("op");
        if (Operator.is(opStr)) {
            //逻辑关系
            Operator operator = Operator.get(opStr);
            JSONObject left = jsonObject.getJSONObject("left");
            JSONObject right = jsonObject.getJSONObject("left");
            ExpressionResult leftResult = compute(left);
            ExpressionResult rightResult = compute(right);

             switch (operator) {
                 case AND:
                     result.result = leftResult.result & rightResult.result;
                     break;
                 case OR:
                     result.result = leftResult.result | rightResult.result;
                     break;
                 case NOT:
                     result.result = !leftResult.result;
                     break;
                 default:
                     throw new Exception("不存在的操作符");
             }
             return result;

        } else if (Comparator.is(opStr)) {
            //比较关系
            Comparator comparator = Comparator.get(opStr);
            String source = jsonObject.getString("left");
            switch (comparator) {
                case BT:
                    String targetStr = jsonObject.getString("right");
                    String[] values = targetStr.split(";");
                    String elExp = String.format("%s > %s && %s < %s", source, values[0], source, values[1]);
                    //todo spring el
                    break;
                case GE:
                    Integer target = jsonObject.getInteger("right");
                    break;
                case IN:
                    targetStr = jsonObject.getString("right");

                    break;
            }
        }
        return result;

    }

    private Integer[] get(String source) {
        String[] values = source.split(",");
        Integer[] targets = new Integer[values.length];
        for (int i = 0; i < values.length; i++) {
            targets[i] = Integer.parseInt(values[i]);
        }
        return targets;
    }

    private Boolean isPrimitive(JSONObject jsonObject) {
        return jsonObject.keySet().size() == 0;
    }
}
