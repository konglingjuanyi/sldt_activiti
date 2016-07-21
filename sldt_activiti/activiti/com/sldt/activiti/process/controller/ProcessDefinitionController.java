package com.sldt.activiti.process.controller;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sldt.framework.common.ControllerHelper;
import com.sldt.framework.common.PageResults;
import com.sldt.mds.dmc.mp.util.ObjectToJSON;

@Controller
@RequestMapping("/activiti/processDefinition.do")
public class ProcessDefinitionController {

	private static Log log = LogFactory.getLog(ProcessDefinitionController.class);

	@Resource
	private RepositoryService repositoryService;

	private String definitionId;

	/**
	 * 查询条件：流程名称
	 */
	private String definitionName;

	/**
	 * 多选删除的主键列表
	 */
	protected String[] ids;
	/**
	 * 查询条件：流程key
	 */
	protected String definitionKey;

	/**
	 * 分页查询
	 * 
	 * @return
	 */
	@RequestMapping(params="method=getProcessDefinitionByPage")
	@ResponseBody
	public Object getProcessDefinitionByPage(HttpServletRequest request, HttpServletResponse response) {
		PageResults page = ControllerHelper.buildPage(request);
		
		ProcessDefinitionQuery query = repositoryService
				.createProcessDefinitionQuery();
		// 查询条件
		if (null != definitionName && !"".equals(definitionName.trim())) {
			query.processDefinitionNameLike("%" + definitionName + "%");
		}
		if (null != definitionKey && !"".equals(definitionKey.trim())) {
			query.processDefinitionKey(definitionKey);
		}

		long rowCount = query.count();
		List<?> result = query.listPage(page.getStartIndex(), page.getPageSize());
		
		page.setTotalRecs((int)rowCount);
		page.setQueryResult(result);
		
		return ObjectToJSON.returnResult(result, page);
	}

	/**
	 * 删除
	 * @return
	 */
	@RequestMapping(params="method=deleteProcessDefinition")
	@ResponseBody
	public String deleteProcessDefinition() {
		for (String id : ids) {
			//id：流程定义的id
			//true：级联删除，删除流程定义会自动删除流程定义对应的流程实例。。。等
			repositoryService.deleteDeployment(id, true);
		}
		return "listAction";
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	public String getDefinitionKey() {
		return definitionKey;
	}

	public void setDefinitionKey(String definitionKey) {
		this.definitionKey = definitionKey;
	}

	/**
	 * 流程定义转换模型：从源码拷贝，不需要，太明白
	 */
	@RequestMapping(params="method=convert2Model")
	@ResponseBody
	public void convert2Model(HttpServletRequest request, HttpServletResponse response) {
		try {
			ProcessDefinition processDefinition = repositoryService
					.createProcessDefinitionQuery()
					.processDefinitionId(definitionId).singleResult();

			InputStream bpmnStream = repositoryService.getResourceAsStream(
					processDefinition.getDeploymentId(),
					processDefinition.getResourceName());
			XMLInputFactory xif = XMLInputFactory.newInstance();
			InputStreamReader in = new InputStreamReader(bpmnStream, "UTF-8");
			XMLStreamReader xtr = xif.createXMLStreamReader(in);
			BpmnModel bpmnModel = new BpmnXMLConverter()
					.convertToBpmnModel(xtr);

			if (bpmnModel.getMainProcess() == null
					|| bpmnModel.getMainProcess().getId() == null) {
				response.getWriter().print("转换失败...");
			} else {

				if (bpmnModel.getLocationMap().size() == 0) {
					response.getWriter().print("转换失败...");
				} else {

					BpmnJsonConverter converter = new BpmnJsonConverter();
					ObjectNode modelNode = converter.convertToJson(bpmnModel);
					Model modelData = repositoryService.newModel();

					ObjectNode modelObjectNode = new ObjectMapper()
							.createObjectNode();
					modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME,
							processDefinition.getName());
					modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION,
							1);
					modelObjectNode.put(
							ModelDataJsonConstants.MODEL_DESCRIPTION,
							processDefinition.getDescription());
					modelData.setMetaInfo(modelObjectNode.toString());
					modelData.setName(processDefinition.getName());

					repositoryService.saveModel(modelData);

					repositoryService.addModelEditorSource(modelData.getId(),
							modelNode.toString().getBytes("utf-8"));

					response.sendRedirect(request.getContextPath()
							+ "/service/editor?id="
							+ modelData.getId());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getDefinitionName() {
		return definitionName;
	}

	public void setDefinitionName(String definitionName) {
		this.definitionName = definitionName;
	}

	public void setDefinitionId(String definitionId) {
		this.definitionId = definitionId;
	}

}
