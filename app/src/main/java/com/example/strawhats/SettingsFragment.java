package com.example.strawhats;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings,container,false);
        Button Currency = view.findViewById(R.id.Change_Currency);
        Button PasswordChange = view. findViewById(R.id.Change_Password);
        Button ChangeDateFormat = view. findViewById(R.id.Change_Date_Formate);
        Button MethodOfPayment = view. findViewById(R.id.Method_of_Payment);

        Currency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),currency.class));
            }
        });

        PasswordChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),Password_Change.class));
            }
        });

        ChangeDateFormat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),Change_Date_Formate.class));
            }
        });

        MethodOfPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),Method_Of_Payment.class));
            }
        });




       return view;

    }
}