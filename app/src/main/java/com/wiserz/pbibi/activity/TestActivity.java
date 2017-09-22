package com.wiserz.pbibi.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.wiserz.pbibi.R;
import com.wiserz.pbibi.fragment.TestFragment1;
import com.wiserz.pbibi.fragment.TestFragment2;

public class TestActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        final TestFragment1 fragment1 = new TestFragment1();
        final TestFragment2 fragment2 = new TestFragment2();

        Button btnLoadFrag1 = (Button) findViewById(R.id.btn_show_fragment1);
        btnLoadFrag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(fragment1, "fragment1");
            }
        });

        Button btnLoagFrag2 = (Button) findViewById(R.id.btn_show_fragment2);
        btnLoagFrag2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(fragment2, "fragment2");
            }
        });

        findViewById(R.id.btn_hide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.hide(fragment2);
                transaction.show(fragment1);
                transaction.commit();
            }
        });
    }


    private void addFragment(Fragment fragment, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }
}
