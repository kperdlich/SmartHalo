package bike.smarthalo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bike.smarthalo.sdk.models.BleDevice;

public class BleDeviceAdapter extends ArrayAdapter<BleDevice> {
    public BleDeviceAdapter(Activity context, ArrayList<BleDevice> devices) {
        super(context, 0, devices);
    }

    public void replaceDeviceList(List<BleDevice> list) {
        clear();
        addAll(list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        BleDevice device = getItem(position);

        TextView nameTextView = listItemView.findViewById(R.id.name);
        nameTextView.setText(device.name);

        TextView numberTextView = listItemView.findViewById(R.id.address);
        numberTextView.setText(String.valueOf(device.address));

        return listItemView;
    }
}
