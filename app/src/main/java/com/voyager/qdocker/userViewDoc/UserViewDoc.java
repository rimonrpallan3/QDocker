package com.voyager.qdocker.userViewDoc;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.voyager.qdocker.R;
import com.voyager.qdocker.SignInPage.model.UserDetails;
import com.voyager.qdocker.UserLanding.UserLanding;
import com.voyager.qdocker.UserQrCodeGenrater.UserQrCodeRenerate;
import com.voyager.qdocker.adminLanding.model.QrResult;
import com.voyager.qdocker.common.Config;
import com.voyager.qdocker.userAbout.UserAbout;
import com.voyager.qdocker.userViewDoc.adapter.ListFileAdapter;
import com.voyager.qdocker.userViewDoc.model.DocList;
import com.voyager.qdocker.userViewDoc.model.UploadDocs;
import com.voyager.qdocker.userViewDoc.presenter.IUserViewDocPresenter;
import com.voyager.qdocker.userViewDoc.presenter.UserViewDocPresenter;
import com.voyager.qdocker.userViewDoc.view.IUserViewDocView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.models.sort.SortingTypes;
import droidninja.filepicker.utils.Orientation;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by User on 22-Mar-18.
 */

public class UserViewDoc extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, IUserViewDocView{
    UserDetails userDetails;
    Toolbar userViewDocToolbar;
    String fireBaseToken;

    public static final int RC_FILE_PICKER_PERM = 321;
    private int MAX_ATTACHMENT_COUNT = 20;
    private ArrayList<String> docPaths = new ArrayList<>();
    private ArrayList<String> photoPaths = new ArrayList<>();
    private ArrayList<DocList> docLists;
    private List<DocList> currentSelectedItems = new ArrayList<>();
    private List<String> fileDoneList;
    @BindView(R.id.recycleViewUserUploadDoc)
    RecyclerView recycleViewUserUploadDoc;
    @BindView(R.id.uploadLoader)
    FrameLayout uploadLoader;
    ListFileAdapter listFileAdapter;
    FirebaseStorage storage;
    StorageReference storageRef;
    DatabaseReference mDatabase;
    IUserViewDocPresenter iUserViewDocPresenter;
    String uploadId;
    QrResult qrResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_view_doc);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        userDetails = (UserDetails) intent.getParcelableExtra("UserDetails");
        if (userDetails != null) {
            System.out.println("UserViewDoc -- UserDetails- name : " + userDetails.getUserName());
        }
        qrResult = new QrResult();
        mDatabase = FirebaseDatabase.getInstance().getReference("user");
        qrResult.setRefDabaseRef("user");
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl(Config.FIREBASE_STORAGE_URL);
        fireBaseToken = FirebaseInstanceId.getInstance().getToken();
        userViewDocToolbar = (Toolbar) findViewById(R.id.userViewDocToolbar);
        setSupportActionBar(userViewDocToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        userViewDocToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
        fileDoneList = new ArrayList<>();
        iUserViewDocPresenter = new UserViewDocPresenter(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

                case FilePickerConst.REQUEST_CODE_DOC:
                    if (data != null) {
                        if (resultCode == Activity.RESULT_OK && data != null) {
                            docPaths = new ArrayList<>();
                            docLists = new ArrayList<>();
                            docPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
                            ArrayList<File> inFiles = new ArrayList<>();
                            File directory;
                            for (int i = 0; i < docPaths.size(); i++) {
                                directory = new File(docPaths.get(i).toString());
                                inFiles.add(directory);
                            }
                            for (File f : inFiles) {
                                DocList docList = new DocList();
                                docList.setSelectedListDoc(inFiles);
                                System.out.println("UserViewDoc onActivityResult for each Files name:  " + f.getName());
                                System.out.println("UserViewDoc onActivityResult for each absolute path:  " + f.getAbsolutePath());
                                docList.setDocFileName(f.getName());
                                docList.setDocFileAbsolutePath(f.getAbsolutePath());
                                docLists.add(docList);
                            }
                        }
                    }else {
                        System.out.println("UserViewDoc onActivityResult for each Files data: null ");
                    }
                    break;
        }
        addThemToView(docPaths,docLists);
    }

    private void addThemToView(ArrayList<String> docPaths,ArrayList<DocList> docLists) {
        ArrayList<String> filePaths = new ArrayList<>();
        if (docPaths != null)
            filePaths.addAll(docPaths);
        if (recycleViewUserUploadDoc != null) {
            if(docLists!=null && docLists.size()>0) {
                listFileAdapter = new ListFileAdapter(docLists, this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recycleViewUserUploadDoc.setLayoutManager(mLayoutManager);
                recycleViewUserUploadDoc.setItemAnimator(new DefaultItemAnimator());
                recycleViewUserUploadDoc.setAdapter(listFileAdapter);
                recycleViewUserUploadDoc.setItemAnimator(new DefaultItemAnimator());
            }
        }

        Toast.makeText(this, "Num of files selected: " + filePaths.size(), Toast.LENGTH_SHORT).show();
    }

    @Override public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
    }

    @Override public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_view_doc_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onPickDoc() {
        String[] doc = { ".doc", ".docx" };
        String[] pdfs = { ".pdf" };
        int maxCount = MAX_ATTACHMENT_COUNT - photoPaths.size();
        if ((docPaths.size() + photoPaths.size()) == MAX_ATTACHMENT_COUNT) {
            Toast.makeText(this, "Cannot select more than " + MAX_ATTACHMENT_COUNT + " items",
                    Toast.LENGTH_SHORT).show();
        } else {
            FilePickerBuilder.getInstance()
                    .setMaxCount(maxCount)
                    .setSelectedFiles(docPaths)
                    .setActivityTheme(R.style.FilePickerTheme)
                    .setActivityTitle("Please select doc")
                    .addFileSupport("DOC", doc)
                    .addFileSupport("PDF", pdfs, R.drawable.pdf_blue)
                    .enableDocSupport(false)
                    .sortDocumentsBy(SortingTypes.name)
                    .withOrientation(Orientation.UNSPECIFIED)
                    .pickFile(this);
        }
    }
    @OnClick(R.id.uploadDocToFireBase)
    public void uploadDocToFireBase(){
        System.out.println("Please uploadDocToFireBase in: ");
        if(currentSelectedItems!=null){
            mDatabase.child(userDetails.getUserId()).child("doc").removeValue();
            System.out.println("Please uploadDocToFireBase currentSelectedItems: "+currentSelectedItems.size());
            for(int i = 0; i < currentSelectedItems.size();i++){
                uploadLoader.setVisibility(View.VISIBLE);
                final DocList docList = currentSelectedItems.get(i);
                System.out.println("Please uploadDocToFireBase currentSelectedItems: "+currentSelectedItems.get(i));
                UploadTask uploadTask;
                Uri file = Uri.fromFile(new File(docList.getDocFileAbsolutePath()));
                StorageReference riversRef = storageRef.child(userDetails.getUserId()).child("doc").child(file.getLastPathSegment());
                uploadTask = riversRef.putFile(file);
                fileDoneList.add("uploading");
                final int finalI = i;
                // Register observers to listen for when the download is done or if it fails
                final int position = i;
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        System.out.println("UserViewDoc uploadDocToFireBase onFailure");
                        Snackbar.make(findViewById(android.R.id.content),getResources().getString(R.string.snackUploadUnSuccessMsg), Snackbar.LENGTH_SHORT).show();
                        fileDoneList.remove(finalI);
                        fileDoneList.add(finalI, "done");
                        iUserViewDocPresenter.setUplodLoder(fileDoneList, position);
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        UploadDocs uploadDocs = new UploadDocs(docList.getDocFileName(),taskSnapshot.getDownloadUrl().toString());
                        uploadId = mDatabase.push().getKey();
                        qrResult.setInnerChild("doc");
                        qrResult.setQrUserId(userDetails.getUserId());

                        mDatabase.child(userDetails.getUserId()).child("doc").child(uploadId).setValue(uploadDocs);
                        System.out.println("UserViewDoc uploadDocToFireBase onSuccess downloadUrl: "+downloadUrl);
                        fileDoneList.remove(finalI);
                        fileDoneList.add(finalI, "done");
                        iUserViewDocPresenter.setUplodLoder(fileDoneList, position);
                    }
                });
            }
        }else {
            System.out.println("Please Select any one ");
            Snackbar.make(findViewById(android.R.id.content),getResources().getString(R.string.snack_error_message_noList), Snackbar.LENGTH_LONG).show();

        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // If there was an upload in progress, get its reference and create a new StorageReference
        final String stringRef = savedInstanceState.getString("reference");
        if (stringRef == null) {
            return;
        }
        storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(stringRef);

        // Find all UploadTasks under this StorageReference (in this example, there should be one)
        List<UploadTask> tasks = storageRef.getActiveUploadTasks();
        if (tasks.size() > 0) {
            // Get the task monitoring the upload
            UploadTask task = tasks.get(0);

            // Add new listeners to the task using an Activity scope
            task.addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot state) {
                    handleSuccess(state); //call a user defined function to handle the event.
                }
            });
        }
    }

    public void handleSuccess(UploadTask.TaskSnapshot state){

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // If there's an upload in progress, save the reference so you can query it later
        if (storageRef != null) {
            outState.putString("user", storageRef.toString());
        }
    }

    @AfterPermissionGranted(RC_FILE_PICKER_PERM)
    public void pickDoc() {
        if (EasyPermissions.hasPermissions(this, FilePickerConst.PERMISSIONS_FILE_PICKER)) {
            onPickDoc();
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_doc_picker),
                    RC_FILE_PICKER_PERM, FilePickerConst.PERMISSIONS_FILE_PICKER);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.pickDocs:
                pickDoc();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void hideLoader(List<String> fileDoneList, int position) {
        String fileDone = fileDoneList.get(position);
        if(fileDone.equals("uploading")){
            uploadLoader.setVisibility(View.VISIBLE);
        } else if(fileDone.equals("done")){
            Snackbar.make(findViewById(android.R.id.content),getResources().getString(R.string.snackUploadSuccessMsg), Snackbar.LENGTH_SHORT).show();
            uploadLoader.setVisibility(View.GONE);
            String databseRef = "user/"+userDetails.getUserId()+"/doc/"+uploadId;
            iUserViewDocPresenter.generateQrCode(databseRef,userDetails);
        }
    }

    @Override
    public void generateQrCodeActivity(String databseRef, UserDetails userDetails) {
        System.out.println("UserViewDoc generateQrCodeActivity  userDetails: "+userDetails.getUserId());
        System.out.println("UserViewDoc generateQrCodeActivity  databseRef: "+databseRef);
        Intent intent;
        intent = new Intent(this, UserQrCodeRenerate.class);
        intent.putExtra("databseRef",databseRef);
        intent.putExtra("UserDetails", userDetails);
        intent.putExtra("QrResult", qrResult);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemCheck(DocList docCheckedLists) {
        currentSelectedItems.add(docCheckedLists);
    }

    @Override
    public void onItemUncheck(DocList docUnCheckedLists) {
        currentSelectedItems.remove(docUnCheckedLists);
    }
}