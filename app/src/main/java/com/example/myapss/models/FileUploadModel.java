//package com.example.myapss.models;

//public class FileUploadModel {
//    public String fileName;
//    public String fileUri;
//    public String fileType; // image, pdf, document
//    public long fileSize;
//    public long timestamp;
//
//    public FileUploadModel(String fileName, String fileUri, long timestamp) {
//        this.fileName = fileName;
//        this.fileUri = fileUri;
//        this.timestamp = timestamp;
//    }
//
//    public FileUploadModel(String fileName, String fileUri, String fileType, long fileSize, long timestamp) {
//        this.fileName = fileName;
//        this.fileUri = fileUri;
//        this.fileType = fileType;
//        this.fileSize = fileSize;
//        this.timestamp = timestamp;
//    }
//
//    // ✅ Getters
//    public String getFileName() {
//        return fileName;
//    }
//
//    public String getFileUri() {
//        return fileUri;
//    }
//
//    public String getFileType() {
//        return fileType;
//    }
//
//    public long getFileSize() {
//        return fileSize;
//    }
//
//    public long getTimestamp() {
//        return timestamp;
//    }
//}

//public class FileUploadModel {
//
//    private String fileName;
//    private String fileUri;
//    private String timestamp;
//    private String fileType;
//
//    public FileUploadModel(String fileName, String fileUri, long timestamp) {
//        this.fileName = fileName;
//        this.fileUri = fileUri;
//        this.timestamp = timestamp;
//    }
//
//    public String getFileName() {
//        return fileName;
//    }
//
//    public String getFileUri() {
//        return fileUri;
//    }
//
//    public long getTimestamp() {
//        return timestamp;
//    }
//    public String setFileUri() {
//        return fileUri;
//    }
//    public String setFileType() {
//        return fileType;
//    }
//    public String SetTimrdtamp() {
//        return timestamp;
//    }
//}

//package com.example.myapss.models;
//
//public class FileUploadModel {
//    public String fileName;
//    public String fileUri;
//    public String fileType; // image, pdf, document
//    public long fileSize;
//    public long timestamp;
//    public String getFileUri;
//
//    public FileUploadModel(String fileName, String string, long l) {
//        this.fileName = this.fileName;
//        this.fileUri = fileUri;
//        this.timestamp = timestamp;
//    }
//
////    public FileUploadModel(String fileName, String fileUri, String fileType, long fileSize, long timestamp) {
////        this.fileName = fileName;
////        this.fileUri = fileUri;
////        this.fileType = fileType;
////        this.fileSize = fileSize;
////        this.timestamp = timestamp;
////    }
//
//    // ✅ Getters
//    public String getFileName() {
//        return fileName;
//    }
//
//    public String getFileUri(String string) {
//        return fileUri;
//    }
//
//    public String getFileType(String file) {
//        return fileType;
//    }
//
//    public long getFileSize() {
//        return fileSize;
//    }
//
//    public long getTimestamp(long l) {
//        return timestamp;
//    }
//}

package com.example.myapss.models;

import java.io.Serializable;

public class FileUploadModel implements Serializable {

    private String fileName;
    public String fileUri;
    private String fileType;   // image, pdf, document
    private long fileSize;
    private long timestamp;

    // ✅ MAIN CONSTRUCTOR (USED IN PAGE2)
    public FileUploadModel(String fileName, String fileUri, long timestamp) {
        this.fileName = fileName;
        this.fileUri = fileUri;
        this.timestamp = timestamp;
    }

    // ✅ OPTIONAL FULL CONSTRUCTOR (FOR FUTURE API UPLOAD)
    public FileUploadModel(String fileName,
                           String fileUri,
                           String fileType,
                           long fileSize,
                           long timestamp) {
        this.fileName = fileName;
        this.fileUri = fileUri;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.timestamp = timestamp;
    }

    // ✅ REQUIRED EMPTY CONSTRUCTOR (FOR SERIALIZATION)
    public FileUploadModel() {}

    // ✅ GETTERS
    public String getFileName() {
        return fileName;
    }

    public String getFileUri() {
        return fileUri;
    }

    public String getFileType() {
        return fileType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public long getTimestamp() {
        return timestamp;
    }

    // ✅ SETTERS (OPTIONAL BUT SAFE)
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileUri(String fileUri) {
        this.fileUri = fileUri;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

