package com.modosmart.agileiot.modosmartagileiot.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.modosmart.agileiot.modosmartagileiot.R;
import com.modosmart.agileiot.modosmartagileiot.utils.ConstantsUtils;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class WiFiFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {
    private io.socket.client.Socket mSocket;
    private Switch wifiControlSwitch;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_wifi, container, false);

        wifiControlSwitch = view.findViewById(R.id.wifi_control_switch);
        wifiControlSwitch.setOnCheckedChangeListener(this);

        initSocket();

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        mSocket.disconnect();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (mSocket != null && mSocket.connected()) {
                mSocket.emit("intesis_message", ConstantsUtils.INTESIS_MESSAGE_SET_STATUS_ON);
                mSocket.emit("intesis_disconnect");
            }
        } else {
            if (mSocket != null && mSocket.connected()) {
                mSocket.emit("intesis_message", ConstantsUtils.INTESIS_MESSAGE_SET_STATUS_OFF);
                mSocket.emit("intesis_disconnect");
            }
        }
    }

    private void initSocket() {
        try {
            if (mSocket == null || !mSocket.connected()) {
                mSocket = IO.socket(ConstantsUtils.SOCKET_SERVER);

                if (!mSocket.connected()) {
                    mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                        @Override
                        public void call(Object... args) {
                            // Get Status of the AC
                            mSocket.emit("intesis_message", ConstantsUtils.INTESIS_MESSAGE_GET_STATUS);
                        }

                    }).on("intesis_callback", new Emitter.Listener() {

                        @Override
                        public void call(Object... args) {
                            String message_callback = args[0].toString();
                            if (message_callback.charAt(13) == 'F') {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        wifiControlSwitch.setChecked(false);
                                    }
                                });
                            } else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        wifiControlSwitch.setChecked(true);
                                    }
                                });
                            }
                        }

                    }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                        @Override
                        public void call(Object... args) {
                        }

                    });
                    mSocket.connect();
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
