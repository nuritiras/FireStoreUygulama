package com.example.uygulama8sayfa370;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.uygulama8sayfa370.databinding.ActivityUrunListeleBinding;

public class UrunListele extends AppCompatActivity {
    ActivityUrunListeleBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUrunListeleBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);

    }
}