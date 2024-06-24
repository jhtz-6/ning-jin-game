package com.myf.common.util;

import java.util.function.Supplier;

/**
 * @Author: myf
 * @CreateTime: 2024-05-18  15:05
 * @Description: BooleanLogicExecuteUtils
 */
public class BooleanLogicExecuteUtils {

    public static void execute (boolean condition , Runnable conditionIfTrue, Runnable conditionIfFalse){

        if(condition){
            conditionIfTrue.run();
        }else{
            conditionIfFalse.run();
        }
    }

    public static <T> T executeWithResult (boolean condition , Supplier<T> conditionIfTrue, Supplier<T> conditionIfFalse){
        return condition ? conditionIfTrue.get() : conditionIfFalse.get();
    }
}
