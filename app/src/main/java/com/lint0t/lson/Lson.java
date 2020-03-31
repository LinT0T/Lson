package com.lint0t.lson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Lson<T> {
    private String json;
    private ArrayList<String> key = new ArrayList<>(), value = new ArrayList<>();
    private String[] spKey = new String[1000];
    private JSONArray[] listJson = new JSONArray[1000];


    public T fromJson(String json, Class<T> tClass) {
        this.json = json;
        try {
          //  Field field = tClass.getDeclaredField("major");
          //  String type = field.getType().toString();
          //  System.out.println("aaaaaaaaaaaaaaaaaaa " + type);
            //System.out.println(tClass.getDeclaredClasses()[0]);
            nice(json);
            //  getKeyAndValue(json);
            Object obj = tClass.newInstance();
            setField(tClass, obj);
            return tClass.cast(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    private void getKeyAndValue(String json) throws JSONException {
        String s = "";
        int num = 0;
        boolean start = false, isKey = true, isList = false, isListWithObject = false;
        for (int i = 0; i < json.length(); i++) {   //算法：读到"开始记录到下一个"，如果在":"与","之间为value不需读取，但需要去除第一个"
            if (json.charAt(i) == ':') {
                isKey = false;
            }
            if (json.charAt(i) == '[') {
                isList = true;
                spKey[key.size()] = "list";
                System.out.println(spKey[key.size()]);
                System.out.println(key.size());
            }
            if (json.charAt(i) == ']') {
                isList = false;
            }
            if (json.charAt(i) == '：' && isList) {
                isListWithObject = true;
                spKey[key.size()] = "objectList";
                System.out.println(spKey[key.size()]);
                System.out.println(key.size());
            } else {
                isListWithObject = false;
            }
            if (json.charAt(i) == ',' && !isList) {
                isKey = true;
            }
            if (json.charAt(i) == '"' && isKey) {
                num++;
                start = true;

            }
            if (num >= 2) {
                start = false;
                num = 0;
            }
            if (start) {
                s += json.charAt(i);

            } else {
                if (!s.equals("")) {
                    s = s.substring(1);
                    key.add(s);
                }
                s = "";
                start = false;
            }
        }
        JSONObject jsonObject = new JSONObject(json);
        for (int i = 0; i < key.size(); i++) {
            // System.out.println("-=-=-==-=-==-=-" + jsonObject.get(key.get(i)).toString());
            if (spKey[i + 1] == "list") {
                // System.out.println("-=-=-==-=-==-=-" + jsonObject.getJSONArray(key.get(i)).toString());
                //  value.add(jsonObject.getJSONArray(key.get(i)).toString());
                JSONArray jsonArray = jsonObject.getJSONArray(key.get(i));
                listJson[i] = jsonArray;
                System.out.println("sssssssssssssssssss" + listJson[i]);
            }

            value.add(jsonObject.get(key.get(i)).toString());
        }
    }

    private void setField(Class<T> tClass, Object obj) throws NoSuchFieldException, IllegalAccessException, JSONException, ClassNotFoundException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        for (int i = 0; i < key.size(); i++) {
            Field field = tClass.getDeclaredField(key.get(i));
            field.setAccessible(true);
            System.out.println("-------------------");
            System.out.println("?" + key.get(i));
            String type = field.getType().toString();
            if (type.equals("int")) {
                int result = Integer.parseInt(value.get(i));
                field.set(obj, result);
            }
            if (type.equals("class java.lang.String")) {
                field.set(obj, value.get(i));
            }
            if (type.equals("interface java.util.List") || type.equals("class java.util.ArrayList")) {
                if (spKey[i + 1] == "list") {
                    ArrayList arrayList = new ArrayList();
                    JSONArray jsonArray = listJson[i];
                    System.out.println(jsonArray);
                    System.out.println(i);
                    for (int j = 0; j < jsonArray.length(); j++) {
                        arrayList.add(jsonArray.get(j));
                    }
                    field.set(obj, arrayList);
                } else if (spKey[i + 1] == "objectList") {
                    Type genericType = field.getGenericType();
                    ParameterizedType parameterizedType = (ParameterizedType) genericType;
                    Type[] types = parameterizedType.getActualTypeArguments();
                        String s = types[0].toString();
                        s = s.substring(6);
                        Class aClass = Class.forName(s);
                      //  System.out.println(aClass.getName());
                        JSONArray jsonArray = listJson[i];
                    Field[] declaredFields = aClass.getDeclaredFields();
                    ArrayList<Object> arrayList = new ArrayList<>();
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject jsonObject= jsonArray.getJSONObject(j);
                      //  Object o = aClass.getConstructor(aClass).newInstance(aClass.newInstance());
                        Constructor con2 = aClass.getDeclaredConstructor(tClass);
                        con2.setAccessible(true);

                        Object obj2 = con2.newInstance(obj);
                      //  System.out.println(jsonArray.get(j).toString());
                        for (int k = 0; k < declaredFields.length; k++) {
                            Field f = declaredFields[k];
                            f.setAccessible(true);
                            System.out.println("-----------1-------------");
                            System.out.println(f.getName());
                            if (! f.getName().equals("this$0")) {
                                f.set(obj2, jsonObject.get(f.getName()));
                                System.out.println(f.get(obj2));
                          //      System.out.println(jsonObject.get("money"));
                            }
                        }
                        arrayList.add(obj2);

                    }
                    field.set(obj,arrayList);
                }

                Object o = field.get(obj);
                System.out.println(o);
            }
        }
    }

    private void nice(String json) throws JSONException {
        String s = "";
        int num = 0;
        boolean start = false, isKey = true, isList = false, isListWithObject = false;
        for (int i = 0; i < json.length(); i++) {   //算法：读到"开始记录到下一个"，如果在":"与","之间为value不需读取，但需要去除第一个"
            if (json.charAt(i) == ':') {
                isKey = false;
            }
            if (json.charAt(i) == '[') {
                isList = true;
                spKey[key.size()] = "list";
                System.out.println(spKey[key.size()]);
                System.out.println(key.size());
            }
            if (json.charAt(i) == ']') {
                isList = false;
            }
            if (json.charAt(i) == '{' && isList) {
                isListWithObject = true;
                spKey[key.size()] = "objectList";
                System.out.println(spKey[key.size()]);
                System.out.println(key.size());
            } else {
                isListWithObject = false;
            }
            if (json.charAt(i) == ',' && !isList) {
                isKey = true;
            }
            if (json.charAt(i) == '"' && isKey) {
                num++;
                start = true;

            }
            if (num >= 2) {
                start = false;
                num = 0;
            }
            if (start) {
                s += json.charAt(i);

            } else {
                if (!s.equals("")) {
                    s = s.substring(1);
                    key.add(s);
                }
                s = "";
                start = false;
            }
        }
        JSONObject jsonObject = new JSONObject(json);
        for (int i = 0; i < key.size(); i++) {
            // System.out.println("-=-=-==-=-==-=-" + jsonObject.get(key.get(i)).toString());
            if (spKey[i + 1] == "list") {
                // System.out.println("-=-=-==-=-==-=-" + jsonObject.getJSONArray(key.get(i)).toString());
                //  value.add(jsonObject.getJSONArray(key.get(i)).toString());
                JSONArray jsonArray = jsonObject.getJSONArray(key.get(i));
                listJson[i] = jsonArray;
                System.out.println("sssssssssssssssssss" + listJson[i]);
            }
            if (spKey[i + 1] == "objectList") {
                // System.out.println("-=-=-==-=-==-=-" + jsonObject.getJSONArray(key.get(i)).toString());
                //  value.add(jsonObject.getJSONArray(key.get(i)).toString());
                JSONArray jsonArray = jsonObject.getJSONArray(key.get(i));
                listJson[i] = jsonArray;
                System.out.println("bbbbbbbbbbbbbbbbbb" + listJson[i]);
            }
            System.out.println(key);
            value.add(jsonObject.get(key.get(i)).toString());
        }
    }
}
