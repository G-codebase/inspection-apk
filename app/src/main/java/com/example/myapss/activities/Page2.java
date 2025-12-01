package com.example.myapss.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapss.R;
import com.example.myapss.adapters.FilePreviewAdapter;
import com.example.myapss.models.FileUploadModel;
import com.example.myapss.models.InspectionData;
import com.example.myapss.network.ApiClient;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.gson.Gson;

import java.io.File;
import java.util.*;

import com.example.myapss.network.Page2Api;
import com.example.myapss.models.Page2Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Page2 extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 300;

    // ---------- BCP ----------
    private SwitchMaterial clearReadabilitySwitch, dimFunctionSwitch, buttonsWorkingSwitch;

    // ---------- DAU ----------
    private SwitchMaterial batteryReplacementSwitch;
    private Spinner batteryYearSpinner, batteryMonthSpinner;
    private EditText dauConditionInput;

    // ---------- Fixed Capsule ----------
    private EditText capsuleInput, beaconModelInput, beaconSerialInput, beaconVoltageInput;
    private Spinner beaconYearSpinner, beaconMonthSpinner;

    // ---------- Float Free ----------
    private EditText floatFreeModelInput, epribSerialInput, hruModelInput;
    private SwitchMaterial hruReplaceSwitch;
    private Spinner hruYearSpinner, hruMonthSpinner;

    // ---------- RVI ----------
    private SwitchMaterial rviCablesSwitch;
    private EditText rviPowerInput, rviModelInput;

    // ---------- Files ----------
    private final Map<String, List<FileUploadModel>> fileUploads = new HashMap<>();
    private final Map<String, FilePreviewAdapter> fileAdapters = new HashMap<>();
    private Button prevButton, nextButton;
    private String currentFieldName = "";
    private Uri cameraUri;
    private InspectionData inspectionData;
    private ActivityResultLauncher<Uri> cameraLauncher;
    private ActivityResultLauncher<String> filePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page2);

        inspectionData = (InspectionData) getIntent().getSerializableExtra("inspectionData");

        initializeViews();
        setupSpinners();
        setupFileUploads();
        setupButtons();
        setupActivityResultLaunchers();
        loadExistingData();
//        setOnClickListener();
    }

    private void initializeViews() {
        clearReadabilitySwitch = findViewById(R.id.clear_readability_switch);
        dimFunctionSwitch = findViewById(R.id.dim_function_switch);
        buttonsWorkingSwitch = findViewById(R.id.buttons_working_switch);

        batteryReplacementSwitch = findViewById(R.id.battery_replacement_switch);
        batteryYearSpinner = findViewById(R.id.battery_year_spinner);
        batteryMonthSpinner = findViewById(R.id.battery_month_spinner);
        dauConditionInput = findViewById(R.id.dau_condition_input);

        capsuleInput = findViewById(R.id.capsule_input);
        beaconModelInput = findViewById(R.id.beacon_model_input);
        beaconYearSpinner = findViewById(R.id.beacon_year_spinner);
        beaconMonthSpinner = findViewById(R.id.beacon_month_spinner);
        beaconSerialInput = findViewById(R.id.beacon_serial_input);
        beaconVoltageInput = findViewById(R.id.beacon_voltage_input);

        floatFreeModelInput = findViewById(R.id.float_free_model_input);
        epribSerialInput = findViewById(R.id.eprib_serial_input);
        hruModelInput = findViewById(R.id.hru_model_input);
        hruReplaceSwitch = findViewById(R.id.hru_replace_switch);
        hruYearSpinner = findViewById(R.id.hru_year_spinner);
        hruMonthSpinner = findViewById(R.id.hru_month_spinner);

        rviCablesSwitch = findViewById(R.id.rvi_cables_switch);
        rviPowerInput = findViewById(R.id.rvi_power_input);
        rviModelInput = findViewById(R.id.rvi_model_input);

        prevButton = findViewById(R.id.btn_prev);
        nextButton = findViewById(R.id.btn_next);
    }

    private void setupSpinners() {
        List<String> years = new ArrayList<>();
        years.add("Select");
        for (int i = 2025; i <= 2035; i++) years.add(String.valueOf(i));

        List<String> months = new ArrayList<>();
        months.add("Select");
        for (int i = 1; i <= 12; i++) months.add(String.format("%02d", i));

        // <CHANGE> Create custom adapter with non-selectable first item (index 0)
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(
                this,
                R.layout.spinner_item,
                years
        ) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;  // Disable first item
            }
// for year
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

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(
                this,
                R.layout.spinner_item,
                months
        ) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;  // Disable first item
            }
// for month
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

        yearAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        monthAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        batteryYearSpinner.setAdapter(yearAdapter);
        batteryMonthSpinner.setAdapter(monthAdapter);
        beaconYearSpinner.setAdapter(yearAdapter);
        beaconMonthSpinner.setAdapter(monthAdapter);
        hruYearSpinner.setAdapter(yearAdapter);
        hruMonthSpinner.setAdapter(monthAdapter);

        // Set popup background
        batteryYearSpinner.setPopupBackgroundResource(R.drawable.spinner_popup_background);
        batteryMonthSpinner.setPopupBackgroundResource(R.drawable.spinner_popup_background);
        beaconYearSpinner.setPopupBackgroundResource(R.drawable.spinner_popup_background);
        beaconMonthSpinner.setPopupBackgroundResource(R.drawable.spinner_popup_background);
        hruYearSpinner.setPopupBackgroundResource(R.drawable.spinner_popup_background);
        hruMonthSpinner.setPopupBackgroundResource(R.drawable.spinner_popup_background);
    }

    private void setupFileUploads() {
        String[] fields = {
                "bcp_photos","dau_door","dau_serial","dau_grounding",
                "dau_backup","fixed_capsule","cable_gland",
                "beacon_expiry","float_free","hru_photo","rvi_photo"
        };
        for (String field : fields) {
            fileUploads.put(field, new ArrayList<>());
            int recyclerId = getResources().getIdentifier("recycler_" + field, "id", getPackageName());
            RecyclerView recyclerView = findViewById(recyclerId);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            FilePreviewAdapter adapter =
                    new FilePreviewAdapter(this, field, fileUploads.get(field),
                            (fName, file) -> removeFile(fName, file));
            recyclerView.setAdapter(adapter);
            fileAdapters.put(field, adapter);
            int pickId = getResources().getIdentifier("btn_pick_" + field, "id", getPackageName());
            findViewById(pickId).setOnClickListener(v -> pickFile(field));
            int camId = getResources().getIdentifier("btn_camera_" + field, "id", getPackageName());
            findViewById(camId).setOnClickListener(v -> checkCameraPermissionAndOpen(field));
        }
    }

    private void setSpinnerValue(Spinner spinner, String value) {
        if (value == null || value.equals("Select")) return;
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (value.equals(adapter.getItem(i))) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void loadExistingData() {
        if (inspectionData == null) return;

        // <CHANGE> Load switches
        clearReadabilitySwitch.setChecked(inspectionData.clearReadability);
        dimFunctionSwitch.setChecked(inspectionData.dimFunction);
        buttonsWorkingSwitch.setChecked(inspectionData.buttonsWorking);
        batteryReplacementSwitch.setChecked(inspectionData.batteryReplacement);
        hruReplaceSwitch.setChecked(inspectionData.hruReplace);
        rviCablesSwitch.setChecked(inspectionData.rviCables);

        // <CHANGE> Load text fields
        dauConditionInput.setText(inspectionData.dauCondition);
        capsuleInput.setText(inspectionData.capsuleDescription);
        beaconModelInput.setText(inspectionData.beaconModel);
        beaconSerialInput.setText(inspectionData.beaconSerial);
        beaconVoltageInput.setText(inspectionData.beaconVoltage);
        floatFreeModelInput.setText(inspectionData.floatFreeModel);
        epribSerialInput.setText(inspectionData.epribSerial);
        hruModelInput.setText(inspectionData.hruModel);
        rviPowerInput.setText(inspectionData.rviPower);
        rviModelInput.setText(inspectionData.rviModel);

        // <CHANGE> Load spinner selections
        setSpinnerValue(batteryYearSpinner, inspectionData.batteryExpiryYear);
        setSpinnerValue(batteryMonthSpinner, inspectionData.batteryExpiryMonth);
        setSpinnerValue(beaconYearSpinner, inspectionData.beaconBatteryYear);
        setSpinnerValue(beaconMonthSpinner, inspectionData.beaconBatteryMonth);
        setSpinnerValue(hruYearSpinner, inspectionData.hruBatteryYear);
        setSpinnerValue(hruMonthSpinner, inspectionData.hruBatteryMonth);

        // <CHANGE> Load uploaded files
        if (inspectionData.uploadedFiles != null) {
            for (String field : inspectionData.uploadedFiles.keySet()) {
                if (!fileUploads.containsKey(field)) continue;
                List<FileUploadModel> list = fileUploads.get(field);
                list.clear();
                for (String uriStr : inspectionData.uploadedFiles.get(field)) {
                    FileUploadModel model =
                            new FileUploadModel(
                                    getFileName(Uri.parse(uriStr)),
                                    uriStr,
                                    System.currentTimeMillis()
                            );
                    list.add(model);
                }
                fileAdapters.get(field).notifyDataSetChanged();
            }
        }
    }

    private void saveData() {
        inspectionData.clearReadability = clearReadabilitySwitch.isChecked();
        inspectionData.dimFunction = dimFunctionSwitch.isChecked();
        inspectionData.buttonsWorking = buttonsWorkingSwitch.isChecked();
        inspectionData.batteryReplacement = batteryReplacementSwitch.isChecked();
        inspectionData.batteryExpiryYear = batteryYearSpinner.getSelectedItem().toString();
        inspectionData.batteryExpiryMonth = batteryMonthSpinner.getSelectedItem().toString();
        inspectionData.dauCondition = dauConditionInput.getText().toString();
        inspectionData.capsuleDescription = capsuleInput.getText().toString();
        inspectionData.beaconModel = beaconModelInput.getText().toString();
        inspectionData.beaconSerial = beaconSerialInput.getText().toString();
        inspectionData.beaconVoltage = beaconVoltageInput.getText().toString();
        inspectionData.beaconBatteryYear = beaconYearSpinner.getSelectedItem().toString();
        inspectionData.beaconBatteryMonth = beaconMonthSpinner.getSelectedItem().toString();
        inspectionData.floatFreeModel = floatFreeModelInput.getText().toString();
        inspectionData.epribSerial = epribSerialInput.getText().toString();
        inspectionData.hruModel = hruModelInput.getText().toString();
        inspectionData.hruReplace = hruReplaceSwitch.isChecked();
        inspectionData.hruBatteryYear = hruYearSpinner.getSelectedItem().toString();
        inspectionData.hruBatteryMonth = hruMonthSpinner.getSelectedItem().toString();
        inspectionData.rviCables = rviCablesSwitch.isChecked();
        inspectionData.rviPower = rviPowerInput.getText().toString();
        inspectionData.rviModel = rviModelInput.getText().toString();

        inspectionData.uploadedFiles = new HashMap<>();
        for (String key : fileUploads.keySet()) {
            List<String> uris = new ArrayList<>();
            for (FileUploadModel f : fileUploads.get(key)) {
                uris.add(f.fileUri);
            }
            inspectionData.uploadedFiles.put(key, uris);
        }

        Gson gson = new Gson();
        String json = gson.toJson(inspectionData);

        RequestBody jsonBody =
                RequestBody.create(MediaType.parse("application/json"), json);
    }

    private void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private boolean validatePage2() {
        if (!clearReadabilitySwitch.isChecked()) {
            showError("Clear Readability switch is OFF");
            return false;
        }
        if (!dimFunctionSwitch.isChecked()) {
            showError("Dim Function switch is OFF");
            return false;
        }
        if (!buttonsWorkingSwitch.isChecked()) {
            showError("Buttons Working switch is OFF");
            return false;
        }
        if (dauConditionInput.getText().toString().trim().isEmpty()) {
            showError("DAU Condition is empty");
            return false;
        }
        if (capsuleInput.getText().toString().trim().isEmpty()) {
            showError("Capsule Description is empty");
            return false;
        }
        if (beaconModelInput.getText().toString().trim().isEmpty()) {
            showError("Beacon Model is empty");
            return false;
        }
        if (beaconSerialInput.getText().toString().trim().isEmpty()) {
            showError("Beacon Serial is empty");
            return false;
        }
        if (beaconVoltageInput.getText().toString().trim().isEmpty()) {
            showError("Beacon Voltage is empty");
            return false;
        }
        if (floatFreeModelInput.getText().toString().trim().isEmpty()) {
            showError("Float Free Model is empty");
            return false;
        }
        if (epribSerialInput.getText().toString().trim().isEmpty()) {
            showError("EPIRB Serial is empty");
            return false;
        }
        if (hruModelInput.getText().toString().trim().isEmpty()) {
            showError("HRU Model is empty");
            return false;
        }
        if (rviPowerInput.getText().toString().trim().isEmpty()) {
            showError("RVI Power is empty");
            return false;
        }
        if (rviModelInput.getText().toString().trim().isEmpty()) {
            showError("RVI Model is empty");
            return false;
        }
        if (batteryYearSpinner.getSelectedItemPosition() == 0) {
            showError("Battery Year not selected");
            return false;
        }
        if (batteryMonthSpinner.getSelectedItemPosition() == 0) {
            showError("Battery Month not selected");
            return false;
        }
        if (beaconYearSpinner.getSelectedItemPosition() == 0) {
            showError("Beacon Year not selected");
            return false;
        }
        if (beaconMonthSpinner.getSelectedItemPosition() == 0) {
            showError("Beacon Month not selected");
            return false;
        }
        if (hruYearSpinner.getSelectedItemPosition() == 0) {
            showError("HRU Year not selected");
            return false;
        }
        if (hruMonthSpinner.getSelectedItemPosition() == 0) {
            showError("HRU Month not selected");
            return false;
        }

        return true;
    }

    private void setupActivityResultLaunchers() {
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                success -> {
                    if (success && cameraUri != null && currentFieldName != null) {
                        FileUploadModel model = new FileUploadModel(getFileName(cameraUri), cameraUri.toString(), System.currentTimeMillis());
                        fileUploads.get(currentFieldName).add(model);
                        fileAdapters.get(currentFieldName).notifyDataSetChanged();
                    }
                });
        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null && currentFieldName != null) {
                        FileUploadModel model = new FileUploadModel(getFileName(uri), uri.toString(), System.currentTimeMillis());
                        fileUploads.get(currentFieldName).add(model);
                        fileAdapters.get(currentFieldName).notifyDataSetChanged();
                    }
                });
    }

    private void pickFile(String fieldName) {
        currentFieldName = fieldName;
        filePickerLauncher.launch("*/*");
    }

    private void checkCameraPermissionAndOpen(String fieldName) {
        currentFieldName = fieldName;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION
            );
        } else {
            launchCamera();
        }
    }

    private void launchCamera() {
        File photoFile = new File(
                getExternalFilesDir(null),
                "CAM_" + System.currentTimeMillis() + ".jpg"
        );
        cameraUri = FileProvider.getUriForFile(
                this,
                getPackageName() + ".provider",
                photoFile
        );
        cameraLauncher.launch(cameraUri);
    }

    private String getFileName(Uri uri) {
        String result = "file";
        if ("content".equals(uri.getScheme())) {
            Cursor cursor = getContentResolver()
                    .query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex("_display_name");
                    if (index != -1) result = cursor.getString(index);
                }
            } finally {
                if (cursor != null) cursor.close();
            }
        } else if (uri.getPath() != null) {
            result = new File(uri.getPath()).getName();
        }
        return result;
    }

    private void removeFile(String fieldName, FileUploadModel file) {
        fileUploads.get(fieldName).remove(file);
        fileAdapters.get(fieldName).notifyDataSetChanged();
        Toast.makeText(this, "File removed", Toast.LENGTH_SHORT).show();
    }

    private void setupButtons() {
        prevButton.setOnClickListener(v -> finish());
//        nextButton.setOnClickListener(v -> {
//            if (!validatePage2()) return;
//            saveData();
//            Intent intent = new Intent(Page2.this, Page3.class);
//            intent.putExtra("inspectionData", inspectionData);
//            startActivity(intent);
//        });

        nextButton.setOnClickListener(v -> {

            if (!validatePage2()) return;

            saveData();   // ✅ collect data

            try {
                Gson gson = new Gson();
                String json = gson.toJson(inspectionData);

                RequestBody jsonBody =
                        RequestBody.create(MediaType.parse("application/json"), json);

                Map<String, List<MultipartBody.Part>> fileMap =
                        prepareMultipartFiles();

                Page2Api api =
                        ApiClient.getInstance().create(Page2Api.class);    // have to do

                Call<Page2Response> call =
                        api.savePage2(inspectionData.inspectionId, jsonBody, fileMap);

                call.enqueue(new Callback<Page2Response>() {
                    @Override
                    public void onResponse(Call<Page2Response> call,
                                           Response<Page2Response> response) {

                        if (response.isSuccessful()) {

                            Toast.makeText(Page2.this,
                                    "Page 2 uploaded successfully",
                                    Toast.LENGTH_LONG).show();

                            startActivity(
                                    new Intent(Page2.this, Page3.class)
                            );

                        } else {
                            showError("Server error: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Page2Response> call, Throwable t) {
                        showError("Upload failed: " + t.getMessage());
                    }
                });

            } catch (Exception e) {
                showError("Upload error: " + e.getMessage());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            launchCamera();
        }
    }

    private Map<String, List<MultipartBody.Part>> prepareMultipartFiles() {

        Map<String, List<MultipartBody.Part>> map = new HashMap<>();

        for (String key : fileUploads.keySet()) {

            List<MultipartBody.Part> parts = new ArrayList<>();

            for (FileUploadModel f : fileUploads.get(key)) {

                Uri uri = Uri.parse(f.fileUri);

                File file = new File(uri.getPath());

                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), file);

                MultipartBody.Part body =
                        MultipartBody.Part.createFormData(
                                key,
                                file.getName(),
                                requestFile);

                parts.add(body);
            }

            map.put(key, parts);
        }

        return map;
    }
}