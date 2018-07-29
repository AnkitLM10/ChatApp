package com.example.ankit.chatapp;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by ankit on 7/28/2018.
 */
public class CustomRecyclerAdapter  extends RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder> {
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Project");
    private Context context;
    ArrayList<String> arr ;
    ArrayList<Long> time;
    TextView textView;
    String user,promoMan;
    SharedPreferences sharedPreferences;
    public CustomRecyclerAdapter(Context context) {
        this.context = context;
        arr=new ArrayList<>();
        time=new ArrayList<>();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.setIsRecyclable(false);

        String msg = arr.get(position);
        long time =this.time.get(position);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YY' 'hh:mm");
        holder.textViewTime.setText(simpleDateFormat.format(time));

        if(msg.charAt(0)=='1')
{
    holder.ll.setGravity(Gravity.RIGHT);

    holder.linearLayout.setGravity(Gravity.RIGHT);
    holder.textView.setText(msg.substring(1));

}
else
{
    Log.d("oo",msg);
    holder.ll.setBackgroundColor(Color.WHITE);
    holder.textView.setText(msg.substring(1));




}


    }

    @Override
    public int getItemCount() {

        return arr.size();
    }
    public void add(String pro,Long time)
    {
        arr.add(pro);
        this.time.add(time);
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

LinearLayout linearLayout,ll;
        TextView textView,textViewTime;
        public ViewHolder(View view) {
            super(view);
            ll=(LinearLayout)view.findViewById(R.id.ll);
            linearLayout = (LinearLayout)view.findViewById(R.id.linearLayout);
            textView = (TextView)view.findViewById(R.id.textView);
            textViewTime = (TextView)view.findViewById(R.id.textViewTime);
        }

    }


}
