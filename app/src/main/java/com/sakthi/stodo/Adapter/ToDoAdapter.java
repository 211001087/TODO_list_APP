package com.sakthi.stodo.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.sakthi.stodo.AddNewTask;
import com.sakthi.stodo.MainActivity;
import com.sakthi.stodo.Model.ToDOModel;
import com.sakthi.stodo.R;

import java.util.List;

public class ToDoAdapter extends RecyclerView .Adapter<ToDoAdapter.MyViewHolder>{
    private List<ToDOModel> todoList;
    private MainActivity activity;
    private FirebaseFirestore firestore;

    public  ToDoAdapter(MainActivity mainActivity, List<ToDOModel> todoList)
    {
        this.todoList=todoList;
        activity =mainActivity;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.each_task , parent ,false);
        firestore =FirebaseFirestore.getInstance();
        return new MyViewHolder(view);

    }
    public void deleteTask(int position){
        ToDOModel toDOModel =todoList.get(position);
        firestore.collection("task").document(toDOModel.TaskId).delete();
        todoList.remove(position);
        notifyItemRemoved(position);
    }
    public Context getContext(){
     return  activity;
}

    public void editTask(int position) {
        ToDOModel toDOModel = todoList.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("task", toDOModel.getTask());
        bundle.putString("due", toDOModel.getDue());
        bundle.putString("id", toDOModel.TaskId);
        AddNewTask addNewTask = new AddNewTask();
        addNewTask.setArguments(bundle);
        addNewTask.show(activity.getSupportFragmentManager(), addNewTask.getTag());
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ToDOModel toDOModel = todoList.get(position);
        holder.mCheckBox.setText(toDOModel.getTask());
        holder.mDueDateTv.setText("Due On" + toDOModel.getDue());

        holder.mCheckBox.setChecked(toBoolean(toDOModel.getStatus()));

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                  firestore.collection("task").document(toDOModel.TaskId).update("status" ,1 );
                }else{
                    firestore.collection("task").document(toDOModel.TaskId).update("status",0);
                }
            }
        });
    }
    private boolean toBoolean(int status)
    {
        return  status!=0;
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder{
        TextView mDueDateTv;
        CheckBox mCheckBox;
        public MyViewHolder(@NonNull View itemView)
        {
          super(itemView);

          mDueDateTv = itemView.findViewById(R.id.due_date_tv);
          mCheckBox = itemView.findViewById(R.id.mcheckbox);
        }

     }
}
