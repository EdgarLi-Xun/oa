package com.sega.application.oa.utils;


import java.awt.*;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.DecimalFormat;
import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author baoqianyong
 * @date 2019/06/03
 */
public class Utils {
    private static final Pattern pattern = Pattern.compile("\\{\\w+\\}");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");

    private static final String TEMP_PREFIX = "{";

    /**
     * 判断集合是否为空
     *
     * @param collec 集合
     * @return
     */
    public static boolean isEmpty(Collection collec) {
        return collec == null || collec.size() == 0;
    }

    public static boolean isNotEmpty(Collection collec) {
        return collec != null && collec.size() > 0;
    }

    public static boolean isEmpty(Map map) {
        return map == null || map.size() == 0;
    }

    public static boolean isNotEmpty(Map map) {
        return map != null && map.size() != 0;
    }

    /**
     * 补足 size 位字符， 不足补0
     *
     * @param size
     * @param value
     * @return
     */
    public static String fillZero(int size, int value) {
        return String.format("%0" + size + "d", value);
    }


    public static boolean isTrue(Boolean v) {
        return v != null && Boolean.TRUE.equals(v);
    }

    public static boolean isTrue(Integer v) {
        if (v != null) {
            return v != 0;
        }
        return false;
    }

    /**
     * 模板字符串转换
     * eg: template => my name is {name}, my age is {age}
     * map => {name: 'tom', age： 10}
     * return => my name is 张三, age is 10
     *
     * @param template 模板字符串
     * @param map      数据
     * @return 返回转换后的字符串
     */
    public static String formateStr(String template, Map<String, Object> map) {

        // 如果是有效字符串模板才开始
        if (template == null) {
            return "";
        }
        if (template.indexOf(TEMP_PREFIX) == -1) {
            return template;
        }
        Matcher matcher = pattern.matcher(template);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String param = matcher.group();
            Object value = map.get(param.substring(1, param.length() - 1));
            matcher.appendReplacement(sb, value == null ? "" : value.toString());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 返回数组形式的分割字符串
     * 效果和 String.split 相似， 返回结果不是数组而是集合list
     *
     * @param str
     * @param regex
     * @return
     */
    public static List<String> split(String str, String regex) {
        return split(str, regex, null, null);
    }

    /**
     * 返回数组形式的分割字符串
     * 效果和 String.split 相似， 返回结果不是数组而是集合list
     *
     * @param str
     * @param regex
     * @param convert 转换
     * @return
     */
    public static List<String> splitC(String str, String regex, Function<String, String> convert) {
        return split(str, regex, convert, null);
    }

    public static List<String> splitF(String str, String regex, Predicate<String> filter) {
        return split(str, regex, null, filter);
    }

    /**
     * 字符串转换
     *
     * @param str     字符串
     * @param regex   分隔符
     * @param convert 字符转换
     * @param include 是否包含
     * @return
     */
    public static List<String> split(String str, String regex, Function<String, String> convert, Predicate<String> include) {
        if (str != null && str.length() > 0) {
            List<String> list = new ArrayList<>();
            for (String s : str.split(regex)) {
                s = s.trim();
                // 不包含
                if (include != null && !include.test(s)) {
                    continue;
                }
                if (convert != null) {
                    s = convert.apply(s);
                }
                list.add(s);
            }
            return list;
        }
        return Collections.emptyList();
    }

    /**
     * 字符链接
     *
     * @param c
     * @param sp
     * @return
     */
    public static String join(String sp, Collection c) {
        if (Utils.isNotEmpty(c)) {
            StringBuffer sb = new StringBuffer();
            Iterator it = c.iterator();
            while (it.hasNext()) {
                Object obj = it.next();
                if (obj != null) {
                    sb.append(obj.toString());
                    if (it.hasNext()) {
                        sb.append(sp);
                    }
                }
            }
            return sb.toString();
        }
        return null;
    }

    public static String join(String sp, String... strs) {
        if (strs.length > 0) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < strs.length; i++) {
                sb.append(strs[i]);
                if (i < strs.length - 1) {
                    sb.append(sp);
                }
            }
            return sb.toString();
        }
        return null;
    }

    /**
     * 导出数据格式转化
     *
     * @param obj
     * @return
     */
    public static String nullToExcelValue(Object obj) {
        if (obj == null) {
            return "";
        } else if (obj instanceof Double) {
            Double v = Double.parseDouble(obj.toString());
            if (v == 0) {
                return "0";
            } else {
                return v.toString();
            }
        } else {
            return obj.toString();
        }
    }

    /**
     * 反射获取所有字段
     *
     * @param clz
     * @return
     */
    public static List<Field> getAllFields(Class clz) {
        List<Field> fieldList = new ArrayList<>();
        Class tempClass = clz;
        while (tempClass != null) {
            //当父类为null的时候说明到达了最上层的父类(Object类).
            fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            //得到父类,然后赋给自己
            tempClass = tempClass.getSuperclass();
        }
        return fieldList;
    }

    /**
     * lambda 表达式的封装 list
     *
     * @param list
     * @param mapper
     * @return
     */
    public static <T, R> List<R> listField(Collection<? extends T> list, Function<? super T, ? extends R> mapper) {
        if (list == null) {
            return Collections.emptyList();
        }
        list.removeIf(Objects::isNull);
        return list.stream().map(mapper).collect(Collectors.toList());
    }

    /**
     * lambda 表达式的封装 set
     *
     * @param list
     * @param mapper
     * @return
     */
    public static <T, R> Set<R> setField(Collection<? extends T> list, Function<? super T, ? extends R> mapper) {
        if (list == null) {
            return Collections.emptySet();
        }
        return list.stream().map(mapper).collect(Collectors.toSet());
    }

    /**
     * 更改以前的实现， 以前如果 value 为 null 会报空指针异常
     *
     * @param list
     * @param keyFunc
     * @param valueFunc
     */
    public static <K, V, E> Map<K, V> map(List<? extends E> list, Function<E, ? extends K> keyFunc, Function<E, ? extends V> valueFunc, Supplier<Map<K, V>> supplier) {
        Map<K, V> map = supplier.get();
        if (Utils.isNotEmpty(list)) {
            for (E e : list) {
                K key = keyFunc.apply(e);
                V value = valueFunc.apply(e);
                if (key == null) {
                    continue;
                }
                if (value == null && map instanceof ConcurrentHashMap) {
                    continue;
                }
                map.put(key, value);
            }
        }
        return map;
    }

    public static <K, V> Map<K, V> map(Collection<V> list, Function<V, ? extends K> keyFunc) {
        Map<K, V> map = new HashMap<>();
        if (Utils.isNotEmpty(list)) {
            for (V e : list) {
                K key = keyFunc.apply(e);
                if (key == null) {
                    continue;
                }
                map.put(key, e);
            }
        }
        return map;
    }

    /**
     * 更改以前的实现， 以前如果 value 为 null 会报空指针异常
     *
     * @param list
     * @param keyFunc
     * @param valueFunc
     */
    public static <K, V, E> Map<K, V> map(List<? extends E> list, Function<E, ? extends K> keyFunc, Function<E, ? extends V> valueFunc) {
        return map(list, keyFunc, valueFunc, HashMap::new);
    }

    /**
     * 返回的是有序的
     *
     * @param list
     * @param keyFunc
     * @param valueFunc
     * @return
     */
    public static <K, V, E> Map<K, V> linkedMap(List<? extends E> list, Function<E, ? extends K> keyFunc, Function<E, ? extends V> valueFunc) {
        return map(list, keyFunc, valueFunc, LinkedHashMap::new);
    }

    /**
     * 线程安全的
     *
     * @param list
     * @param keyFunc
     * @param valueFunc
     * @return
     */
    public static <K, T, E> Map<K, T> concurrentMap(List<? extends E> list, Function<E, ? extends K> keyFunc, Function<E, ? extends T> valueFunc) {
        return map(list, keyFunc, valueFunc, ConcurrentHashMap::new);
    }

    /**
     * group 分组
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T, K> Map<K, List<T>> groupMap(Collection<? extends T> list, Function<T, ? extends K> classifier) {
        if (list == null) {
            return Collections.emptyMap();
        }
        Map<K, List<T>> map = new HashMap<>();
        List<T> itemList = null;
        for (T t : list) {
            K k = classifier.apply(t);
            itemList = map.computeIfAbsent(k, e -> new ArrayList<>());
            itemList.add(t);
        }
        return map;
    }

    /**
     * 根据 map 进行 comparator 比较得到序号， 回填到 indexField 上
     * 例如： 根据 map 中的 a 字段进行比较， 得到序号后， 设置到 b 字段上
     *
     * @param map        数据源
     * @param comField   比较器字段
     * @param indexField 回填的字段
     * @param <T>
     */
    public static <T> void sort(Map<?, T> map, Function<T, Comparable> comField, BiConsumer<T, Integer> indexField, boolean desc) {
        List<T> list = new ArrayList<>();
        for (Map.Entry<?, T> entry : map.entrySet()) {
            list.add(entry.getValue());
        }
        if (desc) {
            Collections.sort(list, (v1, v2)-> comField.apply(v2).compareTo(comField.apply(v1)));
        }else {
            Collections.sort(list, Comparator.comparing(comField));
        }

        int index = 1;
        T last = null;
        boolean same = true;
        for (T t : list) {
            if (last != null) {
                if (comField.apply(t).equals(comField.apply(last))) {
                    same = true;
                }
            }
            indexField.accept(t, same ? index : ++index);
            last = t;
            same = false;
        }
    }

    /**
     * 字符串转整数， 不符合的转换为 null
     *
     * @param s
     * @return
     */
    public static Integer parse(String s) {
        Integer v = null;
        if (s != null && s.matches("^[\\+-]?[01-9]+")) {
            v = Integer.parseInt(s);
        }
        return v;
    }

    /**
     * 判断数组是否是从1开始的连续数字
     *
     * @param lists
     * @return
     */
    public static boolean isContinuationInteger(List<Integer> lists) {
        Boolean isContinuation = true;
        for (int i = 1; i <= lists.size(); i++) {
            if (!lists.contains(i)) {
                isContinuation = false;
                break;
            }
        }
        return isContinuation;
    }

    /**
     * 函数功能说明 Double 值根据num值保留小数点位 xurun 2016年8月3日 修改者名字 修改日期 修改内容
     *
     * @param @param  value
     * @param @param  num
     * @param @return
     * @return Double
     * @throws
     */
    public static Double formatNum(Number value, Integer num) {
        StringBuffer str_format = new StringBuffer("0");
        str_format.append(".");
        if (num > 0 && num <= 15) {
            for (int i = 0; i <= num; i++) {
                str_format.append("0");
            }
        }
        DecimalFormat df = new DecimalFormat(str_format.toString());
        return Double.valueOf(df.format(value));
    }

    /**
     * 驼峰转下划线
     *
     * @param str
     * @return
     */
    public static String humpToUnderline(String str) {
        StringBuilder sb = new StringBuilder(str);
        int temp = 0;//定位
        if (!str.contains("_")) {
            for (int i = 0; i < str.length(); i++) {
                if (Character.isUpperCase(str.charAt(i))) {
                    sb.insert(i + temp, "_");
                    temp += 1;
                }
            }
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 颜色转rgb #ff00ff
     *
     * @param str
     * @return
     */
    public static Color str2Color(String str) {
        try {
            if (str != null && str.startsWith("#") && (str.length() == 7 || str.length() == 4)) {
                return Color.decode(str);
            }
        } catch (NumberFormatException e) {
        }
        return null;
    }

    /**
     * 支持验证小数
     *
     * @param s
     * @return
     */
    public static Boolean isNumber(String s) {
        return s != null && s.length() > 0 && s.matches("([1-9]\\d*)|([1-9]\\d*\\.\\d+)|(0\\.\\d+)|0");
    }

    /**
     * 是否是整数
     * @param num
     * @return
     */
    public static boolean isInteger(String num) {
        if (num == null) {
            return false;
        }
        return num.matches("^[+-]{0,1}[01-9]\\d*");
    }


    /**
     * 合并两个 set
     *
     * @param s1
     * @param s2
     * @param <T>
     * @return
     */
    public static <T> Set<T> merge(Set<T> s1, Set<T> s2) {
        try {
            Set<T> set = s1.getClass().newInstance();
            set.addAll(s1);
            set.addAll(s2);
            return set;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptySet();
    }

    /**
     * 是否是邮箱
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        return EMAIL_PATTERN.matcher(email).find();
    }

    /**
     * 获取资源流的形式
     *
     * @param fileName
     * @return
     */
    public static InputStream getResourceStream(String fileName) {
        return Thread.currentThread().getClass().getClassLoader().getResourceAsStream(fileName);
    }

    /**
     * 将一组数据平均分成n组
     * @param source 要分组的数据源
     * @param n      平均分成n组
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> averageAssign(List<T> source, int n) {
        List<List<T>> result = new ArrayList<List<T>>();
        int remainder = source.size() % n;  //(先计算出余数)
        int number = source.size() / n;  //然后是商
        int offset = 0;//偏移量
        for (int i = 0; i < n; i++) {
            List<T> value = null;
            if (remainder > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remainder--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }

}
