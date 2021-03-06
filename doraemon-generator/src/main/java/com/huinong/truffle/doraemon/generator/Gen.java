package com.huinong.truffle.doraemon.generator;

import com.google.common.collect.Lists;
import com.huinong.truffle.doraemon.generator.codegen.HnCodeGenerator;
import com.huinong.truffle.doraemon.generator.codegen.HnJavaClientCodegen;
import com.huinong.truffle.doraemon.generator.enums.ServiceEnum;
import com.huinong.truffle.doraemon.generator.utils.PathUtils;
import com.huinong.truffle.doraemon.generator.utils.ServiceUtils;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import java.io.File;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.openapitools.codegen.ClientOptInput;
import org.openapitools.codegen.CodegenConstants;

public class Gen {

  public static void main(String[] args) throws Exception {

    //openApiV3 解析器
    OpenAPIV3Parser openAPIV3Parser = new OpenAPIV3Parser();

    ServiceEnum serviceEnum = ServiceEnum.MESSAGE_CENTER;
    List<String> beanDir = Lists.newArrayList(PathUtils.BEAN_BASE_DIR);
    beanDir.add(ServiceUtils.serviceId2FeignClient(serviceEnum.getServiceId(), false));

    List<String> feignDir = Lists.newArrayList(PathUtils.FEIGN_BASE_DIR);
    beanDir.add(ServiceUtils.serviceId2FeignClient(serviceEnum.getServiceId(), false));

    FileUtils.deleteDirectory(new File(PathUtils.combinePath(beanDir)));
    FileUtils.deleteDirectory(new File(PathUtils.combinePath(feignDir)));

    OpenAPI openAPI = openAPIV3Parser.read(serviceEnum.getUrl());

    String serviceId = serviceEnum.getServiceId().toLowerCase();
    ClientOptInput input = new ClientOptInput().config(new HnJavaClientCodegen(serviceId)).openAPI(openAPI);
    HnCodeGenerator apiCodegen = new HnCodeGenerator(serviceId);
    apiCodegen.setGeneratorPropertyDefault(CodegenConstants.APIS, "true");
    ;
    apiCodegen.setGeneratorPropertyDefault(CodegenConstants.MODELS, "true");
    apiCodegen.opts(input).generate();
  }


}
