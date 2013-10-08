package com.ihelpoo.common.util;

/**
 * User: wdx
 * Date: 10/7/13
 * Time: 3:30 PM
 * To change this template use File | Settings | File Templates.
 */
public enum ID {
    INSTANCE;

    public long next() {
        return paticle(1);
    }

    public long paticle(int machine) {
        double time = System.currentTimeMillis();
        double base = Math.pow(2, 41);
        base += time;
        double random = Math.random() * Math.pow(2, 12);
        String baseStr = Long.toBinaryString((long) base);
        String machineStr = Integer.toBinaryString(machine);
        String randomStr = Long.toBinaryString((long) random);
        return Long.parseLong(baseStr + machineStr + randomStr, 2);
    }


    public static void main(String[] args) {
        System.out.println(ID.INSTANCE.next());
    }

}
