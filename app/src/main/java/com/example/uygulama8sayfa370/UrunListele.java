package com.example.uygulama8sayfa370;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uygulama8sayfa370.databinding.ActivityUrunListeleBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class UrunListele extends AppCompatActivity {
    ActivityUrunListeleBinding binding;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    HashMap<String, Object> mData;
    DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUrunListeleBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();

        mData = new HashMap<>();
    }

    public void onClickSave(View view) {
        int urunAdeti = Integer.parseInt(binding.editTextNumberAdet.getText().toString());
        float urunFiyati = Float.parseFloat(binding.editTextNumberFiyat.getText().toString());
        String urunIsmi = binding.editTextUrunAdi.getText().toString();
        mData.put("urunAdeti", urunAdeti);
        mData.put("urunFiyati", urunFiyati);
        mData.put("urunAdi", urunIsmi);

        if (!TextUtils.isEmpty(urunIsmi)) {
            mFirestore.collection("urunler").document(mUser.getUid())
                    .set(mData)
                    .addOnCompleteListener(UrunListele.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                                Toast.makeText(UrunListele.this, "Kayıt işlemi başarılı", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(UrunListele.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Ürün ismi boş geçilemez.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickListe(View view) {
        docRef = mFirestore.collection("urunler").document(mUser.getUid());
        docRef.get()
                .addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            binding.textViewUrunAdedi.setText("Ürün Adedi:" + documentSnapshot.getData().get("urunAdeti"));
                            binding.textViewUrunFiyati.setText("Ürün Fiyatı:" + documentSnapshot.getData().get("urunFiyati"));
                            binding.textViewUrunIsmi.setText("Ürün Adı:" + documentSnapshot.getData().get("urunAdi"));
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UrunListele.this, "Hata oluştu" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void onClickSignOut(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent signOut = new Intent(this, MainActivity.class);
        startActivity(signOut);
    }

    public void onClickUpdate(View view) {
        int urunAdeti = Integer.parseInt(binding.editTextNumberAdet.getText().toString());
        float urunFiyati = Float.parseFloat(binding.editTextNumberFiyat.getText().toString());
        String urunIsmi = binding.editTextUrunAdi.getText().toString();
        mData.put("urunAdeti", urunAdeti);
        mData.put("urunFiyati", urunFiyati);
        mData.put("urunAdi", urunIsmi);
        mFirestore.collection("urunler").document(mUser.getUid())
                .update(mData)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UrunListele.this, "Veriler başarı ile güncellendi.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void onClickDelete(View view) {
        mFirestore.collection("urunler").document(mUser.getUid())
                .delete()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            Toast.makeText(UrunListele.this, "Data başarı ile silindi.", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(UrunListele.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}