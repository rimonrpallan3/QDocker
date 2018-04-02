package com.voyager.qdocker.adminAddDetails;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.voyager.qdocker.adminAddDetails.presenter.IAdminAddPresenter;
import com.voyager.qdocker.adminAddDetails.view.IAdminAddView;
import com.voyager.qdocker.adminLanding.AdminLanding;
import com.voyager.qdocker.adminAddDetails.presenter.AdminAddPresenter;
import com.voyager.qdocker.R;
import com.voyager.qdocker.SignInPage.model.AdminDetails;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 20-Mar-18.
 */

public class AdminAddAddDetails extends AppCompatActivity implements View.OnClickListener, IAdminAddView {

    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    IAdminAddPresenter iAdminAddPresenter;
    @BindView(R.id.adminRegNo)
    EditText adminRegNo;
    @BindView(R.id.adminQrgType)
    EditText adminQrgType;
    AdminDetails adminDetails;
    String edAdminRegNo ="";
    String edAdminQrgType ="";
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_details_layout);
        ButterKnife.bind(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sharedPrefs = getSharedPreferences(getResources().getString(R.string.sharedPrefFileAdmin),
                Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
        Intent intent = getIntent();
        adminDetails = (AdminDetails) intent.getParcelableExtra("AdminDetails");
        iAdminAddPresenter = new AdminAddPresenter(this);
    }

    public void btnSubmit(View v){
        btnSubmit.setEnabled(false);
        edAdminRegNo = adminRegNo.getText().toString();
        edAdminQrgType = adminQrgType.getText().toString();
        if(edAdminRegNo !=null && edAdminRegNo.length()>0 && adminQrgType !=null && adminQrgType.length()>0){
        //iAdminAddPresenter.storeMoveToLanding(adminRegNo.getText().toString(), adminQrgType.getText().toString(),adminDetails);
            if (adminDetails != null) {
                adminDetails.setAdminRegNo(edAdminRegNo);
                adminDetails.setAdminQrgType(edAdminQrgType);
                storeValuePref(adminDetails);
                mDatabase.child("admin").child(adminDetails.getUserId()).setValue(adminDetails);
                Intent intent = new Intent(this, AdminLanding.class);
                intent.putExtra("AdminDetails", adminDetails);
                startActivity(intent);
                finish();
            }
        }else {
            adminRegNo.setText("");
            adminQrgType.setText("");
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
            System.out.println("-----------AdminAddAddDetails storeValuePref "+jsonString);
        }
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
    }
}