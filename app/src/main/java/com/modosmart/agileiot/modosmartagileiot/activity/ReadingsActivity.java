package com.modosmart.agileiot.modosmartagileiot.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.modosmart.agileiot.modosmartagileiot.R;
import com.modosmart.agileiot.modosmartagileiot.utils.ConstantsUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ReadingsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private io.socket.client.Socket mSocket;
    private Switch wifiControlSwitch;
    private TextView roomSensorTemperatureValue;
    private TextView roomSensorHumidityValue;
    private TextView roomSensorPresenceValue;
    private TextView roomSensorBatteryValue;
    private TextView roomSensorFirmwareValue;

//    private TextView windowSensorBatteryValue;
//    private TextView windowSensorFirmwareValue;

    private boolean firstTimeFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readings);

        wifiControlSwitch = findViewById(R.id.ac_switch);
        wifiControlSwitch.setOnCheckedChangeListener(this);

        roomSensorTemperatureValue = findViewById(R.id.room_sensor_temperature_value);
        roomSensorHumidityValue = findViewById(R.id.room_sensor_humidity_value);
        roomSensorPresenceValue = findViewById(R.id.room_sensor_presence_value);
        roomSensorBatteryValue = findViewById(R.id.room_sensor_battery_value);
        roomSensorFirmwareValue = findViewById(R.id.room_sensor_firmware_value);

//        windowSensorBatteryValue = findViewById(R.id.window_sensor_battery_value);
//        windowSensorFirmwareValue = findViewById(R.id.window_sensor_firmware_value);

        initSocket();

        Timer timer = new Timer ();
        TimerTask task = new TimerTask () {
            @Override
            public void run () {
                // your code here...
                if (mSocket.connected()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Get temperature
                            JSONObject room_sensor_temperature = new JSONObject();
                            try {
                                room_sensor_temperature.put("command", "readingDevice");
                                room_sensor_temperature.put("deviceId", ConstantsUtils.ROOM_SENSOR_ID);
                                room_sensor_temperature.put("componentId", "Temperature");
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                            }
                            mSocket.emit("agile", room_sensor_temperature);
                            // Get humidity
                            JSONObject room_sensor_humidity = new JSONObject();
                            try {
                                room_sensor_humidity.put("command", "readingDevice");
                                room_sensor_humidity.put("deviceId", ConstantsUtils.ROOM_SENSOR_ID);
                                room_sensor_humidity.put("componentId", "Humidity");
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                            }
                            mSocket.emit("agile", room_sensor_humidity);
                            // Get presence
                            JSONObject room_sensor_presence = new JSONObject();
                            try {
                                room_sensor_presence.put("command", "readingDevice");
                                room_sensor_presence.put("deviceId", ConstantsUtils.ROOM_SENSOR_ID);
                                room_sensor_presence.put("componentId", "Presence");
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                            }
                            mSocket.emit("agile", room_sensor_presence);
                            // Get battery
                            JSONObject room_sensor_battery = new JSONObject();
                            try {
                                room_sensor_battery.put("command", "readingDevice");
                                room_sensor_battery.put("deviceId", ConstantsUtils.ROOM_SENSOR_ID);
                                room_sensor_battery.put("componentId", "Battery Level");
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                            }
                            mSocket.emit("agile", room_sensor_battery);
                            // Get firmware
                            JSONObject room_sensor_firmware = new JSONObject();
                            try {
                                room_sensor_firmware.put("command", "readingDevice");
                                room_sensor_firmware.put("deviceId", ConstantsUtils.ROOM_SENSOR_ID);
                                room_sensor_firmware.put("componentId", "Firmware Revision");
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                            }
                            mSocket.emit("agile", room_sensor_firmware);



                            // Get battery
//                            JSONObject window_sensor_battery = new JSONObject();
//                            try {
//                                window_sensor_battery.put("command", "readingDevice");
//                                window_sensor_battery.put("deviceId", ConstantsUtils.WINDOW_SENSOR_ID);
//                                window_sensor_battery.put("componentId", "Battery Level");
//                            } catch (JSONException e) {
//                                // TODO Auto-generated catch block
//                            }
//                            mSocket.emit("agile", room_sensor_battery);
//                            // Get firmware
//                            JSONObject window_sensor_firmware = new JSONObject();
//                            try {
//                                window_sensor_firmware.put("command", "readingDevice");
//                                window_sensor_firmware.put("deviceId", ConstantsUtils.WINDOW_SENSOR_ID);
//                                window_sensor_firmware.put("componentId", "Firmware Revision");
//                            } catch (JSONException e) {
//                                // TODO Auto-generated catch block
//                            }
//                            mSocket.emit("agile", window_sensor_firmware);
                        }
                    });
                }
            }
        };

        // schedule the task to run starting now and then every hour...
        timer.schedule (task, 0L, 1000*10);   // 10 seconds
    }

    @Override
    public void onStop() {
        super.onStop();
        mSocket.disconnect();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (mSocket != null && mSocket.connected() && !firstTimeFlag) {
                mSocket.emit("intesis_message", ConstantsUtils.INTESIS_MESSAGE_SET_STATUS_ON);
            }
        } else {
            if (mSocket != null && mSocket.connected() && !firstTimeFlag) {
                mSocket.emit("intesis_message", ConstantsUtils.INTESIS_MESSAGE_SET_STATUS_OFF);
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
                            int length = message_callback.length();
                            if (length >= 13 && message_callback.charAt(13) == 'F') {
                                firstTimeFlag = false;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        wifiControlSwitch.setChecked(false);
                                    }
                                });
                            } else if (length >= 13 && message_callback.charAt(13) == 'N') {
                                firstTimeFlag = false;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        wifiControlSwitch.setChecked(true);
                                    }
                                });
                            }
                        }

                    }).on("agile_reading_device_callback", new Emitter.Listener() {

                        @Override
                        public void call(Object... args) {
                            JSONObject message_callback = (JSONObject)args[0];
                            try {
                                String deviceId = message_callback.getString("deviceID");
                                String componentId = message_callback.getString("componentID");
                                String value = message_callback.getString("value");

                                if (deviceId.equals(ConstantsUtils.ROOM_SENSOR_ID)) {
                                    switch (componentId) {
                                        case "Temperature":
                                            Double temperature_value = Double.parseDouble(value);
                                            value = String.format("%.2f", temperature_value);
                                            value = value + "°C";
                                            final String finalValue = value;
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    roomSensorTemperatureValue.setText(finalValue);
                                                }
                                            });
                                            break;
                                        case "Humidity":
                                            Double humidity_value = Double.parseDouble(value);
                                            value = String.format("%.2f", humidity_value);
                                            value = value + "%";
                                            final String finalValue1 = value;
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    roomSensorHumidityValue.setText(finalValue1);
                                                }
                                            });
                                            break;
                                        case "Presence":
                                            final String finalValue2 = value;
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    roomSensorPresenceValue.setText(finalValue2);
                                                }
                                            });
                                            break;
                                        case "Battery Level":
                                            value = value + "%";
                                            final String finalValue3 = value;
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    roomSensorBatteryValue.setText(finalValue3);
                                                }
                                            });
                                            break;
                                        case "Firmware Revision":
                                            final String finalValue4 = value;
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    roomSensorFirmwareValue.setText(finalValue4);
                                                }
                                            });
                                            break;
                                    }
                                } else if (deviceId.equals(ConstantsUtils.WINDOW_SENSOR_ID)) {
                                    switch (componentId) {
//                                        case "Battery Level":
//                                            value = value + "%";
//                                            final String finalValue5 = value;
//                                            runOnUiThread(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    windowSensorBatteryValue.setText(finalValue5);
//                                                }
//                                            });
//                                            break;
//                                        case "Firmware Revision":
//                                            final String finalValue6 = value;
//                                            runOnUiThread(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    windowSensorFirmwareValue.setText(finalValue6);
//                                                }
//                                            });
//                                            break;
                                    }
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
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
