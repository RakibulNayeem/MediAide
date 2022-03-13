package com.rakibulnayeem.mediaide.Doctor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerDoctorTabAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitle = new ArrayList<>();

    public ViewPagerDoctorTabAdapter(@NonNull FragmentManager fm) {

        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
//        if (position==0){
//            return new ChamberFragment();
//        }
//
//        else if(position == 1){
//            return new ExperienceFragment();
//        }
//        else { // position == 2
//            return new AboutDoctorFragment();
//        }


        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
        //no of tabs
    }


    @Override
    public CharSequence getPageTitle(int position) {

//        if (position==0){
//            return "Chambers";
//        }
//        else if(position == 1){
//            return "Experience";
//        }
//        else {
//            return "About";
//        }

        return fragmentTitle.get(position);

    }

    public void AddFragment(Fragment fragment, String title){
        fragmentList.add(fragment);
        fragmentTitle.add(title);
    }
}
