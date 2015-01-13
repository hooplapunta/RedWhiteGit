package me.redwhite.redwhite.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import me.redwhite.redwhite.R;
import me.redwhite.redwhite.models.Community;
import me.redwhite.redwhite.models.Question;

/**
 * Created by t-rochew on 12/15/2014.
 */
public class CommunityListAdapter extends ArrayAdapter<Community> {

    private final Activity context;
    private final List<Community> questions;

    public CommunityListAdapter(Context context, List<Community> objects) {
        super(context, R.layout.list_community, objects);

        this.context = (Activity) context;
        this.questions = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_browse_questions, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.tvShortName);
        TextView txtName = (TextView) rowView.findViewById(R.id.tvName);
        ImageView imageView =(ImageView)rowView.findViewById(R.id.img);

        txtName.setText(questions.get(position).getName());
        txtTitle.setText("#" +questions.get(position).getShortname());
        Picasso.with(context).load(questions.get(position).getImageurl()).into(imageView);

        return rowView;
    }
}
