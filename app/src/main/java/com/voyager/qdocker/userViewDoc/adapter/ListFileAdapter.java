package com.voyager.qdocker.userViewDoc.adapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.voyager.qdocker.R;
import com.voyager.qdocker.userViewDoc.model.DocList;
import com.voyager.qdocker.userViewDoc.view.IUserViewDocView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rimon on 24-03-2018.
 */

public class ListFileAdapter extends RecyclerView.Adapter<ListFileAdapter.MyViewHolder> {

    private ArrayList<DocList> docLists;
    Activity activity;
    String time;
    String date;
    int userID;
    IUserViewDocView iUserViewDocView;

    public ListFileAdapter(ArrayList<DocList> docLists,IUserViewDocView iUserViewDocView) {
        this.docLists = docLists;
        this.iUserViewDocView = iUserViewDocView;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_file_upload, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final DocList docList = docLists.get(position);
        System.out.println(" have entered onBindViewHolder : "+ position);
        System.out.println("ListFileAdapter onBindViewHolder DocList file name: "+ docList.getDocFileName());
        System.out.println("ListFileAdapter onBindViewHolder DocList file path: "+ docList.getDocFileAbsolutePath());
        holder.contentFileName.setText(docList.getDocFileName());
        holder.checkBoxContentFileUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //holder.checkBoxContentFileUpload.setChecked(!holder.checkBoxContentFileUpload.isChecked());
                if ( holder.checkBoxContentFileUpload.isChecked()) {
                    iUserViewDocView.onItemCheck(docList);
                } else {
                    iUserViewDocView.onItemUncheck(docList);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return docLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.checkBoxContentFileUpload)
        public CheckBox checkBoxContentFileUpload;
        @BindView(R.id.contentFileName)
        public TextView contentFileName;
        @BindView(R.id.contentFileUploadBtn)
        public LinearLayout contentFileUploadBtn;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
