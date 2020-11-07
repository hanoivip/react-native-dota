package org.cocos2dx.sdk;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothService {
    private static final boolean f2549D = true;
    public static final String DEVICE_NAME = "device_name";
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_WRITE = 3;
    private static final UUID MY_UUID;
    private static final String NAME = "BluetoothChat";
    public static final int STATE_CONNECTED = 3;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_LISTEN = 1;
    public static final int STATE_NONE = 0;
    private static final String TAG = "BluetoothChatService";
    public static final String TOAST = "toast";
    private AcceptThread mAcceptThread;
    private final BluetoothAdapter mAdapter;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private final Handler mHandler;
    private int mState;

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket bluetoothServerSocket = null;
            try {
                bluetoothServerSocket = BluetoothService.this.mAdapter.listenUsingRfcommWithServiceRecord(BluetoothService.NAME, BluetoothService.MY_UUID);
            } catch (Throwable e) {
                Log.e(BluetoothService.TAG, "listen() failed", e);
            }
            this.mmServerSocket = bluetoothServerSocket;
        }

        public void cancel() {
            Log.d(BluetoothService.TAG, "cancel " + this);
            try {
                this.mmServerSocket.close();
            } catch (Throwable e) {
                Log.e(BluetoothService.TAG, "close() of server failed", e);
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
            r4 = this;
            r0 = "BluetoothChatService";
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r2 = "BEGIN mAcceptThread";
            r1 = r1.append(r2);
            r1 = r1.append(r4);
            r1 = r1.toString();
            android.util.Log.d(r0, r1);
            r0 = "AcceptThread";
            r4.setName(r0);
        L_0x001d:
            r0 = org.cocos2dx.sdk.BluetoothService.this;
            r0 = r0.mState;
            r1 = 3;
            if (r0 == r1) goto L_0x0047;
        L_0x0026:
            r0 = r4.mmServerSocket;	 Catch:{ IOException -> 0x003f }
            r0 = r0.accept();	 Catch:{ IOException -> 0x003f }
            if (r0 == 0) goto L_0x001d;
        L_0x002e:
            r1 = org.cocos2dx.sdk.BluetoothService.this;
            monitor-enter(r1);
            r2 = org.cocos2dx.sdk.BluetoothService.this;	 Catch:{ all -> 0x003c }
            r2 = r2.mState;	 Catch:{ all -> 0x003c }
            switch(r2) {
                case 0: goto L_0x0059;
                case 1: goto L_0x004f;
                case 2: goto L_0x004f;
                case 3: goto L_0x0059;
                default: goto L_0x003a;
            };	 Catch:{ all -> 0x003c }
        L_0x003a:
            monitor-exit(r1);	 Catch:{ all -> 0x003c }
            goto L_0x001d;
        L_0x003c:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x003c }
            throw r0;
        L_0x003f:
            r0 = move-exception;
            r1 = "BluetoothChatService";
            r2 = "accept() failed";
            android.util.Log.e(r1, r2, r0);
        L_0x0047:
            r0 = "BluetoothChatService";
            r1 = "END mAcceptThread";
            android.util.Log.i(r0, r1);
            return;
        L_0x004f:
            r2 = org.cocos2dx.sdk.BluetoothService.this;	 Catch:{ all -> 0x003c }
            r3 = r0.getRemoteDevice();	 Catch:{ all -> 0x003c }
            r2.connected(r0, r3);	 Catch:{ all -> 0x003c }
            goto L_0x003a;
        L_0x0059:
            r0.close();	 Catch:{ IOException -> 0x005d }
            goto L_0x003a;
        L_0x005d:
            r0 = move-exception;
            r2 = "BluetoothChatService";
            r3 = "Could not close unwanted socket";
            android.util.Log.e(r2, r3, r0);	 Catch:{ all -> 0x003c }
            goto L_0x003a;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.cocos2dx.sdk.BluetoothService.AcceptThread.run():void");
        }
    }

    private class ConnectThread extends Thread {
        private final BluetoothDevice mmDevice;
        private final BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice bluetoothDevice) {
            this.mmDevice = bluetoothDevice;
            BluetoothSocket bluetoothSocket = null;
            try {
                bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(BluetoothService.MY_UUID);
            } catch (Throwable e) {
                Log.e(BluetoothService.TAG, "create() failed", e);
            }
            this.mmSocket = bluetoothSocket;
        }

        public void cancel() {
            try {
                this.mmSocket.close();
            } catch (Throwable e) {
                Log.e(BluetoothService.TAG, "close() of connect socket failed", e);
            }
        }

        public void run() {
            Log.i(BluetoothService.TAG, "BEGIN mConnectThread");
            setName("ConnectThread");
            BluetoothService.this.mAdapter.cancelDiscovery();
            try {
                this.mmSocket.connect();
                synchronized (BluetoothService.this) {
                    BluetoothService.this.mConnectThread = null;
                }
                BluetoothService.this.connected(this.mmSocket, this.mmDevice);
            } catch (IOException e) {
                BluetoothService.this.connectionFailed();
                try {
                    this.mmSocket.close();
                } catch (Throwable e2) {
                    Log.e(BluetoothService.TAG, "unable to close() socket during connection failure", e2);
                }
                BluetoothService.this.start();
            }
        }
    }

    private class ConnectedThread extends Thread {
        private InputStream mmInStream;
        private OutputStream mmOutStream;
        private BluetoothSocket mmSocket;
        final /* synthetic */ BluetoothService this$0;

        public ConnectedThread(BluetoothService bluetoothService, BluetoothSocket bluetoothSocket) {
            InputStream inputStream = null;
            Throwable e;
            OutputStream outputStream = null;
            this.this$0 = bluetoothService;
            Log.d(BluetoothService.TAG, "create ConnectedThread");
            this.mmSocket = bluetoothSocket;
            try {
                inputStream = bluetoothSocket.getInputStream();
                try {
                    outputStream = bluetoothSocket.getOutputStream();
                } catch (IOException e2) {
                    e = e2;
                    Log.e(BluetoothService.TAG, "temp sockets not created", e);
                    this.mmInStream = inputStream;
                    this.mmOutStream = outputStream;
                }
            } catch (Throwable e3) {
                e = e3;
                Log.e(BluetoothService.TAG, "temp sockets not created", e);
                this.mmInStream = inputStream;
                this.mmOutStream = outputStream;
            }
            this.mmInStream = inputStream;
            this.mmOutStream = outputStream;
        }

        public void cancel() {
            try {
                this.mmSocket.close();
            } catch (Throwable e) {
                Log.e(BluetoothService.TAG, "close() of connect socket failed", e);
            }
        }

        public void run() {
            int i = BluetoothService.STATE_NONE;
            Log.i(BluetoothService.TAG, "BEGIN mConnectedThread");
            byte[] bArr = new byte[102400];
            StringBuilder stringBuilder = new StringBuilder();
            while (true) {
                try {
                    int read = this.mmInStream.read(bArr, BluetoothService.STATE_NONE, 1008);
                    stringBuilder.append(new String(bArr, BluetoothService.STATE_NONE, read));
                    i += read;
                    if (read != 1008) {
                        this.this$0.mHandler.obtainMessage(BluetoothService.STATE_CONNECTING, stringBuilder.toString()).sendToTarget();
                        stringBuilder.delete(BluetoothService.STATE_NONE, stringBuilder.length());
                    }
                } catch (Throwable e) {
                    Throwable th = e;
                    int i2 = i;
                    Log.e(BluetoothService.TAG, "disconnected", th);
                    this.this$0.connectionLost();
                    if (i2 != 0) {
                        return;
                    }
                    return;
                }
            }
        }

        public void write(byte[] bArr) {
            try {
                this.mmOutStream.write(bArr);
                this.mmOutStream.flush();
                this.this$0.mHandler.obtainMessage(BluetoothService.STATE_CONNECTED, -1, -1, bArr).sendToTarget();
            } catch (Throwable e) {
                Log.e(BluetoothService.TAG, "Exception during write", e);
            }
        }
    }

    static {
        MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    }

    public BluetoothService(Context context, Handler handler) {
        this.mAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mState = STATE_NONE;
        this.mHandler = handler;
    }

    private void connectionFailed() {
        setState(STATE_LISTEN);
        Message obtainMessage = this.mHandler.obtainMessage(MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(TOAST, "Unable to connect device");
        obtainMessage.setData(bundle);
        this.mHandler.sendMessage(obtainMessage);
    }

    private void connectionLost() {
        setState(STATE_LISTEN);
        Message obtainMessage = this.mHandler.obtainMessage(MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(TOAST, "Device connection was lost");
        obtainMessage.setData(bundle);
        this.mHandler.sendMessage(obtainMessage);
    }

    private void setState(int i) {
        synchronized (this) {
            Log.d(TAG, "setState() " + this.mState + " -> " + i);
            this.mState = i;
            this.mHandler.obtainMessage(STATE_LISTEN, i, -1).sendToTarget();
        }
    }

    public void connect(BluetoothDevice bluetoothDevice) {
        synchronized (this) {
            Log.d(TAG, "connect to: " + bluetoothDevice);
            if (this.mState == STATE_CONNECTING && this.mConnectThread != null) {
                this.mConnectThread.cancel();
                this.mConnectThread = null;
            }
            if (this.mConnectedThread != null) {
                this.mConnectedThread.cancel();
                this.mConnectedThread = null;
            }
            this.mConnectThread = new ConnectThread(bluetoothDevice);
            this.mConnectThread.start();
            setState(STATE_CONNECTING);
        }
    }

    public void connected(BluetoothSocket bluetoothSocket, BluetoothDevice bluetoothDevice) {
        synchronized (this) {
            Log.d(TAG, "connected");
            if (this.mConnectThread != null) {
                this.mConnectThread.cancel();
                this.mConnectThread = null;
            }
            if (this.mConnectedThread != null) {
                this.mConnectedThread.cancel();
                this.mConnectedThread = null;
            }
            if (this.mAcceptThread != null) {
                this.mAcceptThread.cancel();
                this.mAcceptThread = null;
            }
            this.mConnectedThread = new ConnectedThread(this, bluetoothSocket);
            this.mConnectedThread.start();
            Message obtainMessage = this.mHandler.obtainMessage(MESSAGE_DEVICE_NAME);
            Bundle bundle = new Bundle();
            bundle.putString(DEVICE_NAME, bluetoothDevice.getName());
            obtainMessage.setData(bundle);
            this.mHandler.sendMessage(obtainMessage);
            setState(STATE_CONNECTED);
        }
    }

    public int getState() {
        int i;
        synchronized (this) {
            i = this.mState;
        }
        return i;
    }

    public void start() {
        synchronized (this) {
            Log.d(TAG, "start");
            if (this.mConnectThread != null) {
                this.mConnectThread.cancel();
                this.mConnectThread = null;
            }
            if (this.mConnectedThread != null) {
                this.mConnectedThread.cancel();
                this.mConnectedThread = null;
            }
            if (this.mAcceptThread == null) {
                this.mAcceptThread = new AcceptThread();
                this.mAcceptThread.start();
            }
            setState(STATE_LISTEN);
        }
    }

    public void stop() {
        synchronized (this) {
            Log.d(TAG, "stop");
            if (this.mConnectThread != null) {
                this.mConnectThread.cancel();
                this.mConnectThread = null;
            }
            if (this.mConnectedThread != null) {
                this.mConnectedThread.cancel();
                this.mConnectedThread = null;
            }
            if (this.mAcceptThread != null) {
                this.mAcceptThread.cancel();
                this.mAcceptThread = null;
            }
            setState(STATE_NONE);
        }
    }

    public void write(byte[] bArr) {
        synchronized (this) {
            if (this.mState != STATE_CONNECTED) {
                return;
            }
            ConnectedThread connectedThread = this.mConnectedThread;
            connectedThread.write(bArr);
        }
    }
}
