package com.voyager.qdocker.adminDataPage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.voyager.qdocker.adminLanding.AdminLanding;
import com.voyager.qdocker.adminDataPage.presenter.AdminPresenter;
import com.voyager.qdocker.adminDataPage.presenter.IAdminPresenter;
import com.voyager.qdocker.adminDataPage.view.IAdminView;
import com.voyager.qdocker.R;
import com.voyager.qdocker.SignInPage.model.AdminDetails;
import com.voyager.qdocker.custom.Helper;

/**
 * Created by User on 20-Mar-18.
 */

public class AdminDataPage extends AppCompatActivity implements View.OnClickListener, IAdminView {


    Button btnSubmit;
    IAdminPresenter iAdminPresenter;
    EditText adminId;
    EditText adminQrCode;
    AdminDetails adminDetails;
    String  edAdminId="";
    String  edAdminQrCode="";
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_details_layout);
        sharedPrefs = getSharedPreferences(Helper.UserDetails,
                Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
        Intent intent = getIntent();
        adminDetails = (AdminDetails) intent.getParcelableExtra("AdminDetails");
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        adminId = (EditText) findViewById(R.id.adminId);
        adminQrCode = (EditText) findViewById(R.id.adminQrCode);
        iAdminPresenter = new AdminPresenter(this);
    }

    public void btnSubmit(View v){
        btnSubmit.setEnabled(false);
        edAdminId = adminId.getText().toString();
        edAdminQrCode = adminQrCode.getText().toString();
        if(edAdminId!=null && edAdminId.length()>0 && adminQrCode!=null && adminQrCode.length()>0){
        //iAdminPresenter.storeMoveToLanding(adminId.getText().toString(), adminQrCode.getText().toString(),adminDetails);
            if (adminDetails != null) {
                adminDetails.setAdminId(edAdminId);
                adminDetails.setAdminQrCode(edAdminQrCode);
                storeValuePref(adminDetails);
                Intent intent = new Intent(this, AdminLanding.class);
                intent.putExtra("AdminDetails", adminDetails);
                startActivity(intent);
                finish();
            }
        }else {
            adminId.setText("");
            adminQrCode.setText("");
        }

    }

    public void storeValuePref(AdminDetails adminDetails) {
        this.adminDetails = adminDetails;
        Gson gson = new Gson();
        String jsonString = gson.toJson(adminDetails);
        //UserDetails user1 = gson.fromJson(jsonString,UserDetails.class);
        if(jsonString!=null) {
            editor.putString(getResources().getString(R.string.sharedPrefFileAdmin), jsonString);
            editor.commit();
            System.out.println("-----------AdminDataPage storeValuePref "+jsonString);
        }
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
    }
}