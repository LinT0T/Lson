package com.lint0t.lson;

import java.util.ArrayList;

public class Test {
    /*
    json2 = "{\n" +
            "  \"name\": \"haha\",\n" +
            "  \"num\": 66,\n" +
            "  \"test2\": {\n" +
            "    \"money\": -6.0,\n" +
            "    \"time\": 1583906400000,\n" +
            "    \"tips\": \"\",\n" +
            "    \"waysImage\": \"微信\",\n" +
            "    \"type\": \"娱乐项目\"\n" +
            "  }\n" +
            "}"
     */
    private String name;
    private int num;
    private ArrayList<myTestData> test2;

    public class myTestData{
        private String waysImage,type;
        private double money;
        private String time;

        public myTestData() {
        }
    }
}
