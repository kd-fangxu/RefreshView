package net.oilchem.www.refreshview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.ybq.android.spinkit.SpinKitView;

public class ProTestAct extends AppCompatActivity {

    protected SpinKitView spinKit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_pro_test);
        initView();
    }

    private void initView() {
        spinKit = (SpinKitView) findViewById(R.id.spin_kit);
        spinKit.setActivated(false);
//        spinKit.set
    }
}
