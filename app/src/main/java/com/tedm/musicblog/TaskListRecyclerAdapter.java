package com.tedm.musicblog;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TaskListRecyclerAdapter extends RecyclerView.Adapter<TaskListRecyclerAdapter.ViewHolder> {

    public List<TaskList> tasksList;
    public Context context;

    //private FirebaseFirestore firebaseFirestore;
    //private FirebaseAuth firebaseAuth;

    public TaskListRecyclerAdapter(List<TaskList> tasksList) {
        this.tasksList = tasksList;
    }

    @Override
    public TaskListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_item, parent, false);
        context = parent.getContext();
        return new TaskListRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TaskListRecyclerAdapter.ViewHolder holder, int position) {

        holder.setIsRecyclable(false);
        String userImage = tasksList.get(position).getUser_image();
        String taskUserName = tasksList.get(position).getTask_user();
        String taskMessage = tasksList.get(position).getTask();

        String task_blog_user_image = tasksList.get(position).getBlog_image_thumb();
        String task_blog_user_name = tasksList.get(position).getBlog_user_id();
        String task_blog_image = tasksList.get(position).getBlog_image_url();
        String task_blog_desc = tasksList.get(position).getBlog_desc();

        holder.setTask_UserName(taskUserName);
        holder.setTask_message(taskMessage);
        if(userImage != null) {
            holder.setTask_Image(userImage);
        }

        holder.set_task_blog_desc(task_blog_desc);
        if(task_blog_image != null) {
            holder.set_task_blog_image(task_blog_image);
        }
        if(task_blog_user_image != null) {
            holder.set_task_blog_user_image(task_blog_user_image);
        }
        holder.set_task_blog_user_name(task_blog_user_name);

    }


    @Override
    public int getItemCount() {

        if (tasksList != null) {

            return tasksList.size();

        } else {

            return 0;

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView taskUserName;
        private TextView task_message;

        private CircleImageView task_blog_user_image;
        private TextView task_blog_user_name;
        private ImageView task_blog_image;
        private TextView task_blog_desc;


        private CircleImageView taskUserPicture;
        private CircleImageView task_Blog_user_image;
        private ImageView task_Blog_image;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTask_UserName(String user) {

            taskUserName = mView.findViewById(R.id.task_username);
            taskUserName.setText(user);
        }

        public void setTask_message(String message) {

            task_message = mView.findViewById(R.id.task_message);
            task_message.setText(message);
        }

        public void set_task_blog_user_image(String image) {

            task_Blog_user_image = mView.findViewById(R.id.task_blog_user_image);

            RequestOptions placeHolderOption = new RequestOptions();
            placeHolderOption.placeholder(R.drawable.ellipse);

            Glide.with(context).applyDefaultRequestOptions(placeHolderOption).load(image).into(task_Blog_user_image);
        }

        public void set_task_blog_user_name(String message) {

            task_blog_user_name = mView.findViewById(R.id.task_blog_user_name);
            task_blog_user_name.setText(message);
        }

        public void set_task_blog_image(String image) {

            task_Blog_image = mView.findViewById(R.id.task_blog_image);

            RequestOptions placeHolderOption = new RequestOptions();
            placeHolderOption.placeholder(R.drawable.ellipse);

            Glide.with(context).applyDefaultRequestOptions(placeHolderOption).load(image).into(task_Blog_image);
        }

        public void set_task_blog_desc(String message) {

            task_blog_desc = mView.findViewById(R.id.task_blog_desc);
            task_blog_desc.setText(message);
        }

        public void setTask_Image(String image) {

            taskUserPicture = mView.findViewById(R.id.task_image);

            RequestOptions placeHolderOption = new RequestOptions();
            placeHolderOption.placeholder(R.drawable.ellipse);

            Glide.with(context).applyDefaultRequestOptions(placeHolderOption).load(image).into(taskUserPicture);
        }

    }
}
