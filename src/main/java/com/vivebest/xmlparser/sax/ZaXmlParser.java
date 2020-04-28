package com.vivebest.xmlparser.sax;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.vivebest.xmlparser.util.ConnUtils;
import com.vivebest.xmlparser.vo.Address;
import com.vivebest.xmlparser.vo.EntityVo;

/**
 * SAX解析XML
 * 
 */
public class ZaXmlParser {

	public static void main(String[] args) throws Exception {
		// step 1: 获得SAX解析器工厂实例
		SAXParserFactory factory = SAXParserFactory.newInstance();

		// step 2: 获得SAX解析器实例
		SAXParser parser = factory.newSAXParser();

		// step 3: 开始进行解析
		// 传入待解析的文档的处理器
		String path = "data/za/";
		File file = new File(path);
        File[] tempList = file.listFiles();
        long allBegin = System.currentTimeMillis();
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                //文件名
                String fileName = tempList[i].getAbsolutePath();
                long b = System.currentTimeMillis();
                System.out.println("---------------开始解析文件:" + fileName + "---------------------");
                parser.parse(new File(fileName), new ZaSAXHandler());
                long e = System.currentTimeMillis();
                System.out.println("解析文件 " + fileName + "共耗时:" + (e - b));
            }
        }
        long allEnd = System.currentTimeMillis();
        System.out.println("解析" + tempList.length + "个文件共耗时:" + (allEnd - allBegin));
	}
}

class ZaSAXHandler extends DefaultHandler {
	
	private List<EntityVo> entityVoList = new ArrayList<EntityVo>();
	
	private String versionId = null;
	
	// 使用栈这个数据结构来保存
	private Stack<String> stack = new Stack<String>();

	private EntityVo entityVo = new EntityVo();
	private Address addressVo = new Address();

	private List<String> charactersContainList = Arrays.asList(new String[]{"version","name","listId","listCode","entityType","createdDate","lastUpdateDate","source","OriginalSource","aliase","nativeCharName","program","sdf","addresses","address1","city","country","countryName","postalCode","childId"});
	private List<String> endElementContainList = Arrays.asList(new String[]{"entity","entities","gwl"});
	
	private int count = 0;
	
	@Override
	public void startDocument() throws SAXException {
		System.out.println("start document -> parse begin---");
	}

	@Override
	public void endDocument() throws SAXException {
		System.out.println("end document -> parse end---");
		if(entityVoList.size() == 0) {
			return ;
		}
		multiThreadExecBatchSql(entityVoList,true);
	}

	private void multiThreadExecBatchSql(List<EntityVo> entityVoList,boolean flag) {
		System.out.println("多线程执行批量....");
		
        // 每500条数据开启一条线程
        int threadSize = 5000;
        // 总数据条数
        int dataSize = entityVoList.size();
        // 线程数
        int threadNum = dataSize / threadSize + 1;
        // 定义标记,过滤threadNum为整数
        boolean special = dataSize % threadSize == 0;
        // 创建一个线程池
        ExecutorService exec = Executors.newFixedThreadPool(threadNum);
        // 定义一个任务集合
        List<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>();
        Callable<Integer> task = null;
        List<EntityVo> cutList = null;
        // 确定每条线程的数据
        for (int i = 0; i < threadNum; i++) {
            if (i == threadNum - 1) {
                if (special) {
                    break;
                }
                cutList = entityVoList.subList(threadSize * i, dataSize);
            } else {
                cutList = entityVoList.subList(threadSize * i, threadSize * (i + 1));
            }
            // System.out.println("第" + (i + 1) + "组：" + cutList.toString());
            final List<EntityVo> listEntityVo = cutList;
            task = new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                	execBatchSql(listEntityVo,flag);
                    return 1;
                }
            };
            // 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
            tasks.add(task);
        }
        List<Future<Integer>> results = null;
		try {
			results = exec.invokeAll(tasks);
			for (Future<Integer> future : results) {
	            System.out.println(future.get());//等待完成后再往下执行
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        // 关闭线程池
        exec.shutdown();
		
	}
	
	
	private void execBatchSql(List<EntityVo> entityVoList2, boolean b) {
		
		int all = 0;
		
		System.out.println("执行批量插入...");
		Connection conn = ConnUtils.getConnection();
		PreparedStatement stst = null;
		
		String sql = "INSERT INTO " +"fgadm.zaentity(\r\n" + 
				"            versionid, entityid, entityversionid, name, listid, listcode, \r\n" + 
				"            entitytype, createddate, lastupdatedate, source, originalsource, \r\n" + 
				"            aliases, nativecharnames, programs, sdfs, addresses, otherids)\r\n" + 
				"    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		
		try {
			stst = conn.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for(int i = 0 ; i < entityVoList2.size();i++) {
			EntityVo entityVo = entityVoList2.get(i);
			
			String versionId = entityVo.getVersionId();
			String entityId = entityVo.getEntityId();
			String entityVersionId = entityVo.getEntityVersionId();
			String name = entityVo.getName();
			String listId = entityVo.getListId();
			String listCode = entityVo.getListCode();
			String entityType = entityVo.getEntityType();
			String createdDate = entityVo.getCreatedDate();
			String lastUpdateDate = entityVo.getLastUpdateDate();
			String source = entityVo.getSource();
			String OriginalSource = entityVo.getOriginalSource();
			List<String> aliases = entityVo.getAliases();
			List<String> nativeCharNames = entityVo.getNativeCharNames();
			List<String> programs = entityVo.getPrograms();
			List<String> sdfs = entityVo.getSdfs();
			List<Address> addresses = entityVo.getAddresses();
			List<String> otherIds = entityVo.getOtherIds();
			
			String aliasesStr = StringUtils.join(aliases.toArray(),";");
			String nativeCharNamesStr = StringUtils.join(nativeCharNames.toArray(),";");
			String programsStr = StringUtils.join(programs.toArray(),";");
			String sdfsStr = StringUtils.join(sdfs.toArray(),";");
			String addressesStr = StringUtils.join(addresses.toArray(),";");
			String otherIdsStr = StringUtils.join(otherIds.toArray(),";");
			
			try {
				stst.setString(1, versionId);
				stst.setString(2, entityId);
				stst.setString(3, entityVersionId);
				stst.setString(4, name);
				stst.setString(5, listId);
				stst.setString(6, listCode);
				stst.setString(7, entityType);
				stst.setString(8, createdDate);
				stst.setString(9, lastUpdateDate);
				stst.setString(10, source);
				stst.setString(11, OriginalSource);
				stst.setString(12, aliasesStr);
				stst.setString(13, nativeCharNamesStr);
				stst.setString(14, programsStr);
				stst.setString(15, sdfsStr);
				stst.setString(16, addressesStr);
				stst.setString(17, otherIdsStr);
				
				stst.addBatch();
				all++;    
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			stst.executeBatch();
			System.out.println("All down : " + all);
//			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(b) {
				ConnUtils.release(conn,stst, null);
			}else {
				ConnUtils.release(null,stst, null);
			}
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		stack.push(qName);

		if ("entity".equals(qName)) {
			// 处理属性
			for (int i = 0; i < attributes.getLength(); ++i) {
				String attrName = attributes.getQName(i);
				String attrValue = attributes.getValue(i);
				if ("id".equals(attrName)) {
					entityVo.setEntityId(attrValue);
					continue;
				}
				if ("version".equals(attrName)) {
					entityVo.setEntityVersionId(attrValue);
					continue;
				}
			}
		}
		return;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {

		// 取出标签名
		String tag = stack.peek();
		//如果不包含在内直接跳过，提高效率
		if(!charactersContainList.contains(tag)) {
			return ;
		}
		
		// 代表批次ID
		if ("version".equals(tag)) {
			entityVo.setVersionId(new String(ch, start, length));
			versionId = new String(ch, start, length);
			return;
		}
		if ("name".equals(tag)) {
			entityVo.setName(new String(ch, start, length));
			return;
		}
		if ("listId".equals(tag)) {
			entityVo.setListId(new String(ch, start, length));
			return;
		}
		if ("listCode".equals(tag)) {
			entityVo.setListCode(new String(ch, start, length));
			return;
		}
		if ("entityType".equals(tag)) {
			entityVo.setEntityType(new String(ch, start, length));
			return;
		}
		if ("createdDate".equals(tag)) {
			entityVo.setCreatedDate(new String(ch, start, length));
			return;
		}
		if ("lastUpdateDate".equals(tag)) {
			entityVo.setLastUpdateDate(new String(ch, start, length));
			return;
		}
		if ("source".equals(tag)) {
			entityVo.setSource(new String(ch, start, length));
			return;
		}
		if ("OriginalSource".equals(tag)) {
			entityVo.setOriginalSource(new String(ch, start, length));
			return;
		}
		if ("alias".equals(tag)) {
			entityVo.getAliases().add(new String(ch, start, length));
			return;
		}
		if ("nativeCharName".equals(tag)) {
			entityVo.getNativeCharNames().add(new String(ch, start, length));
			return;
		}
		if ("program".equals(tag)) {
			entityVo.getPrograms().add(new String(ch, start, length));
			return;
		}
		if ("sdf".equals(tag)) {
			entityVo.getSdfs().add(new String(ch, start, length));
			return;
		}
		if ("addresses".equals(tag)) {
			addressVo = new Address();
			entityVo.getAddresses().add(addressVo);
			return;
		}
		if ("address1".equals(tag)) {
			addressVo.setAddress1(new String(ch, start, length));
			return;
		}
		if ("city".equals(tag)) {
			addressVo.setCity(new String(ch, start, length));
			return;
		}
		if ("country".equals(tag)) {
			addressVo.setCountry(new String(ch, start, length));
			return;
		}
		if ("countryName".equals(tag)) {
			addressVo.setCountryName(new String(ch, start, length));
			return;
		}
		if ("postalCode".equals(tag)) {
			addressVo.setPostalCode(new String(ch, start, length));
			return;
		}
		if ("childId".equals(tag)) {
			entityVo.getOtherIds().add(new String(ch, start, length));
			return;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		String tag = stack.pop();// 表示该元素解析完毕，需要从栈中弹出标签
		
		//如果不包含在内直接跳过，提高效率
		if(!endElementContainList.contains(tag)) {
			return ;
		}
		if("entity".equals(tag)) {
//			System.out.println("解析出的entity:" + entityVo.toString());
			entityVoList.add(entityVo);
			if(entityVoList.size() >= 10000) {
				multiThreadExecBatchSql(entityVoList, false);
				entityVoList.clear();
			}
			count++ ;
			entityVo = new EntityVo();
			entityVo.setVersionId(versionId);
			return ;
		}
		if("entities".equals(tag)) {
			System.out.println("entities--xml文件解析完毕");
			return ;
		}
		if("gwl".equals(tag)) {
			System.out.println("gwl--xml文件解析完毕");
			System.out.println("解析总条数为: " + count);
//			entityVoList = new ArrayList<EntityVo>();
//			versionId = null; 
//			stack = new Stack<String>();
//			entityVo = new EntityVo();
//			addressVo = new Address();
			
			return ;
		}
	}
}