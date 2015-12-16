package ianlo.net.laundryviewandroid;

import android.graphics.Color;
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
    public MachineAdapter(Machine[] machines) {
        this.machines = machines;
    }

    @Override
    public MachineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        MachineViewHolder pvh = new MachineViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(MachineViewHolder holder, int position) {
        Machine m = machines[position];
        holder.machineText.setText(m.getStringStatus());
        TextDrawable drawable = null;
        if(m.getStatus() == Machine.AVAILABLE) {
            drawable = TextDrawable.builder()
                    .beginConfig()
                    .withBorder(4)
                    .endConfig()
                    .buildRoundRect(m.getNumber() + "", Color.parseColor("#99CC00"), 5);
        }
        else if(m.getStatus() == Machine.ENDED) {
            drawable = TextDrawable.builder()
                    .beginConfig()
                    .withBorder(4)
                    .endConfig()
                    .buildRoundRect(m.getNumber() + "", Color.parseColor("#FFBB33"), 5);
        }
        else if(m.getStatus() == Machine.OUTOFSERVICE || m.getStatus() == Machine.RUNNING) {
            drawable = TextDrawable.builder()
                    .beginConfig()
                    .withBorder(4)
                    .endConfig()
                    .buildRoundRect(m.getNumber() + "", Color.parseColor("#FF4444"), 5);
        }
        if(drawable != null) {
            holder.machineNumber.setImageDrawable(drawable);
        }
    }

    @Override
    public int getItemCount() {
        return machines.length;
    }

}
