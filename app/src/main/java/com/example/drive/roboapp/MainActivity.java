package com.example.drive.roboapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class MainActivity extends Activity {
    final int JOYSTICK_UPDATE_DELAY = 400;
    final int STRENGTH_MULTIPLIER = 10;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText footSetting;
    EditText shoulderSetting;
    EditText elbowSetting;
    EditText wristSetting;
    EditText handSetting;
    EditText delaySetting;
    Button btnFoot;
    Button btnShoulder;
    Button btnElbow;
    Button btnWrist;
    Button btnHand;
    Button btnSendAll;
    Button btnDelay;
    ToggleButton btnToggleArm;
    DocumentReference robo1DocRef;
    DocumentReference robo2DocRef;
    DocumentReference robo1SettingsDocRef;
    JoystickView leftJoy;
    JoystickView rightJoy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeComponents();
        addListeners();
        CollectionReference robo1ColRef = db.collection("RoboArm1");
        CollectionReference robo2ColRef = db.collection("RoboArm2");
        robo1SettingsDocRef = robo1ColRef.document("settings");
        robo1DocRef = robo1ColRef.document("rotation");
        robo2DocRef = robo2ColRef.document("rotation");
        if(!getIntent().getBooleanExtra("isTesting", false))
        {
            addSnapshotListeners();
        }
    }

    private void initializeComponents() {
        delaySetting = findViewById(R.id.numDelay);
        btnDelay = findViewById(R.id.btnDelay);
        leftJoy = findViewById(R.id.joyViewLeft);
        rightJoy = findViewById(R.id.joyViewRight);
        footSetting = findViewById(R.id.numSetting1);
        shoulderSetting = findViewById(R.id.numSetting2);
        elbowSetting = findViewById(R.id.numSetting3);
        wristSetting = findViewById(R.id.numSetting4);
        handSetting = findViewById(R.id.numSetting5);
        btnFoot = findViewById(R.id.btnSetting1);
        btnShoulder = findViewById(R.id.btnSetting2);
        btnElbow = findViewById(R.id.btnSetting3);
        btnWrist = findViewById(R.id.btnSetting4);
        btnHand = findViewById(R.id.btnSetting5);
        btnSendAll = findViewById(R.id.btnSendAll);
        btnToggleArm = findViewById(R.id.btnToggleArm);
    }

    private void addSnapshotListeners() {
        DocumentReference rotationDocRef;
        if (btnToggleArm.isChecked()) {
            rotationDocRef = robo2DocRef;
        } else {
            rotationDocRef = robo1DocRef;
        }
        rotationDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(MainActivity.this, "listen failed", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    try {
                        footSetting.setText(snapshot.get("footVal").toString());
                        shoulderSetting.setText(snapshot.get("shoulderVal").toString());
                        elbowSetting.setText(snapshot.get("elbowVal").toString());
                        wristSetting.setText(snapshot.get("wristVal").toString());
                        handSetting.setText(snapshot.get("handVal").toString());
                    } catch (Exception ex) {
                        Toast.makeText(MainActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                        ClearFields();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "No data available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void ClearFields() {
        footSetting.setText("");
        shoulderSetting.setText("");
        elbowSetting.setText("");
        wristSetting.setText("");
        handSetting.setText("");
    }

    private void addListeners() {
        leftJoy.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                SendJoystickRotation(angle, strength, true);
            }
        },JOYSTICK_UPDATE_DELAY);
        rightJoy.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                SendJoystickRotation(angle, strength, false);
            }
        },JOYSTICK_UPDATE_DELAY);

        btnDelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateDelay();
            }
        });
        btnFoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsChanged(1);
            }
        });
        btnShoulder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsChanged(2);
            }
        });
        btnElbow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SettingsChanged(3);
            }
        });
        btnWrist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsChanged(4);
            }
        });
        btnHand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsChanged(5);
            }
        });
        btnToggleArm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSnapshotListeners();
            }
        });
        btnSendAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendAll();
            }
        });

    }

    private void UpdateDelay() {
        if(!delaySetting.getText().toString().equals("")) {
            int delay = Integer.parseInt(delaySetting.getText().toString());
            robo1SettingsDocRef.update("delayVal", delay).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(MainActivity.this, "Delay setting updated!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, getString(R.string.settingErrorBeforeException) + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
            Toast.makeText(MainActivity.this, getString(R.string.pleaseAddNumber), Toast.LENGTH_SHORT).show();
    }

    private void SendJoystickRotation(int angle, int strength, boolean DpadLeft) {
        if(angle < 40 || angle > 320)
        {
            if(DpadLeft)
            {
                SendRotation(strength, "footVal", footSetting, true);
            }
            else
            {
                SendRotation(strength, "wristVal", wristSetting, true);
            }
        }
        else if(angle > 50 && angle < 130)
        {
            if(DpadLeft)
            {
                SendRotation(strength, "elbowVal", elbowSetting, true);
            }
            else
            {
                SendRotation(strength, "shoulderVal", shoulderSetting, true);
            }
        }
        else if(angle > 140 && angle < 220)
        {
            if(DpadLeft)
            {
                SendRotation(strength, "footVal", footSetting, false);
            }
            else
            {
                SendRotation(strength, "wristVal", wristSetting, false);
            }

        }
        else if(angle > 230 && angle < 310)
        {
            if(DpadLeft)
            {
                SendRotation(strength, "elbowVal", elbowSetting, false);
            }
            else
                {
                    SendRotation(strength, "shoulderVal", shoulderSetting, false);
            }
        }
    }

    private void SendRotation(int strength, String joint, EditText field, boolean positive) {
        int rotation = CalculateRotation(Integer.parseInt(field.getText().toString()), strength, positive);
        field.setText(Integer.toString(rotation));
        robo1DocRef.update(joint, rotation )
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, getString(R.string.settingErrorBeforeException) + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private int CalculateRotation(int rotation, int strength, boolean increase) {
        int result;
            if(increase)
                result = rotation + (strength / STRENGTH_MULTIPLIER);
            else
                result = rotation - (strength / STRENGTH_MULTIPLIER);
        if(result > 180)
            result = 180;
        else if(result < 0)
            result = 0;
        return result;
    }

    private void SettingsChanged(final int setting) {
            String joint;
            int value;
            switch (setting) {
                case 1:
                    joint = "footVal";
                    if(!footSetting.getText().toString().equals("")) {
                        value = Integer.parseInt(footSetting.getText().toString());
                        break;
                    } else {
                        Toast.makeText(MainActivity.this, getString(R.string.pleaseAddNumber), Toast.LENGTH_SHORT).show();
                        return;
                    }
                case 2:
                    joint = "shoulderVal";
                    if(!shoulderSetting.getText().toString().equals("")) {
                        value = Integer.parseInt(shoulderSetting.getText().toString());
                        break;
                    } else {
                        Toast.makeText(MainActivity.this, getString(R.string.pleaseAddNumber), Toast.LENGTH_SHORT).show();
                        return;
                    }
                case 3:
                    joint = "elbowVal";
                    if(!elbowSetting.getText().toString().equals("")) {
                        value = Integer.parseInt(elbowSetting.getText().toString());
                        break;
                    } else {
                        Toast.makeText(MainActivity.this, getString(R.string.pleaseAddNumber), Toast.LENGTH_SHORT).show();
                        return;
                    }
                case 4:
                    joint = "wristVal";
                    if(!wristSetting.getText().toString().equals("")) {
                        value = Integer.parseInt(wristSetting.getText().toString());
                        break;
                    } else {
                        Toast.makeText(MainActivity.this, getString(R.string.pleaseAddNumber), Toast.LENGTH_SHORT).show();
                        return;
                    }
                case 5:
                    joint = "handVal";
                    if(!handSetting.getText().toString().equals("")) {
                        value = Integer.parseInt(handSetting.getText().toString());
                        break;
                    } else {
                        Toast.makeText(MainActivity.this, getString(R.string.pleaseAddNumber), Toast.LENGTH_SHORT).show();
                        return;
                    }
                default:
                    Toast.makeText(MainActivity.this, "Error updating settings", Toast.LENGTH_SHORT).show();
                    return;
            }
            DocumentReference docRef;
            if (btnToggleArm.isChecked()) {
                docRef = robo2DocRef;
            } else {
                docRef = robo1DocRef;
            }
            docRef
                    .update(joint, value)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this, getString(R.string.updatedSuccessfully), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, getString(R.string.settingErrorBeforeException) + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
         }

    private void SendAll() {
        DocumentReference docRef;
        if (btnToggleArm.isChecked()) {
            docRef = robo2DocRef;
        } else {
            docRef = robo1DocRef;
        }
        if(!footSetting.getText().toString().equals("") &&
                !shoulderSetting.getText().toString().equals("") &&
                !elbowSetting.getText().toString().equals("") &&
                !wristSetting.getText().toString().equals("") &&
                !handSetting.getText().toString().equals("")) {
            Map<String, Object> docData = new HashMap<>();
            docData.put("footVal", Integer.parseInt(footSetting.getText().toString()));
            docData.put("shoulderVal", Integer.parseInt(shoulderSetting.getText().toString()));
            docData.put("elbowVal", Integer.parseInt(elbowSetting.getText().toString()));
            docData.put("wristVal", Integer.parseInt(wristSetting.getText().toString()));
            docData.put("handVal", Integer.parseInt(handSetting.getText().toString()));

            docRef.set(docData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(MainActivity.this, getString(R.string.allSettingsUpdated), Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, getString(R.string.settingErrorBeforeException) + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else{
            Toast.makeText(MainActivity.this, getString(R.string.putNumberInAllFields), Toast.LENGTH_SHORT).show();
        }
    }
}