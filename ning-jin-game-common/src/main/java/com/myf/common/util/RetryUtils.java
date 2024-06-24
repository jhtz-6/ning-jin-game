package com.myf.common.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @Author: myf
 * @CreateTime: 2024-05-18  15:10
 * @Description: RetryUtils
 */
@Slf4j
public class RetryUtils {

    public static <T> T retry(int retryMaxNumber, long intervalTimeMillis, Supplier<T> operation, Predicate<T> condition) {
        int number = 0;
        T result = null;
        while (number <= retryMaxNumber) {
            try {
                result = operation.get();
                if (condition.test(result)) {
                    return result;
                }
                Thread.sleep(intervalTimeMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Thread interrupted while sleeping", e);
            } catch (Exception e) {
                log.error("Error occurred during retry attempt {}", number, e);
            } finally {
                number++;
            }
        }
        return result;
    }
}
