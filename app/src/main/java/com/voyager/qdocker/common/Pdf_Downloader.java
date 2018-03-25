package com.voyager.qdocker.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.voyager.qdocker.R;

import java.io.File;

/**
 * Created by rimon on 26-03-2018.
 */

public class Pdf_Downloader {

    Activity context;
    ProgressDialog progressDialog, mProgressDialog;
    ProgressBar progressBar;

    public Pdf_Downloader(Activity context) {
        this.context = context;
    }

    /**
     * This method opens the previously downloaded church novena file in pdf application,
     * if the file is not present on the device, it is downloaded from the Firebase Storage,
     * followed by opening it in another pdf application.
     * After the file is downloaded, the corresponding download button icon of the novena file in the activity is
     * changed to completed.
     *
     * @param file_url          Firebase storage url of file to be downloaded
     * @param file_name         Name of the file to be downloaded from Firebase storage.
     * @param downloadButton    Reference of download button on the Activity whose icon has to be changed once the file is downloaded.
     */

    public void download_Pdf(String file_url, final String file_name, final ImageButton downloadButton){
        File faith_directory = new File(Environment.getExternalStorageDirectory() +  "/"+context.getResources().getString(R.string.app_name)+"/");
        if (faith_directory.exists() && faith_directory.isDirectory()){
            File QrFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/"+context.getResources().getString(R.string.app_name)+"/", file_name);
            if (QrFile.exists()){
                try{
                    if (file_name.endsWith("pdf")){
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(QrFile), "application/pdf");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        context.startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(QrFile), "image/*");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        context.startActivity(intent);
                    }


                }catch (ActivityNotFoundException anfe){
                    Snackbar.make(context.findViewById(android.R.id.content),
                            "No application found to open downloaded file.", Snackbar.LENGTH_SHORT).show();
                    anfe.printStackTrace();
                }
            }
            else{
                mProgressDialog = new ProgressDialog(context);
                mProgressDialog.setCancelable(false);
                mProgressDialog.setMessage("Starting download...");
                mProgressDialog.setProgressNumberFormat(null);
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMax(0);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.show();
                File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/"+context.getResources().getString(R.string.app_name)+"/");
                try{
                    if(dir.mkdir()) {
                        // System.out.println("Directory created");
                    } else {
                        // System.out.println("Directory is not created");
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                Ion.with(context)
                        .load(file_url)
                        .progressBar(progressBar)
                        .progressDialog(progressDialog)
                        .progress(new ProgressCallback() {
                            @Override
                            public void onProgress(final long downloaded, final long total) {
                                context.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressDialog.setMessage("Downloading...");
                                        mProgressDialog.setMax((int) total);
                                        mProgressDialog.setProgress((int) downloaded);
                                        //mProgressDialog.setMessage("Downloading "+String.valueOf(downloaded)+" / "+String.valueOf(total) + " Bytes");
                                    }
                                });
                            }
                        })
                        .write(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/"+context.getResources().getString(R.string.app_name)+"/"+file_name))
                        .setCallback(new FutureCallback<File>() {
                            @Override
                            public void onCompleted(Exception e, File file) {
                                Toast.makeText(context, "File Downloaded", Toast.LENGTH_SHORT).show();
                                if(file != null){
                                    try {
                                        if (mProgressDialog.isShowing()){
                                            mProgressDialog.dismiss();
                                        }
                                        downloadButton.setImageResource(R.drawable.novena_downloaded);
                                        if (file_name.endsWith("pdf")){
                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                            context.startActivity(intent);
                                        }
                                        else {
                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            intent.setDataAndType(Uri.fromFile(file), "image/*");
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                            context.startActivity(intent);
                                        }
                                        /*Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                        context.startActivity(intent);*/
                                    }
                                    catch (ActivityNotFoundException anfe){
                                        Snackbar.make(context.findViewById(android.R.id.content),
                                                "No application found to open downloaded file.", Snackbar.LENGTH_SHORT).show();
                                        anfe.printStackTrace();
                                    }
                                    catch (Exception e1) {
                                        e1.printStackTrace();
                                    }
                                }
                                // do stuff with the File or error

                            }
                        });
            }
        }
        else {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Starting download...");
            mProgressDialog.setProgressNumberFormat(null);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(0);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.show();
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/"+context.getResources().getString(R.string.app_name)+"/");
            try{
                if(dir.mkdir()) {
                    //System.out.println("Directory created");
                } else {
                    //System.out.println("Directory is not created");
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            Ion.with(context)
                    .load(file_url)
                    .progressBar(progressBar)
                    .progressDialog(progressDialog)
                    .progress(new ProgressCallback() {
                        @Override
                        public void onProgress(final long downloaded, final long total) {
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressDialog.setMessage("Downloading...");
                                    mProgressDialog.setMax((int) total);
                                    mProgressDialog.setProgress((int) downloaded);
                                    //mProgressDialog.setMessage("Downloading "+String.valueOf(downloaded)+" / "+String.valueOf(total) + " Bytes");
                                }
                            });
                        }
                    })
                    .write(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/"+context.getResources().getString(R.string.app_name)+"/"+file_name))
                    .setCallback(new FutureCallback<File>() {
                        @Override
                        public void onCompleted(Exception e, File file) {
                            Toast.makeText(context, "File Downloaded", Toast.LENGTH_SHORT).show();
                            if(file != null){
                                try {
                                    if (mProgressDialog.isShowing()){
                                        mProgressDialog.dismiss();
                                    }
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    context.startActivity(intent);
                                } catch (Exception e1) {
                                    e1.printStackTrace();

                                }
                            }
                            // do stuff with the File or error

                        }
                    });
        }

    }
}
