package cn.com.factorytest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;

import java.util.Arrays;

import android.os.Build;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;

public class WriteMacActivity extends Activity {
    private static final String TAG = "FactoryTest";
    private Context mContext;

    EditText m_EditMac;
    TextView m_MacAddr;
    TextView m_SnAddr;
    TextView m_UsidAddr;
    TextView m_DeviceidAddr;

    TextView m_MacAddr_Title;
    TextView m_SnAddr_Title;
    TextView m_UsbMacAddr_Title;
    TextView m_PcieMacAddr_Title;
    TextView m_UsidAddr_Title;
    TextView m_DeviceidAddr_Title;

    private boolean bIsKeyDown = false;

    private boolean SN_SHOW = false;
	private boolean USID_SHOW = false;
    private boolean DEVICE_ID_SHOW = false;
	private boolean WriteMac_ok_flag = false;
	private boolean WriteSn_ok_flag = false;
	private boolean Write_UsbMac_ok_flag = false;
	private boolean Write_PcieMac_ok_flag = false;
    private int MAC_LENGTH = 17;

    private final int MSG_TIME = 777;
    private TimeHandler mHandler = new TimeHandler();

    private class TimeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TIME: {
                    if (bIsKeyDown) {
                        bIsKeyDown = false;
                        mHandler.removeMessages(MSG_TIME);
                        mHandler.sendEmptyMessageDelayed(MSG_TIME, 1 * 1000);
                    } else {
                        mHandler.removeMessages(MSG_TIME);
                        OnScanText();
                    }
                }
                break;
            }
        }
    }

    private void OnScanText() {
        int nTextlen = m_EditMac.getText().toString().length();
        String strMac = m_EditMac.getText().toString();
        if (strMac.isEmpty()) {
            return;
        }

        if (getResources().getInteger(R.integer.config_mac_length2) == nTextlen && (':' == strMac.charAt(2)) && (':' == strMac.charAt(5))) {
            OnWriteMac(true);
        } else if (getResources().getInteger(R.integer.config_mac_length) == nTextlen) {
            OnWriteMac(true);
        } else if (getResources().getInteger(R.integer.config_usid_length) == nTextlen) {
    		OnWriteUsid();
        } else if (getResources().getInteger(R.integer.config_sn_length) == nTextlen) {
            OnWriteSn();
        } else if (getResources().getInteger(R.integer.config_deviceid_length) == nTextlen) {
            OnWriteDeviceid();
        } else {
            m_EditMac.setText("");
            m_EditMac.requestFocus();

//    		Toast.makeText(this, R.string.ScanError, Toast.LENGTH_SHORT).show();
        }
    }

    public static String extractMacAddress(String input) {
		if (input == "") {
            return null;
        }
        // 查找"HWaddr"后面的空格位置
        int hwaddrIndex = input.indexOf("HWaddr") + "HWaddr".length();
        if (hwaddrIndex == -1) {
            // 没有找到"HWaddr"，返回null或空字符串
            return null;
        }

        // 跳过空格，找到MAC地址开始的位置
        while (input.charAt(hwaddrIndex) == ' ') {
            hwaddrIndex++;
        }

        // 检查是否到达了字符串的末尾
        if (hwaddrIndex >= input.length()) {
            return null;
        }

        // MAC地址长度是17（包括冒号），找到结束位置
        int macEndIndex = hwaddrIndex + 17;
        if (macEndIndex > input.length()) {
            // MAC地址不完整，返回null或空字符串
            return null;
        }

        // 使用substring截取MAC地址
        String macAddress = input.substring(hwaddrIndex, macEndIndex);
        return macAddress;
    }

	public void ShowMac_PCIE()
	{
        String rec = Tools.exec("ifconfig | grep r8169");
		Log.d(TAG, "hlm PCIE mac:" + rec);
        String strMac = extractMacAddress(rec);
        if (strMac != null) {
			Log.e(TAG, "ShowMac_PCIE strMac : " + strMac);
			m_SnAddr.setText(strMac);
        } else {
			m_SnAddr.setTextColor(Color.RED);
			m_SnAddr.setText("ERR");
        }
	}

	public void ShowMac_USB()
	{
        String rec = Tools.exec("ifconfig | grep r8152");
		Log.d(TAG, "hlm USB mac:" + rec);
        String strMac = extractMacAddress(rec);
        if (strMac != null) {
			Log.e(TAG, "ShowMac_USB strMac : " + strMac);
			m_MacAddr.setText(strMac);
        } else {
			m_MacAddr.setTextColor(Color.RED);
			m_MacAddr.setText("ERR");
        }
	}

    public void OnWriteMac(boolean is_otp) {
        Log.e(TAG, "public void OnWriteMac()");

        String strMac = m_EditMac.getText().toString();
        if (strMac.isEmpty()) {
            return;
        }
        Log.e(TAG, "is_otp = " + is_otp + " strMac : " + strMac);

        WriteMac(strMac);
        if (is_otp) {
			if (MainActivity.test_board.equals("ACC-A9A10")) {
				ShowMac_USB();
				ShowMac_PCIE();
			} else {
				ShowMac_OTP();
			}
        } else {
            ShowMac();
        }

        m_EditMac.setText("");
        m_EditMac.requestFocus();
		Log.d(TAG, "hlm WriteSn_ok_flag:" + WriteSn_ok_flag + "WriteMac_ok_flag:" + WriteMac_ok_flag);
		if (MainActivity.test_board.equals("ACC-A9A10")) {
			if(Write_PcieMac_ok_flag && Write_UsbMac_ok_flag)
				this.finish();
		} else {
			if(WriteMac_ok_flag && WriteSn_ok_flag)
				this.finish();
		}
    }

    public void OnWriteSn() {
        Log.e(TAG, "public void OnWriteSn()");

        String strSn = m_EditMac.getText().toString();
        if (strSn.isEmpty()) {
            return;
        }
        Log.e(TAG, "strSn : " + strSn);

        WriteSn(strSn);
        ShowSn();

        m_EditMac.setText("");
        m_EditMac.requestFocus();
		Log.d(TAG, "hlm WriteMac_ok_flag:" + WriteMac_ok_flag + "WriteSn_ok_flag:" + WriteSn_ok_flag);
		if (MainActivity.test_board.equals("ACC-A9A10")) {
			if(Write_PcieMac_ok_flag && Write_UsbMac_ok_flag)
				this.finish();
		} else {
			if(WriteMac_ok_flag && WriteSn_ok_flag)
				this.finish();
		}
    }

    public void OnWriteUsid() {
        Log.e(TAG, "public void OnWriteUsid()");

        String strUsid = m_EditMac.getText().toString();
        if (strUsid.isEmpty()) {
            return;
        }
        Log.e(TAG, "strUsid : " + strUsid);
        //截取前20位
        //805写入usid不管多少位，最终生成的usid都会自动加上mac的12位
        //	905写入的usid他不会自动去加mac了 所以不用截取前20位
        String newstrUsid = strUsid.substring(0, 20);
        Log.e(TAG, "newstrUsid : " + newstrUsid);
        if (Tools.isGxbaby()) {
            WriteUsid(strUsid);
        } else {
            WriteUsid(newstrUsid);
        }
        ShowUsid();

        m_EditMac.setText("");
        m_EditMac.requestFocus();
    }

    public void Vim4WriteUsid() {
        Log.e(TAG, "public void Vim4WriteUsid()");

        boolean format_err = true;
        String strUsid = m_EditMac.getText().toString();
        if (strUsid.isEmpty()) {
            return;
        }
        Log.e(TAG, "WriteUsid=" + strUsid);

        if (strUsid.length() == 10) {
            for (int i = 0; i < 10; i++) {
                int value = (int) strUsid.charAt(i);
                if (((value > 0x2f) && (value < 0x3a)) || ((value > 0x40) && (value < 0x47)) || ((value > 0x60) && (value < 0x67))) {
                    format_err = false;
                } else {
                    format_err = true;
                    break;
                }
            }
        }
        if (!format_err) {
			char[] charArr = strUsid.toCharArray();
            for (int j = 0; j < 10; j++) {
                int value = (int) charArr[j];
                if ((value > 0x2f) && (value < 0x3a)) {
					charArr[j] = (char)(value - 48);
                } else if((value > 0x40) && (value < 0x47)){
					charArr[j] = (char)(value - 55);
				} else if((value > 0x60) && (value < 0x67)){
					charArr[j] = (char)(value - 87);
                }
            }
			strUsid = new String(charArr);
            Tools.writeFile(Tools.Key_OTP_Usid, strUsid);
        }

        ShowUsid();

        m_EditMac.setText("");
        m_EditMac.requestFocus();
    }

    public void OnWriteDeviceid() {
        Log.e(TAG, "public void OnWriteDeviceid()");

        String strDeviceid = m_EditMac.getText().toString();
        if (strDeviceid.isEmpty()) {
            return;
        }
        Log.e(TAG, "strDeviceid : " + strDeviceid);

        WriteDeviceid(strDeviceid);
        ShowDeviceid();

        m_EditMac.setText("");
        m_EditMac.requestFocus();
    }

    public void WriteMac(String strMac) {
		String rec = "";
		String cmd_value = "";
        if (getResources().getBoolean(R.bool.config_write_mac_in_otp)) {
            if (strMac.length() == 17) {
                if ((':' == strMac.charAt(2)) && (':' == strMac.charAt(5)) && (':' == strMac.charAt(8)) && (':' == strMac.charAt(11)) && (':' == strMac.charAt(14))) {
                    String mac = strMac.replaceAll(":", "");
                    int length = mac.length();
                    boolean format_err = true;
                    Log.e(TAG, "OTP MAC= " + mac);
                    for (int i = 0; i < length; i++) {
                        int value = (int) mac.charAt(i);
                        if (((value > 0x2f) && (value < 0x3a)) || ((value > 0x40) && (value < 0x47)) || ((value > 0x60) && (value < 0x67))) {
                            if (i == 1) {
                                if (value > 0x2f && value < 0x3a) {
                                    if (value % 2 == 1) {
                                        format_err = true;
                                        break;
                                    }
                                } else {
                                    if (value % 2 == 0) {
                                        format_err = true;
                                        break;
                                    }
                                }
                            }
                            format_err = false;
                        } else {
                            format_err = true;
                            break;
                        }

                    }
                    Log.d(TAG, "MAC =" + mac + " format_err= " + format_err);

                    if (!format_err) {
						if (MainActivity.test_board.equals("ACC-A9A10")) {
							Tools.exec("cp -rf /system/usr/share/android_usb_mac_tool /data/");//usb
							Tools.exec("chown -R system:system /data/android_usb_mac_tool");
							cmd_value = "cd /data/android_usb_mac_tool;rtunicpg-aarch64-armv8 /# 0 /efuse /nodeid " + mac;
							//Log.d(TAG, "hlm r8152 cmd_value:" + cmd_value);
							rec = Tools.exec(cmd_value);
							Log.d(TAG, "hlm exec(cmd_value):" + rec);
							Log.d(TAG, "hlm exec(cmd_value) ====== end");
							//Write_UsbMac_ok_flag = rec.contains("Successfully");
							rec = Tools.exec("ifconfig | grep r8152");
							Log.d(TAG, "hlm r8152 mac:" + rec);
							Write_UsbMac_ok_flag = rec.contains(strMac);
							Log.d(TAG, "hlm Write_UsbMac_ok_flag:" + Write_UsbMac_ok_flag);
						} else {
							Tools.writeFile(Tools.Key_OTP_Mac, mac);
							WriteMac_ok_flag = true;
						}
					}
				}
			} else if (strMac.length() == 12) {
                String mac = strMac;
				int length = mac.length();
				boolean format_err = true;
				Log.e(TAG, "OTP MAC= " + mac);
				for (int i = 0; i < length; i++) {
					int value = (int) mac.charAt(i);
					if (((value > 0x2f) && (value < 0x3a)) || ((value > 0x40) && (value < 0x47)) || ((value > 0x60) && (value < 0x67))) {
						if (i == 1) {
							if (value > 0x2f && value < 0x3a) {
								if (value % 2 == 1) {
									format_err = true;
									break;
								}
							} else {
								if (value % 2 == 0) {
									format_err = true;
									break;
								}
							}
						}
						format_err = false;
					} else {
						format_err = true;
						break;
					}

				}
				Log.d(TAG, "pcie MAC =" + mac + " format_err= " + format_err);
				if (!format_err) {
					if (MainActivity.test_board.equals("ACC-A9A10")) {
						Tools.exec("cp -rf /system/usr/share/android_pcie_mac_tool /data/;chmod 777 /data/android_pcie_mac_tool/pgload.sh");//pcie
						Tools.exec("chown -R system:system /data/android_pcie_mac_tool");
						rec = Tools.exec("cd /data/android_pcie_mac_tool;./pgload.sh");
						//Log.d(TAG, "hlm exec(./pgload.sh):" + rec);
						//Log.d(TAG, "hlm exec(./pgload.sh) ======= end");
						cmd_value = "cd /data/android_pcie_mac_tool;rtnicpg-aarch64-linux-gnu /# 0 /efuse /nodeid " + mac;
						//Log.d(TAG, "hlm r8169 cmd_value:" + cmd_value);
						rec = Tools.exec(cmd_value);
						Log.d(TAG, "hlm exec(cmd_value):" + rec);
						Log.d(TAG, "hlm exec(cmd_value) ====== end");
						//Write_PcieMac_ok_flag = rec.contains("Successfully");
						Tools.exec("/system/bin/rmmod pgdrv");
						Tools.exec("/system/bin/insmod /vendor_dlkm/lib/modules/r8169.ko");
						rec = Tools.exec("ifconfig | grep r8169");
						Log.d(TAG, "hlm r8169 mac:" + rec);
						Write_PcieMac_ok_flag = rec.contains(strMac);
						Log.d(TAG, "hlm Write_PcieMac_ok_flag:" + Write_PcieMac_ok_flag);
					} else {
						Tools.writeFile(Tools.Key_OTP_Mac, mac);
						WriteMac_ok_flag = true;
					}
				}
			}
			return;
		}

        Tools.writeFile(Tools.Key_Name, Tools.Key_Mac);

        String strTmpMac = "";
        int nLength = strMac.length();

        if (getResources().getInteger(R.integer.config_mac_length) == nLength) {
            for (int i = 0; i < nLength; i += 2) {
                strTmpMac += strMac.substring(i, (i + 2) < nLength ? (i + 2) : nLength);

                if ((i + 2) < nLength) strTmpMac += ':';
            }
        } else if (getResources().getInteger(R.integer.config_mac_length2) == nLength) {
            if ((':' == strMac.charAt(2)) && (':' == strMac.charAt(5)) && (':' == strMac.charAt(8)) && (':' == strMac.charAt(11)) && (':' == strMac.charAt(14))) {
                strTmpMac = strMac;
            } else {
                strTmpMac = "";
            }
        } else {
            strTmpMac = "";
        }
        Log.e(TAG, "strTmpMac : " + strTmpMac);

        String strNewMac = CHexConver.str2HexStr(strTmpMac);
        Log.e(TAG, "strNewMac : " + strNewMac);
        Tools.writeFile(Tools.Key_Write, strNewMac);
    }

	public void WriteSn(String strSn)
	{	
		if (strSn.length() == getResources().getInteger(R.integer.config_sn_length)){
			boolean format_err = true;
			Log.e(TAG, "OTP Sn= " + strSn);
			for (int i=0; i<getResources().getInteger(R.integer.config_sn_length); i++) {
				int value = (int)strSn.charAt(i);
				Log.e(TAG, "Sn value= " + value + "i= " + i);

				if(((value > 0x2f) && (value < 0x3a)) || ((value > 0x40) && (value < 0x47)) || ((value > 0x60) && (value < 0x67))) 
					format_err = false;
				else
					format_err = true;
	
				if(format_err)
					break;
			}
			Log.d(TAG,"SN ="+ strSn + " format_err= "+format_err);
			if (!format_err) {
				Tools.writeFile(Tools.Key_OTP_Sn, strSn);	
				WriteSn_ok_flag = true;

			}
		}
	}
	
	public static void WriteUsid(String strUsid)
	{	
		Tools.writeFile(Tools.Key_Name, Tools.Key_Usid);
		String strNewUsid = CHexConver.str2HexStr(strUsid);
		Log.e(TAG, " : " + strNewUsid);
		 Tools.writeFile(Tools.Key_Write, strNewUsid);		 
	}
	
	public static void WriteDeviceid(String strDeviceid)
	{	
		Tools.writeFile(Tools.Key_Name, Tools.Key_Deviceid);
		String strNewDeviceid = CHexConver.str2HexStr(strDeviceid);
		Log.e(TAG, " : " + strNewDeviceid);
		 Tools.writeFile(Tools.Key_Write, strNewDeviceid);		 
	}
	
	public void ShowMac()
	{	
		Tools.writeFile(Tools.Key_Name, Tools.Key_Mac);
		String strMac =  Tools.readFile(Tools.Key_Read);
		
		Log.e(TAG, "strMac : " + strMac  + ";  length    : " + strMac.length() );				
		m_MacAddr.setText(CHexConver.hexStr2Str(strMac) );
	}

	public void ShowMac_OTP()
	{
		String strTmpMac = "";
		String strMac = Tools.readFile(Tools.Key_OTP_Mac);
		Log.e(TAG, "strMac : " + strMac  + ";  length    : " + strMac.length() );

		int length = strMac.length();
		if (length != 12) {
			m_MacAddr.setTextColor(Color.RED);
			m_MacAddr.setText("ERR");

		} else {
			for(int i = 0; i < length; i += 2) {

				strTmpMac += strMac.substring(i, (i + 2) < length ? (i + 2) :  length );
				if( (i + 2) < length) strTmpMac += ':';
				}
				m_MacAddr.setText(strTmpMac);
		}
	}
	
	public void ShowSn()
	{
		String strSn =  Tools.readFile(Tools.Key_OTP_Sn);
		
		Log.e(TAG, "strSn : " + strSn);
		m_SnAddr.setText(strSn);
	}

	public void ShowUsid()
	{
		Tools.writeFile(Tools.Key_Name, Tools.Key_Usid);
		String strUsid =  Tools.readFile(Tools.Key_Read);
		
		Log.e(TAG, "strUsid : " + strUsid);
		m_UsidAddr.setText(CHexConver.hexStr2Str(strUsid) );
	}
	
	public void ShowDeviceid()
	{
		Tools.writeFile(Tools.Key_Name, Tools.Key_Deviceid);
		String strDeviceid =  Tools.readFile(Tools.Key_Read);
		
		Log.e(TAG, "strDeviceid : " + strDeviceid);
		m_DeviceidAddr.setText(CHexConver.hexStr2Str(strDeviceid) );
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_mac);
		WriteMac_ok_flag = false;
		WriteSn_ok_flag = false;
	    Write_UsbMac_ok_flag = false;
	    Write_PcieMac_ok_flag = false;

        m_EditMac = (EditText) findViewById(R.id.EditTextMac);
        m_EditMac.setInputType(InputType.TYPE_NULL);
        m_EditMac.addTextChangedListener(mTextWatcher);

        m_MacAddr = (TextView) findViewById(R.id.TextView_mac);
        m_SnAddr = (TextView) findViewById(R.id.TextView_sn);
        m_UsidAddr = (TextView) findViewById(R.id.TextView_usid);
        m_DeviceidAddr = (TextView) findViewById(R.id.TextView_deviceid);
		m_MacAddr_Title = (TextView) findViewById(R.id.TextView_mac_title);
		m_MacAddr_Title.setText(m_MacAddr_Title.getText().toString()
				+ "\t\t\t" + getResources().getInteger(R.integer.config_mac_length) + getResources().getString(R.string.showLength));

		m_SnAddr_Title = (TextView) findViewById(R.id.TextView_sn_title);
		m_SnAddr_Title.setText(m_SnAddr_Title.getText().toString()
				+ "\t\t\t" + getResources().getInteger(R.integer.config_sn_length) + getResources().getString(R.string.showLength));

		m_UsbMacAddr_Title = (TextView) findViewById(R.id.TextView_mac_usb_title);
		m_UsbMacAddr_Title.setText(m_UsbMacAddr_Title.getText().toString()
				+ "\t\t\t" + getResources().getInteger(R.integer.config_mac_length) + getResources().getString(R.string.showLength));

		m_PcieMacAddr_Title = (TextView) findViewById(R.id.TextView_mac_pcie_title);
		m_PcieMacAddr_Title.setText(m_PcieMacAddr_Title.getText().toString()
				+ "\t\t\t" + getResources().getInteger(R.integer.config_mac_length) + getResources().getString(R.string.showLength));

		if (MainActivity.test_board.equals("ACC-A9A10")) {
			m_MacAddr_Title.setVisibility(View.GONE);
			m_SnAddr_Title.setVisibility(View.GONE);
		} else {
			m_UsbMacAddr_Title.setVisibility(View.GONE);
			m_PcieMacAddr_Title.setVisibility(View.GONE);
		}
        m_UsidAddr_Title = (TextView) findViewById(R.id.TextView_usid_title);
        m_UsidAddr_Title.setText(m_UsidAddr_Title.getText().toString()
                + "\t\t\t" + getResources().getInteger(R.integer.config_usid_length) + getResources().getString(R.string.showLength));

        m_DeviceidAddr_Title = (TextView) findViewById(R.id.TextView_deviceid_title);
        m_DeviceidAddr_Title.setText(m_DeviceidAddr_Title.getText().toString()
                + "\t\t\t" + getResources().getInteger(R.integer.config_deviceid_length) + getResources().getString(R.string.showLength));

        if (!USID_SHOW) {
            m_UsidAddr_Title.setVisibility(View.GONE);
            m_UsidAddr.setVisibility(View.GONE);
        }
        if (!DEVICE_ID_SHOW) {
            m_DeviceidAddr_Title.setVisibility(View.GONE);
            m_DeviceidAddr.setVisibility(View.GONE);
        }

        if (getResources().getBoolean(R.bool.config_write_mac_in_otp)) {
            //String str_mac = Tools.readFile(Tools.Key_OTP_Mac);
			if (MainActivity.test_board.equals("ACC-A9A10")) {
				ShowMac_USB();
				ShowMac_PCIE();
			} else {
				ShowMac_OTP();
				ShowSn();
			}

        } else {
            if (Tools.isGxbaby()) {
                Tools.writeFile(Tools.Key_Attach, Tools.Key_Attach_Value);
            }
            String strKeyList = Tools.readFile(Tools.Key_List);

            Log.e(TAG, strKeyList);
            if (-1 != strKeyList.indexOf(Tools.Key_Mac)) {
                ShowMac();
            }
            if (-1 != strKeyList.indexOf(Tools.Key_Sn)) {
                ShowSn();
            }
            if (-1 != strKeyList.indexOf(Tools.Key_Usid)) {
                ShowUsid();
            }
            if (-1 != strKeyList.indexOf(Tools.Key_Deviceid)) {
                ShowDeviceid();
            }

        }
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            bIsKeyDown = true;

            mHandler.sendEmptyMessageDelayed(MSG_TIME, 1 * 1000);
        }
    };
}
