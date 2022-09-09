package com.example.firebase.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebase.CraetePetActivity;
import com.example.firebase.CreatePetFragment;
import com.example.firebase.R;
import com.example.firebase.model.Pet;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class PetAdapter extends FirestoreRecyclerAdapter<Pet,PetAdapter.viewHolder> {
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    Activity activity;
    FragmentManager frag;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PetAdapter(@NonNull FirestoreRecyclerOptions<Pet> options, Activity activity, FragmentManager frag) {
        super(options);
        this.activity = activity;
        this.frag=frag;
    }

    @Override
    protected void onBindViewHolder(@NonNull viewHolder holder, int position, @NonNull Pet model) {
        DocumentSnapshot documentSn = getSnapshots().getSnapshot(holder.getAbsoluteAdapterPosition());
        final String id = documentSn.getId();

        holder.name.setText(model.getName());
        holder.age.setText(model.getAge());
        holder.color.setText(model.getColor());
        String photoPet = model.getPhoto();
        try {
            if (!photoPet.equals(""))
                Picasso.with(activity.getApplicationContext())
                        .load(photoPet)
                        .resize(150, 150)
                        .into(holder.photo_pet);
        }catch (Exception e){
            Log.d("Exception", "e: "+e);
        }
        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, CraetePetActivity.class);
                i.putExtra("id_pet", id);
                activity.startActivity(i);

                //Enviar datos por el fragmento
                /*CreatePetFragment createPetFragment = new CreatePetFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id_pet", id);
                createPetFragment.setArguments(bundle);
                createPetFragment.show(frag, "Fragmento Abierto");*/

            }
        });

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            deletedPet(id);
            }
        });
    }

    private void deletedPet(String id) {
        mFirestore.collection("pet").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(activity, "Eliminado Correctamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "Error al Eliminar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_pet_single, parent,false);
        return new viewHolder(v);
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView name,age,color;
        ImageView btn_delete;
        ImageView btn_edit, photo_pet;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nombre);
            age = itemView.findViewById(R.id.edad);
            color = itemView.findViewById(R.id.color);
            btn_delete = itemView.findViewById(R.id.btn_eliminar);
            btn_edit = itemView.findViewById(R.id.btn_editar);
            photo_pet = itemView.findViewById(R.id.photo);
        }
    }
}
