package com.lwjlol.scaffold.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.text.TextUtils;

import com.tencent.mmkv.MMKV;

/**
 * MMKV——基于 mmap 的高性能通用 key-value 组件,代替SharedPreference
 * https://github.com/Tencent/MMKV
 *
 * @author yangbang
 */
public class MMKVUtils {

    //SPUtils
    private static final String SHARE_DATA = "share_data";
    private static final String INIT_SP = "InitSP";
    private static final String COOKIE_SP = "CookiePersistence";
    // USER
    private static final String USERS = "USERS";
    private static final String TAG = "MMKVUtils";

    /**
     * 获取默认MMKV对象
     *
     * @return
     */
    public static MMKV getMMKV() {
        return getMMKV(null);
    }

    /**
     * 获取指定文件MMKV对象
     *
     * @param fileName 指定文件
     * @return
     */
    public static MMKV getMMKV(String fileName) {
        MMKV mmkv;
        if (TextUtils.isEmpty(fileName)) {
            mmkv = MMKV.defaultMMKV(MMKV.MULTI_PROCESS_MODE, null);
        } else {
            mmkv = MMKV.mmkvWithID(fileName, MMKV.MULTI_PROCESS_MODE);
        }
        return mmkv;
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param object
     */
    public static void put(String key, Object object) {
        put(key, object, null);

    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param object
     * @param fileName 指定的文件
     */
    public static void put(String key, Object object, String fileName) {
        MMKV mmkv = getMMKV(fileName);
        if (object instanceof String) {
            mmkv.putString(key, (String) object);
        } else if (object instanceof Integer) {
            mmkv.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            mmkv.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            mmkv.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            mmkv.putLong(key, (Long) object);
        } else if (object instanceof Double) {
            mmkv.encode(key, (Double) object);
        } else if (object instanceof Parcelable) {
            mmkv.encode(key, (Parcelable) object);
        } else {
            if (object != null) {
                mmkv.putString(key, object.toString());
            }
        }
    }

    /**
     * 获取保存的数据
     *
     * @param key
     * @param defValue
     * @return
     */
    public static <T> T get(String key, T defValue) {
        return get(key, defValue, null);
    }

    /**
     * 获取保存的数据
     *
     * @param key
     * @param defValue
     * @param fileName 指定的文件
     * @return
     */
    public static <T> T get(String key, T defValue, String fileName) {
        MMKV mmkv = getMMKV(fileName);
        Object value;
        if (defValue instanceof String) {
            value = mmkv.getString(key, (String) defValue);
        } else if (defValue instanceof Integer) {
            value = mmkv.getInt(key, (Integer) defValue);
        } else if (defValue instanceof Boolean) {
            value = mmkv.getBoolean(key, (Boolean) defValue);
        } else if (defValue instanceof Float) {
            value = mmkv.getFloat(key, (Float) defValue);
        } else if (defValue instanceof Long) {
            value = mmkv.getLong(key, (Long) defValue);
        } else if (defValue instanceof Double) {
            value = mmkv.decodeDouble(key, (Double) defValue);
        } else {
            value = defValue;
        }

        return (T) value;
    }

    public static <T extends Parcelable> T get(String key, Class<T> tClass) {
        return get(key, tClass, null, null);
    }

    public static <T extends Parcelable> T get(String key, Class<T> tClass, T defaultT) {
        return get(key, tClass, defaultT, null);
    }

    public static <T extends Parcelable> T get(String key, Class<T> tClass, String fileName) {
        return get(key, tClass, null, fileName);
    }

    /**
     * 获取实现了Parcelable的对象
     *
     * @param key
     * @param tClass
     * @param defaultT
     * @param fileName
     * @param <T>
     * @return
     */
    public static <T extends Parcelable> T get(String key, Class<T> tClass, T defaultT, String fileName) {
        MMKV mmkv = getMMKV(fileName);
        T t = mmkv.decodeParcelable(key, tClass);
        if (t == null) {
            return defaultT;
        }
        return t;
    }

    /**
     * 移出默认文件某个key值已经对应的值
     *
     * @param key
     */
    public static void remove(String key) {
        remove(key, null);
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     */
    public static void remove(String key, String fileName) {
        MMKV mmkv = getMMKV(fileName);
        mmkv.remove(key);
    }

    /**
     * 清除默认文件所有数据
     */
    public static void clear() {
        clear(null);
    }

    /**
     * 清除指定文件所有数据
     *
     * @param fileName
     */
    public static void clear(String fileName) {
        MMKV mmkv = getMMKV(fileName);
        mmkv.clear();
    }

    /**
     * 查询默认文件下某个key是否已经存在
     *
     * @param key
     * @return
     */
    public static boolean contains(String key) {
        return contains(key, null);
    }


    /**
     * 查询指定文件下某个key是否已经存在
     *
     * @param key
     * @return
     */
    public static boolean contains(String key, String fileName) {
        MMKV mmkv = getMMKV(fileName);
        return mmkv.contains(key);
    }

    /**
     * 迁移SharedPreferences数据到mmkv
     *
     * @param context
     */
    public static void importSharedPreferences(Context context) {
        importSharedPreferences(context, MMKVUtils.USERS, "du_account");
        importSharedPreferences(context, MMKVUtils.SHARE_DATA, null);
        importSharedPreferences(context, MMKVUtils.INIT_SP, null);
        importSharedPreferences(context, MMKVUtils.COOKIE_SP, "CookiePersistence");
    }

    /**
     * 迁移SharedPreferences数据到mmkv
     *
     * @param context
     * @param spFileName   需要迁移的SharedPreferences文件名
     * @param mmkvFileName 需要迁移到mmkv的文件名
     */
    public static void importSharedPreferences(Context context, String spFileName, String mmkvFileName) {
        MMKV mmkv = getMMKV(mmkvFileName);
        //如果已经迁移过就无需迁移
        String key = "is" + spFileName;
        if (mmkv.getBoolean(key, false)) {
            return;
        }
        // 迁移旧数据
        SharedPreferences oldMan = context.getSharedPreferences(spFileName, Context.MODE_PRIVATE);
        mmkv.importFromSharedPreferences(oldMan);
        oldMan.edit().clear().apply();
        mmkv.putBoolean(key, true);
    }

}
