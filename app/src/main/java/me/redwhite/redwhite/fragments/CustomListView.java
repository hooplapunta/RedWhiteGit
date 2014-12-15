package me.redwhite.redwhite.fragments;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import me.redwhite.redwhite.R;


public class CustomListView extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] questions;
    private final Integer[] imageId;
    private final String[] username;


    public CustomListView(Activity context, String[] questions, Integer[] imageId, String[] username){

        super(context, R.layout.list_single,questions);
        this.context = context;
        this.questions=questions;
        this.imageId=imageId;
        this.username = username;

    }

    public View getView(int position, View view, ViewGroup parent){

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_single,null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        TextView txtName = (TextView) rowView.findViewById(R.id.txtName);
        ImageView imageView =(ImageView)rowView.findViewById(R.id.img);

        txtName.setText(username[position]);
        txtTitle.setText(questions[position]);
        imageView.setImageResource(imageId[position]);

        final String titleClicked = questions[position];

return rowView;




    }


}
