package com.yq.bc.pojo.nio;

import java.io.File;
import java.util.Date;
import java.util.Map;

/**
 * 成都一方思致科技有限公司
 *
 * @author yuanqi
 * @description
 * @date 2020/1/19
 */
public class FileInfo {

    private static final long serialVersionUID = 1L;
    private String keyId;
    private String tenantId;
    private String oriFileName;
    private String fileName = "";
    private Long fileByte;
    private String fileSuplusType;
    private String fileContentType;
    private boolean isCompressed = false;
    private String storePath;
    private File file;
    private String remoteMediaID;
    private String remoteMediaName;
    private String remoteAddress = "";
    private String remoteFileName;
    private String remoteFileId;
    private String belongCode;
    private String belongID;
    private String remoteBucket;
    private String remotePath;
    private String remoteFolder;
    private String addUser;
    private String mdifyUser;
    private Date addTime = new Date();
    private Date modifyTime = new Date();
    private int isDelete = 0;
    private String security;
    private String signature = "";
    private Long uploadFileByte;
    private Map<String, Object> attrs;

    public FileInfo() {
    }

    public FileInfo(String storePath) {
        this.storePath = storePath;
    }

    public String getKeyId() {
        return this.keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getOriFileName() {
        return this.oriFileName;
    }

    public void setOriFileName(String oriFileName) {
        this.oriFileName = oriFileName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileByte() {
        return this.fileByte;
    }

    public void setFileByte(Long fileByte) {
        this.fileByte = fileByte;
    }

    public String getFileSuplusType() {
        return this.fileSuplusType;
    }

    public void setFileSuplusType(String fileSuplusType) {
        this.fileSuplusType = fileSuplusType;
    }

    public String getFileContentType() {
        return this.fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public boolean isCompressed() {
        return this.isCompressed;
    }

    public void setCompressed(boolean compressed) {
        this.isCompressed = compressed;
    }

    public String getStorePath() {
        return this.storePath;
    }

    public void setStorePath(String storePath) {
        this.storePath = storePath;
    }

    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getRemoteMediaID() {
        return this.remoteMediaID;
    }

    public void setRemoteMediaID(String remoteMediaID) {
        this.remoteMediaID = remoteMediaID;
    }

    public String getRemoteMediaName() {
        return this.remoteMediaName;
    }

    public void setRemoteMediaName(String remoteMediaName) {
        this.remoteMediaName = remoteMediaName;
    }

    public String getRemoteAddress() {
        return this.remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public String getRemoteFileName() {
        return this.remoteFileName;
    }

    public void setRemoteFileName(String remoteFileName) {
        this.remoteFileName = remoteFileName;
    }

    public String getRemoteFileId() {
        return this.remoteFileId;
    }

    public void setRemoteFileId(String remoteFileId) {
        this.remoteFileId = remoteFileId;
    }

    public String getBelongCode() {
        return this.belongCode;
    }

    public void setBelongCode(String belongCode) {
        this.belongCode = belongCode;
    }

    public String getBelongID() {
        return this.belongID;
    }

    public void setBelongID(String belongID) {
        this.belongID = belongID;
    }

    public String getRemoteBucket() {
        return this.remoteBucket;
    }

    public void setRemoteBucket(String remoteBucket) {
        this.remoteBucket = remoteBucket;
    }

    public String getRemotePath() {
        return this.remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public String getRemoteFolder() {
        return this.remoteFolder;
    }

    public void setRemoteFolder(String remoteFolder) {
        this.remoteFolder = remoteFolder;
    }

    public String getAddUser() {
        return this.addUser;
    }

    public void setAddUser(String addUser) {
        this.addUser = addUser;
    }

    public String getMdifyUser() {
        return this.mdifyUser;
    }

    public void setMdifyUser(String mdifyUser) {
        this.mdifyUser = mdifyUser;
    }

    public Date getAddTime() {
        return this.addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public int getIsDelete() {
        return this.isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public String getSecurity() {
        return this.security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public Map<String, Object> getAttrs() {
        return this.attrs;
    }

    public void setAttrs(Map<String, Object> attrs) {
        this.attrs = attrs;
    }

    public String getSignature() {
        return this.signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Long getUploadFileByte() {
        return this.uploadFileByte;
    }

    public void setUploadFileByte(Long uploadFileByte) {
        this.uploadFileByte = uploadFileByte;
    }

}
