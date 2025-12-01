package com.example.myapss.activities;

//import static android.text.TextUtils.isEmpty;
//import static retrofit2.Response.error;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapss.R;
import com.example.myapss.adapters.FilePreviewAdapter;
import com.example.myapss.models.FileUploadModel;
import com.example.myapss.models.InspectionData;
import com.example.myapss.models.Page3Response;
import com.example.myapss.network.ApiClient;
import com.example.myapss.network.Page3Api;
import com.google.gson.Gson;
import java.util.*;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class Page3 extends AppCompatActivity {
    private EditText xBandChannel, xBandType, xBandInfo;
    private EditText sBandChannel, sBandType, sBandInfo;
    private EditText masterChannel, masterType, masterInfo;
    private EditText backupChannel, backupType, backupInfo;
    private Button btnPrev, btnSubmit;
    private final Map<String, List<FileUploadModel>> fileUploads = new HashMap<>();
    private final Map<String, FilePreviewAdapter> fileAdapters = new HashMap<>();
    private String currentFieldName = "";
    private ActivityResultLauncher<String> filePickerLauncher;
    private InspectionData inspectionData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page3);

        try {
            inspectionData = loadInspectionDataFromPreferences();

            if (inspectionData == null) {
                inspectionData = new InspectionData();
            }

            initViews();
            restorePageDataFromPreferences();
            setupFileUploads();
            setupFilePicker();
            restoreFilesFromInspectionData();
            setupButtons();

        } catch (Exception e) {
            Toast.makeText(this, "Error loading data: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
            finish();
        }
    }

//    private static final Map<String, Integer> FILE_LIMITS = new HashMap<String, Integer>() {{
//        put("makerReport", 3);
//        put("prevCoc", 2);
//        put("dataUpload", 5);
//    }};

    private void restorePageDataFromPreferences() {
        try {
            SharedPreferences prefs = getSharedPreferences("inspection_data", MODE_PRIVATE);

            // Restore X Band fields
            xBandChannel.setText(prefs.getString("xBandSourceChannel", ""));
            xBandType.setText(prefs.getString("xBandSourceType", ""));
            xBandInfo.setText(prefs.getString("xBandSourceInfo", ""));

            // Restore S Band fields
            sBandChannel.setText(prefs.getString("sBandSourceChannel", ""));
            sBandType.setText(prefs.getString("sBandSourceType", ""));
            sBandInfo.setText(prefs.getString("sBandSourceInfo", ""));

            // Restore Master ECDIS fields
            masterChannel.setText(prefs.getString("masterEcdisSourceChannel", ""));
            masterType.setText(prefs.getString("masterEcdisSourceType", ""));
            masterInfo.setText(prefs.getString("masterEcdisSourceInfo", ""));

            // Restore Backup ECDIS fields
            backupChannel.setText(prefs.getString("backupEcdisSourceChannel", ""));
            backupType.setText(prefs.getString("backupEcdisSourceType", ""));
            backupInfo.setText(prefs.getString("backupEcdisSourceInfo", ""));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private InspectionData loadInspectionDataFromPreferences() {
        SharedPreferences prefs = getSharedPreferences("inspection_data", MODE_PRIVATE);

        InspectionData data = new InspectionData();

        // ✅ REQUIRED: Load inspection ID for final submission
        data.inspectionId = prefs.getLong("inspectionId", 0);

        // Page 1 data
        data.engineerName = prefs.getString("engineerName", "");
        data.vesselName = prefs.getString("vesselName", "");
        data.location = prefs.getString("location", "");
        data.vesselFlag = prefs.getString("vesselFlag", "");
        data.mainSerialNumber = prefs.getString("mainSerialNumber", "");
        data.imoNumber = prefs.getString("imoNumber", "");
        data.mmsiNumber = prefs.getString("mmsiNumber", "");
        data.vesselClass = prefs.getString("vesselClass", "");
        data.vdrMake = prefs.getString("vdrMake", "");
        data.dateTime = prefs.getString("dateTime", "");

        // Page 2 data
        data.clearReadability = prefs.getBoolean("clearReadability", false);
        data.dimFunction = prefs.getBoolean("dimFunction", false);
        data.buttonsWorking = prefs.getBoolean("buttonsWorking", false);
        data.batteryReplacement = prefs.getBoolean("batteryReplacement", false);
        data.batteryExpiryYear = prefs.getString("batteryExpiryYear", "Select");
        data.batteryExpiryMonth = prefs.getString("batteryExpiryMonth", "Select");
        data.dauCondition = prefs.getString("dauCondition", "");
        data.capsuleDescription = prefs.getString("capsuleDescription", "");
        data.beaconModel = prefs.getString("beaconModel", "");
        data.beaconSerial = prefs.getString("beaconSerial", "");
        data.beaconVoltage = prefs.getString("beaconVoltage", "");
        data.beaconBatteryYear = prefs.getString("beaconBatteryYear", "Select");
        data.beaconBatteryMonth = prefs.getString("beaconBatteryMonth", "Select");
        data.floatFreeModel = prefs.getString("floatFreeModel", "");
        data.epribSerial = prefs.getString("epribSerial", "");
        data.hruModel = prefs.getString("hruModel", "");
        data.hruReplace = prefs.getBoolean("hruReplace", false);
        data.hruBatteryYear = prefs.getString("hruBatteryYear", "Select");
        data.hruBatteryMonth = prefs.getString("hruBatteryMonth", "Select");
        data.rviCables = prefs.getBoolean("rviCables", false);
        data.rviPower = prefs.getString("rviPower", "");
        data.rviModel = prefs.getString("rviModel", "");

        // File uploads
        String fileUploadsJson = prefs.getString("uploadedFiles", "{}");
        try {
            data.uploadedFiles = new Gson().fromJson(fileUploadsJson,
                    new com.google.gson.reflect.TypeToken<HashMap<String, List<String>>>(){}.getType());
        } catch (Exception e) {
            data.uploadedFiles = new HashMap<>();
        }

        return data;
    }

    private void initViews() {
        xBandChannel = findViewById(R.id.x_band_source_channel);
        xBandType = findViewById(R.id.x_band_source_type);
        xBandInfo = findViewById(R.id.x_band_source_info);
        sBandChannel = findViewById(R.id.s_band_source_channel);
        sBandType = findViewById(R.id.s_band_source_type);
        sBandInfo = findViewById(R.id.s_band_source_info);
        masterChannel = findViewById(R.id.master_ecdis_source_channel);
        masterType = findViewById(R.id.master_ecdis_source_type);
        masterInfo = findViewById(R.id.master_ecdis_source_info);
        backupChannel = findViewById(R.id.backup_ecdis_source_channel);
        backupType = findViewById(R.id.backup_ecdis_source_type);
        backupInfo = findViewById(R.id.backup_ecdis_source_info);
        btnPrev = findViewById(R.id.btn_prev);
        btnSubmit = findViewById(R.id.btn_submit);
    }

    private void setupFileUploads() {
        String[] fields = {"makerReport", "prevCoc", "dataUpload"};
        for (String field : fields) {
            fileUploads.put(field, new ArrayList<>());
            int recyclerId = getResources().getIdentifier(
                    "recycler_" + field, "id", getPackageName());

            if (recyclerId == 0) continue;

            RecyclerView rv = findViewById(recyclerId);
            rv.setLayoutManager(new GridLayoutManager(this, 3));
            FilePreviewAdapter adapter =
                    new FilePreviewAdapter(
                            this,
                            field,
                            fileUploads.get(field),
                            (fName, file) -> removeFile(fName, file)
                    );
            rv.setAdapter(adapter);
            fileAdapters.put(field, adapter);

            int btnId = getResources().getIdentifier(
                    "btn_pick_" + field, "id", getPackageName());
            if (btnId != 0) {
                findViewById(btnId).setOnClickListener(v -> pickFile(field));
            }
        }
    }

    private void setupFilePicker() {
        filePickerLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.GetContent(),
                        uri -> {
                            if (uri == null || currentFieldName == null) return;
                            try {
                                FileUploadModel model =
                                        new FileUploadModel(
                                                getFileName(uri),
                                                uri.toString(),
                                                System.currentTimeMillis()
                                        );
                                fileUploads.get(currentFieldName).add(model);
                                fileAdapters.get(currentFieldName).notifyDataSetChanged();
                            } catch (Exception e) {
                                Toast.makeText(Page3.this, "Error adding file", Toast.LENGTH_SHORT).show();
                            }
                        });
    }

//    private void pickFile(String fieldName) {
//        currentFieldName = fieldName;
//
//        int limit = FILE_LIMITS.get(fieldName);
//        int currentCount = fileUploads.get(fieldName).size();
//
//        if (currentCount >= limit) {
//            Toast.makeText(this,
//                    "Maximum " + limit + " files allowed for this section",
//                    Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        filePickerLauncher.launch("*/*");
//    }

    private void pickFile(String fieldName) {
        currentFieldName = fieldName;
        filePickerLauncher.launch("*/*");
    }

    private void restoreFilesFromInspectionData() {
        if (inspectionData == null || inspectionData.uploadedFiles == null) {
            return;
        }

        try {
            for (String field : inspectionData.uploadedFiles.keySet()) {
                if (!fileUploads.containsKey(field)) continue;
                fileUploads.get(field).clear();

                List<String> uriList = inspectionData.uploadedFiles.get(field);
                if (uriList == null) continue;

                for (String uriStr : uriList) {
                    try {
                        FileUploadModel model = new FileUploadModel(
                                getFileName(Uri.parse(uriStr)),
                                uriStr,
                                System.currentTimeMillis()
                        );
                        fileUploads.get(field).add(model);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                fileAdapters.get(field).notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeFile(String fieldName, FileUploadModel file) {
        if (fileUploads.containsKey(fieldName)) {
            fileUploads.get(fieldName).remove(file);
            fileAdapters.get(fieldName).notifyDataSetChanged();
            Toast.makeText(this, "File removed", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileName(Uri uri) {
        String result = "file";
        if ("content".equals(uri.getScheme())) {
            Cursor cursor = getContentResolver()
                    .query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index != -1) result = cursor.getString(index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) cursor.close();
            }
        }
        return result;
    }

    private boolean validatePage3() {
        if (isEmpty(xBandChannel)) return error("Enter X Band Channel");
        if (isEmpty(xBandType)) return error("Enter X Band Type");
        if (isEmpty(xBandInfo)) return error("Enter X Band Info");
        if (isEmpty(sBandChannel)) return error("Enter S Band Channel");
        if (isEmpty(sBandType)) return error("Enter S Band Type");
        if (isEmpty(sBandInfo)) return error("Enter S Band Info");
        if (isEmpty(masterChannel)) return error("Enter Master ECDIS Channel");
        if (isEmpty(masterType)) return error("Enter Master ECDIS Type");
        if (isEmpty(masterInfo)) return error("Enter Master ECDIS Info");
        if (isEmpty(backupChannel)) return error("Enter Backup ECDIS Channel");
        if (isEmpty(backupType)) return error("Enter Backup ECDIS Type");
        if (isEmpty(backupInfo)) return error("Enter Backup ECDIS Info");

        if (fileUploads.get("makerReport") == null || fileUploads.get("makerReport").isEmpty())
            return error("Maker Report required");
        if (fileUploads.get("prevCoc") == null || fileUploads.get("prevCoc").isEmpty())
            return error("Prev Year COC required");
        if (fileUploads.get("dataUpload") == null || fileUploads.get("dataUpload").isEmpty())
            return error("Data Upload required");

        return true;
    }

    private boolean isEmpty(EditText e) {
        return e.getText().toString().trim().isEmpty();
    }

    private boolean error(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        autoSaveToPreferences();
    }

    private void autoSaveToPreferences() {
        if (inspectionData == null) inspectionData = new InspectionData();
        saveData();

        SharedPreferences prefs = getSharedPreferences("inspection_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Save all Page3 fields
        editor.putString("xBandSourceChannel", inspectionData.xBandSourceChannel);
        editor.putString("xBandSourceType", inspectionData.xBandSourceType);
        editor.putString("xBandSourceInfo", inspectionData.xBandSourceInfo);

        editor.putString("sBandSourceChannel", inspectionData.sBandSourceChannel);
        editor.putString("sBandSourceType", inspectionData.sBandSourceType);
        editor.putString("sBandSourceInfo", inspectionData.sBandSourceInfo);

        editor.putString("masterEcdisSourceChannel", inspectionData.masterEcdisSourceChannel);
        editor.putString("masterEcdisSourceType", inspectionData.masterEcdisSourceType);
        editor.putString("masterEcdisSourceInfo", inspectionData.masterEcdisSourceInfo);

        editor.putString("backupEcdisSourceChannel", inspectionData.backupEcdisSourceChannel);
        editor.putString("backupEcdisSourceType", inspectionData.backupEcdisSourceType);
        editor.putString("backupEcdisSourceInfo", inspectionData.backupEcdisSourceInfo);

        // File uploads - store as JSON
        String fileUploadsJson = new com.google.gson.Gson()
                .toJson(inspectionData.uploadedFiles);
        editor.putString("uploadedFiles", fileUploadsJson);

        editor.apply();
    }

    private void saveData() {
        inspectionData.xBandSourceChannel = xBandChannel.getText().toString();
        inspectionData.xBandSourceType = xBandType.getText().toString();
        inspectionData.xBandSourceInfo = xBandInfo.getText().toString();
        inspectionData.sBandSourceChannel = sBandChannel.getText().toString();
        inspectionData.sBandSourceType = sBandType.getText().toString();
        inspectionData.sBandSourceInfo = sBandInfo.getText().toString();
        inspectionData.masterEcdisSourceChannel = masterChannel.getText().toString();
        inspectionData.masterEcdisSourceType = masterType.getText().toString();
        inspectionData.masterEcdisSourceInfo = masterInfo.getText().toString();
        inspectionData.backupEcdisSourceChannel = backupChannel.getText().toString();
        inspectionData.backupEcdisSourceType = backupType.getText().toString();
        inspectionData.backupEcdisSourceInfo = backupInfo.getText().toString();

        if (inspectionData.uploadedFiles == null)
            inspectionData.uploadedFiles = new HashMap<>();

        for (String key : fileUploads.keySet()) {
            List<String> uriList = new ArrayList<>();
            for (FileUploadModel f : fileUploads.get(key)) {
                uriList.add(f.fileUri);
            }
            inspectionData.uploadedFiles.put(key, uriList);
        }
    }

    private void setupButtons() {
        btnPrev.setOnClickListener(v -> finish());

        btnSubmit.setOnClickListener(v -> {
            if (!validatePage3()) return;

            saveData();

            try {
//                saveData();

//                SharedPreferences prefs = getSharedPreferences("inspection_data", MODE_PRIVATE);
//                prefs.edit().clear().apply();
//
//                Intent home = new Intent(Page3.this, Main.class);
//                startActivity(home);
//                finishAffinity();
//
//            } catch (Exception e) {
//                error("Submission error: " + e.getMessage());
//                e.printStackTrace();
//            }
//        });
//    }

                Gson gson = new Gson();
                String json = gson.toJson(inspectionData);

                RequestBody jsonBody =
                        RequestBody.create(
                                okhttp3.MediaType.parse("application/json"),
                                json
                        );

                Map<String, List<okhttp3.MultipartBody.Part>> fileMap =
                        prepareMultipartFiles();

                Page3Api api =
                        ApiClient.getInstance().create(Page3Api.class);

                Call<Page3Response> call =
                        api.submitFinalInspection(
                                inspectionData.inspectionId,
                                jsonBody,
                                fileMap
                        );

                call.enqueue(new retrofit2.Callback<Page3Response>() {
                    @Override
                    public void onResponse(Call<Page3Response> call,
                                           retrofit2.Response<Page3Response> response) {

                        if (response.isSuccessful() && response.body() != null
                                && response.body().success) {

                            // ✅ Clear autosave data
                            getSharedPreferences("inspection_data", MODE_PRIVATE)
                                    .edit().clear().apply();

                            Toast.makeText(Page3.this,
                                    "Inspection Submitted Successfully",
                                    Toast.LENGTH_LONG).show();

                            Intent home = new Intent(Page3.this, Main.class);
                            startActivity(home);
                            finishAffinity();

                        } else {
                            error("Server rejected submission: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Page3Response> call, Throwable t) {
                        error("Submission failed: " + t.getMessage());
                    }
                });

            } catch (Exception e) {
                error("Final submit error: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private Map<String, List<MultipartBody.Part>> prepareMultipartFiles() {

        Map<String, List<MultipartBody.Part>> map = new HashMap<>();

        for (String key : fileUploads.keySet()) {

            List<MultipartBody.Part> parts = new ArrayList<>();

            for (FileUploadModel f : fileUploads.get(key)) {

                Uri uri = Uri.parse(f.fileUri);
                java.io.File file = new java.io.File(uri.getPath());

                RequestBody requestFile =
                        RequestBody.create(
                                okhttp3.MediaType.parse("multipart/form-data"),
                                file
                        );

                MultipartBody.Part body =
                        MultipartBody.Part.createFormData(
                                key,
                                file.getName(),
                                requestFile
                        );

                parts.add(body);
            }

            map.put(key, parts);
        }

        return map;
    }
}
