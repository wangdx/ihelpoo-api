package com.ihelpoo.common.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * User: wdx
 * Date: 10/15/13
 * Time: 9:38 PM
 * To change this template use File | Settings | File Templates.
 */
public enum Rand {
    INSTANCE;
    public String nextString(){
        return String.valueOf(nextInt());
    }
    public int nextInt(){
        Set<Integer> set = GetRandomNumber(4);
        // 使用迭代器
        Iterator<Integer> iterator = set.iterator();
        // 临时记录数据
        String temp = "";
        while (iterator.hasNext()) {
            temp += iterator.next();
        }
        return Integer.parseInt(temp);
    }



    /**
     * 获取一个不包含0和4的四位随机数，并且四位数不重复
     *
     * @return Set<Integer>
     */
    public Set<Integer> GetRandomNumber(int length) {
        // 使用SET以此保证写入的数据不重复
        Set<Integer> set = new HashSet<Integer>();
        // 随机数
        Random random = new Random();

        while (set.size() < length) {
            // nextInt返回一个伪随机数，它是取自此随机数生成器序列的、在 0（包括）
            // 和指定值（不包括）之间均匀分布的 int 值。
            int next = random.nextInt(10);
            if(next == 4 || next == 0) continue;
            set.add(next);
        }
        return set;
    }

    public int nextInt10(){
        return new Random().nextInt(Integer.MAX_VALUE);
    }

    public static void main(String[] args) {
        System.out.println(Integer.MAX_VALUE);
        System.out.println(Rand.INSTANCE.nextInt10());
    }
}
