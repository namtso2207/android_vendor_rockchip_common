package cn.com.factorytest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.os.StatFs;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.text.Editable;
import android.text.format.Formatter;
import android.Manifest;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.hardware.Camera;
import android.view.MotionEvent;


import java.io.FileReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Enumeration;
import java.net.*;
import java.math.*;
import cn.com.factorytest.helper.SerialHelper;
import cn.com.factorytest.helper.MyFunc;
import cn.com.factorytest.helper.ComBean;
import android_serialport_api.SerialPort;
import java.util.LinkedList;
import java.util.Queue;
import cn.com.factorytest.WriteMacActivity;

public class MainActivity extends Activity implements SurfaceHolder.Callback {

    public static final String TAG = Tools.TAG;

    public static String test_board="A10-3588";
	public static String udisk_backup = "";
    public static boolean tfcard_test = false;
    public static boolean usb20_test = false;
    public static boolean hdmi_sound_test = true;
    public static boolean usb30_test = false;
    public static boolean spi_test = false;
    public static boolean ext_spi_test = false;
    public static boolean gsensor_test = false;
    public static boolean mcu_test = false;
    public static boolean hdmi_test = false;
    public static boolean uart_test = false;
    public static boolean i2c_test = false;
    public static boolean can_test = false;
    public static boolean fusb302_test = false;
    public static boolean gigabit_test = false;
    public static boolean lan_test = false;
    public static boolean wifi_test = false;
    public static boolean bt_test = false;
    public static boolean rtc_test = false;
    public static boolean ageing_test = false;

    public static boolean power_led_test = false;
    public static boolean irkey_test = false;
    public static boolean wol_enable = false;
    public static boolean mic_test = false;
    public static boolean mipi_camera_test = false;
    public static boolean board_key_test = false;
    public static boolean key_test = false;
    public static boolean ssd_test = false;
    public static boolean reset_mcu = false;
    public static boolean wirte_mac = false;
	public static boolean gpio_test = false;
	public static boolean sata_test = false;

    private static boolean ageing_test_ok_flag = false;

    TextView m_firmware_version;
    TextView m_ddr_size;
    TextView m_nand_size;
    TextView m_device_type;
    TextView m_macvalue;
    TextView m_snvalue;
    TextView m_ip;
    TextView m_wifimac;
    TextView m_wifiip;
    TextView m_device_id;

    TextView m_mactitle;
    EditText m_maccheck;
    TextView m_TextView_Time;
    TextView m_TextView_TF;
    TextView m_TextView_USB1;
    TextView m_TextView_USB2;

    TextView m_TextView_MacLabel;
	TextView m_TextView_usb_MacLabel;
	TextView m_TextView_pcie_MacLabel;
	TextView m_TextView_ip_label;
    TextView m_TextView_SPI;
    TextView m_TextView_MCU;
    TextView m_TextView_HDMI;
    TextView m_TextView_UART;
    TextView m_TextView_I2C;
    TextView m_TextView_CAN;
	TextView m_TextView_KEY;
	TextView m_TextView_SSD;
    TextView m_TextView_GSENSOR;
    TextView m_TextView_AGEING;
    TextView m_TextView_FUSB302;
    TextView m_TextView_Gigabit;
    TextView m_TextView_Lan;
    TextView m_TextView_Wifi;
    TextView m_TextView_BT;
    TextView m_TextView_Rtc;
    TextView m_TextView_SATA;

    TextView m_TextView_CPU_THERMAL;
    TextView m_TextView_CPU_FREQ;
	TextView m_TextView_GPIO;
    Button m_Button_write_mac_usid;
    Button m_Button_PowerLed;
    Button m_Button_Key;

    Button m_Button_EnableWol;
    Button m_Button_IRKey;
    Button m_Button_reset_MCU;
    Button m_Button_MIC;
    Button m_Button_Mipi_Camera;

    Handler mHandler = new FactoryHandler();

    private final int MSG_WIFI_TEST_ERROR = 77;
    private final int MSG_WIFI_TEST_OK = 78;
    private final int MSG_LAN_TEST_ERROR = 79;
    private final int MSG_LAN_TEST_OK = 80;
    private final int MSG_TF_TEST_ERROR = 81;
    private final int MSG_TF_TEST_OK = 82;
    private final int MSG_USB1_TEST_ERROR = 83;
    private final int MSG_USB1_TEST_OK = 84;
    private final int MSG_USB2_TEST_ERROR = 85;
    private final int MSG_USB2_TEST_OK = 86;
    private final int MSG_NETLED_TEST_Start = 87;
    private final int MSG_NETLED_TEST_End = 88;
    private final int MSG_POWERLED_TEST_Start = 89;
    private final int MSG_POWERLED_TEST_End = 90;
    private final int MSG_WIFI_TOAST = 91;
    private final int MSG_PLAY_VIDEO = 92;
    private final int MSG_TF_TEST_XL_OK = 93;
    private final int MSG_TF_TEST_XL_ERROR = 94;
    private final int MSG_USB1_TEST_XL_OK = 95;
    private final int MSG_USB1_TEST_XL_ERROR = 96;
    private final int MSG_android_6_0_TEXT_LAYOUT = 97;
    private final int MSG_USB2_TEST_XL_OK = 98;
    private final int MSG_USB2_TEST_XL_ERROR = 99;
    private final int MSG_RTC_TEST_OK = 100;
    private final int MSG_RTC_TEST_ERROR = 101;
    private final int MSG_BT_TEST_ERROR = 102;
    private final int MSG_BT_TEST_OK = 103;
    private final int MSG_MCU_TEST_ERROR = 104;
    private final int MSG_MCU_TEST_OK = 105;
    private final int MSG_HDMI_TEST_ERROR = 106;
    private final int MSG_HDMI_TEST_OK = 107;
    private final int MSG_SPI_TEST_ERROR = 108;
    private final int MSG_SPI_TEST_OK = 109;
    private final int MSG_GIGABIT_TEST_ERROR = 110;
    private final int MSG_GIGABIT_TEST_OK = 111;
    private final int MSG_FUSB302_TEST_ERROR = 112;
    private final int MSG_FUSB302_TEST_OK = 113;
    private final int MSG_GSENSOR_TEST_ERROR = 114;
    private final int MSG_GSENSOR_TEST_OK = 115;
    private final int MSG_AGEING_TEST_ERROR = 116;
    private final int MSG_AGEING_TEST_OK = 117;
    private final int MSG_GET_CPU_STATUS = 118;
    private final int MSG_UART_TEST_ERROR = 119;
    private final int MSG_UART_TEST_OK = 120;
    private final int MSG_I2C_TEST_ERROR = 121;
    private final int MSG_I2C_TEST_OK = 122;	
    private final int MSG_CAN_TEST_ERROR = 123;
    private final int MSG_CAN_TEST_OK = 124;
    private final int MSG_KEY_TEST_ERROR = 125;
    private final int MSG_KEY_TEST_OK = 126;
    private final int MSG_SSD_TEST_ERROR = 127;
    private final int MSG_SSD_TEST_OK = 128;
    private final int MSG_SATA_TEST_ERROR = 129;
    private final int MSG_SATA_TEST_OK = 130;
    private final int MSG_TIME = 777;
    private static final String nullip = "0.0.0.0";
    private static final String USB_PATH = (Tools.isAndroid5_1_1() ? "/storage/udisk" : "/storage/external_storage/sd");
    private static final String USB1_PATH = (Tools.isAndroid5_1_1() ? "/storage/udisk0" : "/storage/external_storage/sda");
    private static final String USB2_PATH = (Tools.isAndroid5_1_1() ? "/storage/udisk1" : "/storage/external_storage/sdb");
    private static final String TFCARD_PATH = (Tools.isAndroid5_1_1() ? "/storage/sdcard" : "/storage/external_storage/sdcard");
    private List<ScanResult> wifiList;

    String configSSID = "";
    int configLevel = 60;

    int wifiLevel = 0;
    String usb_path = "";
    LinearLayout mLeftLayout, mBottomLayout, mBottomLayout2, mBottomLayout3, mBottomLayout4, mBottomLayout5, mBottomLayout6;
    String configFile = "";
    int tag_net = 0;
    int tag_power = 0;
    AudioManager mAudioManager = null;
    int maxVolume;
    int currentVolume;
    String lssue_value = "";
    String client_value = "";
    String readMac = "";
    String readSn = "";
    String readDeviceid = "";
    String uart_data = "";
	
    private boolean bIsKeyDown = false;
    //系统灯和网络灯测试时间 单位s
    int ledtime = 60;
    //videoview 全屏播放时间
    private final long MSG_PLAY_VIDEO_TIME = 480 * 30 * 60 * 1000;

    private Context mContext;
    private BTDeviceReceiver mBTDeviceReceiver;
    private int CONFIG_BT_RSSI = -100;
    private boolean BT_ERR = true;
    private int BT_try_count = 2;
    private int btLevel = 0;
    private final String BTSSID = "Khadas";
    public static int ageing_flag = 0;
    public static int ageing_time = 0;
    public static int ageing_cpu_max = 0;
	//public static int uart_flag = 0;
	String[] uartStatus = new String[4];
	public static int i2c_flag = 0;
	public static int can_flag = 0;
	public static int spi_flag = 0;
	public static int key_flag = 0;
	public static int LAN_flag = 0;
	public static int GIGABIT_flag = 0;

	String[] usb2Status = new String[3];
	String[] usb3Status = new String[4];
	public static int usb3_flag = 0;
	String[] ssdStatus = new String[3];

    private SurfaceView mSurfaceview = null;
    private SurfaceHolder mSurfaceHolder = null;
    private Camera mCamera = null;

    SerialControl ComA,ComB,ComC,ComD;
	DispQueueThread DispQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if(!hdmi_sound_test)
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        else
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);

        initSurfaceView();

        m_firmware_version = (TextView) findViewById(R.id.firmware_version_value);
        m_device_type = (TextView) findViewById(R.id.device_type_value);
        m_macvalue = (TextView) findViewById(R.id.mac_value);
        m_snvalue = (TextView) findViewById(R.id.sn_value);
        m_device_id = (TextView) findViewById(R.id.device_id_value);

        m_ip = (TextView) findViewById(R.id.ip_value);
        m_wifiip = (TextView) findViewById(R.id.wifi_ip_value);
        m_wifimac = (TextView) findViewById(R.id.wifi_mac_value);
        m_nand_size = (TextView) findViewById(R.id.nand_size_value);
        m_ddr_size = (TextView) findViewById(R.id.ddr_size_value);

        m_TextView_CPU_THERMAL = (TextView) findViewById(R.id.cpu_thermal_value);
        m_TextView_CPU_FREQ = (TextView) findViewById(R.id.cpu_freq_value);

        m_TextView_Time = (TextView) findViewById(R.id.TextView_Time);
        m_TextView_TF = (TextView) findViewById(R.id.TextView_TF);
        m_TextView_USB1 = (TextView) findViewById(R.id.TextView_USB1);
        m_TextView_USB2 = (TextView) findViewById(R.id.TextView_USB2);

        m_TextView_MacLabel = (TextView) findViewById(R.id.mac_label);
		m_TextView_usb_MacLabel = (TextView) findViewById(R.id.usb_mac_label);
		m_TextView_pcie_MacLabel = (TextView) findViewById(R.id.pcie_mac_label);
		m_TextView_ip_label = (TextView) findViewById(R.id.ip_label);
        m_TextView_Gigabit = (TextView) findViewById(R.id.TextView_Gigabit);
        m_TextView_Lan = (TextView) findViewById(R.id.TextView_Lan);
        m_TextView_MCU = (TextView) findViewById(R.id.TextView_MCU);
        m_TextView_SPI = (TextView) findViewById(R.id.TextView_SPI);
        m_TextView_HDMI = (TextView) findViewById(R.id.TextView_HDMI);
		m_TextView_UART = (TextView) findViewById(R.id.TextView_UART);
		m_TextView_I2C = (TextView) findViewById(R.id.TextView_I2C);
		m_TextView_CAN = (TextView) findViewById(R.id.TextView_CAN);
		m_TextView_KEY = (TextView) findViewById(R.id.TextView_KEY);
		m_TextView_SSD = (TextView) findViewById(R.id.TextView_SSD);
		m_TextView_SATA = (TextView) findViewById(R.id.TextView_SATA);
        m_TextView_GSENSOR = (TextView) findViewById(R.id.TextView_GSENSOR);
        m_TextView_AGEING = (TextView) findViewById(R.id.TextView_AGEING);
        m_TextView_FUSB302 = (TextView) findViewById(R.id.TextView_FUSB302);
        m_TextView_Wifi = (TextView) findViewById(R.id.TextView_Wifi);
        m_TextView_BT = (TextView) findViewById(R.id.TextView_BT);
        m_TextView_Rtc = (TextView) findViewById(R.id.TextView_Rtc);
		m_TextView_GPIO = (TextView)findViewById(R.id.TextView_GPIO);
        m_Button_write_mac_usid = (Button) findViewById(R.id.Button_Writemac);
        m_Button_PowerLed = (Button) findViewById(R.id.Button_PowerLed);
        m_Button_Key = (Button) findViewById(R.id.KeyTest);
        m_Button_EnableWol = (Button) findViewById(R.id.EnableWol);
        m_Button_IRKey = (Button) findViewById(R.id.IRKeyTest);
        m_Button_reset_MCU = (Button) findViewById(R.id.Button_RstMcu);
        m_Button_MIC = (Button) findViewById(R.id.speaker_MIC);
        m_Button_Mipi_Camera = (Button) findViewById(R.id.Mipi_Camera);

        m_maccheck = (EditText) findViewById(R.id.EditTextMac);
        m_maccheck.setInputType(InputType.TYPE_NULL);
        m_maccheck.addTextChangedListener(mTextWatcher);
        m_mactitle = (TextView) findViewById(R.id.MacTitle);

        if (!irkey_test) {
            m_Button_IRKey.setVisibility(View.GONE);
        }

        if (!mic_test) {
            m_Button_MIC.setVisibility(View.GONE);
        }

        if (!reset_mcu) {
            m_Button_reset_MCU.setVisibility(View.GONE);
        }

        m_Button_Mipi_Camera.setVisibility(View.GONE);
        if (!mipi_camera_test) {
            mSurfaceview.setVisibility(View.GONE);
        }

        if (!mcu_test) {
            m_TextView_MCU.setVisibility(View.GONE);
        }

        if (!ageing_test) {
            m_TextView_AGEING.setVisibility(View.GONE);
        }
        if (!rtc_test) {
            m_TextView_Rtc.setVisibility(View.GONE);
        }

        if (!usb20_test) {
            m_TextView_USB1.setVisibility(View.GONE);
        }

        if (!usb30_test) {
            m_TextView_USB2.setVisibility(View.GONE);
        }

        if (!tfcard_test) {
            m_TextView_TF.setVisibility(View.GONE);
        }
        if (!gsensor_test) {
            m_TextView_GSENSOR.setVisibility(View.GONE);
        }

        if (!fusb302_test) {
            m_TextView_FUSB302.setVisibility(View.GONE);
        }
        if (!hdmi_test) {
            m_TextView_HDMI.setVisibility(View.GONE);
        }
        if (!uart_test) {
            m_TextView_UART.setVisibility(View.GONE);
        }
        if (!i2c_test) {
            m_TextView_I2C.setVisibility(View.GONE);
        }
        if (!can_test) {
            m_TextView_CAN.setVisibility(View.GONE);
        }
        if (!key_test) {
            m_TextView_KEY.setVisibility(View.GONE);
        }
        if (!ssd_test) {
            m_TextView_SSD.setVisibility(View.GONE);
        }
        if (!sata_test) {
            m_TextView_SATA.setVisibility(View.GONE);
        }
        if (!gpio_test) {
            m_TextView_GPIO.setVisibility(View.GONE);
        }
		else{
			m_TextView_GPIO.setText(getResources().getString(R.string.GPIO_Test));
		}
        if (!wirte_mac) {
            m_Button_write_mac_usid.setVisibility(View.GONE);
        }

        if (!power_led_test) {
            m_Button_PowerLed.setVisibility(View.GONE);
        }

        if (!board_key_test) {
            m_Button_Key.setVisibility(View.GONE);
        }
        if (!wol_enable) {
            m_Button_EnableWol.setVisibility(View.GONE);
        }
        if (!spi_test) {
            m_TextView_SPI.setVisibility(View.GONE);
        }

        if (!gigabit_test) {
            m_TextView_Gigabit.setVisibility(View.GONE);
        }

        if (!wifi_test) {
            m_TextView_Wifi.setVisibility(View.GONE);
        }

        if (!bt_test) {
            m_TextView_BT.setVisibility(View.GONE);
        }

        if (!lan_test) {
            m_TextView_Lan.setVisibility(View.GONE);
        }
		if (MainActivity.test_board.equals("ACC-A9A10")) {
			m_TextView_MacLabel.setVisibility(View.GONE);
			m_TextView_ip_label.setVisibility(View.GONE);
		} else {
			m_TextView_usb_MacLabel.setVisibility(View.GONE);
			m_TextView_pcie_MacLabel.setVisibility(View.GONE);
		}
        mLeftLayout = (LinearLayout) findViewById(R.id.Layout_Left);
        mBottomLayout = (LinearLayout) findViewById(R.id.Layout_Bottom);
        mBottomLayout2 = (LinearLayout) findViewById(R.id.Layout_Bottom2);
        mBottomLayout3 = (LinearLayout) findViewById(R.id.Layout_Bottom3);
        mBottomLayout4 = (LinearLayout) findViewById(R.id.Layout_Bottom4);
        mBottomLayout5 = (LinearLayout) findViewById(R.id.Layout_Bottom5);
        mBottomLayout6 = (LinearLayout) findViewById(R.id.Layout_Bottom6);

        mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        mWifiManager.setWifiEnabled(true);
		test_UART();
		test_gpio();
		if(key_test)
			Tools.exec("echo 1 > /sys/class/mcu/key_test");
        updateTime();
        new Thread() {
            public void run() {

                if (ageing_test) {
                    test_cpu_ageing();
                }

                while (true) {
                    try {
                        unregisterBTReceiver();
                        test_Thread();
                        Thread.sleep(10 * 1000);
                    } catch (Exception localException1) {

                    }

                }
            }
        }.start();

        new Thread() {
            public void run() {
                while (true) {
                    try {
                        mHandler.sendEmptyMessage(MSG_GET_CPU_STATUS);
                        Thread.sleep(1000);
						if(2 == VideoFragment.ageing_test_step && !ageing_test_ok_flag){
							mHandler.sendEmptyMessage(MSG_AGEING_TEST_OK);
							ageing_test_ok_flag = true;
						}
						else if(1 == VideoFragment.ageing_test_step && ageing_test_ok_flag){
							mHandler.sendEmptyMessage(MSG_AGEING_TEST_ERROR);
							ageing_test_ok_flag = false;
						}
                    } catch (Exception localException1) {
                    }
                }
            }
        }.start();
    }

    private void initSurfaceView() {
        Log.d(TAG, "CameraTest initSurfaceView");
        mSurfaceview = (SurfaceView) findViewById(R.id.mSurfaceView_mipi);

        mSurfaceHolder = mSurfaceview.getHolder();
        mSurfaceHolder.addCallback(MainActivity.this);
        //mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceview.getHolder().setFixedSize(800, 480);
        mSurfaceview.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holer) {
        try {
            Log.d(TAG, "CameraTest SurfaceHolder.Callback surface Created");

            mCamera = Camera.open(0);
            mCamera.setPreviewDisplay(mSurfaceHolder);

            initCamera();
        } catch (Exception ex) {
            if (null != mCamera) {
                mCamera.release();
                mCamera = null;
            }
            Log.d(TAG, ex.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "CameraTest SurfaceHolder.Callback Surface Changed");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d(TAG, "CameraTest SurfaceHolder.Callback Surface Destroyed");
        if (null != mCamera) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    private void initCamera() {
        if (mCamera != null) {
            try {
                Camera.Parameters parameters = mCamera.getParameters();
                mCamera.setParameters(parameters);
                mCamera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void test_Thread() {
		test_FAN();
        test_AGEING();
        test_volumes();
        test_ETH();
        test_BT();
        test_RTC();
        test_MCU();
        test_FUSB302();
        test_GSENSOR();
        test_HDMI();
        test_Gigabit();
		test_ext_SPI();
        test_SPI();
		test_i2c();
		test_CAN();
		test_SSD();
		test_SATA();
		if(wifi_test)
			test_Wifi();

    }

    private void test_UART() {
		if (uart_test) {
	        for (int i=0; i< uartStatus.length; i++) {
	            uartStatus[i] = getResources().getString(R.string.Test_Fail);
	        }
			if (!MainActivity.test_board.equals("ACC-A9A10")) {
				ComA = new SerialControl();
				ComA.setPort("/dev/ttyWCH0");
				ComA.setBaudRate("115200");
				OpenComPort(ComA);
				ComA.setfactoryTestFlag();
				SetiDelayTime(ComA,"200");
				SetAutoSend(ComA,true);
				SetLoopData(ComA,"ab21cd");

				ComB = new SerialControl();
				ComB.setPort("/dev/ttyWCH1");
				ComB.setBaudRate("115200");
				OpenComPort(ComB);
				ComB.setfactoryTestFlag();
				SetiDelayTime(ComB,"200");
				SetAutoSend(ComB,true);
				SetLoopData(ComB,"ef54jh");
			}
			ComC = new SerialControl();
			ComC.setPort("/dev/ttyWCH2");
			ComC.setBaudRate("115200");
			OpenComPort(ComC);
			ComC.setfactoryTestFlag();
			SetiDelayTime(ComC,"200");
			SetAutoSend(ComC,true);
			SetLoopData(ComC,"qw76rt");

			ComD = new SerialControl();
			ComD.setPort("/dev/ttyWCH3");
			ComD.setBaudRate("115200");
			OpenComPort(ComD);
			ComD.setfactoryTestFlag();
			SetiDelayTime(ComD,"200");
			SetAutoSend(ComD,true);
			SetLoopData(ComD,"yu98op");

			DispQueue = new DispQueueThread();
	        DispQueue.start();
		}
    }
	
private class SerialControl extends SerialHelper{

	public SerialControl(){
	}

	@Override
	protected void onDataReceived(final ComBean ComRecData)
	{
		DispQueue.AddQueue(ComRecData);
	}	
}

private class DispQueueThread extends Thread{
	private Queue<ComBean> QueueList = new LinkedList<ComBean>(); 
	@Override
	public void run() {
		super.run();
		while(!isInterrupted()) {
			final ComBean ComData;
	        while((ComData=QueueList.poll())!=null)
	        {
	        	runOnUiThread(new Runnable()
				{
					public void run()
					{
						uart_data = new String(ComData.bRec);
						Log.d(TAG,"uart ----> " + uart_data);
						if (!MainActivity.test_board.equals("ACC-A9A10")) {
						   if(uart_data.equals("ab21cd")){
								uartStatus[0] = getResources().getString(R.string.Test_Ok);
								SetAutoSend(ComA,false);
							}

						   if(uart_data.equals("ef54jh")){
								uartStatus[1] = getResources().getString(R.string.Test_Ok);
								SetAutoSend(ComB,false);
							}
						}
						if(uart_data.equals("qw76rt")){
							uartStatus[2] = getResources().getString(R.string.Test_Ok);
							SetAutoSend(ComC,false);
						}

					   if(uart_data.equals("yu98op")){
							uartStatus[3] = getResources().getString(R.string.Test_Ok);
							SetAutoSend(ComD,false);					
						}
						if (MainActivity.test_board.equals("ACC-A9A10")) {
							if(uartStatus[2].equals(getResources().getString(R.string.Test_Ok))
									&& uartStatus[3].equals(getResources().getString(R.string.Test_Ok))) {
								mHandler.sendEmptyMessage(MSG_UART_TEST_OK);
							}else{
								mHandler.sendEmptyMessage(MSG_UART_TEST_ERROR);
							}
						} else {
							if(uartStatus[0].equals(getResources().getString(R.string.Test_Ok))
									&& uartStatus[1].equals(getResources().getString(R.string.Test_Ok))
									&& uartStatus[2].equals(getResources().getString(R.string.Test_Ok))
									&& uartStatus[3].equals(getResources().getString(R.string.Test_Ok))) {
								mHandler.sendEmptyMessage(MSG_UART_TEST_OK);
							}else{
								mHandler.sendEmptyMessage(MSG_UART_TEST_ERROR);
							}
						}
					}
				});
	        	try
				{
					Log.d(TAG, "uartStatus[0]=" + uartStatus[0] + "uartStatus[1]=" + uartStatus[1] + "uartStatus[2]=" + uartStatus[2] + "uartStatus[3]=" + uartStatus[3]);
	        		Thread.sleep(50);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
	        	break;
			}
		}
	}

	public synchronized void AddQueue(ComBean ComData){
		QueueList.add(ComData);
	}
}	
		
private void SetLoopData(SerialHelper ComPort,String sLoopData){

		ComPort.setTxtLoopData(sLoopData);
		//ComPort.setHexLoopData(sLoopData);
}
		
private void SetiDelayTime(SerialHelper ComPort,String sTime){
	ComPort.setiDelay(Integer.parseInt(sTime));
}
		
private void SetAutoSend(SerialHelper ComPort,boolean isAutoSend){
	if (isAutoSend)
	{
		ComPort.startSend();
	} else
	{
		ComPort.stopSend();
	}
}

private void sendPortData(SerialHelper ComPort,String sOut){
	if (ComPort!=null && ComPort.isOpen())
	{
			ComPort.sendTxt(sOut);
			//ComPort.sendHex(sOut);
	}
}
	
private void CloseComPort(SerialHelper ComPort){
	if (ComPort!=null){
		ComPort.stopSend();
		ComPort.close();
	}
}
	
private void OpenComPort(SerialHelper ComPort){
	try
	{
		ComPort.open();
	} catch (SecurityException e) {
		ShowMessage("打开串口失败:没有串口读/写权限!");
	} catch (IOException e) {
		ShowMessage("打开串口失败:未知错误!");
	}
}
	
private void ShowMessage(String sMsg)
{
	Toast.makeText(this, sMsg, Toast.LENGTH_SHORT).show();
}
    private void registerBTReceiver() {
		if (bt_test) {
				mBTDeviceReceiver = new BTDeviceReceiver();
				IntentFilter filter = new IntentFilter();
				filter.addAction(BluetoothDevice.ACTION_FOUND);
				filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
				mContext.registerReceiver(mBTDeviceReceiver, filter);
				Log.d(TAG, "registerBTReceiver");
		}
    }

    private class BTDeviceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice btd = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                short rssi = intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
                if (btd != null) {
                    String name = btd.getName();
                    if (name != null) {
                        if (name.equals(BTSSID)) {
                            if (rssi > CONFIG_BT_RSSI) {
                                btLevel = -rssi;
                                BT_ERR = false;
                                mHandler.sendEmptyMessage(MSG_BT_TEST_OK);
                            } else {
                                BT_ERR = true;
                            }
                        }
                        Log.d(TAG, "BT Found device name= " + btd.getName() + "rssi = " + rssi);
                    }
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (BT_ERR) {
                    BT_try_count--;
                    if (BT_try_count > 0) {
                        test_BT();
                    }
                    if (BT_try_count <= 0) {
                        mHandler.sendEmptyMessage(MSG_BT_TEST_ERROR);
                    }
                }
                Log.d(TAG, "BT Found End");
            }
        }
    }


    private void updateEthandWifi() {
        boolean isEthConnected = NetworkUtils.isEthConnected(this);

		if (!MainActivity.test_board.equals("ACC-A9A10")) {
			if (isEthConnected) {
				m_ip.setText(NetworkUtils.getLocalIpAddress(this));
			} else {
				m_ip.setText(nullip);
			}
		}

        WifiManager manager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcpInfo = manager.getDhcpInfo();
        WifiInfo wifiinfo = manager.getConnectionInfo();
        if (wifiinfo != null) {
            m_wifiip.setText(NetworkUtils.int2ip(wifiinfo.getIpAddress()));
            m_wifimac.setText(wifiinfo.getMacAddress());
        } else {
            m_wifiip.setText(nullip);
            m_wifimac.setText(" ");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //readVersion();

        m_ddr_size.setText((Tools.getmem_TOLAL() * 100 / 1024 / 1024 / 100.0) + " GB");
        m_nand_size.setText(Tools.getRomSize(this));
        if (MainActivity.test_board.equals("A10-3588") || MainActivity.test_board.equals("ACC-A9A10")) {
            m_firmware_version.setText(Build.DISPLAY);
        } else {
            m_firmware_version.setText(Build.VERSION.INCREMENTAL);
        }
        m_device_type.setText(MainActivity.test_board);

        updateEthandWifi();


        mHandler.sendEmptyMessageDelayed(MSG_PLAY_VIDEO, MSG_PLAY_VIDEO_TIME);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(mFactoryReceiver, filter);

        IntentFilter mountfilter = new IntentFilter();
        mountfilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        mountfilter.addDataScheme("file");
        registerReceiver(mountReceiver, mountfilter);
		String strMac = "";
		int length = 0;
        if (MainActivity.test_board.equals("ACC-A9A10")) {
            strMac = Tools.exec("ifconfig | grep r8152");
            strMac = WriteMacActivity.extractMacAddress(strMac);
            Log.d(TAG, "hlm USB mac:" + strMac);

			if (strMac.length() != 17) {
				m_macvalue.setTextColor(Color.RED);
				m_macvalue.setText("ERR");
			} else {
				m_macvalue.setTextColor(Color.RED);
				m_macvalue.setText(strMac+" ");
			}

            strMac = Tools.exec("ifconfig | grep r8169");
            strMac = WriteMacActivity.extractMacAddress(strMac);
            Log.d(TAG, "hlm PCIE mac:" + strMac);

			if (strMac.length() != 17) {
				m_ip.setTextColor(Color.RED);
				m_ip.setText("ERR");
			} else {
				m_ip.setTextColor(Color.RED);
				m_ip.setText(strMac+" ");
			}
		} else {
			strMac = Tools.readFile(Tools.Key_OTP_Mac);
			length = strMac.length();
			if (length != 12) {
				m_macvalue.setTextColor(Color.RED);
				m_macvalue.setText("ERR");
			} else {
				String strTmpMac = "";
				for(int i = 0; i < length; i += 2) {
					strTmpMac += strMac.substring(i, (i + 2) < length ? (i + 2) :  length );
					if( (i + 2) < length) strTmpMac += ':';
				}
				m_macvalue.setTextColor(Color.RED);
				m_macvalue.setText(strTmpMac+" ");
			}
		}
		String strSn = Tools.readFile(Tools.Key_OTP_Sn);
		length = strSn.length();
		Log.d(TAG,"SN length= "+length);
		if (length != getResources().getInteger(R.integer.config_sn_length)) {
			m_snvalue.setTextColor(Color.RED);
			m_snvalue.setText("ERR");
		} else {
			m_snvalue.setTextColor(Color.RED);
			m_snvalue.setText(strSn+" ");
		}

        m_maccheck.requestFocus();
        int rec = 2;
        rec = Settings.System.getInt(mContext.getContentResolver(), "Khadas_mipi_camera_test", 2);
        if (rec == 1) {
            m_Button_Mipi_Camera.setTextColor(Color.GREEN);
        } else if (rec == 0) {
            m_Button_Mipi_Camera.setTextColor(Color.RED);
        }

        rec = Settings.System.getInt(mContext.getContentResolver(), "Khadas_speaker_mic_test", 2);
        if (rec == 1) {
            m_Button_MIC.setTextColor(Color.GREEN);
        } else if (rec == 0) {
            m_Button_MIC.setTextColor(Color.RED);
        }

        rec = Settings.System.getInt(mContext.getContentResolver(), "Khadas_irkey_test", 2);
        if (rec == 1) {
            m_Button_IRKey.setTextColor(Color.GREEN);
        } else if (rec == 0) {
            m_Button_IRKey.setTextColor(Color.RED);
        }
        //set red led breath
        //Tools.writeFile("/sys/class/mcu/redled","2");
		/*try {
			Tools.execCommand(new String[]{"sh", "-c", "echo 2 > /sys/class/mcu/redled"});
		} catch (IOException e) {
			e.printStackTrace();
		}*/

        //set fan level 3
        try {
            Tools.execCommand(new String[]{"sh", "-c", "echo 1 > /sys/class/fan/enable"});
            Tools.execCommand(new String[]{"sh", "-c", "echo 0 > /sys/class/fan/mode"});
            Tools.execCommand(new String[]{"sh", "-c", "echo 3 > /sys/class/fan/level"});
        } catch (IOException e) {
            e.printStackTrace();
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
            //bIsKeyDown = true;
            mHandler.sendEmptyMessageDelayed(MSG_TIME, 1 * 1000);
        }
    };

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        //退出产测apk恢复系统音量大小
        if (mAudioManager != null)
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
		CloseComPort(ComA);
		CloseComPort(ComB);
        unregisterBTReceiver();
        super.onDestroy();
    }

    private void unregisterBTReceiver() {
		if (bt_test) {
			Log.d(TAG, "[unregisterBTReceiver]BTReceiver");
			if (mBTDeviceReceiver != null) {
				unregisterReceiver(mBTDeviceReceiver);
				mBTDeviceReceiver = null;
			}
		}
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeMessages(MSG_NETLED_TEST_Start);
        mHandler.removeMessages(MSG_POWERLED_TEST_Start);
        mHandler.removeMessages(MSG_PLAY_VIDEO);
        unregisterReceiver(mFactoryReceiver);
        unregisterReceiver(mountReceiver);
    }


    public void PowerLed_Test(View view) {
        Log.d(TAG, "PowerLed_Test()");
        m_Button_PowerLed.setTag(0);
        mHandler.removeMessages(MSG_POWERLED_TEST_Start);
        mHandler.sendEmptyMessage(MSG_POWERLED_TEST_Start);
    }

    public void rst_mcu(View view) {
		String strSn = "";

		if (MainActivity.test_board.equals("ACC-A9A10")) {
			String rec = Tools.exec("ifconfig | grep r8152");
			rec = WriteMacActivity.extractMacAddress(rec);
			Log.d(TAG, "hlm USB mac:" + rec);
			strSn = rec.replaceAll(":", "");
		} else {
			Log.d(TAG, "rst_mcu()");
			Tools.writeFile("/sys/class/mcu/rst", "0");
			strSn =  Tools.readFile(Tools.Key_OTP_Sn);
		}
		Log.e(TAG, "strSn : " + strSn);
		String path = MainActivity.udisk_backup + "/Pictures/";
		String cmd_val = "ls " + path;
		Log.e(TAG, "cmd_val : " + cmd_val);
		if(Tools.exec(cmd_val).contains("No such file or directory")){
			cmd_val = "mkdir -p" + path;
			Tools.exec(cmd_val);
		}
		cmd_val = "screencap -p " + path + strSn + ".png";
		Log.e(TAG, "screencap cmd_val : " + cmd_val);
		if(Tools.exec(cmd_val).contains("")){
			m_Button_reset_MCU.setTextColor(Color.GREEN);
		}
    }

    public void Write_mac_usid(View view) {
        Log.d(TAG, "Write_mac_usid()");
        m_Button_write_mac_usid.setTag(0);
        Intent intent = new Intent(this, WriteMacActivity.class);
        startActivity(intent);
    }

    public void IRKeyTest(View view) {
        Log.d(TAG, "IRKeyTest()");
        Intent intent = new Intent(this, IRKeyTestActivity.class);
        startActivity(intent);
    }

    public void speaker_MIC(View view) {
        Log.d(TAG, "speaker_MIC()");
        Intent intent = new Intent(this, PhoneMicTestActivity.class);
        startActivity(intent);
    }

    public void Mipi_Camera(View view) {
        Log.d(TAG, "Mipi_Camera()");
        Intent intent = new Intent(this, MipiCameraTestActivity.class);
        startActivity(intent);
    }

    public void EnableWol(View view) {
        Tools.writeFile("/sys/class/wol/test", "1");
        Tools.writeFile("/sys/class/wol/enable", "1");
    }


    public void KeyTest(View view) {
        Log.e(TAG, "KeyTest()");
    }

    private void test_BT() {
		if (bt_test) {
			try {
				Thread.sleep(1000);
			} catch (Exception localException1) {
			}

			BTAdmin localBTAdmin = new BTAdmin();
			registerBTReceiver();
			localBTAdmin.OpenBT();
			if (!localBTAdmin.ScanBT()) {
				mHandler.sendEmptyMessage(MSG_BT_TEST_ERROR);
			}
		}
    }

    private void test_i2c() {
        if (i2c_test){
			try {			
				String str1 = "No such device or address";
				String rec = Tools.exec("i2cget -y 2 0x41 0");//i2c-2->0x41
				Log.d(TAG, "hlm i2c2-41:" + rec);
				if(!rec.contains(str1)){
					i2c_flag = i2c_flag|1;
				}

				rec = Tools.exec("i2cget -y 4 0x41 0");
				Log.d(TAG, "hlm i2c4-41:" + rec);
				if(!rec.contains(str1)){
					//rec = Tools.exec("i2cget -y 4 0x44 0");
					i2c_flag = i2c_flag|2;
				}

				Log.d(TAG, "hlm i2c_flag:" + i2c_flag);
			   if(3 == i2c_flag)
				  mHandler.sendEmptyMessage(MSG_I2C_TEST_OK);
			   else
				  mHandler.sendEmptyMessage(MSG_I2C_TEST_ERROR);				
	        } catch (Exception localException1) {
				Log.d(TAG, "hlm i2c read error");
			}
		}
    }

    private void test_gpio() {
		Log.d(TAG, "hlm test_gpio");
		Tools.exec("echo 119 > /sys/class/gpio/export");//EXT_GPIO3_C7_u
		Tools.exec("echo out > /sys/class/gpio/gpio119/direction");
		Tools.exec("echo 1 >  /sys/class/gpio/gpio119/value");
		Tools.exec("echo 120 > /sys/class/gpio/export");//EXT_GPIO3_D0_u
		Tools.exec("echo out > /sys/class/gpio/gpio120/direction");
		Tools.exec("echo 1 >  /sys/class/gpio/gpio120/value");
		Tools.exec("echo 147 > /sys/class/gpio/export");//EXT_PWM4_M1 & GPIO4_C3_d
		Tools.exec("echo out > /sys/class/gpio/gpio147/direction");
		Tools.exec("echo 1 >  /sys/class/gpio/gpio147/value");
    }

    private void test_ext_SPI() {
        if (ext_spi_test){
			try {
				String str1 = "Driver mcp251x";
				String rec = Tools.exec("ifconfig -a | grep can1");
				Log.d(TAG, "hlm spi-3:" + rec);
				if(rec.contains(str1)){
					spi_flag = spi_flag|2;
				}
	        } catch (Exception localException1) {
				Log.d(TAG, "hlm spi read error");
			}
		}
    }

    private void test_FAN() {
		try {
			Tools.exec("echo 1 > /sys/class/fan/enable");
			Tools.exec("echo 0 > /sys/class/fan/mode");
			Tools.exec("echo 5 > /sys/class/fan/level");
        } catch (Exception localException1) {
		}
    }

    private void test_CAN() {
        if (can_test){
			try {			
				String str1 = "11 22 33 44 AA BB CC DD";
				Tools.exec("ifconfig can0 down");
				Tools.exec("ip link set can0 type can bitrate 25000");
				Tools.exec("ifconfig can0 up");
				Tools.exec("ifconfig can1 down");
				Tools.exec("ip link set can1 type can bitrate 25000");
				Tools.exec("ifconfig can1 up");
				Tools.exec("rm /data/can0.log");
				Tools.exec("candump can0 >> /data/can0.log &");
				Tools.exec("cansend can1 01a#11223344AABBCCDD");
				String rec = Tools.exec("cat /data/can0.log");

				if(rec.contains(str1)){
					Tools.exec("rm /data/can1.log");
					Tools.exec("candump can1 >> /data/can1.log &");
					Tools.exec("cansend can0 02a#11223344AABBCCDD");
					rec = Tools.exec("cat /data/can1.log");
					if(rec.contains(str1)){
						mHandler.sendEmptyMessage(MSG_CAN_TEST_OK);
					}
					else{
						mHandler.sendEmptyMessage(MSG_CAN_TEST_ERROR);
					}
				}
				else{
					mHandler.sendEmptyMessage(MSG_CAN_TEST_ERROR);
				}
	        } catch (Exception localException1) {
				Log.d(TAG, "hlm i2c read error");
			}
		}
    }

    private void test_SSD() {
		if(ssd_test){
	        for (int i=0; i< ssdStatus.length; i++) {
	            ssdStatus[i] = getResources().getString(R.string.Test_Fail);
	        }
	        String rec = Tools.exec("ls /dev/block/nvme*");
			Log.d(TAG, "hlm test_SSD:" + rec);
	        if (rec.contains("nvme0n1p1")) {
				ssdStatus[0] = getResources().getString(R.string.Test_Ok);
			}
			if (!MainActivity.test_board.equals("ACC-A9A10")) {
				if (rec.contains("nvme1n1p1")) {
					ssdStatus[1] = getResources().getString(R.string.Test_Ok);
				}
				if (rec.contains("nvme2n1p1")) {
					ssdStatus[2] = getResources().getString(R.string.Test_Ok);
				}

				if(ssdStatus[0].equals(getResources().getString(R.string.Test_Ok))
						&& ssdStatus[1].equals(getResources().getString(R.string.Test_Ok))
						&& ssdStatus[2].equals(getResources().getString(R.string.Test_Ok))){
					mHandler.sendEmptyMessage(MSG_SSD_TEST_OK);
				}else{
					mHandler.sendEmptyMessage(MSG_SSD_TEST_ERROR);
				}
			} else {
				if(ssdStatus[0].equals(getResources().getString(R.string.Test_Ok))){
					mHandler.sendEmptyMessage(MSG_SSD_TEST_OK);
				}else{
					mHandler.sendEmptyMessage(MSG_SSD_TEST_ERROR);
				}
			}
		}
    }

    private void test_SATA() {
		if(sata_test){
	        String rec = Tools.exec("ls /dev/block/sd*");
			Log.d(TAG, "hlm test_SATA:" + rec);
	        if (rec.contains("sda")) {
				mHandler.sendEmptyMessage(MSG_SATA_TEST_OK);
			}else{
	            mHandler.sendEmptyMessage(MSG_SATA_TEST_ERROR);
	        }
		}
    }

    private void test_SPI() {
        String rec = Tools.exec("cat /proc/cmdline");
		//Log.d(TAG, "hlm cmdline:" + rec);
        if (rec.contains("spi_state=1")) {
			spi_flag = spi_flag|1;
			if(ext_spi_test){
			    if(3 == spi_flag)
					mHandler.sendEmptyMessage(MSG_SPI_TEST_OK);
				else
					mHandler.sendEmptyMessage(MSG_SPI_TEST_ERROR);
			} else {
            	mHandler.sendEmptyMessage(MSG_SPI_TEST_OK);
			}
		}
        else
            mHandler.sendEmptyMessage(MSG_SPI_TEST_ERROR);
    }

    private void test_FUSB302() {
		try {
			String str1 = "0x91";
			String rec = Tools.exec("i2cget -fy 1 0x22 1");
			Log.d(TAG, "hlm FUSB302:" + rec);
			if(rec.contains(str1))
				mHandler.sendEmptyMessage(MSG_FUSB302_TEST_OK);
			else
				mHandler.sendEmptyMessage(MSG_FUSB302_TEST_ERROR);
        } catch (Exception localException1) {
        }
    }

  private void test_MCU() {

        File file = new File("/sys/class/wol/eth0_enable");
        if (file.exists())
            mHandler.sendEmptyMessage(MSG_MCU_TEST_OK);
        else
            mHandler.sendEmptyMessage(MSG_MCU_TEST_ERROR);
  }


    private void test_HDMI() {

        String node = "/sys/devices/platform/display-subsystem/drm/card0/card0-HDMI-A-1/edid";
        File file = new File(node);
        if (file.exists()) {
            String value = Tools.readFile(node);
            Log.d(TAG, "===hdmi i2c====" + value + "======");
            if (value.equals(""))
                mHandler.sendEmptyMessage(MSG_HDMI_TEST_ERROR);
            else
                mHandler.sendEmptyMessage(MSG_HDMI_TEST_OK);
        } else
            mHandler.sendEmptyMessage(MSG_HDMI_TEST_ERROR);
    }

    private void test_GSENSOR() {
		String pathname = "/sys/class/mcu/icm43600";
        try {
			 FileReader reader = new FileReader(pathname);
			 BufferedReader br = new BufferedReader(reader);
			String line;
			while ((line = br.readLine()) != null) {
				int usb = Integer.parseInt(line);
				Log.d(TAG, "hlm usb: " + usb);
				if(1 == usb)
					mHandler.sendEmptyMessage(MSG_GSENSOR_TEST_OK);

				else
					mHandler.sendEmptyMessage(MSG_GSENSOR_TEST_ERROR);
            }
        } catch (Exception localException1) {
        }
    }


    private void test_AGEING() {
        String pathname = "/sys/class/mcu/ageing_test";
        try (FileReader reader = new FileReader(pathname);
             BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                int id = Integer.parseInt(line);
                Log.d(TAG, "hlm AGEING: " + id);
                if (1 == id) {
                    ageing_test_ok_flag = true;
					if (!ageing_test) {
						m_TextView_AGEING.setVisibility(View.VISIBLE);
					}
                    mHandler.sendEmptyMessage(MSG_AGEING_TEST_OK);
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ageing_test_ok_flag = false;
        mHandler.sendEmptyMessage(MSG_AGEING_TEST_ERROR);
    }

    private boolean hasEthGigabit(String value) {

		File file = new File(value);
		if (file.exists()) {
			String rate = Tools.readFile(value);
			if (rate.equals("1000"))
				return true;
			else
				return false;
		} else
			return false;
    }

    private void test_Gigabit() {
		if (MainActivity.test_board.equals("ACC-A9A10")) {
			if (hasEthGigabit("/sys/class/net/eth1/speed"))//usb_lan
				GIGABIT_flag = GIGABIT_flag|0x01;
			else
				GIGABIT_flag = GIGABIT_flag&0x02;

			if (hasEthGigabit("/sys/class/net/eth2/speed"))//pcie_lan
				GIGABIT_flag = GIGABIT_flag|0x02;
			else
				GIGABIT_flag = GIGABIT_flag&0x01;

			if (3 == GIGABIT_flag) {
				mHandler.sendEmptyMessage(MSG_GIGABIT_TEST_OK);
			} else {
				mHandler.sendEmptyMessage(MSG_GIGABIT_TEST_ERROR);
			}
		}
		else {
			if (hasEthGigabit("/sys/class/net/eth0/speed"))
				mHandler.sendEmptyMessage(MSG_GIGABIT_TEST_OK);
			else
				mHandler.sendEmptyMessage(MSG_GIGABIT_TEST_ERROR);
		}
    }

    private void test_RTC() {
        String content = "";
        String node = "/sys/class/rtc/rtc0/time";
        File file = new File(node);
        if (file.exists()) {
            String val = Tools.readFile(node);
            try {
                FileInputStream instream = new FileInputStream(node);
                if (instream != null) {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    //Log.d(TAG, "buffreader = " + buffreader.toString());
                    String line;
                    while ((line = buffreader.readLine()) != null) {
                        content = content + line;
                    }
                    instream.close();
                }
            } catch (FileNotFoundException e) {
                Log.e(TAG, "test_RTC The File doesn\'t not exist.");
                mHandler.sendEmptyMessage(MSG_RTC_TEST_ERROR);
            } catch (IOException e) {
                Log.e(TAG, " readFile error!");
                Log.e(TAG, e.getMessage());
                mHandler.sendEmptyMessage(MSG_RTC_TEST_ERROR);
                return;
            }
            mHandler.sendEmptyMessage(MSG_RTC_TEST_OK);
        } else
            mHandler.sendEmptyMessage(MSG_RTC_TEST_ERROR);
    }

    private void test_cpu_ageing() {

        String shpath = copyAssetGetFilePath("test_cpu_ageing.sh");

        Log.d(TAG, "===shpath====" + shpath);

        File file = new File(shpath);
        if (file.exists()) {
            try {
                Tools.execCommand(new String[]{"sh", "-c", "chmod 777 " + shpath});
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < ageing_cpu_max; i++) {
            try {
                Process ps = Runtime.getRuntime().exec(shpath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String copyAssetGetFilePath(String fileName) {
        try {
            File cacheDir = mContext.getCacheDir();
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            File outFile = new File(cacheDir, fileName);
            if (!outFile.exists()) {
                boolean res = outFile.createNewFile();
                if (!res) {
                    return null;
                }
            } else {
                if (outFile.length() > 10) {
                    return outFile.getPath();
                }
            }
            InputStream is = mContext.getAssets().open(fileName);
            FileOutputStream fos = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int byteCount;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
            is.close();
            fos.close();
            return outFile.getPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void test_Wifi() {
        boolean bWifiScaned = false;
        try {
            Thread.sleep(2000);
        } catch (Exception localException1) {
        }
        configSSID = getResources().getString(R.string.config_ap_ssid);
        WifiAdmin localWifiAdmin = new WifiAdmin(this);
        localWifiAdmin.openWifi();
        localWifiAdmin.startScan();
        wifiList = new ArrayList<ScanResult>();
        wifiList = localWifiAdmin.getWifiList();
        Log.d(TAG, "wifi size: " + wifiList.size());
        if (wifiList != null) {
            for (ScanResult result : wifiList) {
                if (result.SSID.equals(configSSID)) {
                    wifiLevel = WifiManager.calculateSignalLevel(result.level, 100);
                    Log.d(TAG, "wifiLevel: " + wifiLevel);
                    if (wifiLevel >= configLevel) {
                        bWifiScaned = true;
                    }
                }
            }
        }

        if (bWifiScaned) {
            mHandler.sendEmptyMessage(MSG_WIFI_TEST_OK);
        } else {
            mHandler.sendEmptyMessage(MSG_WIFI_TEST_ERROR);
        }
    }


    /**
     * 判断USB与F
     */
    private void test_volumes() {

        if (Tools.isAndroid5_1_1()) {
            test_android5_1();
        } else {
            test_android6_0();
        }
    }

    /**
     * android6.0
     */
    private void test_android6_0() {
        Log.d(TAG, "----- android6.0 -----");
        mHandler.sendEmptyMessage(MSG_android_6_0_TEXT_LAYOUT);
        Boolean[] usbOrSd = Tools.isUsbOrSd(MainActivity.this);

        Log.e("TEST", "usbOrSd:" + usbOrSd[0] + ", " + usbOrSd[1] + ", " + usbOrSd[2]);

        if (usbOrSd[0]) {
            mHandler.sendEmptyMessage(MSG_TF_TEST_XL_OK);
        } else {
            mHandler.sendEmptyMessage(MSG_TF_TEST_XL_ERROR);
        }

        for (int i=0; i< usb2Status.length; i++) {
            usb2Status[i] = getResources().getString(R.string.Test_Fail);
        }

        for (int i=0; i< usb3Status.length; i++) {
            usb3Status[i] = getResources().getString(R.string.Test_Fail);
        }

		String val = Tools.readFile("/sys/kernel/debug/usb/devices");
        if (val.indexOf("(O)") == -1) {
            Log.e(TAG, "=========USB2.0 and USB3.0 is bad");
            mHandler.sendEmptyMessage(MSG_USB1_TEST_XL_ERROR);
            mHandler.sendEmptyMessage(MSG_USB2_TEST_XL_ERROR);
            return;
        }

        int length;
        String[] list = val.split("T:|B:|D:|P:|S:|C:|I:|E:");
        int num = -1;
        int count = getSubCount(val, "Bus=");
        String[] tmp = new String[count];
        for (int z = 0; z < list.length; z++) {

            if (list[z].indexOf("Bus=") != -1) {
                num++;
            }
            if (num == count)
                break;
            if (num == -1)
                continue;
            tmp[num] = tmp[num] + list[z];
        }
		usb3_flag = 0;
        for (int i = 0; i < tmp.length; i++) {
			if ((tmp[i].indexOf("(O)") != -1) && (tmp[i].indexOf("Bus=02") != -1) && (tmp[i].indexOf("Port=00") != -1)) {
				Log.d("TAG", "USB2.0-0 is OK");
                usb2Status[0] = getResources().getString(R.string.Test_Ok);
			}else if ((tmp[i].indexOf("(O)") != -1) && (tmp[i].indexOf("Bus=02") != -1) && (tmp[i].indexOf("Port=01") != -1)) {
                Log.d("TAG", "USB2.0-1 is OK");
                usb2Status[1] = getResources().getString(R.string.Test_Ok);
			}else if ((tmp[i].indexOf("(O)") != -1) && (tmp[i].indexOf("Bus=07") != -1)  && (tmp[i].indexOf("Port=00") != -1)) {
                Log.d("TAG", "USB2.0-2 is OK");
                usb2Status[2] = getResources().getString(R.string.Test_Ok);
            }else if ((tmp[i].indexOf("(O)") != -1) && (tmp[i].indexOf("Bus=06") != -1) && (tmp[i].indexOf("Port=00") != -1)) {
				Log.d("TAG", "USB3.0-0 is OK");
                usb3Status[0] = getResources().getString(R.string.Test_Ok);
			}else if ((tmp[i].indexOf("(O)") != -1) && (tmp[i].indexOf("Bus=06") != -1) && (tmp[i].indexOf("Port=01") != -1)) {
                Log.d("TAG", "USB3.0-1 is OK");
                usb3Status[1] = getResources().getString(R.string.Test_Ok);
            }else if ((tmp[i].indexOf("(O)") != -1) && (tmp[i].indexOf("Bus=06") != -1) && (tmp[i].indexOf("Port=02") != -1)) {
                Log.d("TAG", "USB3.0-2 is OK");
                usb3Status[2] = getResources().getString(R.string.Test_Ok);
            }else if ((tmp[i].indexOf("(O)") != -1) && (tmp[i].indexOf("Bus=06") != -1) && (tmp[i].indexOf("Port=03") != -1)) {
                Log.d("TAG", "USB3.0-3 is OK");
                usb3Status[3] = getResources().getString(R.string.Test_Ok);
            }

			if ((tmp[i].indexOf("(O)") != -1) && (tmp[i].indexOf("Bus=06") != -1))
				usb3_flag++;
		}
		if(4 == usb3_flag){
	        for (int i=0; i< usb3Status.length; i++) {
	            usb3Status[i] = getResources().getString(R.string.Test_Ok);
	        }
		}
		if(usb2Status[0].equals(getResources().getString(R.string.Test_Ok))
				&& usb2Status[1].equals(getResources().getString(R.string.Test_Ok))
                && usb2Status[2].equals(getResources().getString(R.string.Test_Ok))) {
            mHandler.sendEmptyMessage(MSG_USB1_TEST_XL_OK);
        }else{
            mHandler.sendEmptyMessage(MSG_USB1_TEST_XL_ERROR);
        }

		if(usb3Status[0].equals(getResources().getString(R.string.Test_Ok))
                && usb3Status[1].equals(getResources().getString(R.string.Test_Ok))
				&& usb3Status[2].equals(getResources().getString(R.string.Test_Ok))
                && usb3Status[3].equals(getResources().getString(R.string.Test_Ok))) {
            mHandler.sendEmptyMessage(MSG_USB2_TEST_XL_OK);
        }else{
            mHandler.sendEmptyMessage(MSG_USB2_TEST_XL_ERROR);
        }
    }


    private int getSubCount(String str, String key) {
        int count = 0;
        int index = 0;
        while ((index = str.indexOf(key, index)) != -1) {
            index = index + key.length();
            count++;
        }
        return count;
    }

    private void test_android5_1() {
        Log.d(TAG, "----- android5.1 -----");
        List<String> volumes = getVolumes();
        boolean bSdcard = false;
        boolean bSda = false;
        boolean bSdb = false;
        for (String volume : volumes) {
            if (volume.contains(TFCARD_PATH)) {
                bSdcard = true;
            } else if (volume.contains(USB1_PATH)) {
                Log.d(TAG, USB1_PATH + " usb1 " + volume.toString());
                usb_path = volume;
                bSda = true;
            } else if (volume.contains(USB2_PATH)) {
                Log.d(TAG, USB2_PATH + " usb2 " + volume.toString());
                bSdb = true;
            }
        }
        if (bSdcard) {
            mHandler.sendEmptyMessage(MSG_TF_TEST_OK);
        } else {
            mHandler.sendEmptyMessage(MSG_TF_TEST_ERROR);
        }

        {
            mHandler.sendEmptyMessage(MSG_USB1_TEST_ERROR);
            mHandler.sendEmptyMessage(MSG_USB2_TEST_ERROR);
        }
        if (bSda) {
            if (isUsb1()) {
                mHandler.sendEmptyMessage(MSG_USB1_TEST_OK);
            } else {
                mHandler.sendEmptyMessage(MSG_USB2_TEST_OK);
            }
        }

        if (bSdb) {
            //判断两个U盘是否都接入
            if (bSda) {
                mHandler.sendEmptyMessage(MSG_USB1_TEST_OK);
                mHandler.sendEmptyMessage(MSG_USB2_TEST_OK);
            }//只有一个U盘情况下 判断在哪个口
            else {
                if (isUsb1()) {
                    mHandler.sendEmptyMessage(MSG_USB1_TEST_OK);
                } else {
                    mHandler.sendEmptyMessage(MSG_USB2_TEST_OK);
                }
            }
        }
    }


    private List<String> getVolumes() {
        List<String> volumes = new ArrayList<String>();
        try {
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("df").getInputStream()));
            String readline;
            while ((readline = bufferReader.readLine()) != null) {
                Log.d(TAG, "df State:" + readline);
                if (readline.contains(USB_PATH) || readline.contains(TFCARD_PATH)) {
                    String[] result = readline.split(" ");
                    if (result.length > 0) {
                        volumes.add(result[0]);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            return volumes;
        } catch (IOException e) {
            return volumes;
        }
        return volumes;
    }

    //仅仅在一个U盘接入情况下判断接入那个USB口
    private boolean isUsb1() {
        try {
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("lsusb").getInputStream()));
            String readline = bufferReader.readLine();
            //Bus 001 Device 008: ID 05e3:0723
            String USBBus = readline.substring(readline.indexOf("00") + 2, readline.lastIndexOf("Device")).trim();
            Log.d(TAG, "lsusb :  " + USBBus);
            if (USBBus.equals("1")) {
                Log.d(TAG, "lsusb :  is USB1 mount");
                return true;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    private boolean hasEthIpAddress(String value) {

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                if (intf.getName().toLowerCase().equals(value)) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipaddress = inetAddress.getHostAddress().toString();
                            if (!ipaddress.contains("::")) {
                                return true;
                            }
                        }
                    }

                } else {
                    continue;
                }
            }

        } catch (SocketException ex) {
            Log.e(TAG, ex.toString());
        }
        return false;

    }

    private boolean r8152_hasEthIpAddress() {//usb3.0

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                if (intf.getName().toLowerCase().equals("eth1")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipaddress = inetAddress.getHostAddress().toString();
                            if (!ipaddress.contains("::")) {
                                return true;
                            }
                        }
                    }

                } else {
                    continue;
                }
            }

        } catch (SocketException ex) {
            Log.e(TAG, ex.toString());
        }
        return false;

    }

    private boolean r8169_hasEthIpAddress() {//pcie

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                if (intf.getName().toLowerCase().equals("eth2")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipaddress = inetAddress.getHostAddress().toString();
                            if (!ipaddress.contains("::")) {
                                return true;
                            }
                        }
                    }

                } else {
                    continue;
                }
            }

        } catch (SocketException ex) {
            Log.e(TAG, ex.toString());
        }
        return false;

    }

    private void test_ETH() {
		if (MainActivity.test_board.equals("ACC-A9A10")) {
			if (hasEthIpAddress("eth1"))//usb_lan
				LAN_flag = LAN_flag|0x01;
			else
				LAN_flag = LAN_flag&0x02;

			if (hasEthIpAddress("eth2"))//pcie_lan
				LAN_flag = LAN_flag|0x02;
			else
				LAN_flag = LAN_flag&0x01;

			if (3 == LAN_flag) {
				mHandler.sendEmptyMessage(MSG_LAN_TEST_OK);
			} else {
				mHandler.sendEmptyMessage(MSG_LAN_TEST_ERROR);
			}
		}
		else {
			if (hasEthIpAddress("eth0")) {
				mHandler.sendEmptyMessage(MSG_LAN_TEST_OK);
			} else {
				mHandler.sendEmptyMessage(MSG_LAN_TEST_ERROR);
			}
		}
    }

    class FactoryHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TF_TEST_ERROR: {
                    String strTxt = getResources().getString(R.string.TF_Test) + "    " + getResources().getString(R.string.Test_Fail);
                    m_TextView_TF.setText(strTxt);
                    m_TextView_TF.setTextColor(0xFFFF5555);
                }
                break;

                case MSG_TF_TEST_OK: {
                    String strTxt = getResources().getString(R.string.TF_Test) + "    " + getResources().getString(R.string.Test_Ok);
                    m_TextView_TF.setText(strTxt);
                    m_TextView_TF.setTextColor(0xFF55FF55);
                }
                break;

                case MSG_USB1_TEST_ERROR: {
                    String strTxt = getResources().getString(R.string.USB1_Test) + "    " + getResources().getString(R.string.Test_Fail);
                    m_TextView_USB1.setText(strTxt);
                    m_TextView_USB1.setTextColor(0xFFFF5555);
                }
                break;

                case MSG_USB1_TEST_OK: {
                    String strTxt = getResources().getString(R.string.USB1_Test) + "    " + getResources().getString(R.string.Test_Ok);
                    m_TextView_USB1.setText(strTxt);
                    m_TextView_USB1.setTextColor(0xFF55FF55);
                }
                break;

                case MSG_USB2_TEST_ERROR: {
                    String strTxt = getResources().getString(R.string.USB2_Test) + "    " + getResources().getString(R.string.Test_Fail);
                    m_TextView_USB2.setText(strTxt);
                    m_TextView_USB2.setTextColor(0xFFFF5555);
                }
                break;

                case MSG_USB2_TEST_OK: {
                    String strTxt = getResources().getString(R.string.USB2_Test) + "    " + getResources().getString(R.string.Test_Ok);
                    m_TextView_USB2.setText(strTxt);
                    m_TextView_USB2.setTextColor(0xFF55FF55);
                }
                break;

                case MSG_LAN_TEST_OK: {
                    String strTxt = getResources().getString(R.string.Lan_Test);
					if (MainActivity.test_board.equals("ACC-A9A10")) {
						strTxt = strTxt
                        + "  " + getResources().getString(R.string.Lan_Test_1_Ok)
                        + "  " + getResources().getString(R.string.Lan_Test_2_Ok);
					} else {
						strTxt = strTxt + "    " + getResources().getString(R.string.Test_Ok);
					}

                    m_TextView_Lan.setText(strTxt);
                    m_TextView_Lan.setTextColor(0xFF55FF55);
                    Log.d(TAG, "MSG_LAN_TEST_OK");
                }
                break;

                case MSG_LAN_TEST_ERROR: {
                    String strTxt = getResources().getString(R.string.Lan_Test);
					if (MainActivity.test_board.equals("ACC-A9A10")) {
						if(LAN_flag == 0x1)
							strTxt = strTxt + "  " + getResources().getString(R.string.Lan_Test_1_Ok);
						else
							strTxt = strTxt + "  " + getResources().getString(R.string.Lan_Test_1_Fail);

						if(LAN_flag == 0x2)
							strTxt = strTxt + "  " + getResources().getString(R.string.Lan_Test_2_Ok);
						else
							strTxt = strTxt + "  " + getResources().getString(R.string.Lan_Test_2_Fail);
					} else {
						strTxt = strTxt + "  " + getResources().getString(R.string.Test_Fail);
					}

                    m_TextView_Lan.setText(strTxt);
                    m_TextView_Lan.setTextColor(0xFFFF5555);
                    Log.d(TAG, "MSG_LAN_TEST_ERROR");
                }
                break;

                case MSG_GIGABIT_TEST_OK: {
                    String strTxt = getResources().getString(R.string.Gigabit_Test);
					if (MainActivity.test_board.equals("ACC-A9A10")) {
						strTxt = strTxt
                        + "  " + getResources().getString(R.string.Gigabit_Test_1_Ok)
                        + "  " + getResources().getString(R.string.Gigabit_Test_2_Ok);
					} else {
						strTxt = strTxt + "    " + getResources().getString(R.string.Test_Ok);
					}

                    m_TextView_Gigabit.setText(strTxt);
                    m_TextView_Gigabit.setTextColor(0xFF55FF55);
                    Log.d(TAG, "MSG_GIGABIT_TEST_OK");
                }
                break;

                case MSG_GIGABIT_TEST_ERROR: {
                    String strTxt = getResources().getString(R.string.Gigabit_Test);
					if (MainActivity.test_board.equals("ACC-A9A10")) {
						if(GIGABIT_flag == 0x1)
							strTxt = strTxt + "  " + getResources().getString(R.string.Gigabit_Test_1_Ok);
						else
							strTxt = strTxt + "  " + getResources().getString(R.string.Gigabit_Test_1_Fail);

						if(GIGABIT_flag == 0x2)
							strTxt = strTxt + "  " + getResources().getString(R.string.Gigabit_Test_2_Ok);
						else
							strTxt = strTxt + "  " + getResources().getString(R.string.Gigabit_Test_2_Fail);
					} else {
						strTxt = strTxt + "  " + getResources().getString(R.string.Test_Fail);
					}

                    m_TextView_Gigabit.setText(strTxt);
                    m_TextView_Gigabit.setTextColor(0xFFFF5555);
                    Log.d(TAG, "MSG_GIGABIT_TEST_ERROR");
                }
                break;

                case MSG_HDMI_TEST_OK: {
                    String strTxt = getResources().getString(R.string.HDMI_Test) + "    " + getResources().getString(R.string.Test_Ok);

                    m_TextView_HDMI.setText(strTxt);
                    m_TextView_HDMI.setTextColor(0xFF55FF55);
                    Log.d(TAG, "MSG_HDMI_TEST_OK");
                }
                break;

                case MSG_HDMI_TEST_ERROR: {
                    String strTxt = getResources().getString(R.string.HDMI_Test) + "    " + getResources().getString(R.string.Test_Fail);

                    m_TextView_HDMI.setText(strTxt);
                    m_TextView_HDMI.setTextColor(0xFFFF5555);
                    Log.d(TAG, "MSG_HDMI_TEST_ERROR");
                }
                break;

                case MSG_UART_TEST_OK: {
					String strTxt = getResources().getString(R.string.Uart_Test);
					if (MainActivity.test_board.equals("ACC-A9A10")) {
						strTxt = strTxt
							+ "  " + getResources().getString(R.string.Test_Ok);
					} else {
						strTxt = strTxt
							+ "  " + getResources().getString(R.string.Uart_Test_1_Ok)
							+ "  " + getResources().getString(R.string.Uart_Test_2_Ok)
							+ "  " + getResources().getString(R.string.Uart_Test_3_Ok)
							+ "  " + getResources().getString(R.string.Uart_Test_4_Ok);
					}
                    m_TextView_UART.setText(strTxt);
                    m_TextView_UART.setTextColor(0xFF55FF55);
					Log.d(TAG, "MSG_UART_TEST_OK");
                }
                break;

                case MSG_UART_TEST_ERROR: {
                    String strTxt = getResources().getString(R.string.Uart_Test);
					if (MainActivity.test_board.equals("ACC-A9A10")) {
						strTxt = strTxt
							+ "  " + getResources().getString(R.string.Test_Fail);
					} else {
						if(uartStatus[0].equals(getResources().getString(R.string.Test_Ok)))
							strTxt = strTxt + "  " + getResources().getString(R.string.Uart_Test_1_Ok);
						else
							strTxt = strTxt + "  " + getResources().getString(R.string.Uart_Test_1_Fail);

						if(uartStatus[1].equals(getResources().getString(R.string.Test_Ok)))
							strTxt = strTxt + "  " + getResources().getString(R.string.Uart_Test_2_Ok);
						else
							strTxt = strTxt + "  " + getResources().getString(R.string.Uart_Test_2_Fail);

						if(uartStatus[2].equals(getResources().getString(R.string.Test_Ok)))

							strTxt = strTxt + "  " + getResources().getString(R.string.Uart_Test_3_Ok);
						else
							strTxt = strTxt + "  " + getResources().getString(R.string.Uart_Test_3_Fail);

						if(uartStatus[3].equals(getResources().getString(R.string.Test_Ok)))
							strTxt = strTxt + "  " + getResources().getString(R.string.Uart_Test_4_Ok);
						else
							strTxt = strTxt + "  " + getResources().getString(R.string.Uart_Test_4_Fail);
					}
                    m_TextView_UART.setText(strTxt);
                    m_TextView_UART.setTextColor(0xFFFF5555);
                    Log.d(TAG, "MSG_UART_TEST_ERROR");
                }
                break;

                case MSG_I2C_TEST_OK: {
                    String strTxt = getResources().getString(R.string.I2C_Test)
                        + "    " + getResources().getString(R.string.I2C_Test_1_Ok)
                        + "    " + getResources().getString(R.string.I2C_Test_2_Ok);

                    m_TextView_I2C.setText(strTxt);
                    m_TextView_I2C.setTextColor(0xFF55FF55);
                }
                break;

                case MSG_I2C_TEST_ERROR: {
                    String strTxt = getResources().getString(R.string.I2C_Test);
					if(i2c_flag == 0x1)
                        strTxt = strTxt + "    " + getResources().getString(R.string.I2C_Test_1_Ok);
                    else
                        strTxt = strTxt + "    " + getResources().getString(R.string.I2C_Test_1_Fail);

                    if(i2c_flag == 0x2)
                        strTxt = strTxt + "    " + getResources().getString(R.string.I2C_Test_2_Ok);
                    else
                        strTxt = strTxt + "    " + getResources().getString(R.string.I2C_Test_2_Fail);

                    m_TextView_I2C.setText(strTxt);
                    m_TextView_I2C.setTextColor(0xFFFF5555);
                    Log.d(TAG, "MSG_I2C_TEST_ERROR");
                }
                break;

                case MSG_CAN_TEST_OK: {
                    String strTxt = getResources().getString(R.string.CAN_Test) + "    " + getResources().getString(R.string.Test_Ok);

                    m_TextView_CAN.setText(strTxt);
                    m_TextView_CAN.setTextColor(0xFF55FF55);
                    //Log.d(TAG, "MSG_CAN_TEST_OK");
                }
                break;

                case MSG_CAN_TEST_ERROR: {
                    String strTxt = getResources().getString(R.string.CAN_Test) + "    " + getResources().getString(R.string.Test_Fail);

                    m_TextView_CAN.setText(strTxt);
                    m_TextView_CAN.setTextColor(0xFFFF5555);
                    //Log.d(TAG, "MSG_CAN_TEST_ERROR");
                }
                break;

                case MSG_GSENSOR_TEST_OK: {
                    String strTxt = getResources().getString(R.string.GSENSOR_Test) + "    " + getResources().getString(R.string.Test_Ok);

                    m_TextView_GSENSOR.setText(strTxt);
                    m_TextView_GSENSOR.setTextColor(0xFF55FF55);
                    Log.d(TAG, "MSG_GSENSOR_TEST_OK");
                }
                break;

                case MSG_GSENSOR_TEST_ERROR: {
                    String strTxt = getResources().getString(R.string.GSENSOR_Test) + "    " + getResources().getString(R.string.Test_Fail);

                    m_TextView_GSENSOR.setText(strTxt);
                    m_TextView_GSENSOR.setTextColor(0xFFFF5555);
                    Log.d(TAG, "MSG_GSENSOR_TEST_ERROR");
                }
                break;

                case MSG_AGEING_TEST_OK: {
                    String strTxt = getResources().getString(R.string.AGEING_Test) + "    " + getResources().getString(R.string.Test_Ok);

                    m_TextView_AGEING.setText(strTxt);
                    m_TextView_AGEING.setTextColor(0xFF55FF55);
                    Log.d(TAG, "MSG_AGEING_TEST_OK");
                }
                break;

                case MSG_AGEING_TEST_ERROR: {
                    String strTxt = getResources().getString(R.string.AGEING_Test) + "    " + getResources().getString(R.string.Test_In);

                    m_TextView_AGEING.setText(strTxt);
                    m_TextView_AGEING.setTextColor(0xFFFF5555);
                    Log.d(TAG, "MSG_AGEING_TEST_ERROR");
                }
                break;

                case MSG_FUSB302_TEST_OK: {
                    String strTxt = getResources().getString(R.string.FUSB302_Test) + "    " + getResources().getString(R.string.Test_Ok);

                    m_TextView_FUSB302.setText(strTxt);
                    m_TextView_FUSB302.setTextColor(0xFF55FF55);
                    Log.d(TAG, "MSG_FUSB302_TEST_OK");
                }
                break;

                case MSG_FUSB302_TEST_ERROR: {
                    String strTxt = getResources().getString(R.string.FUSB302_Test) + "    " + getResources().getString(R.string.Test_Fail);

                    m_TextView_FUSB302.setText(strTxt);
                    m_TextView_FUSB302.setTextColor(0xFFFF5555);
                    Log.d(TAG, "MSG_FUSB302_TEST_ERROR");
                }
                break;

                case MSG_SPI_TEST_OK: {
					String strTxt = getResources().getString(R.string.SPI_Test);
					if(ext_spi_test){

						strTxt = strTxt + "    " + getResources().getString(R.string.SPI_Test_1_Ok)
							+ "    " + getResources().getString(R.string.SPI_Test_2_Ok);
					}else{
						strTxt = strTxt + "    " + getResources().getString(R.string.Test_Ok);
					}
                    m_TextView_SPI.setText(strTxt);
                    m_TextView_SPI.setTextColor(0xFF55FF55);
                }
                break;

                case MSG_SPI_TEST_ERROR: {
                	String strTxt = getResources().getString(R.string.SPI_Test);
					if(ext_spi_test){
						if(spi_flag == 0x1)
							strTxt = strTxt + "    " + getResources().getString(R.string.SPI_Test_1_Ok);
						else
							strTxt = strTxt + "    " + getResources().getString(R.string.SPI_Test_1_Fail);

						if(spi_flag == 0x2)
							strTxt = strTxt + "    " + getResources().getString(R.string.SPI_Test_2_Ok);
						else
							strTxt = strTxt + "    " + getResources().getString(R.string.SPI_Test_2_Fail);
					}else{
						strTxt = strTxt + "    " + getResources().getString(R.string.Test_Fail);
					}

                    m_TextView_SPI.setText(strTxt);
                    m_TextView_SPI.setTextColor(0xFFFF5555);
                    Log.d(TAG, "MSG_SPI_TEST_ERROR");
                }
                break;

                case MSG_KEY_TEST_OK: {
					String strTxt = getResources().getString(R.string.KEY_Test);
					if(key_test){

						strTxt = strTxt + "    " + getResources().getString(R.string.KEY_Test_1_Ok)
							+ "    " + getResources().getString(R.string.KEY_Test_2_Ok);
					}else{
						strTxt = strTxt + "    " + getResources().getString(R.string.Test_Ok);
					}
                    m_TextView_KEY.setText(strTxt);
                    m_TextView_KEY.setTextColor(0xFF55FF55);
                }
                break;

                case MSG_KEY_TEST_ERROR: {
                	String strTxt = getResources().getString(R.string.KEY_Test);
					if(key_test){
						if(key_flag == 0x1 || key_flag == 0x5)
							strTxt = strTxt + "    " + getResources().getString(R.string.KEY_Test_1_Ok);
						else
							strTxt = strTxt + "    " + getResources().getString(R.string.KEY_Test_1_Fail);

						if(key_flag == 0x6)
							strTxt = strTxt + "    " + getResources().getString(R.string.KEY_Test_2_Ok);
						else
							strTxt = strTxt + "    " + getResources().getString(R.string.KEY_Test_2_Fail);
					}else{
						strTxt = strTxt + "    " + getResources().getString(R.string.Test_Fail);
					}

                    m_TextView_KEY.setText(strTxt);
                    m_TextView_KEY.setTextColor(0xFFFF5555);
                    Log.d(TAG, "MSG_KEY_TEST_ERROR");
                }
                break;

                case MSG_MCU_TEST_OK: {
                    String strTxt = getResources().getString(R.string.MCU_Test) + "    " + getResources().getString(R.string.Test_Ok);

                    m_TextView_MCU.setText(strTxt);
                    m_TextView_MCU.setTextColor(0xFF55FF55);
                    Log.d(TAG, "MSG_MCU_TEST_OK");
                }
                break;

                case MSG_MCU_TEST_ERROR: {
                    String strTxt = getResources().getString(R.string.MCU_Test) + "    " + getResources().getString(R.string.Test_Fail);

                    m_TextView_MCU.setText(strTxt);
                    m_TextView_MCU.setTextColor(0xFFFF5555);
                    Log.d(TAG, "MSG_MCU_TEST_ERROR");
                }
                break;

                case MSG_WIFI_TEST_OK: {
                    String strTxt = getResources().getString(R.string.Wifi_Test) + "    " + configSSID + "    " + wifiLevel + "    " + getResources().getString(R.string.Test_Ok);

                    m_TextView_Wifi.setText(strTxt);
                    m_TextView_Wifi.setTextColor(0xFF55FF55);
                }
                break;

                case MSG_WIFI_TEST_ERROR: {
                    String strTxt = getResources().getString(R.string.Wifi_Test) + "    " + getResources().getString(R.string.Test_Fail);
                    m_TextView_Wifi.setText(strTxt);
                    m_TextView_Wifi.setTextColor(0xFFFF5555);
                }
                break;
                case MSG_BT_TEST_OK: {
                    String strTxt = getResources().getString(R.string.BT_Test) + "    " + BTSSID + "    " + btLevel + "    " + getResources().getString(R.string.Test_Ok);
                    m_TextView_BT.setText(strTxt);
                    m_TextView_BT.setTextColor(0xFF55FF55);
                }
                break;
                case MSG_BT_TEST_ERROR: {
                    String strTxt = getResources().getString(R.string.BT_Test) + "    " + getResources().getString(R.string.Test_Fail);
                    m_TextView_BT.setText(strTxt);
                    m_TextView_BT.setTextColor(0xFFFF5555);
                }
                break;
                case MSG_RTC_TEST_OK: {
                    String strTxt = getResources().getString(R.string.Rtc_Test) + "    " + getResources().getString(R.string.Test_Ok);
                    m_TextView_Rtc.setText(strTxt);
                    m_TextView_Rtc.setTextColor(0xFF55FF55);

                }
                break;
                case MSG_RTC_TEST_ERROR: {
                    String strTxt = getResources().getString(R.string.Rtc_Test) + "    " + getResources().getString(R.string.Test_Fail);
                    m_TextView_Rtc.setText(strTxt);
                    m_TextView_Rtc.setTextColor(0xFFFF5555);

                }
                break;
                case MSG_POWERLED_TEST_Start:
                    tag_power++;
                    if (tag_power > ledtime) {
                        mHandler.removeMessages(MSG_POWERLED_TEST_Start);
                        mHandler.sendEmptyMessage(MSG_POWERLED_TEST_End);
                        return;
                    }
                    Log.d(TAG, "MSG_POWERLED_TEST_Start: " + tag_power);
                    if (tag_power % 2 == 1) {
                        m_Button_PowerLed.setText(getResources().getString(R.string.PowerKey_TestIng) + "!");
                        Tools.writeFile(Tools.Power_Led, "off");
                        mHandler.removeMessages(MSG_POWERLED_TEST_Start);
                        mHandler.sendEmptyMessageDelayed(MSG_POWERLED_TEST_Start, 1000);
                    } else if (tag_power % 2 == 0) {
                        m_Button_PowerLed.setText(getResources().getString(R.string.PowerKey_TestIng) + "!!");
                        Tools.writeFile(Tools.Power_Led, "on");
                        mHandler.removeMessages(MSG_POWERLED_TEST_Start);
                        mHandler.sendEmptyMessageDelayed(MSG_POWERLED_TEST_Start, 1000);
                    }
                    break;
                case MSG_POWERLED_TEST_End:
                    tag_power = 0;
                    m_Button_PowerLed.setText(getResources().getString(R.string.PowerKey_Test));
                    Tools.writeFile(Tools.Power_Led, "on");
                    break;
                case MSG_PLAY_VIDEO:
                    mLeftLayout.setVisibility(View.GONE);
                    mBottomLayout.setVisibility(View.GONE);
                    mBottomLayout2.setVisibility(View.GONE);
                    mBottomLayout3.setVisibility(View.GONE);
                    mBottomLayout4.setVisibility(View.GONE);
                    mBottomLayout5.setVisibility(View.GONE);
                    mBottomLayout6.setVisibility(View.GONE);
                    break;
                case MSG_TIME: {
                /*    if(bIsKeyDown)
                    {
                        bIsKeyDown = false;
                        mHandler.removeMessages(MSG_TIME);
                        mHandler.sendEmptyMessageDelayed(MSG_TIME, 1 * 1000); 
                    }
                    else*/
                    {
                        mHandler.removeMessages(MSG_TIME);
                        OnScanText();
                    }
                }
                break;

                case MSG_TF_TEST_XL_ERROR: {
                    String strTxt = getResources().getString(R.string.TF_Test)
                            + "    " + getResources().getString(R.string.Test_Fail);
                    m_TextView_TF.setText(strTxt);
                    m_TextView_TF.setTextColor(0xFFFF5555);
                }
                break;

                case MSG_TF_TEST_XL_OK: {
                    String strTxt = getResources().getString(R.string.TF_Test)
                            + "    " + getResources().getString(R.string.Test_Ok);
                    m_TextView_TF.setText(strTxt);
                    m_TextView_TF.setTextColor(0xFF55FF55);
                }
                break;

                case MSG_USB1_TEST_XL_ERROR: {
	                String strTxt = getResources().getString(R.string.USB1_Test);
	                if(usb2Status[0].equals(getResources().getString(R.string.Test_Ok)))
	                    strTxt = strTxt + "  " + getResources().getString(R.string.Test_USB2_1_Ok);
	                else
	                    strTxt = strTxt + "  " + getResources().getString(R.string.Test_USB2_1_Fail);

	                if(usb2Status[1].equals(getResources().getString(R.string.Test_Ok)))
	                    strTxt = strTxt + "  " + getResources().getString(R.string.Test_USB2_2_Ok);
	                else
	                    strTxt = strTxt + "  " + getResources().getString(R.string.Test_USB2_2_Fail);

	                if(usb2Status[2].equals(getResources().getString(R.string.Test_Ok)))
	                    strTxt = strTxt + "  " + getResources().getString(R.string.Test_USB2_3_Ok);
	                else
	                    strTxt = strTxt + "  " + getResources().getString(R.string.Test_USB2_3_Fail);

	                m_TextView_USB1.setText(strTxt);
	                m_TextView_USB1.setTextColor(0xFFFF5555);
                }
                break;

                case MSG_USB1_TEST_XL_OK: {
					String strTxt = getResources().getString(R.string.USB1_Test)
							+ "  " + getResources().getString(R.string.Test_USB2_1_Ok)
	                        + "  " + getResources().getString(R.string.Test_USB2_2_Ok)
	                        + "  " + getResources().getString(R.string.Test_USB2_3_Ok);
					m_TextView_USB1.setText(strTxt);
					m_TextView_USB1.setTextColor(0xFF55FF55);
				}
                break;

                case MSG_USB2_TEST_XL_ERROR: {
	                String strTxt = getResources().getString(R.string.USB2_Test);
	                if(usb3Status[0].equals(getResources().getString(R.string.Test_Ok)))
	                    strTxt = strTxt + "  " + getResources().getString(R.string.Test_USB3_1_Ok);
	                else
	                    strTxt = strTxt + "  " + getResources().getString(R.string.Test_USB3_1_Fail);

	                if(usb3Status[1].equals(getResources().getString(R.string.Test_Ok)))
	                    strTxt = strTxt + "  " + getResources().getString(R.string.Test_USB3_2_Ok);
	                else
	                    strTxt = strTxt + "  " + getResources().getString(R.string.Test_USB3_2_Fail);

	                if(usb3Status[2].equals(getResources().getString(R.string.Test_Ok)))
	                    strTxt = strTxt + "  " + getResources().getString(R.string.Test_USB3_3_Ok);
	                else
	                    strTxt = strTxt + "  " + getResources().getString(R.string.Test_USB3_3_Fail);

					if (MainActivity.test_board.equals("A10-3588")) {
						if(usb3Status[3].equals(getResources().getString(R.string.Test_Ok)))
							strTxt = strTxt + "  " + getResources().getString(R.string.Test_USB3_4_Ok);
						else
							strTxt = strTxt + "  " + getResources().getString(R.string.Test_USB3_4_Fail);
					}
                    m_TextView_USB2.setText(strTxt);
                    m_TextView_USB2.setTextColor(0xFFFF5555);
                }
                break;

                case MSG_USB2_TEST_XL_OK: {
						String strTxt = getResources().getString(R.string.USB2_Test);
					if (MainActivity.test_board.equals("A10-3588")) {
								strTxt = strTxt
								+ "  " + getResources().getString(R.string.Test_USB3_1_Ok)
								+ "  " + getResources().getString(R.string.Test_USB3_2_Ok)
								+ "  " + getResources().getString(R.string.Test_USB3_3_Ok)
								+ "  " + getResources().getString(R.string.Test_USB3_4_Ok);
					} else {
								strTxt = strTxt
								+ "  " + getResources().getString(R.string.Test_USB3_1_Ok)
								+ "  " + getResources().getString(R.string.Test_USB3_2_Ok)
								+ "  " + getResources().getString(R.string.Test_USB3_3_Ok);
					}

                    m_TextView_USB2.setText(strTxt);
                    m_TextView_USB2.setTextColor(0xFF55FF55);
                }
                break;

                case MSG_SSD_TEST_ERROR: {
					String strTxt = getResources().getString(R.string.SSD_Test);
					if (MainActivity.test_board.equals("A10-3588")) {
						if(ssdStatus[0].equals(getResources().getString(R.string.Test_Ok)))
							strTxt = strTxt + "    " + getResources().getString(R.string.Test_SSD_1_Ok);
						else
							strTxt = strTxt + "    " + getResources().getString(R.string.Test_SSD_1_Fail);

						if(ssdStatus[1].equals(getResources().getString(R.string.Test_Ok)))
							strTxt = strTxt + "    " + getResources().getString(R.string.Test_SSD_2_Ok);
						else
							strTxt = strTxt + "    " + getResources().getString(R.string.Test_SSD_2_Fail);

						if(ssdStatus[2].equals(getResources().getString(R.string.Test_Ok)))
							strTxt = strTxt + "    " + getResources().getString(R.string.Test_SSD_3_Ok);
						else
							strTxt = strTxt + "    " + getResources().getString(R.string.Test_SSD_3_Fail);
					} else {
						 strTxt = strTxt
							+ "    " + getResources().getString(R.string.Test_Fail);
					}

                    m_TextView_SSD.setText(strTxt);
                    m_TextView_SSD.setTextColor(0xFFFF5555);
                }
                break;

                case MSG_SSD_TEST_OK: {
					String strTxt = getResources().getString(R.string.SSD_Test);
					if (MainActivity.test_board.equals("A10-3588")) {
						   strTxt = strTxt
								+ "    " + getResources().getString(R.string.Test_SSD_1_Ok)
								+ "    " + getResources().getString(R.string.Test_SSD_2_Ok)
								+ "    " + getResources().getString(R.string.Test_SSD_3_Ok);
					} else {
						  strTxt = strTxt
								+ "    " + getResources().getString(R.string.Test_Ok);
					}
                    m_TextView_SSD.setText(strTxt);
                    m_TextView_SSD.setTextColor(0xFF55FF55);
                }
                break;

                case MSG_SATA_TEST_ERROR: {
					String strTxt = getResources().getString(R.string.SATA_Test)
						+ "    " + getResources().getString(R.string.Test_Fail);

                    m_TextView_SATA.setText(strTxt);
                    m_TextView_SATA.setTextColor(0xFFFF5555);
                }
                break;

                case MSG_SATA_TEST_OK: {
					String strTxt = getResources().getString(R.string.SATA_Test)
						+ "    " + getResources().getString(R.string.Test_Ok);

                    m_TextView_SATA.setText(strTxt);
                    m_TextView_SATA.setTextColor(0xFF55FF55);
                }
                break;

                case MSG_GET_CPU_STATUS:
                    m_TextView_CPU_THERMAL.setText(Tools.readFile(Tools.cpu_thermal));
                    m_TextView_CPU_FREQ.setText("0-3:" + Tools.readFile(Tools.cpu0_cpufreq) + " 4-7: " +
                            Tools.readFile(Tools.cpu4_cpufreq));
                    break;
            }
        }
    }

    private void CheckSameMac(String Scanmac) {

        if (Scanmac.equalsIgnoreCase(readMac)) {

            Toast.makeText(getApplicationContext(), getResources().getString(R.string.testled), Toast.LENGTH_LONG).show();
            m_mactitle.setText(readMac + "   " + getResources().getString(R.string.the_same_mac));
            m_mactitle.setTextColor(Color.GREEN);

            Log.e(TAG, "PowerLed_Test()");
            m_Button_PowerLed.setTag(0);
            mHandler.removeMessages(MSG_POWERLED_TEST_Start);
            mHandler.sendEmptyMessage(MSG_POWERLED_TEST_Start);

        } else {
            m_mactitle.setText(Scanmac + "   " + getResources().getString(R.string.the_diff_mac));
            m_mactitle.setTextColor(Color.RED);
            m_maccheck.requestFocus();
        }
    }

    private void OnScanText() {

        String strMac = m_maccheck.getText().toString();
        int nLength = m_maccheck.getText().toString().length();

        if (strMac.isEmpty()) {
            return;
        }
        m_maccheck.setText("");

        String strTmpMac = "";

        if (getResources().getInteger(R.integer.config_mac_length) == nLength) {
            for (int i = 0; i < nLength; i += 2) {
                strTmpMac += strMac.substring(i, (i + 2) < nLength ? (i + 2) : nLength);

                if ((i + 2) < nLength) strTmpMac += ':';
            }
            strMac = strTmpMac;
            CheckSameMac(strMac);
        } else if (getResources().getInteger(R.integer.config_mac_length2) == nLength && (':' == strMac.charAt(2)) && (':' == strMac.charAt(5))) {
            CheckSameMac(strMac);
        } else {
            strTmpMac = "";
            m_mactitle.setText(strMac + "   " + getResources().getString(R.string.the_diff_mac));
            m_mactitle.setTextColor(Color.RED);
            m_maccheck.requestFocus();
        }

    }

    private static final int BAIDU_READ_PHONE_STATE = 100;
    private static WifiManager mWifiManager;
    private BroadcastReceiver mFactoryReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				test_ETH();
                updateEthandWifi();
            } else if (action.equals(Intent.ACTION_TIME_TICK) || action.equals(Intent.ACTION_TIME_CHANGED)) {
                updateTime();
            }
        }
    };


    private BroadcastReceiver mountReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Uri uri = intent.getData();
            if (uri.getScheme().equals("file")) {
                if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
                    String path = uri.getPath();
                    Log.d(TAG, "mFactoryReceiver mount patch is " + path);
                    if (path.contains(USB1_PATH)) {
                        if (isUsb1()) {
                            mHandler.sendEmptyMessage(MSG_USB1_TEST_OK);
                        } else {
                            mHandler.sendEmptyMessage(MSG_USB2_TEST_OK);
                        }
                    } else if (path.contains(USB2_PATH)) {
                        List<String> volumes = getVolumes();
                        boolean isUSB1MOUNT = false;
                        for (String volume : volumes) {
                            if (volume.contains(USB1_PATH)) {
                                isUSB1MOUNT = true;
                                mHandler.sendEmptyMessage(MSG_USB1_TEST_OK);
                                mHandler.sendEmptyMessage(MSG_USB2_TEST_OK);
                            }
                        }
                        if (!isUSB1MOUNT) {
                            if (isUsb1())
                                mHandler.sendEmptyMessage(MSG_USB1_TEST_OK);
                            else
                                mHandler.sendEmptyMessage(MSG_USB2_TEST_OK);
                        }
                    } else if (path.contains(TFCARD_PATH)) {
                        mHandler.sendEmptyMessage(MSG_TF_TEST_OK);
                    }
                }
            }
        }
    };

    private void updateTime() {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd/  E ");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        m_TextView_Time.setText(sdf1.format(new Date()) + sdf2.format(new Date()));
    }

    private void readVersion() {

        String lssue = getResources().getString(R.string.lssue_ver);
        String client = getResources().getString(R.string.client_ver);
        String verfile = getResources().getString(R.string.versionfile);

        File OutputFile = new File(verfile);
        if (!OutputFile.exists()) {
            Toast.makeText(getApplicationContext(), verfile + getResources().getString(R.string.noexist), Toast.LENGTH_LONG).show();
            return;
        }

        try {
            FileInputStream instream = new FileInputStream(verfile);
            if (instream != null) {
                InputStreamReader inputreader = new InputStreamReader(instream);
                BufferedReader buffreader = new BufferedReader(inputreader);

                //Log.d(TAG, "buffreader = " + buffreader.toString());

                String line;
                while ((line = buffreader.readLine()) != null) {
                    if (line.startsWith(lssue)) {
                        lssue_value = line.replace(lssue, "").replace("=", "").trim().toString();
                    }
                    if (line.startsWith(client)) {
                        client_value = line.replace(client, "").replace("=", "").trim().toString();
                    }
                }

                instream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "readVersion The File doesn\'t not exist.");
        } catch (IOException e) {
            Log.e(TAG, " readFile error!");
            Log.e(TAG, e.getMessage());
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        mHandler.removeMessages(MSG_PLAY_VIDEO);
        mHandler.sendEmptyMessageDelayed(MSG_PLAY_VIDEO, MSG_PLAY_VIDEO_TIME);
        mLeftLayout.setVisibility(View.VISIBLE);
        mBottomLayout.setVisibility(View.VISIBLE);
        mBottomLayout2.setVisibility(View.VISIBLE);
        mBottomLayout3.setVisibility(View.VISIBLE);
        //mBottomLayout4.setVisibility(View.VISIBLE);//wifi show
        mBottomLayout5.setVisibility(View.VISIBLE);
        mBottomLayout6.setVisibility(View.VISIBLE);
		Log.d(TAG,"onKeyDown ----> " + keyCode);
        return super.onKeyDown(keyCode, event);
		//return true;
    }

	@Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d(TAG,"onKeyUp ----> " + keyCode);
		if(KeyEvent.KEYCODE_VOLUME_DOWN == keyCode){
			key_flag = key_flag|0x1;
			if(7 == key_flag)
				mHandler.sendEmptyMessage(MSG_KEY_TEST_OK);
			else
				mHandler.sendEmptyMessage(MSG_KEY_TEST_ERROR);
		} else if(KeyEvent.KEYCODE_VOLUME_UP == keyCode){//Two power keys need to be pressed
			if(4 == key_flag || 5 == key_flag)
				key_flag = key_flag|0x2;
			else
				key_flag = key_flag|0x4;

			if(7 == key_flag)
				mHandler.sendEmptyMessage(MSG_KEY_TEST_OK);
			else
				mHandler.sendEmptyMessage(MSG_KEY_TEST_ERROR);
		}
        return super.onKeyUp(keyCode, event);
		//return true;
    }
}
