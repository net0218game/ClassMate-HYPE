package com.hype.classmate.ui.settings;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hype.classmate.R;
import com.hype.classmate.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SettingsFragment extends Fragment {

    private SettingsViewModel mViewModel;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    TextView usernameTextView;


    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Button loginBtn = view.findViewById(R.id.loginButton);
        Button logoutBtn = view.findViewById(R.id.logoutButton);
        Switch nightmodeSwitch = view.findViewById(R.id.nightmodeSwitch);

        usernameTextView = view.findViewById(R.id.usernameTextView);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        if (user != null) {
            logoutBtn.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
            FirebaseDatabase.getInstance().getReference("Users/" + user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    usernameTextView.setText(Objects.requireNonNull(snapshot.child("name").getValue()).toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else {
            loginBtn.setVisibility(View.VISIBLE);
            logoutBtn.setVisibility(View.GONE);
        }

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                // Load Login Activity
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });


        /* nightmodeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    nightmodeSwitch.setText("Night Mode");
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    nightmodeSwitch.setText("Light Mode");
                }
            }
        });*/

        boolean isNightModeOn = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
        nightmodeSwitch.setChecked(isNightModeOn);
        if (isNightModeOn) {
            nightmodeSwitch.setText("Night Mode");
        } else {
            nightmodeSwitch.setText("Light Mode");
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        // TODO: Use the ViewModel

    }

}