package com.voyager.qdocker.UserQrCodeGenrater;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.voyager.qdocker.R;
import com.voyager.qdocker.SignInPage.model.UserDetails;
import com.voyager.qdocker.adminLanding.model.QrResult;
import com.voyager.qdocker.custom.qrmodule.encoding.QrGenerator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rimon on 25-03-2018.
 */

public class UserQrCodeRenerate extends AppCompatActivity {

    public Toolbar userQrToolbar;
    UserDetails userDetails;
    String databseRef="";
    @BindView(R.id.imgQrGenerated)
    ImageView imgQrGenerated;
    Bundle bundle;
    QrResult qrResult;
    String json ="";
    private ErrorCorrectionLevel mEcc = ErrorCorrectionLevel.L;
    private PorterDuff.Mode mXfermode = PorterDuff.Mode.SRC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_qr_generator);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        bundle = new Bundle();
        userDetails = (UserDetails) intent.getParcelableExtra("UserDetails");
        qrResult = (QrResult) intent.getParcelableExtra("QrResult");
        databseRef= intent.getStringExtra("databseRef");
        System.out.println("UserQrCodeRenerate -- databseRef- name : " + databseRef);
        if (userDetails != null) {
            System.out.println("UserQrCodeRenerate -- UserDetails- name : " + userDetails.getUserName());
            System.out.println("UserQrCodeRenerate -- databseRef- name : " + databseRef);
        }
        if(qrResult != null){
            Gson gson = new Gson();
            json = gson.toJson(qrResult);
        }

        userQrToolbar = (Toolbar) findViewById(R.id.userQrToolbar);
        setSupportActionBar(userQrToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        userQrToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                generateQrCode();
            }
        }, 1000);

    }

    public void generateQrCode(){
        if(databseRef!=null&&databseRef.length()>0) {
            Bitmap qrCode = null;
            try {
                qrCode = new QrGenerator.Builder()
                        .content(json)
                        .qrSize(500)
                        .margin(2)
                        .color(Color.BLACK)
                        .bgColor(Color.WHITE)
                        .ecc(ErrorCorrectionLevel.H)
                        .overlay(this, R.mipmap.ic_launcher)
                        .overlaySize(100)
                        .overlayAlpha(255)
                        .overlayXfermode(PorterDuff.Mode.SRC_ATOP)
                        .encode();
            } catch (WriterException e) {
                e.printStackTrace();
            }

            imgQrGenerated.setImageBitmap(qrCode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}