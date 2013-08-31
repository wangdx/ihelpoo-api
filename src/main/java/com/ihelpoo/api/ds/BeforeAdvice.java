package com.ihelpoo.api.ds;

public class BeforeAdvice {
    public void setMasterDataSource() {
        CustomerContextHolder.setCustomerType("master");
    }
    public void setSlaveDataSource() {
        CustomerContextHolder.setCustomerType("slave");
    }
}