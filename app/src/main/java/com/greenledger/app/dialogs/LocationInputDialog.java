package com.greenledger.app.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.greenledger.app.R;
import com.greenledger.app.models.embedded.Location;

public class LocationInputDialog extends DialogFragment {
    private TextInputLayout addressLayout;
    private TextInputLayout districtLayout;
    private TextInputLayout stateLayout;
    private TextInputLayout pincodeLayout;

    private TextInputEditText addressField;
    private TextInputEditText districtField;
    private TextInputEditText stateField;
    private TextInputEditText pincodeField;

    private LocationSelectedListener listener;

    public interface LocationSelectedListener {
        void onLocationSelected(Location location);
    }

    public static LocationInputDialog newInstance() {
        return new LocationInputDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_location_input, null);
        initializeViews(view);

        builder.setView(view)
                .setTitle(R.string.enter_location)
                .setPositiveButton(R.string.save, null)
                .setNegativeButton(R.string.cancel, (dialog, id) -> dismiss());

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(v -> validateAndSave());
        });

        return dialog;
    }

    private void initializeViews(View view) {
        addressLayout = view.findViewById(R.id.addressLayout);
        districtLayout = view.findViewById(R.id.districtLayout);
        stateLayout = view.findViewById(R.id.stateLayout);
        pincodeLayout = view.findViewById(R.id.pincodeLayout);

        addressField = view.findViewById(R.id.addressField);
        districtField = view.findViewById(R.id.districtField);
        stateField = view.findViewById(R.id.stateField);
        pincodeField = view.findViewById(R.id.pincodeField);
    }

    private void validateAndSave() {
        boolean isValid = true;

        // Reset errors
        addressLayout.setError(null);
        districtLayout.setError(null);
        stateLayout.setError(null);
        pincodeLayout.setError(null);

        // Get values
        String address = addressField.getText().toString().trim();
        String district = districtField.getText().toString().trim();
        String state = stateField.getText().toString().trim();
        String pincode = pincodeField.getText().toString().trim();

        // Validate fields
        if (TextUtils.isEmpty(address)) {
            addressLayout.setError(getString(R.string.error_required_field));
            isValid = false;
        }

        if (TextUtils.isEmpty(district)) {
            districtLayout.setError(getString(R.string.error_required_field));
            isValid = false;
        }

        if (TextUtils.isEmpty(state)) {
            stateLayout.setError(getString(R.string.error_required_field));
            isValid = false;
        }

        if (TextUtils.isEmpty(pincode)) {
            pincodeLayout.setError(getString(R.string.error_required_field));
            isValid = false;
        } else if (pincode.length() != 6) {
            pincodeLayout.setError(getString(R.string.error_invalid_pincode));
            isValid = false;
        }

        if (!isValid) {
            return;
        }

        // Create location object
        Location location = new Location();
        location.setAddress(address);
        location.setDistrict(district);
        location.setState(state);
        location.setPincode(pincode);

        if (listener != null) {
            listener.onLocationSelected(location);
        }

        dismiss();
    }

    public void setLocationSelectedListener(LocationSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentActivity) {
            try {
                listener = (LocationSelectedListener) context;
            } catch (ClassCastException e) {
                throw new ClassCastException(context
                        + " must implement LocationSelectedListener");
            }
        }
    }
}
