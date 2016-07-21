package com.sldt.activiti.process.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sldt.framework.common.ControllerHelper;
import com.sldt.framework.common.PageResults;
import com.sldt.framework.common.PageVo;

@Controller
@RequestMapping("/activiti/processModel.do")
public class ProcessModelController {

	private static Log log = LogFactory.getLog(ProcessModelController.class);
	
	/**
	 * 业务对象
	 */
	@Resource
	private RepositoryService repositoryService;
	
	/**
	 * 分页查询
	 * @return
	 */
	@RequestMapping(params="method=getProcessModelByPage")
	@ResponseBody
	public PageVo getProcessModelByPage(HttpServletRequest request, HttpServletResponse response) {
		PageResults page = ControllerHelper.buildPage(request);
		
		String name = request.getParameter("name");
		
		//创建query
		ModelQuery query = repositoryService.createModelQuery();
		
		if (null != name && !"".equals(name)) {
			query.modelNameLike("%" + name + "%");
		}
		
		//使用querey查询总行数
		long rowCount = query.count();
		//查询一页数据
		List<Model> result = query.listPage(page.getStartIndex(), page.getPageSize());
		page.setTotalRecs(Integer.valueOf(rowCount+""));
		PageVo pv = new PageVo(page.getPageSize(), page.getCurrPage(), page.getTotalRecs());
		pv.setRows(result);
		return pv;
	}
	
	/**
	 * 删除
	 * @return
	 */
	@RequestMapping(params="method=deleteProcessModel")
	@ResponseBody
	public JSONObject deleteProcessModel(HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		try {
			request.setCharacterEncoding("UTF-8");
			String idsStr = request.getParameter("idsStr");
			String[] ids = idsStr.split("\\,");
			
			for (String id : ids) {
				repositoryService.deleteModel(id);
			}
			json.put("code", "0");
			json.put("msg", "删除成功!");
			return json;
		} catch (Exception e) {
			log.info("数删除出错！");
			json.put("msg", "数删除出错！" + e.getMessage());
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * 创建模型：从源码拷贝，不需要，太明白
	 */
	@RequestMapping(params="method=addProcessModel")
	@ResponseBody
	public void addProcessModel(HttpServletRequest request, HttpServletResponse response) {
		try {
			String name = request.getParameter("name");
			String description = request.getParameter("description");
			String key = request.getParameter("key");
			
			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode editorNode = objectMapper.createObjectNode();
			editorNode.put("id", "canvas");
			editorNode.put("resourceId", "canvas");
			ObjectNode stencilSetNode = objectMapper.createObjectNode();
			stencilSetNode.put("namespace",
					"http://b3mn.org/stencilset/bpmn2.0#");
			editorNode.put("stencilset", stencilSetNode);
			Model modelData = repositoryService.newModel();

			ObjectNode modelObjectNode = objectMapper.createObjectNode();
			modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
			modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
			description = StringUtils.defaultString(description);
			modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION,
					description);
			modelData.setMetaInfo(modelObjectNode.toString());
			modelData.setName(name);
			modelData.setKey(StringUtils.defaultString(key));
			
			//保存模型
			repositoryService.saveModel(modelData);
			repositoryService.addModelEditorSource(modelData.getId(),
					editorNode.toString().getBytes("UTF-8"));
			
			//跳转到activiti modeler渲染模型到页面
			response.sendRedirect(request.getContextPath()
					+ "/service/editor?id=" + modelData.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据Model部署流程：从源码拷贝，不需要，太明白
	 */
	@RequestMapping(params="method=deploy")
	@ResponseBody
	public JSONObject deploy(HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		try {
			String modelId = request.getParameter("modelId");
			
			Model modelData = repositoryService.getModel(modelId);
			ObjectNode modelNode = (ObjectNode) new ObjectMapper()
					.readTree(repositoryService.getModelEditorSource(modelData
							.getId()));
			byte[] bpmnBytes = null;

			BpmnModel model = new BpmnJsonConverter()
					.convertToBpmnModel(modelNode);
			bpmnBytes = new BpmnXMLConverter().convertToXML(model, "UTF-8");

			String processName = modelData.getName() + ".bpmn20.xml";
			Deployment deployment = repositoryService.createDeployment()
					.name(modelData.getName())
					.addString(processName, new String(bpmnBytes)).deploy();
			//response.getWriter().print("<script>alert('发布成功');history.go(-1);</script>");
			
			json.put("code", "0");
			json.put("msg", "发布成功!");
			return json;
		} catch (Exception e) {
			log.info("发布出错！");
			json.put("msg", "发布出错！" + e.getMessage());
			e.printStackTrace();
			return json;
		}
	}
	
	/**
	 * 导出model的xml文件：从源码拷贝，不需要，太明白
	 */
	@RequestMapping(params="method=exportBpmnFile")
	@ResponseBody
	public ResponseEntity<byte[]> exportBpmnFile(HttpServletRequest request, HttpServletResponse response) {
		ByteArrayInputStream in = null;
		BpmnModel bpmnModel = null;
		byte[] bpmnBytes = null;
		 HttpHeaders headers = null;
		try {
			String modelId = request.getParameter("modelId");
			
			Model modelData = repositoryService.getModel(modelId);
			BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
			JsonNode editorNode = new ObjectMapper().readTree(repositoryService
					.getModelEditorSource(modelData.getId()));
			 bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
			BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
			bpmnBytes = xmlConverter.convertToXML(bpmnModel);
			
			//ActionContext.getContext().put("exportBpmnFileName", bpmnModel.getMainProcess().getId() + ".bpmn20.xml");

			//in = new ByteArrayInputStream(bpmnBytes);
		 headers = new HttpHeaders();   
		 String fileName=new String((modelData.getName()+".xml").getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题  
		 headers.setContentDispositionFormData("attachment", fileName);
		 headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<byte[]>(bpmnBytes,headers, HttpStatus.CREATED);    
	}
}
