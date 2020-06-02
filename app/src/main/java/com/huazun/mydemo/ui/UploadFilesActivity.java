package com.huazun.mydemo.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.huazun.mydemo.Globals;
import com.huazun.mydemo.R;
import com.huazun.mydemo.server.upload.ProgressRequestBody;
import com.huazun.mydemo.server.upload.UploadAPI;
import com.huazun.mydemo.server.upload.UploadFileResponse;
import com.huazun.mydemo.ui.adapter.FileExplorerRecyclerAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadFilesActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "UploadFilesActivity";
    @BindView(R.id.uploadFilesBackBtn) ImageView backBtn;
    @BindView(R.id.uploadFilesSettingsBtn) ImageView settingsBtn;
    @BindView(R.id.uploadFilesBTStatusImage) ImageView btStatusImage;
    @BindView(R.id.uploadFilesBTStatusText) TextView btStatusText;
    @BindView(R.id.uploadFilesProgressBar) ContentLoadingProgressBar progressBar;
    @BindView(R.id.uploadFilesComeDiskBtn)
    MaterialButton comeDiskBtn;
    @BindView(R.id.uploadFilesComeBTBtn)
    MaterialButton comeBTBtn;
    @BindView(R.id.uploadFilesUploadBtn)
    MaterialButton uploadBtn;
    @BindView(R.id.uploadFilesRecyclerView)
    RecyclerView recyclerView;
//    private ImageView backBtn, settingsBtn, btStatusImage;
//    private TextView btStatusText;
//    private ContentLoadingProgressBar progressBar ;
    private String uploadFolderPath = "";
    private File uploadFolder = null;
    private ArrayList<File> fileList = null;
    private FileExplorerRecyclerAdapter adapter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_files);
        ButterKnife.bind(this);

//        backBtn = (ImageView)findViewById(R.id.uploadFilesBackBtn);
//        settingsBtn = (ImageView)findViewById(R.id.uploadFilesSettingsBtn);
//        btStatusImage = (ImageView)findViewById(R.id.uploadFilesBTStatusImage);
//        btStatusText = (TextView)findViewById(R.id.uploadFilesBTStatusText);
//        progressBar = (ContentLoadingProgressBar)findViewById(R.id.uploadFilesProgressBar);


        initPage();

        backBtn.setOnClickListener(this);
        settingsBtn.setOnClickListener(this);
        btStatusText.setOnClickListener(this);
        comeDiskBtn.setOnClickListener(this);
        comeBTBtn.setOnClickListener(this);
        uploadBtn.setOnClickListener(this);
    }

    private void initPage(){
        uploadFolderPath = getExternalFilesDir(null).getPath() + File.separator + "upload" + File.separator;
        uploadFolder = new File(uploadFolderPath);
        if (!uploadFolder.exists()){
            uploadFolder.mkdir();
        }
        fileList = new ArrayList<File>();

        getFileList();

//        adapter = new FileExplorerRecyclerAdapter(this, fileList, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FileExplorerRecyclerAdapter(this, fileList, false);
        recyclerView.setAdapter(adapter);

    }

    private void getFileList(){
        fileList.clear();
        File[] files = uploadFolder.listFiles();
        for (int i=0;i<files.length;i++){
            File file = files[i];
            if (!file.isDirectory()){
                fileList.add(file);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.uploadFilesBackBtn:
                finish();
                break;
            case R.id.uploadFilesSettingsBtn:
                setServerUrl();
                break;
            case R.id.uploadFilesBTStatusText:
                btStatus();
                break;
            case R.id.uploadFilesComeDiskBtn:
                comeFromDisk();
                break;
            case R.id.uploadFilesComeBTBtn:
                comeFromBT();
                break;
            case R.id.uploadFilesUploadBtn:
                prepareUpload();
                break;
        }
    }

    private void setServerUrl(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.dialog_input_server, null);
        builder.setView(view);
        builder.setCancelable(false);
        final EditText serverEdit = (EditText) view.findViewById(R.id.inputServerEdit);
        serverEdit.setText(getServerUrl());
        MaterialButton okBtn =(MaterialButton) view.findViewById(R.id.inputServerOKBtn);
        MaterialButton cancelBtn =(MaterialButton) view.findViewById(R.id.inputServerCancelBtn);
        final AlertDialog dialog = builder.create();
        dialog.show();

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serverUrl = serverEdit.getText().toString();
                SharedPreferences sharedPreferences= getSharedPreferences(Globals.SERVER_URL, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Globals.SERVER_URL, serverUrl);
                editor.commit();
                dialog.cancel();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
    }

    private String getServerUrl() {
        SharedPreferences sharedPreferences= getSharedPreferences(Globals.SERVER_URL, Context.MODE_PRIVATE);
        String serverUrl = sharedPreferences.getString(Globals.SERVER_URL, "");
        return serverUrl;
    }

    private void btStatus(){
        final String[] items = {"Bt Server 1", "Bt Server 2", "Bt Server 3", "Bt Server 4", "Bt Server 5", "Bt Server 6"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher)
                .setTitle("Blue Tooth List")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        connectBluetooth(items[i]);

                    }
                });
        builder.create().show();
    }

    private void connectBluetooth(String btName){
        btStatusText.setText(btName);
        btStatusImage.setImageResource(R.mipmap.point_green);
    }

    private void comeFromDisk(){
        Intent intent = new Intent(UploadFilesActivity.this, FileExplorerActivity.class);
        startActivityForResult(intent, Globals.REQUEST_CODE);
    }

    private void comeFromBT(){

    }

    private void prepareUpload(){
        ArrayList<File> cloneFiles = (ArrayList<File>) fileList.clone();
        toUpload(cloneFiles);
    }



    private void toUpload(final ArrayList<File> cloneFiles){
        //https://cloud.tencent.com/developer/article/1176364
        //https://www.cnblogs.com/zhujiabin/p/7601658.html
        if (cloneFiles.size() == 0)
            return;
        final File uploadFile = cloneFiles.get(0);
        final String fileName = uploadFile.getName();
            progressBar.setVisibility(View.VISIBLE);
            ProgressRequestBody requestFile = new ProgressRequestBody(uploadFile, "application/octet-stream", new ProgressRequestBody.UploadCallbacks() {
                @Override
                public void onProgressUpdate(int percentage) {
                    Log.e(TAG, "onProgressUpdate: " + percentage);
                    progressBar.setProgress(percentage);
                }

                @Override
                public void onError() {
                }

                @Override
                public void onFinish() {
                }
            });
//        RequestBody requestBody =
//                RequestBody.create(MediaType.parse("application/otcet-stream"), file);
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("aFile", uploadFile.getName(), requestFile);
            String descriptionString = "This is a description";
            RequestBody description =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), descriptionString);

            UploadAPI.uploadFile(description, body, new Callback<UploadFileResponse>() {
                @Override
                public void onResponse(Call<UploadFileResponse> call, Response<UploadFileResponse> response) {
                    System.out.println("11111111111111============");
                    progressBar.setVisibility(View.GONE);
                    UploadFileResponse resp = response.body();
                    if (resp != null) {
                        System.out.println("2222222222222============");
                        if (uploadFile.delete()){
                            System.out.println("333333333333333============");
                            getFileList();
                            adapter.notifyDataSetChanged();
                            Toast.makeText(UploadFilesActivity.this, fileName + " uploaded.", Toast.LENGTH_SHORT).show();
                        } else {
                            System.out.println("4444444444444============");
                        }

                    } else {
                        System.out.println("555555555555============");
                    }
                    cloneFiles.remove(uploadFile);
                    toUpload(cloneFiles);
                }

                @Override
                public void onFailure(Call<UploadFileResponse> call, Throwable t) {
                    System.out.println("11111111111111---------------");
                    progressBar.setVisibility(View.GONE);
                    cloneFiles.remove(uploadFile);
                    toUpload(cloneFiles);
                    Toast.makeText(UploadFilesActivity.this, "图片上传失败，稍后重试", Toast.LENGTH_SHORT).show();
                }
            });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Globals.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String filePath = data.getStringExtra("filePath") ;
                copyFileToUploadFolder(filePath);
                getFileList();
                adapter.notifyDataSetChanged();
            }
        }

    }

    private void copyFileToUploadFolder(String filePath){
        File olderFile = new File(filePath);
        String fileName = olderFile.getName();
        File newFolder = new File(uploadFolderPath);
        copyFile(filePath, newFolder + File.separator + fileName);
    }

    public void copyFile(String oldPath, String newPath) {
        InputStream inStream = null;
        FileOutputStream fos = null;
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {
                inStream = new FileInputStream(oldPath);
                fos = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;
                    fos.write(buffer, 0, byteread);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inStream != null){
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

/*
    private void uploadPicture() {
        mFlCircleProgress.setVisibility(View.VISIBLE);
        File file = new File(mPicPath);
        //是否需要压缩
        //实现上传进度监听
        ProgressRequestBody requestFile = new ProgressRequestBody(file, "image/*", new ProgressRequestBody.UploadCallbacks() {
            @Override
            public void onProgressUpdate(int percentage) {
                Log.e(TAG, "onProgressUpdate: " + percentage);
                mCircleProgress.setProgress(percentage);
            }
            @Override
            public void onError() {
            }
            @Override
            public void onFinish() {
            }
        });
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        mApi.uploadFile(body).enqueue(new Callback<UploadVideoResp>() {
            @Override
            public void onResponse(Call<UploadVideoResp> call, Response<UploadVideoResp> response) {
                mFlCircleProgress.setVisibility(View.GONE);
                UploadVideoResp resp = response.body();
                if (resp != null) {
                    Toast.makeText(mContext, "图片上传成功！", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<UploadVideoResp> call, Throwable t) {
                mFlCircleProgress.setVisibility(View.GONE);
                Toast.makeText(mContext, "图片上传失败，稍后重试", Toast.LENGTH_SHORT).show();
            }
        });
    }*/
}
