package com.example.firebase;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreatePetFragment extends DialogFragment {
    Button btn_add;
    EditText name,age,color;
    private FirebaseFirestore mfirestore;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_pet, container, false);
        mfirestore = FirebaseFirestore.getInstance();

        name = v.findViewById(R.id.nombre);
        age = v.findViewById(R.id.edad);
        color = v.findViewById(R.id.color);
        btn_add = v.findViewById(R.id.btn_add);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namepet = name.getText().toString().trim();
                String agepet = age.getText().toString().trim();
                String colorpet = color.getText().toString().trim();
                if (namepet.isEmpty() && agepet.isEmpty() && colorpet.isEmpty()){
                    System.out.println("entre");
                    Context context = getContext();
                    CharSequence text = "Ingresar los datos";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                }else {
                    postPet(namepet,agepet,colorpet);

                }
            }
        });

        return  v;
    }

    private void postPet(String namepet, String agepet, String colorpet) {
        Map<String,Object> map = new HashMap<>();
        map.put("name", namepet);
        map.put("age", agepet);
        map.put("color", colorpet);
        mfirestore.collection("pet").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Context context = getContext();
                CharSequence text = "Creado exitosamente";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                getDialog().dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Context context = getContext();
                CharSequence text = "Error al ingresar";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }
}