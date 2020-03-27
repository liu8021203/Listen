package com.ting.provider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.Map;
import java.util.Set;

import androidx.annotation.Nullable;

public class SharedPreferenceProxy implements SharedPreferences{
    private Context mContext;
    private String name = "hahaha";


    public SharedPreferenceProxy(Context context) {
        mContext = context;
    }

    @Override
    public Map<String, ?> getAll() {
        return null;
    }

    @Nullable
    @Override
    public String getString(String key, @Nullable String defValue) {
        Bundle bundle = new Bundle();
        Bundle res = mContext.getContentResolver().call(PreferenceUtil.URI
                , PreferenceUtil.METHOD_QUERY_VALUE
                , name
                , bundle);

        return res == null ? defValue : res.getString(key);
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String key, @Nullable Set<String> defValues) {
        return null;
    }

    @Override
    public int getInt(String key, int defValue) {
        return 0;
    }

    @Override
    public long getLong(String key, long defValue) {
        return 0;
    }

    @Override
    public float getFloat(String key, float defValue) {
        return 0;
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return false;
    }

    @Override
    public boolean contains(String key) {
        return false;
    }

    @Override
    public Editor edit() {
        return null;
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {

    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {

    }
}
