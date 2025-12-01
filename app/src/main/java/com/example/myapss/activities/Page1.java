package com.example.myapss.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapss.R;
import com.example.myapss.models.InspectionData;
import com.example.myapss.models.Page1Request;
import com.example.myapss.models.Page1Response;
import com.example.myapss.network.ApiClient;
import com.example.myapss.network.Page1Api;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;

public class Page1 extends AppCompatActivity {

    private EditText engineerNameInput, vesselNameInput, locationInput, vesselFlagInput,
            mainSerialInput, inspectionDateInput, imoNumberInput, mmsiNumberInput, vesselClassInput;

    private Spinner vdrMakeSpinner;
    private Button nextButton, cancelButton;
    private Calendar calendar = Calendar.getInstance();

    InspectionData inspectionData = new InspectionData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page1);

        initializeViews();
        setupVDRSpinner();
        setupDatePicker();
        setupListeners();
    }

    private void initializeViews() {

        engineerNameInput = findViewById(R.id.engineer_name_input);
        vesselNameInput = findViewById(R.id.vessel_name_input);
        locationInput = findViewById(R.id.Location_input);
        vesselFlagInput = findViewById(R.id.Vessel_Flag_input);
        mainSerialInput = findViewById(R.id.Main_Serial_Number_input);
        inspectionDateInput = findViewById(R.id.inspection_date_input);
        imoNumberInput = findViewById(R.id.imo_Number_input);
        mmsiNumberInput = findViewById(R.id.mmsi_Number_input);
        vesselClassInput = findViewById(R.id.Vessel_Class_input);
        vdrMakeSpinner = findViewById(R.id.vdr_make_spinner);

        nextButton = findViewById(R.id.btn_next);
        cancelButton = findViewById(R.id.btn_cancel);
    }

    private void setupVDRSpinner() {

        List<String> vdrList = new ArrayList<>();
        vdrList.add("Select VDR Make");
        vdrList.add("DANELEC");
        vdrList.add("NETWAVE");
        vdrList.add("HEADWAY");
        vdrList.add("SAL NAVIGATION");
        vdrList.add("NSR");
        vdrList.add("JRC");
        vdrList.add("FURUNO");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.spinner_item,
                vdrList
        ) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;  // disable first item
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                if (position == 0) {
                    tv.setTextColor(getColor(R.color.gray));
                } else {
                    tv.setTextColor(getColor(R.color.black));
                }

                return view;
            }
        };

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        vdrMakeSpinner.setAdapter(adapter);

        vdrMakeSpinner.setSelection(0);

        // Apply rounded popup
        vdrMakeSpinner.setPopupBackgroundResource(R.drawable.spinner_popup_background);
    }

    private void setupDatePicker() {

        inspectionDateInput.setOnClickListener(v -> {

            DatePickerDialog dialog = new DatePickerDialog(
                    Page1.this,
                    (view, year, month, day) -> {

                        // Get current device time
                        Calendar now = Calendar.getInstance();
                        int hour = now.get(Calendar.HOUR_OF_DAY);
                        int minute = now.get(Calendar.MINUTE);

                        // Format -> DD/MM/YYYY HH:mm
                        String formattedDateTime =
                                day + "/" + (month + 1) + "/" + year + " " +
                                        String.format("%02d:%02d", hour, minute);

                        inspectionDateInput.setText(formattedDateTime);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            dialog.show();
        });
    }

    private void setupListeners() {
        nextButton.setOnClickListener(v -> goToPage2());
        cancelButton.setOnClickListener(v -> finish());
    }

    private boolean validatePage1() {

        return !engineerNameInput.getText().toString().trim().isEmpty() &&
                !vesselNameInput.getText().toString().trim().isEmpty() &&
                !locationInput.getText().toString().trim().isEmpty() &&
                !vesselFlagInput.getText().toString().trim().isEmpty() &&
                !mainSerialInput.getText().toString().trim().isEmpty() &&
                !inspectionDateInput.getText().toString().trim().isEmpty() &&
                !imoNumberInput.getText().toString().trim().isEmpty() &&
                !mmsiNumberInput.getText().toString().trim().isEmpty() &&
                !vesselClassInput.getText().toString().trim().isEmpty() &&
                vdrMakeSpinner.getSelectedItemPosition() != 0;  // 🔥 Correct validation
    }

    private void goToPage2() {

        if (!validatePage1()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        inspectionData.engineerName = engineerNameInput.getText().toString();
        inspectionData.vesselName = vesselNameInput.getText().toString();
        inspectionData.location = locationInput.getText().toString();
        inspectionData.vesselFlag = vesselFlagInput.getText().toString();
        inspectionData.mainSerialNumber = mainSerialInput.getText().toString();
        inspectionData.imoNumber = imoNumberInput.getText().toString();
        inspectionData.mmsiNumber = mmsiNumberInput.getText().toString();
        inspectionData.vesselClass = vesselClassInput.getText().toString();
        inspectionData.vdrMake = vdrMakeSpinner.getSelectedItem().toString();
        inspectionData.dateTime = inspectionDateInput.getText().toString();

//        Intent intent = new Intent(Page1.this, Page2.class);
//        intent.putExtra("inspectionData", inspectionData);
//        startActivity(intent);

        // ✅ Call API
        Page1Request request = new Page1Request(inspectionData);

        Page1Api api =
                ApiClient.getInstance().create(Page1Api.class);

        Call<Page1Response> call =
                api.savePage1(request);

        call.enqueue(new retrofit2.Callback<Page1Response>() {
            @Override
            public void onResponse(Call<Page1Response> call, retrofit2.Response<Page1Response> response) {

                if (response.isSuccessful() && response.body() != null && response.body().success) {

                    // ✅ STORE INSPECTION ID FOR PAGE2 & PAGE3
                    SharedPreferences prefs = getSharedPreferences("inspection_data", MODE_PRIVATE);
                    prefs.edit()
                            .putLong("inspectionId", response.body().inspectionId)
                            .apply();

                    Toast.makeText(Page1.this, "Inspection Created", Toast.LENGTH_SHORT).show();

                    Intent next = new Intent(Page1.this, Page2.class);
                    startActivity(next);

                    // ✅ Save inspectionId for Page2 & Page3
                    inspectionData.inspectionId =
                            response.body().inspectionId;

                    Toast.makeText(Page1.this,
                            "Page 1 saved",
                            Toast.LENGTH_SHORT).show();

                    Intent intent =
                            new Intent(Page1.this, Page2.class);

                    intent.putExtra("inspectionData", inspectionData);
                    startActivity(intent);

                } else {
                    Toast.makeText(Page1.this,
                            "Server error while saving Page 1",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Page1Response> call, Throwable t) {
                Toast.makeText(Page1.this,
                        "Network error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

//        Intent resultIntent = new Intent();
//        resultIntent.putExtra("inspectionData", inspectionData);
//        setResult(RESULT_OK, resultIntent);
//        finish();
    }