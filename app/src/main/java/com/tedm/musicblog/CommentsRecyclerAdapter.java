package com.tedm.musicblog;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.ViewHolder> {

    public List<Comments> commentsList;
    public List<User> usersList;

    public Context context;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    String current_user_id;
    String userImage;

    String blog_post_id;
    String blog_user_idc;
    String blog_image_url;
    String blog_desc;
    String blog_image_thumb;
    String blog_song_id;
    String blog_song_title;
    String blog_song_duration;
    String blog_song_link;


    //private FirebaseFirestore firebaseFirestore;
    //private FirebaseAuth firebaseAuth;

    public CommentsRecyclerAdapter(List<Comments> commentsList, List<User> usersList, String blog_post_id, String blog_user_idc, String blog_image_url, String blog_desc, String blog_image_thumb, String blog_song_id, String blog_song_title, String blog_song_duration, String blog_song_link) {
        this.commentsList = commentsList;
        this.usersList = usersList;
        this.blog_post_id=blog_post_id;
        this.blog_user_idc = blog_user_idc;
        this.blog_image_url = blog_image_url;
        this.blog_desc = blog_desc;
        this.blog_image_thumb = blog_image_thumb;
        this.blog_song_id = blog_song_id;
        this.blog_song_title = blog_song_title;
        this.blog_song_duration = blog_song_duration;
        this.blog_song_link = blog_song_link;

    }

    @Override
    public CommentsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_item, parent, false);
        context = parent.getContext();
        return new CommentsRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CommentsRecyclerAdapter.ViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        current_user_id = firebaseAuth.getCurrentUser().getUid();

        final String commentUserName = commentsList.get(position).getUser_id();
        final String commentMessage = commentsList.get(position).getMessage();
        userImage = commentsList.get(position).getUserimage();
        holder.setComment_UserName(commentUserName);
        holder.setComment_message(commentMessage);
        holder.setUser_image(userImage);

        holder.AddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Object> tasksMap = new HashMap<>();
                tasksMap.put("user_image", userImage);
                tasksMap.put("blog_post_id", blog_post_id);
                tasksMap.put("blog_user_id", blog_user_idc);
                tasksMap.put("blog_image_url", blog_image_url);
                tasksMap.put("blog_desc", blog_desc);
                tasksMap.put("blog_image_thumb", blog_image_thumb);
                tasksMap.put("blog_song_id", blog_song_id);
                tasksMap.put("blog_song_title", blog_song_title);
                tasksMap.put("blog_song_duration", blog_song_duration);
                tasksMap.put("blog_song_link", blog_song_link);
                tasksMap.put("task_user", commentUserName);
                tasksMap.put("task", commentMessage);
                tasksMap.put("user_id", current_user_id);
                tasksMap.put("task_type", "random");
                tasksMap.put("percentage", 0);

                firebaseFirestore.collection("Users/" + current_user_id + "/Tasks").add(tasksMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {

                        if(!task.isSuccessful()){

                        } else {


                        }

                    }
                });
            }
        });

    }


    @Override
    public int getItemCount() {

        if (commentsList != null) {

            return commentsList.size();

        } else {

            return 0;

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView commentUserName;
        private TextView comment_message;

        private CircleImageView blogUserPicture;

        private Button AddTask;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            AddTask = mView.findViewById(R.id.comment_add_task);
        }

        public void setComment_UserName(String user) {

            commentUserName = mView.findViewById(R.id.comment_username);
            commentUserName.setText(user);
        }

        public void setComment_message(String message) {

            comment_message = mView.findViewById(R.id.comment_message);
            comment_message.setText(message);
        }

        public void setUser_image(String image) {

            blogUserPicture = mView.findViewById(R.id.comment_image);

            RequestOptions placeHolderOption = new RequestOptions();
            placeHolderOption.placeholder(R.drawable.ellipse);

            Glide.with(context).applyDefaultRequestOptions(placeHolderOption).load(image).into(blogUserPicture);
        }

    }
}
