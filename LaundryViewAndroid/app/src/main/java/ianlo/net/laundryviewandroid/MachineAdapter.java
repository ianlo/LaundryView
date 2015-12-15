package ianlo.net.laundryviewandroid;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ianlo on 2015-12-14.
 */
public class MachineAdapter extends RecyclerView.Adapter<MachineAdapter.MachineViewHolder> {
    private Machine[] machines;


    public static class MachineViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView machineText;

        MachineViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            machineText = (TextView)itemView.findViewById(R.id.machine_text);
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
        holder.machineText.setText(machines[position].getStringStatus());
    }

    @Override
    public int getItemCount() {
        return machines.length;
    }

}
