package ianlo.net.laundryviewandroid;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.Calendar;

import ui.MainActivity;

/**
 * Created by ianlo on 2015-12-14.
 */
public class MachineAdapter extends RecyclerView.Adapter<MachineAdapter.MachineViewHolder> {

    private Machine[] machines;
    private Context context;
    private MainActivity mainActivity;

    public static class MachineViewHolder extends RecyclerView.ViewHolder {
        private CardView cv;
        private TextView machineText;
        private ImageView machineNumber;
        private int timeRemaining = -1;

        MachineViewHolder(View itemView, final MainActivity mainActivity) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            machineText = (TextView) itemView.findViewById(R.id.machine_text);
            machineNumber = (ImageView) itemView.findViewById(R.id.machine_number);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Ask if the user wants a notification.
                    if (timeRemaining > 0) {
                        // Build the dialog to ask.
                        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                        builder.setTitle("Notify Me");
                        builder.setMessage("Would you like to be notified when the machine is finished? Past notifications will be cancelled.");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                AlarmManager alarmMgr = (AlarmManager) mainActivity.getSystemService(Context.ALARM_SERVICE);
                                Intent intent = new Intent(mainActivity, NotificationReceiver.class);
                                PendingIntent alarmIntent = PendingIntent.getBroadcast(mainActivity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                // Set the notification to the time when the machine is finished
                                alarmMgr.set(AlarmManager.RTC, Calendar.getInstance().getTimeInMillis() + timeRemaining * 60 * 1000, alarmIntent);

                                Log.d("CMU Laundry", "Notification in " + timeRemaining + " minutes.");
                                Toast.makeText(mainActivity, "Notification Enabled", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                }
                        );
                        // Create the AlertDialog.
                        builder.show();
                    }
                }
            });
        }
    }

    public MachineAdapter(Context context, Machine[] machines, MainActivity mainActivity) {
        this.context = context;
        this.machines = machines;
        this.mainActivity = mainActivity;
    }

    @Override
    public MachineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new MachineViewHolder(v, mainActivity);
    }

    @Override
    public void onBindViewHolder(MachineViewHolder holder, int position) {
        // Get the Machine and set the TextView of the CardView accordingly.
        Machine m = machines[position];
        holder.machineText.setText(m.getStringStatus());
        holder.timeRemaining = m.getTimeRemaining();
        TextDrawable drawable = null;
        // Change the drawable colour based on the status of the Machine.
        if (m.getStatus() == Machine.AVAILABLE) {
            drawable = TextDrawable.builder()
                    .beginConfig()
                    .withBorder(4)
                    .endConfig()
                    .buildRoundRect(m.getNumber() + "", ContextCompat.getColor(context, R.color.green), 5);
        } else if (m.getStatus() == Machine.ENDED) {
            drawable = TextDrawable.builder()
                    .beginConfig()
                    .withBorder(4)
                    .endConfig()
                    .buildRoundRect(m.getNumber() + "", ContextCompat.getColor(context, R.color.orange), 5);
        } else if (m.getStatus() == Machine.RUNNING) {
            drawable = TextDrawable.builder()
                    .beginConfig()
                    .withBorder(4)
                    .endConfig()
                    .buildRoundRect(m.getNumber() + "", ContextCompat.getColor(context, R.color.red), 5);
        } else if (m.getStatus() == Machine.OUTOFSERVICE) {
            drawable = TextDrawable.builder()
                    .beginConfig()
                    .withBorder(4)
                    .endConfig()
                    .buildRoundRect(m.getNumber() + "", ContextCompat.getColor(context, R.color.grey), 5);
        }
        // If the Drawable was created successfully, set the CardView Drawable to the one we created.
        if (drawable != null) {
            holder.machineNumber.setImageDrawable(drawable);
        }
    }

    @Override
    public int getItemCount() {
        return machines.length;
    }

}
