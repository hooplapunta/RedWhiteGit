package me.redwhite.redwhite.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import me.redwhite.redwhite.R;
import me.redwhite.redwhite.models.Question;

/**
 * Created by t-rochew on 12/15/2014.
 */
public class ProfileQuestionListAdapter extends ArrayAdapter<Question> {

    private final Activity context;
    private final List<Question> questions;

    public ProfileQuestionListAdapter(Context context, List<Question> objects) {
        super(context, R.layout.list_single, objects);

        this.context = (Activity) context;
        this.questions = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_single, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        TextView txtName = (TextView) rowView.findViewById(R.id.txtName);
        ImageView imageView =(ImageView)rowView.findViewById(R.id.img);

        txtName.setText(questions.get(position).getQuestion());
        txtTitle.setText("#" +questions.get(position).getCreated_username());
        Picasso.with(context).load(questions.get(position).getImage_url()).into(imageView);

        return rowView;
    }
}
