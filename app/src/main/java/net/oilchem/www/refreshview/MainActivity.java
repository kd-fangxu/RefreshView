package net.oilchem.www.refreshview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    protected RefreshView refreshView;
    protected TextView tvTest1;
    protected TextView tvTest2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        initView();

    }

    private void initView() {
        refreshView = (RefreshView) findViewById(R.id.refreshView);

        refreshView.setRefreshListener(new RefreshView.onRefreshListener() {
            @Override
            public void onRefresh(RefreshView refreshView) {
                Log.e("Main", "OnRefresh");
            }
        });

        refreshView.setLoadListener(new RefreshView.onLoadListener() {
            @Override
            public void onLoad(RefreshView refreshView) {
                Log.e("Main", "OnLoad");
            }
        });
        tvTest1 = (TextView) findViewById(R.id.tv_test1);
        tvTest2 = (TextView) findViewById(R.id.tv_test2);


        tvTest1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshView.setRefreshing(true);
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshView.setRefreshing(false);
                    }
                },1000);
            }
        });

        tvTest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshView.setLoading(true);
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshView.setLoading(false);
                    }
                },1000);
            }
        });
    }
}
