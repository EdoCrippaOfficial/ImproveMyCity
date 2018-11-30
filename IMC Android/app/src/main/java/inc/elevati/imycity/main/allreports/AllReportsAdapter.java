package inc.elevati.imycity.main.allreports;

import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import inc.elevati.imycity.R;
import inc.elevati.imycity.main.MainContracts;
import inc.elevati.imycity.utils.GlideApp;
import inc.elevati.imycity.utils.Report;

/** Adapter class that organizes report data to show it in a RecyclerView hosted in AllReportsFragment */
public class AllReportsAdapter extends RecyclerView.Adapter<AllReportsAdapter.MyViewHolder> {

    /** The context reference */
    private Fragment context;

    /** The report list to be shown */
    private List<Report> reports;

    /** The presenter that allows communication with the view */
    private MainContracts.AllReportsPresenter presenter;

    /** Class that holds the required View objects in a layout */
    static class MyViewHolder extends RecyclerView.ViewHolder {

        /** The progress bar shown during image loading*/
        ProgressBar pb_loading;

        /** TextView for the report title */
        TextView tv_title;

        /** TextView for the report description */
        TextView tv_desc;

        /** ImageView for the report image*/
        ImageView iv_image;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.pb_loading = itemView.findViewById(R.id.pb_image);
            this.tv_title = itemView.findViewById(R.id.tv_title);
            this.tv_desc = itemView.findViewById(R.id.tv_desc);
            this.iv_image = itemView.findViewById(R.id.iv_report_image);
        }
    }

    AllReportsAdapter(Fragment context, MainContracts.AllReportsPresenter presenter) {
        this.reports = new ArrayList<>();
        this.context = context;
        this.presenter = presenter;
    }

    /**
     * Called when a single item holder in RecyclerView is created, here
     * click listener is attached to ViewHolder
     * @param parent the parent View
     * @param viewType the view type
     * @return the ViewHolder created
     */
    @NonNull
    @Override
    public AllReportsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_list_item, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.showReport(reports.get(viewHolder.getAdapterPosition()));
            }
        });
        return viewHolder;
    }

    /**
     * Method called when a report is bound to ViewHolder, here all Views are initialized
     * @param holder the ViewHolder
     * @param position the report position in the list
     */
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Report r = reports.get(position);
        holder.tv_title.setText(r.getTitle());
        holder.tv_desc.setText(r.getDescription());
        holder.pb_loading.setVisibility(View.VISIBLE);

        // Image loading from cloud storage with Glide
        GlideApp.with(context)
                .load(r.getImageReference(Report.IMAGE_THUMBNAIL))
                .placeholder(R.drawable.ic_image)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.pb_loading.setVisibility(View.GONE);
                        holder.iv_image.setImageResource(R.drawable.ic_no_image);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.pb_loading.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.iv_image);
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    /**
     * Method called to change the reports order in the list
     * @param order the requested order, should be one defined in AllReportsFragment
     */
    void sortReports(int order) {
        switch (order) {
            case AllReportsFragment.REPORT_SORT_DATE_NEWEST:
                Collections.sort(reports, new Comparator<Report>() {
                    @Override
                    public int compare(Report r1, Report r2) {
                        return Long.compare(r2.getTimestamp(), r1.getTimestamp());
                    }
                });
                break;
            case AllReportsFragment.REPORT_SORT_DATE_OLDEST:
                Collections.sort(reports, new Comparator<Report>() {
                    @Override
                    public int compare(Report r1, Report r2) {
                        return Long.compare(r1.getTimestamp(), r2.getTimestamp());
                    }
                });
                break;
            case AllReportsFragment.REPORT_SORT_STARS_MORE:
                break;
            case AllReportsFragment.REPORT_SORT_STARS_LESS:
                break;
        }
        notifyDataSetChanged();
    }

    /**
     * Method called to update the reports list
     * @param reports the new report list
     * @param order the order used to show reports
     */
    void updateReports(List<Report> reports, int order) {
        this.reports.clear();
        this.reports.addAll(reports);
        sortReports(order);
    }

    /**
     * Method called to retrieve a report from the list
     * @param position the report position in the list
     * @return the report in the specified positon
     */
    public Report getItemAt(int position) {
        return reports.get(position);
    }
}
