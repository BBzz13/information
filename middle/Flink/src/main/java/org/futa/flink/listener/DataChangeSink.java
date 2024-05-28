package org.futa.flink.listener;

//import cn.hutool.core.util.ObjectUtil;
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.data.empower.alpha.entity.AlphaCustomer;
//import com.data.empower.alpha.mapper.AlphaCustomerMapper;
//import com.data.empower.constant.AlphaSearchConstants;
//import com.data.empower.entity.DataChangeInfo;
//import com.data.empower.enums.SearchTypeEnum;
//import com.data.empower.search.dto.AlphaSearchDocDTO;
//import com.data.empower.search.entity.SearchArticle;
//import com.data.empower.search.entity.SearchCampus;
//import com.data.empower.search.entity.SearchExhibition;
//import com.data.empower.search.entity.SearchInstrument;
//import com.data.empower.search.mapper.*;
//import com.data.empower.security.util.SpringUtil;
//import lombok.extern.slf4j.Slf4j;
//
//import org.elasticsearch.action.bulk.BulkRequest;
//import org.elasticsearch.action.delete.DeleteRequest;
//import org.elasticsearch.action.index.IndexRequest;
//import org.elasticsearch.client.RequestOptions;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.elasticsearch.client.indices.CreateIndexRequest;
//import org.elasticsearch.client.indices.GetIndexRequest;
//import org.elasticsearch.client.indices.PutMappingRequest;
//import org.elasticsearch.common.xcontent.XContentBuilder;
//import org.elasticsearch.common.xcontent.XContentFactory;
//import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangtao
 * @Description: 数据处理
 * @Date: 2023/11/11;
 */
@Component
public class DataChangeSink extends RichSinkFunction<DataChangeInfo> {

    private static final long serialVersionUID = -74375380912179188L;

//    private RestHighLevelClient restHighLevelClient;

//    private SearchCollegeMapper searchCollegeMapper;
//    private SearchCampusMapper searchCampusMapper;
//    private SearchExhibitionMapper searchExhibitionMapper;
//    private SearchArticleMapper searchArticleMapper;
//    private AlphaCustomerMapper alphaCustomerMapper;
//    private SearchInstrumentMapper searchInstrumentMapper;

    /**
     * 在open()方法中动态注入Spring容器的类
     * 在启动SpringBoot项目是加载了Spring容器，其他地方可以使用@Autowired获取Spring容器中的类；
     * 但是Flink启动的项目中，默认启动了多线程执行相关代码，导致在其他线程无法获取Spring容器，
     * 只有在Spring所在的线程才能使用@Autowired，故在Flink自定义的Sink的open()方法中初始化Spring容器
     */
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
//        restHighLevelClient = SpringUtil.getBean(RestHighLevelClient.class);
//        searchCollegeMapper = SpringUtil.getBean(SearchCollegeMapper.class);
//        searchCampusMapper = SpringUtil.getBean(SearchCampusMapper.class);
//        searchExhibitionMapper = SpringUtil.getBean(SearchExhibitionMapper.class);
//        searchArticleMapper = SpringUtil.getBean(SearchArticleMapper.class);
//        alphaCustomerMapper = SpringUtil.getBean(AlphaCustomerMapper.class);
//        searchInstrumentMapper = SpringUtil.getBean(SearchInstrumentMapper.class);
    }

    @Override
    public void invoke(DataChangeInfo dataChangeInfo, Context context) throws Exception {
        //log.info("收到变更原始数据:{}", dataChangeInfo);
//        if (ObjectUtil.isNotNull(dataChangeInfo)) {
//            //业务代码
//            BulkRequest bulkRequest = new BulkRequest();
//            //根据表名获取ES索引库名字
//            String table_name = dataChangeInfo.getTableName();
//            String search_table_name = SearchTypeEnum.getValue(table_name);
//            //判断索引库是否存在，创建索引库
//            createSearchIndex(search_table_name);
//            //组装索引库参数
//            AlphaSearchDocDTO searchDocDTO = JSONObject.parseObject(dataChangeInfo.getAfterData(), AlphaSearchDocDTO.class);
//            //设置索引库数据类型
//            searchDocDTO.setSearchType(SearchTypeEnum.getType(table_name));
//            Map afterDataMap = JSON.parseObject(dataChangeInfo.getAfterData(), HashMap.class);
//            //标签处理
//            if (ObjectUtil.isNotEmpty(afterDataMap.get("abstracts"))) {
//                searchDocDTO.setAbstracts(afterDataMap.get("abstracts").toString().replaceAll("<[^>]*>", "")
//                        .replaceAll("\\s|&nbsp;", ""));
//            }
//
//            if (dataChangeInfo.getTableName().equals(SearchTypeEnum.ALPHA_SEARCH_TEACHER.getCode())) {
//                List<String> units = searchCollegeMapper.getUnitNameList((Integer) afterDataMap.get("id"));
//                searchDocDTO.setUnit(String.join(",", units));
//            }
//            if (dataChangeInfo.getTableName().equals(SearchTypeEnum.ALPHA_SEARCH_COLLEGE.getCode())) {
//                Integer campusId = (Integer) afterDataMap.get("campus_id");
//                searchDocDTO.setName(afterDataMap.get("campus_name").toString() + afterDataMap.get("name").toString());
//                SearchCampus searchCampus = searchCampusMapper.selectById(campusId);
//                searchDocDTO.setFeature(searchCampus.getFeature());
//                searchDocDTO.setProvince(searchCampus.getProvince());
//                searchDocDTO.setAddress(searchCampus.getAddress());
//            }
//            if (dataChangeInfo.getTableName().equals(SearchTypeEnum.ALPHA_SEARCH_SCHOOL.getCode())) {
//                //获取地址
//                searchDocDTO.setAddress((String) afterDataMap.get("city"));
//            }
//            //仪器
//            if(dataChangeInfo.getTableName().equals(SearchTypeEnum.ALPHA_SEARCH_INSTRUMENT.getCode())){
//                Integer customerId = (Integer) afterDataMap.get("customer_id");
//                Integer instrumentId = (Integer) afterDataMap.get("id");
//                SearchInstrument searchInstrument = searchInstrumentMapper.selectById(instrumentId);
//                if (ObjectUtil.isNotEmpty(searchInstrument.getContent())){
//                    searchDocDTO.setAbstracts(searchInstrument.getContent().replaceAll("<[^>]*>", ""));
//                }
//                if(ObjectUtil.isNotEmpty(customerId)){
//                    AlphaCustomer alphaCustomer = alphaCustomerMapper.selectById(customerId);
//                    searchDocDTO.setCompanyName(alphaCustomer.getCompanyName());
//                    searchDocDTO.setIcon(alphaCustomer.getIcon());
//                }
//            }
//            //会展时间
//            if (dataChangeInfo.getTableName().equals(SearchTypeEnum.ALPHA_SEARCH_EXHIBITION.getCode())) {
//                Integer exhibitionId = (Integer) afterDataMap.get("id");
//                SearchExhibition searchExhibition = searchExhibitionMapper.selectById(exhibitionId);
//                searchDocDTO.setStartTime(searchExhibition.getStartTime().toString());
//                searchDocDTO.setEndTime(searchExhibition.getEndTime().toString());
//                searchDocDTO.setPicture(searchExhibition.getImageUrl());
//                searchDocDTO.setFiled(searchExhibition.getField());
//            }
//            //招标发布时间
//            if (dataChangeInfo.getTableName().equals(SearchTypeEnum.ALPHA_SEARCH_ARTICLE.getCode())) {
//                Integer articleId = (Integer) afterDataMap.get("id");
//                SearchArticle searchArticle = searchArticleMapper.selectById(articleId);
//                searchDocDTO.setSubmitTime(searchArticle.getStartTime());
//                searchDocDTO.setCity(searchArticle.getCity());
//                searchDocDTO.setWebsiteName(searchArticle.getWebsiteName());
//            }
//            if (StringUtils.equalsAny(dataChangeInfo.getEventType(), AlphaSearchConstants.READ, AlphaSearchConstants.CREATE, AlphaSearchConstants.UPDATE)) {
//                // 3.添加请求参数，发送请求
//                bulkRequest.add(new IndexRequest(search_table_name)
//                        .source(JSON.toJSONString(searchDocDTO), XContentType.JSON)
//                        .id(String.valueOf(searchDocDTO.getId()))
//                );
//                restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
//            } else if (AlphaSearchConstants.DELETE.equals(dataChangeInfo.getEventType())) {
//                Map beforeDataMap = JSON.parseObject(dataChangeInfo.getBeforeData(), HashMap.class);
//                Integer id = (Integer) beforeDataMap.get("id");
//                DeleteRequest deleteRequest = new DeleteRequest(search_table_name).id(String.valueOf(id));
//                restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
//            }
//        }
    }


    /**
     * 创建索引库
     */
    public void createSearchIndex(String index) {
//        try {
//            GetIndexRequest request = new GetIndexRequest(index);
//            boolean exists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
//            if (!exists) {
//                // 创建索引库
//                CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
//                restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
//                //创建mapping映射
//                PutMappingRequest putMappingRequest = new PutMappingRequest(index);
//                XContentBuilder xContentBuilder = XContentFactory.jsonBuilder();
//                xContentBuilder.startObject();
//                xContentBuilder.field("dynamic", "false");
//                xContentBuilder.startObject("properties");
//                xContentBuilder.startObject("id");
//                xContentBuilder.field("type", "keyword");
//                xContentBuilder.endObject();
//                xContentBuilder.startObject("name");
//                xContentBuilder.field("type", "text");
//                xContentBuilder.field("analyzer", "ik_max_word");
//                xContentBuilder.field("copy_to", "all");
//                xContentBuilder.endObject();
//                xContentBuilder.startObject("picture");
//                xContentBuilder.field("type", "text");
//                xContentBuilder.field("index", "false");
//                xContentBuilder.endObject();
//                xContentBuilder.startObject("all");
//                xContentBuilder.field("type", "text");
//                xContentBuilder.field("analyzer", "ik_max_word");
//                xContentBuilder.endObject();
//                //添加不同库的属性
//                addXContentBuilder(xContentBuilder, index);
//                xContentBuilder.endObject();
//                xContentBuilder.endObject();
//
//                putMappingRequest.source(xContentBuilder);
//                restHighLevelClient.indices().putMapping(putMappingRequest, RequestOptions.DEFAULT);
//            }
//        } catch (IOException e) {
//            log.error("es创建索引库失败：{}", e.getMessage());
//        }
    }

//    private static XContentBuilder addXContentBuilder(XContentBuilder xContentBuilder, String index) throws IOException {
//        if (index.equals(SearchTypeEnum.ALPHA_SEARCH_TEACHER.getValue())) {
//            xContentBuilder.startObject("abstracts");
//            xContentBuilder.field("type", "text");
//            xContentBuilder.field("analyzer", "ik_max_word");
//            xContentBuilder.field("copy_to", "all");
//            xContentBuilder.endObject();
//            xContentBuilder.startObject("job_title");
//            xContentBuilder.field("type", "text");
//            xContentBuilder.endObject();
//            xContentBuilder.startObject("education");
//            xContentBuilder.field("type", "text");
//            xContentBuilder.endObject();
//            xContentBuilder.startObject("unit");
//            xContentBuilder.field("type", "text");
//            xContentBuilder.field("copy_to", "all");
//            xContentBuilder.endObject();
//        }
//        if (index.equals(SearchTypeEnum.ALPHA_SEARCH_SCHOOL.getValue()) || index.equals(SearchTypeEnum.ALPHA_SEARCH_COLLEGE.getValue())) {
//            xContentBuilder.startObject("abstracts");
//            xContentBuilder.field("type", "text");
//            xContentBuilder.field("analyzer", "ik_max_word");
//            xContentBuilder.field("copy_to", "all");
//            xContentBuilder.endObject();
//            xContentBuilder.startObject("address");
//            xContentBuilder.field("type", "text");
//            xContentBuilder.endObject();
//            xContentBuilder.startObject("feature");
//            xContentBuilder.field("type", "text");
//            xContentBuilder.endObject();
//            xContentBuilder.startObject("province");
//            xContentBuilder.field("type", "keyword");
//            xContentBuilder.endObject();
//        }
//        if (index.equals(SearchTypeEnum.ALPHA_SEARCH_INSTRUMENT.getValue())) {
//            xContentBuilder.startObject("abstracts");
//            xContentBuilder.field("type", "text");
//            xContentBuilder.field("analyzer", "ik_max_word");
//            xContentBuilder.field("copy_to", "all");
//            xContentBuilder.endObject();
//            xContentBuilder.startObject("minPrice");
//            xContentBuilder.field("type", "double");
//            xContentBuilder.field("copy_to", "all");
//            xContentBuilder.endObject();
//            xContentBuilder.startObject("maxPrice");
//            xContentBuilder.field("type", "double");
//            xContentBuilder.field("copy_to", "all");
//            xContentBuilder.endObject();
//            xContentBuilder.startObject("brand");
//            xContentBuilder.field("type", "text");
//            xContentBuilder.endObject();
//            xContentBuilder.startObject("origin");
//            xContentBuilder.field("type", "text");
//            xContentBuilder.endObject();
//            xContentBuilder.startObject("model");
//            xContentBuilder.field("type", "text");
//            xContentBuilder.endObject();
//        }
//        if (index.equals(SearchTypeEnum.ALPHA_SEARCH_ARTICLE.getValue())) {
//            xContentBuilder.startObject("submitTime");
//            xContentBuilder.field("type", "date");
//            xContentBuilder.endObject();
//            xContentBuilder.startObject("type");
//            xContentBuilder.field("type", "text");
//            xContentBuilder.endObject();
//            xContentBuilder.startObject("province");
//            xContentBuilder.field("type", "keyword");
//            xContentBuilder.endObject();
//            xContentBuilder.startObject("city");
//            xContentBuilder.field("type", "keyword");
//            xContentBuilder.endObject();
//            xContentBuilder.startObject("websiteName");
//            xContentBuilder.field("type", "keyword");
//            xContentBuilder.endObject();
//        }
//        if (index.equals(SearchTypeEnum.ALPHA_SEARCH_EXHIBITION.getValue())) {
//            xContentBuilder.startObject("abstracts");
//            xContentBuilder.field("type", "text");
//            xContentBuilder.field("analyzer", "ik_max_word");
//            xContentBuilder.field("copy_to", "all");
//            xContentBuilder.endObject();
//            xContentBuilder.startObject("address");
//            xContentBuilder.field("type", "text");
//            xContentBuilder.endObject();
//            xContentBuilder.startObject("startTime");
//            xContentBuilder.field("type", "text");
//            xContentBuilder.field("fielddata", "true");
//            xContentBuilder.field("analyzer", "ik_max_word");
//            xContentBuilder.endObject();
//            xContentBuilder.startObject("endTime");
//            xContentBuilder.field("type", "text");
//            xContentBuilder.endObject();
//            xContentBuilder.startObject("province");
//            xContentBuilder.field("type", "keyword");
//            xContentBuilder.endObject();
//            xContentBuilder.startObject("filed");
//            xContentBuilder.field("type", "keyword");
//            xContentBuilder.endObject();
//        }
//        if (index.equals(SearchTypeEnum.ALPHA_SEARCH_THESIS.getValue())) {
//            xContentBuilder.startObject("abstracts");
//            xContentBuilder.field("type", "text");
//            xContentBuilder.field("analyzer", "ik_max_word");
//            xContentBuilder.field("copy_to", "all");
//            xContentBuilder.endObject();
//        }
//
//        return xContentBuilder;
//    }
}
