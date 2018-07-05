package com.qtec.homestay;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.qtec.homestay.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

//  private Activity mBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);


//    mBinding.btn2.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        System.out.println("v = " + v);
//
//      }
//    });
//
//    mBinding.tv2.setText("");


  }
}
