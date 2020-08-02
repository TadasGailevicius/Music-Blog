package com.tedm.musicblog;

import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskListActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar taskToolbar;

    private EditText task_field;
    private TextView task_user_field;
    private ImageView task_post_btn;

    private RecyclerView task_list;
    private TaskListRecyclerAdapter taskRecyclerAdapter;
    private List<TaskList> taskList;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private String blog_post_id;
    private String current_user_id;

    private FloatingActionButton addPostBtn;


    String blog_user_idc;
    String blog_image_url;
    String blog_desc;
    String blog_image_thumb;
    String blog_song_id;
    String blog_song_title;
    String blog_song_duration;
    String blog_song_link;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        //setSupportActionBar(taskToolbar);
        //getSupportActionBar().setTitle("Tasks");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        current_user_id = firebaseAuth.getCurrentUser().getUid().toString();
        blog_post_id = getIntent().getStringExtra("blog_post_id");

        task_field = findViewById(R.id.task_field);
        task_user_field = findViewById(R.id.task_username);
        task_post_btn = findViewById(R.id.task_post_btn);
        task_list = findViewById(R.id.task_list);

        task_post_btn.setOnClickListener(this);

        //RecyclerView Firebase List
        taskList = new ArrayList<>();
        taskRecyclerAdapter = new TaskListRecyclerAdapter(taskList);
        task_list.setHasFixedSize(true);
        task_list.setLayoutManager(new LinearLayoutManager(this));
        task_list.setAdapter(taskRecyclerAdapter);

        addPostBtn = findViewById(R.id.add_post_btn);
        //CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams)addPostBtn.getLayoutParams();
        //p.setAnchorId(View.NO_ID);
        //addPostBtn.setLayoutParams(p);
       // addPostBtn.setVisibility(View.GONE);


        firebaseFirestore.collection("Users/" + firebaseAuth.getCurrentUser().getUid() + "/Tasks")
                .addSnapshotListener(TaskListActivity.this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                        if (!documentSnapshots.isEmpty()) {

                            for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                                if (doc.getType() == DocumentChange.Type.ADDED) {

                                    String taskId = doc.getDocument().getId();
                                    TaskList taskListt = doc.getDocument().toObject(TaskList.class);
                                    taskList.add(taskListt);
                                    taskRecyclerAdapter.notifyDataSetChanged();


                                }
                            }

                        }

                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.task_post_btn:

                Toast.makeText(TaskListActivity.this, "HELP ME: ", Toast.LENGTH_LONG).show();

                String task_message = task_field.getText().toString();


                Map<String, Object> tasksMap = new HashMap<>();
                tasksMap.put("task", task_message);
                tasksMap.put("user_id", current_user_id);
                tasksMap.put("taskType", "Mastering");
                tasksMap.put("timestamp", FieldValue.serverTimestamp());
                tasksMap.put("percentage", 0);

                firebaseFirestore.collection("Posts/" + blog_post_id + "/Tasks").add(tasksMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {

                        if(!task.isSuccessful()){

                            Toast.makeText(TaskListActivity.this, "Error Posting Comment : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        } else {

                            task_field.setText("");

                        }

                    }
                });

                break;
        }
    }
}
