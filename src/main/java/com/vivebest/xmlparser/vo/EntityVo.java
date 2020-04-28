package com.vivebest.xmlparser.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * XML实体信息封装 
 * @author VNB
 *
 */
public class EntityVo {

	private String versionId;
	private String entityId;
	private String entityVersionId;
    private String name;
    private String listId;
    private String listCode;
    private String entityType;
    private String createdDate;
    private String lastUpdateDate;
    private String source;
    private String OriginalSource;
    private List<String> aliases; //alias
    private List<String> nativeCharNames;//nativeCharName
    private List<String> programs;//program
    private List<String> sdfs;//sdf
    private List<Address> addresses;
    private List<String> otherIds;
    
    public EntityVo() {
    	this.aliases = new ArrayList<String>(); //alias
        this.nativeCharNames = new ArrayList<String>();//nativeCharName
        this.programs = new ArrayList<String>();//program
        this.sdfs = new ArrayList<String>();//sdf
        this.addresses = new ArrayList<Address>();//address
        this.otherIds = new ArrayList<String>();//childId
    }
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getListId() {
		return listId;
	}
	public void setListId(String listId) {
		this.listId = listId;
	}
	public String getListCode() {
		return listCode;
	}
	public void setListCode(String listCode) {
		this.listCode = listCode;
	}
	public String getEntityType() {
		return entityType;
	}
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getOriginalSource() {
		return OriginalSource;
	}
	public void setOriginalSource(String originalSource) {
		OriginalSource = originalSource;
	}
	public List<String> getAliases() {
		return aliases;
	}
	public void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}
	public List<String> getNativeCharNames() {
		return nativeCharNames;
	}
	public void setNativeCharNames(List<String> nativeCharNames) {
		this.nativeCharNames = nativeCharNames;
	}
	public List<String> getPrograms() {
		return programs;
	}
	public void setPrograms(List<String> programs) {
		this.programs = programs;
	}
	public List<String> getSdfs() {
		return sdfs;
	}
	public void setSdfs(List<String> sdfs) {
		this.sdfs = sdfs;
	}
	public List<Address> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}
	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
    
	public String getEntityVersionId() {
		return entityVersionId;
	}
	public void setEntityVersionId(String entityVersionId) {
		this.entityVersionId = entityVersionId;
	}
	
	public List<String> getOtherIds() {
		return otherIds;
	}

	public void setOtherIds(List<String> otherIds) {
		this.otherIds = otherIds;
	}

	/**
	 * 清空,初始化
	 */
	public void init() {
//		this.versionId = null; //versionId无需清空
		this.entityId = null;
		this.name = null;
		this.listId = null;
		this.listCode = null;
		this.entityType = null;
		this.createdDate = null;
		this.lastUpdateDate = null;
		this.source = null;
		this.OriginalSource = null;
		this.aliases.clear();//alias
		this.nativeCharNames.clear();//nativeCharName
		this.programs.clear();//program
		this.sdfs.clear();//sdf
		this.addresses.clear();
		this.entityVersionId = null;
	}
	
	@Override
	public String toString() {
		return "EntityVo [versionId=" + versionId + ", entityId=" + entityId + ", entityVersionId=" + entityVersionId
				+ ", name=" + name + ", listId=" + listId + ", listCode=" + listCode + ", entityType=" + entityType
				+ ", createdDate=" + createdDate + ", lastUpdateDate=" + lastUpdateDate + ", source=" + source
				+ ", OriginalSource=" + OriginalSource + ", aliases=" + aliases + ", nativeCharNames=" + nativeCharNames
				+ ", programs=" + programs + ", sdfs=" + sdfs + ", addresses=" + addresses + ", otherIds=" + otherIds
				+ "]";
	}

	public static void main(String[] args) {
	}
	
}
