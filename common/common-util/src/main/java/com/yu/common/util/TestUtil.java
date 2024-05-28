package com.yu.common.util;

import java.util.ArrayList;
import java.util.List;

public class TestUtil {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        for(int i = 1;i <=100 ;i++){
            list.add(i);
        }

        while (list.size() != 1) {
            List<Integer> list1 = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if ((i+1) % 2 == 0) {
                    list1.add(list.get(i));
                }
            }
            list = list1;
            System.out.println(list1);
        }
    }
}