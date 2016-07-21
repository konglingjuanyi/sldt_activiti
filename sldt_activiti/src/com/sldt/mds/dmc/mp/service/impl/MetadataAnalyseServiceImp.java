package com.sldt.mds.dmc.mp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sldt.framework.common.PageResults;
import com.sldt.mds.dmc.mp.dao.MetadataAnalyseDao;
import com.sldt.mds.dmc.mp.service.MetadataAnalyseService;
import com.sldt.mds.dmc.mp.vo.AnalyseFlow;

@Service(value="metaAnalyService")
@Transactional(readOnly=true)
public class MetadataAnalyseServiceImp implements MetadataAnalyseService{
	
	private static Log log = LogFactory.getLog(MetadataAnalyseServiceImp.class);
	@Resource(name="metaAnalyDao")
	private MetadataAnalyseDao metaAnalyDao;

	@Override
	public void delImpactSide(PageResults page) {
           metaAnalyDao.delImpactSide(page);		
	}

	@Override
	public List<Map<String, Object>> getInstanceId(PageResults page) {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		try {
			list = getSNADBNamespace(page);
			String context = page.getParamStr("context");
			if(context!=null){
				
				String contexts [] = context.split("/");
				
				for (int i = 1; i < contexts.length; i++) {
					//i==0的时候属于空,i==1为系统，系统已经取到instanceId
					if(i==0||i==1){
						continue;
					}else if(i==2){
						page.setParameter("classifer", "PASchema");
					}else if(i==3){
						page.setParameter("classifer", "PAModule");
					}else if(i==4){
						page.setParameter("classifer", "PATable");
					}else if(i==5){
						page.setParameter("classifer", "PAColumn");
					}
					if(list.size()>0){
						Map<String,Object> obj = list.get(0);
						page.setParameter("instanceCode", contexts[i]);
						page.setParameter("parentId", obj.get("INSTANCE_ID"));
						list = metaAnalyDao.getInstanceId(page);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> getSNADBNamespace(PageResults page) {
		
		return metaAnalyDao.getSNADBNamespace(page);
	}

	@Override
	public List<Map<String, Object>> getImpactSide(PageResults page) {
		//获取当前元数据影响方线条数据
		List<AnalyseFlow> flows = getAnalyseFlow(page);
		
		List<Map<String,Object>> sysMaps = new ArrayList<Map<String,Object>>();
		String ids = "";
		for (AnalyseFlow analyseFlow : flows) {
			String downupnamespace = analyseFlow.getDownupnamespace();
			String ns [] = downupnamespace.split("/");
			String sysNs = "";
			//获取表级唯一ID
			if(ns.length>=5){
				ids+=",'"+ns[5]+"'";
			}
			//获取系统
			if(ns.length >= 3){
				for (int i = 0; i < ns.length; i++) {
					if(i>=3){
						break;
					}
					//第0个为空
					if(i==0){
						continue;
					}
					sysNs = sysNs+"/"+ns[i];
				} 
			}
			page.setParameter("namespace", sysNs);
			//查询所影响的系统
			List<Map<String,Object>> sysList = metaAnalyDao.getImpactSys(page);
			//如果查询到系统
			if(sysList.size()>0){
				if(sysMaps.size()<=0){
					sysMaps.add(sysList.get(0));
				}else{
					boolean flag = true;
					for (Map<String, Object> sysMap : sysMaps) {
						//如果已经存在当前的系统code
						if(sysMap.get("INSTANCE_CODE").equals(sysList.get(0).get("INSTANCE_CODE"))){
							flag = false;
							break;
						}
					}
					if(flag){
						sysMaps.add(sysList.get(0));
					}
				}
			}
		}
		page.setParameter("ids", ids);
		/********查询当前影响方系统下的影响表*********/
		for (Map<String, Object> sysMap : sysMaps) {
			page.setParameter("namespace", sysMap.get("NAMESPACE"));
			sysMap.put("IMPACT_TABLE", metaAnalyDao.getImpactTable(page));
		}
		return sysMaps;
	}
	
	public List<AnalyseFlow> getAnalyseFlow(PageResults page){
		List<AnalyseFlow> flows = new ArrayList<AnalyseFlow>();
		String startMetadataId = page.getParamStr("instanceId");
//		page.setParameter("sessionId", "f560c1cb-4049-46d3-a692-93cc540dd0b6");
		try {
			flows = metaAnalyDao.getAnalyseFlow(page);
			flows = filterAnalyse(flows, startMetadataId);
			flows = getDirectImpactSide(flows, startMetadataId);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return flows;
	}

	@Override
	public List<Map<String, Object>> getImpactSys(PageResults page) {
		
		return metaAnalyDao.getImpactSys(page);
	}

	@Override
	public List<Map<String, Object>> getImpactTable(PageResults page) {
		return metaAnalyDao.getImpactTable(page);
	}
	
	public List<AnalyseFlow> filterAnalyse(List<AnalyseFlow> flows,String startMetadataId){
		//分析结果中的数据流，存于一个map中方便存取
		Map<String, List<AnalyseFlow>> flowMap  = new HashMap<String, List<AnalyseFlow>>();
		for (int i = 0, size = flows.size(); i < size; i++) {
			AnalyseFlow flow = flows.get(i);
			if (!flowMap.containsKey(flow.getUpstreamId())) {
				flowMap.put(flow.getUpstreamId(), new ArrayList<AnalyseFlow>());
			}
			flowMap.get(flow.getUpstreamId()).add(flow);
		}
		List<AnalyseFlow> newFlows = new ArrayList<AnalyseFlow>();
		AnalyseFlow root = new AnalyseFlow();
		root.setId(startMetadataId);
		if(flowMap==null||flowMap.size()<=0){
			return newFlows;
		}
		
		list(flowMap, startMetadataId, root);
		
		
		removeEtlAnalyseFlow(root.getChildAnalyseFlow(),startMetadataId,newFlows);
		 
		return newFlows;
	}
	
	public void list(Map<String, List<AnalyseFlow>> dbFlowMap,String id,AnalyseFlow root){
		List<AnalyseFlow> downFlows = dbFlowMap.get(id);
		
		for (AnalyseFlow analyseFlow : downFlows) {
			
			if(containsChild(root.getChildAnalyseFlow(), analyseFlow.getDownstreamId())){
				analyseFlow.setId(analyseFlow.getDownstreamId());
				analyseFlow.setpAnalyseFlow(root);
				root.addChildAnalyseFlow(analyseFlow);
			}
			
			if(dbFlowMap.get(analyseFlow.getDownstreamId())!=null){
				if(!containsParnet(analyseFlow, analyseFlow.getDownstreamId())){
					list(dbFlowMap,analyseFlow.getDownstreamId(),analyseFlow);
				}
			}
		}
	}
	
	public List<AnalyseFlow> getDirectImpactSide(List<AnalyseFlow> flows,String startMetadataId){
		List<AnalyseFlow> newFlows = new ArrayList<AnalyseFlow>();
		
		for (AnalyseFlow analyseFlow : flows) {
			if(analyseFlow.getUpstreamId().equals(startMetadataId)){
				newFlows.add(analyseFlow);
			}
		}
		
		return newFlows;
	}
	public void removeEtlAnalyseFlow(List<AnalyseFlow> downFlows,String startMetadataId,List<AnalyseFlow> newFlows){
		//用于存储上游节点为tablemapping对象的下游元数据信息 , 信息存储
		Map<String,String> downStreamMap = new HashMap<String, String>();
		for (AnalyseFlow analyseFlow : downFlows) {
			//判断是否为ETL数据线条
			if(analyseFlow.getDownUpstreamModelType().equals("0")){
				//不是相同的节点
				if(!analyseFlow.getDownstreamId().equals(startMetadataId)){
					System.out.println("start："+startMetadataId+"，找到结束点，结束点为：>>>"+analyseFlow.getDownstreamId());
					AnalyseFlow newAnalyseFlow = new AnalyseFlow();
					newAnalyseFlow.setUpstreamId(startMetadataId);
					newAnalyseFlow.setUpnamespace(analyseFlow.getUpnamespace());
					newAnalyseFlow.setDownstreamId(analyseFlow.getDownstreamId());
					newAnalyseFlow.setDownupnamespace(analyseFlow.getDownupnamespace());
					newAnalyseFlow.setDownstreamId(analyseFlow.getDownstreamId());
					newAnalyseFlow.setUpstreamModelType("0");
					newAnalyseFlow.setDownUpstreamModelType("0");
					newFlows.add(newAnalyseFlow);
					//判断上游是否为tableMapping元模型
					//用于控制将tableMapping元模型所关联的ETLtable模型对象替换成数据字典的table对象
					if(analyseFlow.getUpstreamClassifier().trim().toUpperCase().equals("PAETLTABLE")
							||analyseFlow.getUpstreamClassifier().trim().toUpperCase().equals("PAETLCOLUMN")
							||analyseFlow.getUpstreamClassifier().trim().toUpperCase().equals("PAETLSCHEMA")
							||analyseFlow.getUpstreamClassifier().trim().toUpperCase().equals("PAETLCATALOG")
							){
						//如果找到了数据字典table，并且它的上级是tableMapping，那么将startMetadataId替换成当前元数据ID
						downStreamMap.put(analyseFlow.getDownUpstreamCode(), analyseFlow.getDownstreamId());
					}
				}
			}
		}
		
		for (AnalyseFlow analyseFlow : downFlows) {
			//判断是否为ETL数据线条
			if(analyseFlow.getDownUpstreamModelType().equals("1")){
				//单条线中不允许存在相同instanceid，如果相同，则代表当前线条结束
//				sgFlow.put(analyseFlow.getDownstreamId(), analyseFlow);
				//不是同一节点
				if(!analyseFlow.getDownstreamId().equals(startMetadataId)){//否则继续往下寻找
					//判断下游是否为tableMapping元模型
					//用于控制将tableMapping元模型所关联的ETLtable模型对象替换成数据字典的table对象
					if((analyseFlow.getDownUpstreamClassifier().trim().toUpperCase().equals("TABLEMAPPING")
							||analyseFlow.getDownUpstreamClassifier().trim().toUpperCase().equals("COLUMNMAPPING")
							||analyseFlow.getDownUpstreamClassifier().trim().toUpperCase().equals("PAETLCATALOG")
							||analyseFlow.getDownUpstreamClassifier().trim().toUpperCase().equals("PAETLSCHEMA")
							
							)
							&& downStreamMap.containsKey(analyseFlow.getUpstreamCode())){
						System.out.println("因遇见下游mapping并且与上游数据字典CODE对象一致，故将："+startMetadataId+"替换成上游数据字典ID："+downStreamMap.get(analyseFlow.getUpstreamCode()));
						startMetadataId = downStreamMap.get(analyseFlow.getUpstreamCode());
					}
					System.out.println("start："+startMetadataId+"，未找到结束点，继续寻找下一个start为>>>"+analyseFlow.getDownstreamId());
					if(analyseFlow.getChildAnalyseFlow()!=null&&analyseFlow.getChildAnalyseFlow().size()>0){
						removeEtlAnalyseFlow(analyseFlow.getChildAnalyseFlow(),startMetadataId,newFlows);
					}else{
						String downId = getParnetMMId(analyseFlow, analyseFlow.getId());
						if(downId!=null){
							System.out.println("start："+startMetadataId+"，找到结束点，结束点为：>>>"+analyseFlow.getDownstreamId()+"，元数据回调父节点！");
							AnalyseFlow newAnalyseFlow = new AnalyseFlow();
							newAnalyseFlow.setUpstreamId(startMetadataId);
							newAnalyseFlow.setDownstreamId(downId);
							newAnalyseFlow.setUpnamespace(analyseFlow.getUpnamespace());
							newAnalyseFlow.setDownupnamespace(analyseFlow.getDownupnamespace());
							newAnalyseFlow.setUpstreamModelType("0");
							newAnalyseFlow.setDownUpstreamModelType("0");
							newFlows.add(newAnalyseFlow);
						}
					}
				}
			}
		}
	}
	
	public boolean containsParnet(AnalyseFlow analyseFlow,String downId){
		if(analyseFlow.getpAnalyseFlow()!=null){
			if(analyseFlow.getpAnalyseFlow().getId().equals(downId)){
				return true;
			}else{
				return containsParnet(analyseFlow.getpAnalyseFlow(), downId);
			}
		}else{
			return false;
		}
	}
	
	public boolean containsChild(List<AnalyseFlow> downFlows,String downId){
		if(downFlows==null){
			return true;
		}
		
		for (int i = 0; i < downFlows.size(); i++) {
			if(downFlows.get(i).getId().equals(downId)){
				return false;
			}
		}
		return true;
	}
	
	public String getParnetMMId(AnalyseFlow analyseFlow,String downId){
		if(analyseFlow.getpAnalyseFlow()!=null){
			if(analyseFlow.getpAnalyseFlow().getId().equals(downId)){
				if(analyseFlow.getpAnalyseFlow().getUpstreamModelType()==null||analyseFlow.getpAnalyseFlow().getUpstreamModelType().equals("0")){
					return analyseFlow.getpAnalyseFlow().getUpstreamId();
				}else{
					return getParnetMMId(analyseFlow.getpAnalyseFlow(), analyseFlow.getpAnalyseFlow().getId());
				}
			}else{
				return getParnetMMId(analyseFlow.getpAnalyseFlow(), downId);
			}
		}else{
			return null;
		}
	}

	@Override
	public void insImpactSide(PageResults page) {
		metaAnalyDao.insImpactSide(page);
	}
	
}
