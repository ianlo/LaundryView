package ianlo.net.laundryviewandroid;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

/**
 * Created by ianlo on 2015-12-14.
 */
public class MachineAdapter extends RecyclerView.Adapter<MachineAdapter.MachineViewHolder> {

    private Machine[] machines;
    private Context context;

    public static class MachineViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView machineText;
        ImageView machineNumber;
        MachineViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            machineText = (TextView)itemView.findViewById(R.id.machine_text);
            machineNumber = (ImageView) itemView.findViewById(R.id.machine_number);
        }
    }

    public MachineAdapter(Context context, Machine[] machines) {
        this.context = context;
        this.machines = machines;
    }

    @Override
    public MachineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new MachineViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MachineViewHolder holder, int position) {
        //Get the machine and set the machineText accordingly.
        Machine m = machines[position];
        holder.machineText.setText(m.getStringStatus());
        TextDrawable drawable = null;
        //Change the drawable colour based on the status of the machine.
        if(m.getStatus() == Machine.AVAILABLE) {
            drawable = TextDrawable.builder()
                    .beginConfig()
                    .withBorder(4)
                    .endConfig()
                    .buildRoundRect(m.getNumber() + "", ContextCompat.getColor(context, R.color.green), 5);
        }
        else if(m.getStatus() == Machine.ENDED) {
            drawable = TextDrawable.builder()
                    .beginConfig()
                    .withBorder(4)
                    .endConfig()
                    .buildRoundRect(m.getNumber() + "", ContextCompat.getColor(context, R.color.orange), 5);
        }
        else if(m.getStatus() == Machine.RUNNING) {
            drawable = TextDrawable.builder()
                    .beginConfig()
                    .withBorder(4)
                    .endConfig()
                    .buildRoundRect(m.getNumber() + "", ContextCompat.getColor(context, R.color.red), 5);
        }
        else if(m.getStatus() == Machine.OUTOFSERVICE) {
            drawable = TextDrawable.builder()
                    .beginConfig()
                    .withBorder(4)
                    .endConfig()
                    .buildRoundRect(m.getNumber() + "", ContextCompat.getColor(context, R.color.grey), 5);
        }
        //If the drawable was created successfully, set the drawable to the one we created.
        if(drawable != null) {
            holder.machineNumber.setImageDrawable(drawable);
        }
    }

    @Override
    public int getItemCount() {
        return machines.length;
    }

}
