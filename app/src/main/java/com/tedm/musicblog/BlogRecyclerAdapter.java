package com.tedm.musicblog;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {

    public List<BlogPost> blog_list;
    public List<User> user_list;
    public Context context;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    String userImage;
    String userUsername;

    String blog_user_idc;
    String blog_image_url;
    String blog_desc;
    String blog_image_thumb;
    String blog_song_id;
    String blog_song_title;
    String blog_song_duration;
    String blog_song_link;


    public BlogRecyclerAdapter(List<BlogPost> blog_list, List<User> user_list){

        this.blog_list = blog_list;
        this.user_list = user_list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.blog_list_item, viewGroup, false);
        context = viewGroup.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        viewHolder.setIsRecyclable(false);

        final String blogPostId = blog_list.get(i).BlogPostId;

        final String currentUserId = firebaseAuth.getCurrentUser().getUid();

        String desc_data = blog_list.get(i).getDesc();
        viewHolder.setDescText(desc_data);

        final String image_url = blog_list.get(i).image_url;
        final String thumbUri = blog_list.get(i).image_thumb;
        if(image_url != null)
        viewHolder.setBlogImage(image_url,thumbUri);

        String blog_user_id = blog_list.get(i).getUser_id();

        if(blog_user_id.equals(currentUserId)){
            viewHolder.blogDeleteBtn.setEnabled(true);
            viewHolder.blogDeleteBtn.setVisibility(View.VISIBLE);
        }

        final String userName = user_list.get(i).getName();
        userImage = user_list.get(i).getImage();
        userUsername  = user_list.get(i).getName();
        String songTitle = blog_list.get(i).getSong_title();
        String songDuration = blog_list.get(i).getSong_duration();
        String songId = blog_list.get(i).getSong_id();
        String songLink = blog_list.get(i).getSong_link();

        blog_user_idc = blog_list.get(i).getUser_id();
        blog_image_url = blog_list.get(i).getImage_url();
        blog_desc = blog_list.get(i).getDesc();
        blog_image_thumb = blog_list.get(i).getImage_thumb();
        blog_song_id = blog_list.get(i).getSong_id();
        blog_song_title = blog_list.get(i).getSong_title();
        blog_song_duration = blog_list.get(i).getSong_duration();
        blog_song_link = blog_list.get(i).getSong_link();

        viewHolder.setUserData(userName,userImage);
        if(songLink != null) {
            viewHolder.setSongTitle(songTitle);
            viewHolder.setSongDuration(songDuration);
        }

        long millisecond = blog_list.get(i).getTimestamp().getTime();
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(millisecond)).toString();
        viewHolder.setTime(dateString);

        // Get Likes Count
        firebaseFirestore.collection("Posts/" + blogPostId + "/Likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if(!queryDocumentSnapshots.isEmpty()){

                    int count = queryDocumentSnapshots.size();

                    viewHolder.updateLikesCount(count);

                } else {

                    viewHolder.updateLikesCount(0);

                }

            }
        });

        // Get Likes
        firebaseFirestore.collection("Posts/" + blogPostId + "/Likes").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if(documentSnapshot.exists()){

                    viewHolder.blogLikeBtn.setImageDrawable(context.getDrawable(R.mipmap.action_like_accent));
                } else {
                    viewHolder.blogLikeBtn.setImageDrawable(context.getDrawable(R.mipmap.action_like_gray));
                }

            }
        });


        // Likes Feature
        viewHolder.blogLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseFirestore.collection("Posts/" + blogPostId + "/Likes").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(!task.getResult().exists()){

                            Map<String,Object> likesMap = new HashMap<>();
                            likesMap.put("timestamp", FieldValue.serverTimestamp());

                            firebaseFirestore.collection("Posts/" + blogPostId + "/Likes").document(currentUserId).set(likesMap);
                        } else {

                            firebaseFirestore.collection("Posts/" + blogPostId + "/Likes").document(currentUserId).delete();
                        }

                    }
                });

            }
        });

        viewHolder.blogCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commentIntent = new Intent(context,CommentsActivity.class);
                commentIntent.putExtra("blog_post_id",blogPostId);
                commentIntent.putExtra("blog_user_idc",userName);
                commentIntent.putExtra("blog_image_url", image_url);
                commentIntent.putExtra("blog_desc", blog_desc);
                commentIntent.putExtra("blog_image_thumb", userImage);
                commentIntent.putExtra("blog_song_id", blog_song_id);
                commentIntent.putExtra("blog_song_title", blog_song_title);
                commentIntent.putExtra("blog_song_duration", blog_song_duration);
                commentIntent.putExtra("blog_song_link", blog_song_link);
                context.startActivity(commentIntent);
            }
        });

        viewHolder.blogDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseFirestore.collection("Posts").document(blogPostId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        blog_list.remove(i);
                        user_list.remove(i);
                    }
                });

            }
        });




    }

    @Override
    public int getItemCount() {
        return blog_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView descView;
        private ImageView blogImageView;
        private TextView blogDate;

        private TextView blogUserName;
        private CircleImageView blogUserPicture;

        private ImageView blogLikeBtn;
        private TextView blogLikeCount;

        private ImageView blogCommentBtn;
        private Button blogDeleteBtn;

        private TextView songTitle;
        private TextView songDuration;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;

            blogLikeBtn = mView.findViewById(R.id.blog_like_btn);
            blogCommentBtn = mView.findViewById(R.id.blog_comment_icon);
            blogDeleteBtn = mView.findViewById(R.id.blog_delete_btn);
        }

        public void setDescText(String descText){

            descView = mView.findViewById(R.id.blog_desc);
            descView.setText(descText);

        }

        public void setBlogImage(String downloadUri, String thumbUri){

            blogImageView = mView.findViewById(R.id.blog_image);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.rectangle);

            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(downloadUri).thumbnail(
                    Glide.with(context).load(thumbUri)
            ).into(blogImageView);

        }

        public void setTime(String date){
            blogDate = mView.findViewById(R.id.blog_date);
            blogDate.setText(date);

        }

        public void setSongTitle(String title){
            songTitle = mView.findViewById(R.id.blog_list_item_songname);
            songTitle.setText("Uploaded track name: " + title);
        }

        public void setSongDuration(String duration){
            songDuration = mView.findViewById(R.id.blog_list_item_songduration);
            songDuration.setText("Uploaded track duration: " + duration);
        }

        public void setUserData(String name, String image){
            blogUserPicture = mView.findViewById(R.id.blog_user_image);
            blogUserName = mView.findViewById(R.id.blog_user_name);

            RequestOptions placeHolderOption = new RequestOptions();
            placeHolderOption.placeholder(R.drawable.ellipse);

            Glide.with(context).applyDefaultRequestOptions(placeHolderOption).load(image).into(blogUserPicture);
            blogUserName.setText(name);

        }

        public void updateLikesCount(int count){
            blogLikeCount = mView.findViewById(R.id.blog_like_count);
            blogLikeCount.setText(count + " Likes");
        }


    }
}
