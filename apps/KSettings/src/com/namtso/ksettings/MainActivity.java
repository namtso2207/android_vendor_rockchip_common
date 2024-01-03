package com.namtso.ksettings;

import android.content.Context;
import android.os.SystemProperties;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends PreferenceActivity implements Preference.OnPreferenceClickListener {

    private ListPreference FAN_Preference;
    private SwitchPreference WOL_Preference;
    private SwitchPreference EXT_WOL_Preference;

    private Context mContext;

    private static final String FAN_KEY = "FAN_KEY";
    private static final String WOL_KEY = "WOL_KEY";
    private static final String EXT_WOL_KEY = "EXT_WOL_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.main);
        mContext = this;
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        FAN_Preference = (ListPreference) findPreference(FAN_KEY);
        bindPreferenceSummaryToValue(FAN_Preference);
        WOL_Preference = (SwitchPreference)findPreference(WOL_KEY);
        //WOL_Preference.setChecked(true);
        WOL_Preference.setOnPreferenceClickListener(this);
        try {
            String ret = ComApi.execCommand(new String[]{"sh", "-c", "cat /sys/class/wol/eth0_enable"});
            if(ret.equals("1")){
                WOL_Preference.setChecked(true);
            }else{
                WOL_Preference.setChecked(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        EXT_WOL_Preference = (SwitchPreference)findPreference(EXT_WOL_KEY);
        //EXT_WOL_Preference.setChecked(true);
        EXT_WOL_Preference.setOnPreferenceClickListener(this);
        try {
            String ret = ComApi.execCommand(new String[]{"sh", "-c", "cat /sys/class/wol/eth1_enable"});
            if(ret.equals("1")){
                EXT_WOL_Preference.setChecked(true);
            }else{
                EXT_WOL_Preference.setChecked(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * bindPreferenceSummaryToValue 拷贝至as自动生成的preferences的代码，用于绑定显示实时值
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            String key = preference.getKey();
            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                // Set the summary to reflect the new value.
                preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

                if(FAN_KEY.equals(key)){
                    //Log.d("wjh","f===" + index);
                    //set Fan Level
                    switch (index){
                        case 0:
                            try {
                                ComApi.execCommand(new String[]{"sh", "-c", "echo 0 > /sys/class/fan/enable"});
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 1:
                            try {
                                ComApi.execCommand(new String[]{"sh", "-c", "echo 1 > /sys/class/fan/enable"});
                                ComApi.execCommand(new String[]{"sh", "-c", "echo 1 > /sys/class/fan/mode"});
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 2:
                            try {
                                ComApi.execCommand(new String[]{"sh", "-c", "echo 1 > /sys/class/fan/enable"});
                                ComApi.execCommand(new String[]{"sh", "-c", "echo 0 > /sys/class/fan/mode"});
                                ComApi.execCommand(new String[]{"sh", "-c", "echo 1 > /sys/class/fan/level"});
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 3:
                            try {
                                ComApi.execCommand(new String[]{"sh", "-c", "echo 1 > /sys/class/fan/enable"});
                                ComApi.execCommand(new String[]{"sh", "-c", "echo 0 > /sys/class/fan/mode"});
                                ComApi.execCommand(new String[]{"sh", "-c", "echo 2 > /sys/class/fan/level"});
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 4:
                            try {
                                ComApi.execCommand(new String[]{"sh", "-c", "echo 1 > /sys/class/fan/enable"});
                                ComApi.execCommand(new String[]{"sh", "-c", "echo 0 > /sys/class/fan/mode"});
                                ComApi.execCommand(new String[]{"sh", "-c", "echo 3 > /sys/class/fan/level"});
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 5:
                            try {
                                ComApi.execCommand(new String[]{"sh", "-c", "echo 1 > /sys/class/fan/enable"});
                                ComApi.execCommand(new String[]{"sh", "-c", "echo 0 > /sys/class/fan/mode"});
                                ComApi.execCommand(new String[]{"sh", "-c", "echo 4 > /sys/class/fan/level"});
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 6:
                            try {
                                ComApi.execCommand(new String[]{"sh", "-c", "echo 1 > /sys/class/fan/enable"});
                                ComApi.execCommand(new String[]{"sh", "-c", "echo 0 > /sys/class/fan/mode"});
                                ComApi.execCommand(new String[]{"sh", "-c", "echo 5 > /sys/class/fan/level"});
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                    SystemProperties.set("persist.sys.fan_control", "" + index);

                }

            }  else {
                // For all other preferences, set the summary to the value's
                // simple string representation.

                preference.setSummary(stringValue);
            }
            return true;
        }
    };
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        final String key = preference.getKey();
        if (WOL_KEY.equals(key)){
            if (WOL_Preference.isChecked()) {
                //Toast.makeText(this,"true",Toast.LENGTH_SHORT).show();
                try {
                    ComApi.execCommand(new String[]{"sh", "-c", "echo 1 > /sys/class/wol/eth0_enable"});
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                //Toast.makeText(this,"false",Toast.LENGTH_SHORT).show();
                try {
                    ComApi.execCommand(new String[]{"sh", "-c", "echo 0 > /sys/class/wol/eth0_enable"});
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else if (EXT_WOL_KEY.equals(key)){
            if (EXT_WOL_Preference.isChecked()) {
                //Toast.makeText(this,"true",Toast.LENGTH_SHORT).show();
                try {
                    ComApi.execCommand(new String[]{"sh", "-c", "echo 1 > /sys/class/wol/eth1_enable"});
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                //Toast.makeText(this,"false",Toast.LENGTH_SHORT).show();
                try {
                    ComApi.execCommand(new String[]{"sh", "-c", "echo 0 > /sys/class/wol/eth1_enable"});
                } catch (IOException e) {
                    e.printStackTrace();
                }
			}
        }
        return true;
    }
}
