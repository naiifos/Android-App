package com.example.janazahapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class CommentsActivity extends AppCompatActivity {


    private RecyclerView CommentsList;
    private ImageButton PostCommentButton;
    private EditText CommentinputText;
    private String Post_Key,current_user_id;

    private DatabaseReference UsersRef, PostsRef;
    private FirebaseAuth mAuth;
    private Comments obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Intent intent  = getIntent();
        Post_Key = intent.getStringExtra("id");
        Log.d("postkey","postkey: "+Post_Key);

        final GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(CommentsActivity.this);
        mAuth = FirebaseAuth.getInstance();
        current_user_id=account.getId();

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Event").child(Post_Key).child("Comments");


        CommentsList = (RecyclerView) findViewById(R.id.comments_list);
        CommentsList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        CommentsList.setLayoutManager(linearLayoutManager);
        CommentinputText = (EditText)findViewById(R.id.commentInput);
        PostCommentButton = (ImageButton) findViewById(R.id.post_comment_btn);

        PostCommentButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Event").child(Post_Key).child("Comments");

                String userName = account.getDisplayName();
                            ValidateComment(userName);
                            reff.push().setValue(obj);
                            CommentinputText.setText("");

            }


        });



    }

    @Override

    protected void onStart(){

        super.onStart();
        FirebaseRecyclerOptions<Comments> options =
                new FirebaseRecyclerOptions.Builder<Comments>()
                        .setQuery(PostsRef, Comments.class).build();

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Comments,CommentsViewHolder>(options)
        {
            @NonNull
            @Override
            public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_comments_layout, parent, false);

                return new CommentsActivity.CommentsViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull CommentsViewHolder commentsViewHolder, int i, @NonNull Comments comments)
            {
              Log.d("users","username :"+comments.getUsername());
                commentsViewHolder.setComment(comments.getComment());
                commentsViewHolder.setDate(comments.getUsername()+ " Commented on "+comments.getDate());
                commentsViewHolder.setTime(comments.getTime());
            }

        };
        firebaseRecyclerAdapter.startListening();
        CommentsList.setAdapter(firebaseRecyclerAdapter);

    }


    public static  class  CommentsViewHolder extends RecyclerView.ViewHolder{


        View mView;


        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);

          mView = itemView;

        }

        public void setTime(String time) {

            TextView myTime = (TextView) mView.findViewById(R.id.comment_time);
            myTime.setText(" Time : " +time);
        }

        public void setDate(String date) {
            TextView myDate = (TextView) mView.findViewById(R.id.comment_date);
            myDate.setText(date);
        }

        public void setComment(String comment) {

            TextView myComment = (TextView) mView.findViewById(R.id.comment_text);
           myComment.setText(comment);
        }
    }

    private void ValidateComment(String userName) {

        String commentText = CommentinputText.getText().toString();

        if(TextUtils.isEmpty(commentText))
        {
        }else{

            Calendar calFordDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
            final  String saveCurrentDate = currentDate.format(calFordDate.getTime());

            Calendar calFordTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
            final  String saveCurrentTime = currentTime.format(calFordDate.getTime());

            obj = new Comments(commentText,saveCurrentDate,saveCurrentTime,userName);
            final String RandomKey = current_user_id + saveCurrentDate + saveCurrentTime;


        }
    }


}







