package com.dong.json.el;

import com.alibaba.fastjson.JSONObject;
import com.sun.deploy.util.ArrayUtil;
import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import sun.reflect.generics.tree.ArrayTypeSignature;

import java.util.Map;

public class RuleEngine {

    public ExpressionResult compute(JSONObject jsonObject, Map<String, Object> context) throws Exception {
        if (jsonObject == null) {
            return null;
        }
        ExpressionResult result = new ExpressionResult();
        String opStr = jsonObject.getString("op");
        if (Operator.is(opStr)) {
            //逻辑关系
            Operator operator = Operator.get(opStr);
            JSONObject left = jsonObject.getJSONObject("left");
            JSONObject right = jsonObject.getJSONObject("right");
            ExpressionResult leftResult = compute(left, context);
            ExpressionResult rightResult = compute(right, context);

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
                     throw new Exception("逻辑符尚未实现");
             }
            result.setMessage(result.result ? "成功" : "审核失败");
            System.out.println(result);
             return result;

        } else if (Comparator.is(opStr)) {
            //比较关系
            Comparator comparator = Comparator.get(opStr);
            String source = jsonObject.getString("left");
            switch (comparator) {
                case BT:
                    String targetStr = jsonObject.getString("right");
                    String[] values = targetStr.split(",");
                    String elExp = String.format("%s > %s && %s < %s", source, values[0], source, values[1]);
                    JexlEngine jexl = new JexlEngine();
                    Expression expression = jexl.createExpression(elExp);
                    JexlContext jc = new MapContext();
                    for (String key : context.keySet()) {
                        jc.set(key, context.get(key));
                    }

                    System.out.println(elExp);
                    Boolean boolResult = (Boolean) expression.evaluate(jc);
                    result.setResult(boolResult);
                    result.setMessage(boolResult ? "成功" : source + "审核失败");
                    break;
                case GE:
                    Integer target = jsonObject.getInteger("right");
                    break;
                case IN:
                    targetStr = jsonObject.getString("right");

                    break;
                default:
                    throw new Exception("比较符尚未实现");
            }
        } else {
            throw new Exception("不存在的操作符");
        }
        System.out.println(result);
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
