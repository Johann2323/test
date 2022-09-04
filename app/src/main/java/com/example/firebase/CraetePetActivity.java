package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CraetePetActivity extends AppCompatActivity {
Button btn_add;
EditText name,age,color;
private FirebaseFirestore mfirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_craete_pet);
        this.setTitle("Crear Mascota");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mfirestore = FirebaseFirestore.getInstance();

        name = findViewById(R.id.nombre);
        age = findViewById(R.id.edad);
        color = findViewById(R.id.color);
        btn_add = findViewById(R.id.btn_add);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namepet = name.getText().toString().trim();
                String agepet = age.getText().toString().trim();
                String colorpet = color.getText().toString().trim();
                if (namepet.isEmpty() && agepet.isEmpty() && colorpet.isEmpty()){
                    System.out.println("entre");
                    Context context = CraetePetActivity.this;
                    CharSequence text = "Ingresar los datos";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                }else {
                    postPet(namepet,agepet,colorpet);

                }
            }
        });
    }

    private void postPet(String namepet, String agepet, String colorpet) {
        Map<String,Object> map = new HashMap<>();
        map.put("name", namepet);
        map.put("age", agepet);
        map.put("color", colorpet);
mfirestore.collection("pet").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
    @Override
    public void onSuccess(DocumentReference documentReference) {
        Context context = CraetePetActivity.this;
        CharSequence text = "Creado exitosamente";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    finish();
    }
}).addOnFailureListener(new OnFailureListener() {
    @Override
    public void onFailure(@NonNull Exception e) {
        Toast.makeText(getApplicationContext(),"Error al ingresar",Toast.LENGTH_SHORT);
    }
});
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}