package sh.lilith.dgame;

import android.Manifest;
import 	android.content.pm.PackageManager;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.StatFs;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import com.loopj.android.http.BuildConfig;
import android.net.ConnectivityManager;

import java.util.HashMap;
import java.util.Set;
import org.cocos2dx.sdk.DTClip;
import org.cocos2dx.lib.Cocos2dxActivity;
import org.cocos2dx.lib.Cocos2dxGLSurfaceView;
import org.cocos2dx.lib.Cocos2dxVideoView;
import org.cocos2dx.platform.Platform;
import org.cocos2dx.sdk.DTConfigure;
import org.cocos2dx.sdk.DTHandler;
import org.cocos2dx.sdk.DTMsg;
import org.cocos2dx.sdk.DTPlatform;
import org.cocos2dx.sdk.DTTools;

import 	androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;

/**
 * Director: DTTools, DTConfigure, Platform
 * 
 * @author toannguyen
 * 
 */
public class DGame extends Cocos2dxActivity {
	public static final String DEVICE_NAME = "device_name";
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_STATE_CHANGE = Toast.LENGTH_LONG;
	public static final int MESSAGE_TOAST = 5;
	public static final int MESSAGE_WRITE = 3;
	private static final int REQUEST_CONNECT_DEVICE = 10045;
	private static final int REQUEST_ENABLE_BT = 10046;
	private static final int REQUEST_FOTO = 10047;
	public static final String TOAST = "toast";
	static Activity gameActivity;
	static Context gameContext;
	private static BluetoothAdapter mBluetoothAdapter;
	private static String mConnectedDeviceName;
	private static HashMap<String, Comparable> mNewDevicesArrayAdapter;
	private static HashMap<String, Comparable> mPairedDevicesArrayAdapter;
	private static final BroadcastReceiver mReceiver;
	private DTConfigure config;
	private DTHandler handler;
	private DTPlatform platform;
	private DTTools tools;
	private static DTClip dtclip;

	static {
		System.loadLibrary("hellolua");

		mBluetoothAdapter = null;
		mConnectedDeviceName = null;

		mReceiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if ("android.bluetooth.device.action.FOUND".equals(action)) {
					BluetoothDevice bluetoothDevice = (BluetoothDevice) intent
							.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
					if (bluetoothDevice.getBondState() != 12) {
						DGame.mNewDevicesArrayAdapter.put(
								bluetoothDevice.getAddress(),
								bluetoothDevice.getAddress());
					}
					DGame.deviceListchangeInvoke(0);
				} else if ("android.bluetooth.adapter.action.DISCOVERY_FINISHED"
						.equals(action)) {
					if (DGame.mNewDevicesArrayAdapter.size() == 0) {
						DGame.mNewDevicesArrayAdapter.clear();
					}
					DGame.deviceListchangeInvoke(DGame.MESSAGE_STATE_CHANGE);
				}
			}
		};
	}

	public static boolean copyStringToSystem(String paramString) {
		dtclip.copyStringToSystem(paramString);
		return true;
	}

	public static void CloseBluetooth() {
		OpenBluetooth(false);
		System.out.println("CloseBT");
	}

	public static final String GetMyOwnBTIdentity() {
		return mBluetoothAdapter == null ? BuildConfig.VERSION_NAME
				: mBluetoothAdapter.getAddress() + "="
						+ mBluetoothAdapter.getAddress();
	}

	public static void OpenBluetooth(boolean z) {
		if (mBluetoothAdapter != null) {
			System.out.println("OpenBT");
			if (!z) {
				System.out.println("BT Shutdown");
				mBluetoothAdapter.disable();
			} else if (mBluetoothAdapter.isEnabled()) {
				ensureDiscoverable();
				System.out.println("BTSetup Already ok!!");
			} else {
				System.out.println("BTStart request");
				gameActivity.startActivityForResult(new Intent(
						"android.bluetooth.adapter.action.REQUEST_ENABLE"),
						REQUEST_ENABLE_BT);
			}
		}
	}

	private static void cancelDiscovery() {
		if (mBluetoothAdapter != null) {
			System.out.println("cancelDiscovery()");
			mBluetoothAdapter.cancelDiscovery();
		}
	}

	public static void deviceListChanged(final String str, final int i) {
		Cocos2dxGLSurfaceView.getInstance().queueEvent(new Runnable() {
			public void run() {
				DGame.doDeviceListChanged(str, i);
			}
		});
	}

	private static void deviceListchangeInvoke(int i) {
		HashMap hashMap = new HashMap();
		hashMap.putAll(mPairedDevicesArrayAdapter);
		hashMap.putAll(mNewDevicesArrayAdapter);
		deviceListChanged(hashMap.toString(), i);
	}

	public static native void doDeviceListChanged(String str, int i);

	private static void doDiscovery() {
		if (mBluetoothAdapter != null) {
			System.out.println("doDiscovery()");
			if (mBluetoothAdapter.isDiscovering()) {
				mBluetoothAdapter.cancelDiscovery();
			}
			mBluetoothAdapter.startDiscovery();
			getNearByBTDeviceList();
		}
	}

	public static void enableBTDiscovery(boolean z) {
		if (mBluetoothAdapter != null) {
			System.out.println("enableBTDiscovery");
			if (z) {
				doDiscovery();
			} else {
				cancelDiscovery();
			}
		}
	}

	private static void ensureDiscoverable() {
		if (mBluetoothAdapter != null && mBluetoothAdapter.getScanMode() != 23) {
			Intent intent = new Intent(
					"android.bluetooth.adapter.action.REQUEST_DISCOVERABLE");
			intent.putExtra(
					"android.bluetooth.adapter.extra.DISCOVERABLE_DURATION",
					300);
			gameActivity.startActivity(intent);
		}
	}

	public static void getNearByBTDeviceList() {
		if (mBluetoothAdapter != null) {
			mPairedDevicesArrayAdapter.clear();
			mNewDevicesArrayAdapter.clear();
			Set<BluetoothDevice> bondedDevices = mBluetoothAdapter
					.getBondedDevices();
			if (bondedDevices.size() > 0) {
				for (BluetoothDevice bluetoothDevice : bondedDevices) {
					mPairedDevicesArrayAdapter.put(
							bluetoothDevice.getAddress(),
							bluetoothDevice.getAddress());
				}
			} else {
				mPairedDevicesArrayAdapter.clear();
			}
			deviceListchangeInvoke(0);
		}
	}

	public static boolean isBluetoothEnabled() {
		return mBluetoothAdapter != null ? mBluetoothAdapter.isEnabled()
				: false;
	}

	public static void refreshNearbyBT() {
		if (mBluetoothAdapter != null) {
			if (mBluetoothAdapter.isEnabled()) {
				doDiscovery();
			} else {
				System.out
						.println("bluetooth not enabled,cancel refreshNearbyBT");
			}
		}
	}

	private static void setupChat() {
		ensureDiscoverable();
		refreshNearbyBT();
	}

	protected void onActivityResult(int i, int i2, Intent intent) {
		super.onActivityResult(i, i2, intent);
		switch (i) {
		case REQUEST_CONNECT_DEVICE /* 10045 */:
		case REQUEST_FOTO /* 10047 */:
		case REQUEST_ENABLE_BT /* 10046 */:
			if (i2 == -1) {
				System.out.println("BT enable ok");
				setupChat();
				return;
			}
			Toast.makeText(this, "R.string.bt_not_enabled_leaving", Toast.LENGTH_SHORT).show();
			finish();
		default:
			//this.platform.onActivityResult(i, i2, intent);
		}
	}

	boolean storageGranted = false;
	boolean phoneGranted = false;

	private void checkPermissions()
	{
		if (ContextCompat.checkSelfPermission(this,
				Manifest.permission.WRITE_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED) {

			// Permission is not granted
			// Should we show an explanation?
			/*if (ActivityCompat.shouldShowRequestPermissionRationale(this,
					Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
				// Show an explanation to the user *asynchronously* -- don't block
				// this thread waiting for the user's response! After the user
				// sees the explanation, try again to request the permission.

			} else {
				// No explanation needed; request the permission
				ActivityCompat.requestPermissions(this,
						new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
						1);

				// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
				// app-defined int constant. The callback method gets the
				// result of the request.
			}*/
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
					1);
			Log.d("DGame", "Permission: write ext storage not granted");
		} else {
			// Permission has already been granted
			storageGranted = true;
			Log.d("DGame", "Permission: write ext storage granted");
		}

		if (ContextCompat.checkSelfPermission(this,
				Manifest.permission.READ_PHONE_STATE)
				!= PackageManager.PERMISSION_GRANTED) {

			// Permission is not granted
			// Should we show an explanation?
			/*(if (ActivityCompat.shouldShowRequestPermissionRationale(this,
					Manifest.permission.READ_PHONE_STATE)) {
				// Show an explanation to the user *asynchronously* -- don't block
				// this thread waiting for the user's response! After the user
				// sees the explanation, try again to request the permission.

			} else {
				// No explanation needed; request the permission
				ActivityCompat.requestPermissions(this,
						new String[]{Manifest.permission.READ_PHONE_STATE},
						2);

				// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
				// app-defined int constant. The callback method gets the
				// result of the request.
			}*/
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.READ_PHONE_STATE},
					2);
			Log.d("DGame", "Permission: phone state not granted");
		} else {
			// Permission has already been granted
			Log.d("DGame", "Permission: phone state granted");
			phoneGranted = true;
		}

		if (phoneGranted && storageGranted)
		{
			this.startLogin();
		}
		else
			Toast.makeText(this, "Cần cho phép ứng dụng các quyền để tiếp tục!", Toast.LENGTH_LONG);

	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {
		switch (requestCode) {
			case 1: {//write storage code
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// permission was granted, yay! Do the
					// contacts-related task you need to do.
					storageGranted = true;
				} else {
					// permission denied, boo! Disable the
					// functionality that depends on this permission.
				}
				return;
			}

			case 2: {//write storage code
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// permission was granted, yay! Do the
					// contacts-related task you need to do.
					phoneGranted = true;
				} else {
					// permission denied, boo! Disable the
					// functionality that depends on this permission.
				}
				return;
			}
		}
		if (phoneGranted && storageGranted) {
			this.startLogin();
		} else
			Toast.makeText(this, "Cần cho phép ứng dụng các quyền để tiếp tục!", Toast.LENGTH_LONG);
	}

	protected void startLogin()
	{
	}

	protected void onCreate(Bundle bundle) {
        //get intent parameter
		Intent intent = getIntent();
		int uid = intent.getIntExtra("uid", 0);
		String token = intent.getStringExtra("token");
        
        //continue..
		super.onCreate(bundle);
		this.checkPermissions();
		this.config = DTConfigure.getInstance();
		this.config.setActivity(this);
		this.config.setContext(this);
		this.config.setApp(this.getApplication());
		this.tools = new DTTools(this.config);
		this.handler = new DTHandler(this.config);
		this.platform = new Platform();
		this.platform.setConfigure(this.config);
		this.dtclip = new DTClip(this.config);
		gameActivity = this;
		gameContext = this;
		Cocos2dxVideoView.activityInstance = this;

		// Bluetooth setup
		mPairedDevicesArrayAdapter = new HashMap();
		mNewDevicesArrayAdapter = new HashMap();
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		registerReceiver(mReceiver, new IntentFilter(
				"android.bluetooth.device.action.FOUND"));
		registerReceiver(mReceiver, new IntentFilter(
				"android.bluetooth.adapter.action.DISCOVERY_FINISHED"));
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth not available", MESSAGE_STATE_CHANGE).show();
		}

		// Check network
		ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		//For 3G check
		boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.isConnectedOrConnecting();
		//For WiFi Check
		boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.isConnectedOrConnecting();
		if (!is3g && !isWifi)
		{
			Toast.makeText(this, "Kiểm tra lại kết nối mạng trước khi tiếp tục", Toast.LENGTH_LONG).show();
			this.finish();
		}
		else
		{
			if (!isWifi)
				Toast.makeText(this, "Hiện đang sử dụng mạng 3G/4G, chú ý kiểm soát dung lượng", Toast.LENGTH_SHORT).show();

			// Check storage
			StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
			long bytesAvailable = (long)stat.getBlockSize() * (long)stat.getAvailableBlocks();
			long megAvailable = bytesAvailable / (1024 * 1024);
			if (megAvailable < 300)
				Toast.makeText(this, "Thẻ nhớ còn ít hơn 300M, cần xoá bớt dữ liệu rác nếu không thể vào game DotaUS.", Toast.LENGTH_SHORT).show();
			System.out.println("Available MB : " + megAvailable);

			this.platform.gameInit();
			this.platform.setID();
			this.platform.Init();
            
            //save user info to platform
            this.platform.userid=String.valueOf(uid);
            this.platform.token=token;
		}


	}

	public Cocos2dxGLSurfaceView onCreateGLSurfaceView() {
		return new LuaGLSurfaceView(this);
	}

	protected void onDestroy() {
		super.onDestroy();
		this.platform.destroy();
		if (mBluetoothAdapter != null) {
			mBluetoothAdapter.cancelDiscovery();
		}
		unregisterReceiver(mReceiver);
	}

	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		this.platform.onNewIntent(intent);
	}

	protected void onPause() {
		super.onPause();
		this.platform.pause();
	}

	protected void onResume() {
		super.onResume();
		this.platform.onGameResume();
	}

	protected void onStart() {
		super.onStart();
		this.platform.onGameStart();
	}

	protected void onStop() {
		super.onStop();
		this.platform.onGameStop();
	}
}
