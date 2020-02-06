package com.example.wallpaper.Utils;

import com.example.wallpaper.R;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class Functions {

    public static void changeMainFragment(FragmentActivity fragmentActivity, Fragment fragment){
        fragmentActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, fragment)
                .commit();
    }
}
