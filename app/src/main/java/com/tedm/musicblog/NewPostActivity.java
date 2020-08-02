package com.tedm.musicblog;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.MimeTypeFilter;

import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.masoudss.lib.*;
import com.tedm.musicblog.ui.ChatMain;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import java.util.*;

import id.zelory.compressor.Compressor;

public class NewPostActivity extends AppCompatActivity{

    private String myUrl = "";
    private String myUrl1 = "";

    private Toolbar newPostToolbar;

    private ImageView newPostImage;
    private EditText newPostDesc;
    private EditText newPostSongTitle;

    private Button newPostBtn;

    private Button chatBtn;

    private Uri postImageUri = null;

    private ProgressBar newPostProgress;

    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private String current_user_id;

    private Bitmap compressedImageFile;

    // Uplaod music files to firebase

    TextView textViewImage;
    ProgressBar progressBar;

    Uri audioUri;

    StorageReference mStorageRef;
    StorageTask mUplaodTask;
    DatabaseReference referenceSongs;

    private String song_id = "";
    private String song_link = "";
    private String song_title = "";
    private String song_duration = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        current_user_id = firebaseAuth.getCurrentUser().getUid();

        newPostToolbar = findViewById(R.id.new_post_toolbar);
        setSupportActionBar(newPostToolbar);
        getSupportActionBar().setTitle("Add New Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        newPostImage = findViewById(R.id.new_post_image);
        newPostDesc = findViewById(R.id.new_post_desc);
        newPostSongTitle = findViewById(R.id.new_post_music_title);
        newPostBtn = findViewById(R.id.post_btn);
        newPostProgress = findViewById(R.id.new_post_progress);
        chatBtn = findViewById(R.id.btn_chat);

        // Uplaod music files to firebase

        textViewImage = findViewById(R.id.new_post_fileselection_tv);
        progressBar = findViewById(R.id.new_post_fileselection_pb);

        referenceSongs = FirebaseDatabase.getInstance().getReference().child("songs");
        mStorageRef = FirebaseStorage.getInstance().getReference().child("songs");


        /*
        WaveformSeekBar waveformSeekBar = new WaveformSeekBar(this);
        waveformSeekBar.setProgress(33);
        waveformSeekBar.setWaveWidth(Utils.INSTANCE.dp(this,5));
        waveformSeekBar.setWaveGap(Utils.INSTANCE.dp(this,2));
        waveformSeekBar.setWaveMinHeight(Utils.INSTANCE.dp(this,5));
        waveformSeekBar.setWaveCornerRadius(Utils.INSTANCE.dp(this,2));
        waveformSeekBar.setWaveGravity(WaveGravity.CENTER);
        waveformSeekBar.setWaveBackgroundColor(ContextCompat.getColor(this,R.color.white));
        waveformSeekBar.setWaveProgressColor(ContextCompat.getColor(this,R.color.colorPrimary));
        waveformSeekBar.setSample(file.);
        waveformSeekBar.setSampleFrom(AUDIO_FILE || AUDIO_PATH);
        */


        newPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(512, 512)
                        .setAspectRatio(1, 1)
                        .start(NewPostActivity.this);

            }
        });

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NewPostActivity.this, ChatMain.class);
                startActivity(i);
            }
        });

        newPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String desc = newPostDesc.getText().toString();

                if(!TextUtils.isEmpty(desc) && postImageUri != null){

                    newPostProgress.setVisibility(View.VISIBLE);

                    final String randomName = UUID.randomUUID().toString();

                    // PHOTO UPLOAD
                    File newImageFile = new File(postImageUri.getPath());
                    try {

                        compressedImageFile = new Compressor(NewPostActivity.this)
                                .setMaxHeight(720)
                                .setMaxWidth(720)
                                .setQuality(50)
                                .compressToBitmap(newImageFile);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageData = baos.toByteArray();


                    // PHOTO UPLOAD

                    final StorageReference storageReference1 = storageReference.child("post_images");
                    final StorageReference storageReference2 = storageReference1.child(randomName + ".jpg");

                    UploadTask uploadTask = storageReference2.putBytes(imageData);

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return storageReference2.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                final Uri downloadUri = task.getResult();
                                myUrl = downloadUri.toString();

                                File newThumbFile = new File(postImageUri.getPath());
                                try {

                                    compressedImageFile = new Compressor(NewPostActivity.this)
                                            .setMaxHeight(100)
                                            .setMaxWidth(100)
                                            .setQuality(1)
                                            .compressToBitmap(newThumbFile);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] thumbData = baos.toByteArray();

                                // Change Point

                                final StorageReference storageReference1 = storageReference.child("post_images/thumbs");
                                final StorageReference storageReference2 = storageReference1.child(randomName + ".jpg");

                                UploadTask uploadTask = storageReference2.putBytes(thumbData);

                                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                        if (!task.isSuccessful()) {
                                            throw task.getException();
                                        }

                                        // Continue with the task to get the download URL
                                        return storageReference2.getDownloadUrl();
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            final Uri downloadUri = task.getResult();
                                            myUrl1 = downloadUri.toString();

                                            Map<String, Object> postMap = new HashMap<>();
                                            postMap.put("image_url", myUrl);
                                            postMap.put("image_thumb", myUrl1);
                                            postMap.put("desc", desc);
                                            postMap.put("user_id", current_user_id);
                                            postMap.put("timestamp", FieldValue.serverTimestamp());
                                            postMap.put("song_id", song_id);
                                            postMap.put("song_link", song_link);
                                            postMap.put("song_title", song_title);
                                            postMap.put("song_duration", song_duration);

                                            firebaseFirestore.collection("Posts").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentReference> task) {

                                                    if(task.isSuccessful()){

                                                        Toast.makeText(NewPostActivity.this, "Post was added", Toast.LENGTH_LONG).show();
                                                        Intent mainIntent = new Intent(NewPostActivity.this, MainActivity.class);
                                                        startActivity(mainIntent);
                                                        finish();

                                                    } else {


                                                    }

                                                    newPostProgress.setVisibility(View.INVISIBLE);

                                                }
                                            });
                                        } else {
                                            newPostProgress.setVisibility(View.INVISIBLE);

                                        }
                                    }
                                });

                            } else {

                            }
                        }
                    });







                } else {
                    Toast.makeText(NewPostActivity.this,"Please input all data",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void openAudioFile(View view){
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("audio/*");
        startActivityForResult(i,101);
    }

    private String getFileName(Uri audioUri){
        String result = null;

        if(audioUri.getScheme().equals("content")){
            Cursor cursor = getContentResolver().query(audioUri, null, null, null, null);

            try {
                if(cursor != null && cursor.moveToFirst()){
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }

        if(result == null){
            result = audioUri.getPath();
            int cut = result.lastIndexOf('/');
            if(cut != -1){
                result = result.substring(cut + 1);
            }
        }

        return result;
    }

    public void uploadAudioToFirebase(View view){
        if(textViewImage.getText().toString().equals("No File selected")){
            Toast.makeText(getApplicationContext(), "Please select an audio", Toast.LENGTH_LONG).show();
        }

        else {
            if(mUplaodTask!= null && mUplaodTask.isInProgress()){
                Toast.makeText(getApplicationContext(),"Song upload is already in progress",Toast.LENGTH_LONG).show();
            }
            uploadFile();
        }
    }

    private String getDurationFromMillis(int durationInMillis){
        Date date = new Date(durationInMillis);
        SimpleDateFormat simple = new SimpleDateFormat("mm:ss", Locale.getDefault());
        String myTime = simple.format(date);
        return myTime;
    }

    private void uploadFile(){
        if(audioUri!= null){
            String durationTxt;
            Toast.makeText(getApplicationContext(), "Uploading please wait...", Toast.LENGTH_LONG).show();

            progressBar.setVisibility(View.VISIBLE);

            final StorageReference storageReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtention(audioUri));


            int durationInMillis =  findSongDuration(audioUri);

            if(durationInMillis == 0) {
                durationTxt = "NA";
            }

            durationTxt = getDurationFromMillis(durationInMillis);

            final String finalDurationTxt = durationTxt;
            mUplaodTask = storageReference.putFile(audioUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    UploadSong uploadSong = new UploadSong(newPostSongTitle.getText().toString(),
                                            finalDurationTxt,uri.toString());
                                    String uploadId = referenceSongs.push().getKey();
                                    referenceSongs.child(uploadId).setValue(uploadSong);

                                    song_id = uploadId;
                                    song_duration = uploadSong.getSongDuration();
                                    song_title = uploadSong.getSongTitle();
                                    song_link = uploadSong.getSongLink();
                                }
                            });



                            
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressBar.setProgress((int)progress);
                        }
                    });

        } else {
            Toast.makeText(getApplicationContext(), "No file selected to upload", Toast.LENGTH_LONG).show();
        }

    }

    private int findSongDuration(Uri audioUri){
        int timeInMillisec = 0;

        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(this,audioUri);
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            timeInMillisec = Integer.parseInt(time);

            retriever.release();
            return timeInMillisec;

        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }

    }

    public String getFileExtention(Uri audioUri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mine = MimeTypeMap.getSingleton();
        return mine.getExtensionFromMimeType(contentResolver.getType(audioUri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                postImageUri = result.getUri();
                newPostImage.setImageURI(postImageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }

        if(requestCode == 101 && resultCode == RESULT_OK && data.getData() != null){

            audioUri = data.getData();
            String fileName = getFileName(audioUri);

            textViewImage.setText(fileName);
        }
    }

    public void openSongsActivity(View view){
        Intent i = new Intent(NewPostActivity.this,ShowSongsActivity.class);
        startActivity(i);
    }




}