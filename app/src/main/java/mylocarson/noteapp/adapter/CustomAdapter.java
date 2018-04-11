package mylocarson.noteapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mylocarson.noteapp.R;
import mylocarson.noteapp.utils.NoteAppModel;

/**
 * Created by user on 17/02/2018.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    ArrayList<NoteAppModel> noteAppArrayList ;

    public CustomAdapter(ArrayList<NoteAppModel> noteAppModelArrayList){
        this.noteAppArrayList = noteAppModelArrayList;
    }
    
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_list2,
                parent,false);
        return new MyViewHolder(view);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgUser;
        private TextView itemTxtTitle;
        private TextView itemTxtMessage;
        private TextView id;
        public LinearLayout view_background;
        public LinearLayout view_foreground;

        public MyViewHolder(View view){
            super(view);

//            this.imgUser = (ImageView) itemView.findViewById(R.id.img_user);
//            this.itemTxtTitle = (TextView) itemView.findViewById(R.id.item_txt_title);
//            this.itemTxtMessage = (TextView) itemView.findViewById(R.id.item_txt_message);
            this.itemTxtTitle = (TextView) itemView.findViewById(R.id.name);
            this.itemTxtMessage = (TextView) itemView.findViewById(R.id.description);
            this.view_background = (LinearLayout) itemView.findViewById(R.id.view_background);
            this.view_foreground =(LinearLayout)itemView.findViewById(R.id.view_foreground);
//            this.id = (TextView)itemView.findViewById(R.id.storeId);


        }


    }



    @Override
    public void onBindViewHolder(CustomAdapter.MyViewHolder holder, int position) {
        NoteAppModel model = noteAppArrayList.get(position);
        holder.itemTxtTitle.setText( model.getTitle());
        holder.itemTxtMessage.setText(model.getNote());
    }

    @Override
    public int getItemCount() {
        return this.noteAppArrayList.size();
    }

    public void removeNote(int position){
        noteAppArrayList.remove(position);
        notifyItemRemoved(position);
    }
    public void restoreNote(NoteAppModel model, int position){
        noteAppArrayList.add(position,model);
        notifyItemInserted(position);
    }

}
