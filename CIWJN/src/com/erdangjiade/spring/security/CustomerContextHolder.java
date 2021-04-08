package com.erdangjiade.spring.security;

public class CustomerContextHolder {
    public static final String DATASOURCE_CIWJN = "CIWJN";
    public static final String DATASOURCE_REALTIME = "realTimeData";

    public static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    public static void setCustomerType(String customerType) {
        contextHolder.set(customerType);
    }

    public static String getCustomerType() {
        return contextHolder.get();
    }

    public static void clearCustomerType() {
        contextHolder.remove();
    }
}
