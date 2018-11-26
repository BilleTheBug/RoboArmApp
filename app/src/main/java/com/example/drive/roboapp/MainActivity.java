package com.example.drive.roboapp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText numSetting1;
    EditText numSetting2;
    EditText numSetting3;
    EditText numSetting4;
    EditText numSetting5;
    TextView txtRotation;
    Button btnSetting1;
    Button btnSetting2;
    Button btnSetting3;
    Button btnSetting4;
    Button btnSetting5;
    Button btnSendAll;
    ToggleButton btnToggleArm;
    DocumentReference robo1settingsDocRef;
    DocumentReference robo2settingsDocRef;
    DocumentReference robo1rotationDocRef;
    DocumentReference robo2rotationDocRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeComponents();
        addListeners();
        //Sørger for at fjerne fokus fra felter, når keyboardet fjernes. virker btw ikke
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        CollectionReference robo1ColRef = db.collection("RoboArm1");
        CollectionReference robo2ColRef = db.collection("RoboArm2");
        robo1settingsDocRef = robo1ColRef.document("settings");
        robo2settingsDocRef = robo2ColRef.document("settings");
        robo1rotationDocRef = robo1ColRef.document("rotation");
        robo2rotationDocRef = robo2ColRef.document("rotation");
        addSnapshotListeners();
    }

    private void initializeComponents() {
        numSetting1 = findViewById(R.id.numSetting1);
        numSetting2 = findViewById(R.id.numSetting2);
        numSetting3 = findViewById(R.id.numSetting3);
        numSetting4 = findViewById(R.id.numSetting4);
        numSetting5 = findViewById(R.id.numSetting5);
        btnSetting1 = findViewById(R.id.btnSetting1);
        btnSetting2 = findViewById(R.id.btnSetting2);
        btnSetting3 = findViewById(R.id.btnSetting3);
        btnSetting4 = findViewById(R.id.btnSetting4);
        btnSetting5 = findViewById(R.id.btnSetting5);
        btnSendAll = findViewById(R.id.btnSendAll);
        txtRotation = findViewById(R.id.txtRotation);
        btnToggleArm = findViewById(R.id.btnToggleArm);
    }

    private void addSnapshotListeners() {
        DocumentReference settingsDocRef;
        DocumentReference rotationDocRef;
        if(btnToggleArm.isChecked()) {
            settingsDocRef = robo2settingsDocRef;
            rotationDocRef = robo2rotationDocRef;
        }
        else
        {
            settingsDocRef = robo1settingsDocRef;
            rotationDocRef = robo1rotationDocRef;
        }
        settingsDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(MainActivity.this, "listen failed", Toast.LENGTH_SHORT).show();
                    return;
                }

               if (snapshot != null && snapshot.exists()) {
                    try {
                        numSetting1.setText(snapshot.get("setting1").toString());
                        numSetting2.setText(snapshot.get("setting2").toString());
                        numSetting3.setText(snapshot.get("setting3").toString());
                    }
                    catch (Exception ex)
                    {
                        Toast.makeText(MainActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "No data available", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                        txtRotation.setText(String.format("Rotation:\nx: %s, y: %s, z: %s",
                                snapshot.get("x").toString(),
                                snapshot.get("y").toString(),
                                snapshot.get("z").toString()));
                    }
                    catch (Exception ex)
                    {
                        Toast.makeText(MainActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "No data available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addListeners() {
        btnSetting1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsChanged(1, Integer.parseInt(numSetting1.getText().toString()));
            }
        });
        btnSetting2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsChanged(2, Integer.parseInt(numSetting2.getText().toString()));
            }
        });
        btnSetting3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsChanged(3, Integer.parseInt(numSetting3.getText().toString())); //husk at tjekke null
            }
        });
        btnSetting4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsChanged(4, Integer.parseInt(numSetting4.getText().toString()));
            }
        });
        btnSetting5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsChanged(5, Integer.parseInt(numSetting5.getText().toString()));
            }
        });
        btnToggleArm.setOnClickListener (new View.OnClickListener() {
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

    private void SettingsChanged(final int setting, int value) {
        if(value != -1)
        {
            DocumentReference docRef;
            if(btnToggleArm.isChecked()) {
                docRef = robo2settingsDocRef;
            }
            else
            {
                docRef = robo1settingsDocRef;
            }
            docRef
                    .update("setting" + setting, value)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this, "Setting " + setting + " updated!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else{
            Toast.makeText(MainActivity.this, "Value is null", Toast.LENGTH_SHORT).show();
        }
    }

    private void SendAll(){
        DocumentReference docRef = db.collection("test").document("rotation");
        Map<String, Object> docData = new HashMap<>();
        docData.put("footVal", Integer.parseInt(numSetting1.getText().toString()));
        docData.put("shoulderVal", Integer.parseInt(numSetting2.getText().toString()));
        docData.put("elbowVal", Integer.parseInt(numSetting3.getText().toString()));
        docData.put("wristVal", Integer.parseInt(numSetting4.getText().toString()));
        docData.put("handVal", Integer.parseInt(numSetting5.getText().toString()));
        docRef.set(docData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "All setting updated!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error updating settings: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
