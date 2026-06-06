package com.example.slingkong02;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.UserViewHolder> {

    //this context we will use to inflate the layout
    private Context context;

    //storing all the records in a list
    private List<Record> recordsList;

    public RecordAdapter(Context context, List<Record> recordsList) {
        this.context = context;
        this.recordsList = recordsList;
    }


    //runs automatically when the RecyclerView needs to display a new item/This runs only when a new row needs to be created.
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) //builds an empty row
    {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_layout, null); // The 'null' parameter means it's not attached to the parent yet; RecyclerView handles that.
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) { //fills a row with data
        // reads eachtime from the  arraylist object and writes to the listview Item

        //getting the record of the specified position
        Record record = recordsList.get(position);


        holder.tvName.setText(record.getName());

        // convert int to String by:  ""+int
        holder.tvRecord.setText(""+record.getScore()); //setText accepts only strings

    }

    @Override
    public int getItemCount() {
        return recordsList.size();
    }


    public class UserViewHolder extends RecyclerView.ViewHolder{ //container that holds references to the views inside one row
        TextView tvName, tvRecord;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView); //the whole row layout (which is the inflated custom_layout)

            //initializing our views with their ids
            tvName = itemView.findViewById(R.id.tvName);
            tvRecord = itemView.findViewById(R.id.tvScore);
        }
    }
}
