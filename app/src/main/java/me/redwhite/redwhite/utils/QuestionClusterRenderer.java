package me.redwhite.redwhite.utils;

import android.content.Context;
import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import me.redwhite.redwhite.R;

/**
 * Created by Rong Kang on 2/7/2015.
 */
public class QuestionClusterRenderer extends DefaultClusterRenderer<QuestionMarker> {

    private final IconGenerator mIconGenerator;


    public QuestionClusterRenderer(Context context, GoogleMap map,
                                   ClusterManager<QuestionMarker> clusterManager) {
        super(context, map, clusterManager);
        mIconGenerator = new IconGenerator(context);
        mIconGenerator.setContentPadding(74, 36, 72, 36);
        mIconGenerator.setTextAppearance(R.style.ClusterFont);
        mIconGenerator.setBackground(context.getResources().getDrawable(R.drawable.ic_crowdopsquestionstack));
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster<QuestionMarker> cluster) {
        //start clustering if at least 2 items overlap
        return cluster.getSize() > 2;
    }

    @Override
    protected void onBeforeClusterItemRendered(QuestionMarker item, MarkerOptions markerOptions) {
        markerOptions.icon(item.getIcon());
        markerOptions.snippet(item.getSnippet());
        markerOptions.title(item.getTitle());
        super.onBeforeClusterItemRendered(item, markerOptions);
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<QuestionMarker> cluster, MarkerOptions markerOptions) {
        int bucket = getBucket(cluster);
//        BitmapDescriptor descriptor = mIcons.get(bucket);
//        if (descriptor == null) {
//            mColoredCircleBackground.getPaint().setColor(getColor(bucket));
//            descriptor = BitmapDescriptorFactory.fromBitmap(mIconGenerator.makeIcon(getClusterText(bucket)));
//            mIcons.put(bucket, descriptor);
//        }

        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromBitmap(mIconGenerator.makeIcon(getClusterText(bucket)));

        // TODO: consider adding anchor(.5, .5) (Individual markers will overlap more often)
        markerOptions.icon(descriptor);

    }
}
